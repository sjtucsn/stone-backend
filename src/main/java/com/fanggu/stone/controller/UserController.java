package com.fanggu.stone.controller;

import com.fanggu.stone.dao.UserMapper;
import com.fanggu.stone.model.User;
import com.fanggu.stone.response.BasicResponse;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;

import static com.fanggu.stone.constant.ResultCode.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserMapper userMapper;

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    public BasicResponse userRegisterAction(@RequestBody User user) {
        if (userMapper.getUserByTel(user.getUserTel()) == null) {
            // 用户注册时的默认类别为普通用户 userType若为1则为方古用户
            user.setUserType(0);
            userMapper.insertUser(user);
            return new BasicResponse(SUCCESS, "注册成功");
        } else {
            return new BasicResponse(FAIL, "该手机号已注册！");
        }
    }

    @PostMapping("/login")
    public BasicResponse<User> userLoginAction(String userTel, String password, HttpSession httpSession) {
        User user = userMapper.getUserByLogin(userTel, password);
        if (user == null) {
            return new BasicResponse<>(FAIL, "用户名或密码错误");
        } else {
            httpSession.setAttribute("status", "online");
            return new BasicResponse<>(SUCCESS, user);
        }
    }

    // 根据id获取用户信息
    @GetMapping("/info")
    public BasicResponse<User> getUserInfoAction(Integer userId) {
        User user = userMapper.getUserById(userId);
        if (user != null) {
            return new BasicResponse<>(SUCCESS, user);
        } else {
            return new BasicResponse<>(FAIL, "用户不存在");
        }
    }

    // 根据电话号码搜索用户
    @GetMapping("/search")
    public BasicResponse<User> searchUserAction(String userTel) {
        User user = userMapper.getUserByTel(userTel);
        if (user != null) {
            return new BasicResponse<>(SUCCESS, user);
        } else {
            return new BasicResponse<>(FAIL, "用户不存在");
        }
    }

    // 用户基本信息更新
    @PostMapping("/update")
    public BasicResponse<User> userUpdateAction(@RequestBody User user) {
        userMapper.updateUser(user);
        user = userMapper.getUserById(user.getUserId());
        return new BasicResponse<>(SUCCESS, user);
    }

    // 用户类别更新 1 方古管理员 0 普通用户
    @PostMapping("/authority/update")
    public BasicResponse<User> userAuthorityUpdateAction(@RequestBody User user) {
        userMapper.updateUserAuthority(user);
        user = userMapper.getUserById(user.getUserId());
        return new BasicResponse<>(SUCCESS, user);
    }

    // 用户头像上传
    @PostMapping("/avatar/change")
    @Transactional(rollbackFor = Exception.class)
    public BasicResponse avatarChangeAction(MultipartFile avatar, Integer userId) throws IOException{
        if (avatar == null || userId == null) {
            return new BasicResponse(PARAM_ERROR, "参数错误");
        }
        if (!avatar.getContentType().startsWith("image")) {
            return new BasicResponse(PARAM_ERROR, "不支持该图片格式");
        }
        // 创建头像保存目录，为/static/avatar/userId/
        String classPath = getClass().getClassLoader().getResource("").getPath();
        File avatarDir = new File(classPath + "/static/avatar/" + userId + "/");
        if (!avatarDir.exists()) {
            avatarDir.mkdirs();
        }
        int size = avatarDir.list().length;
        String avatarPath = "/avatar/" + userId + "/" + size + "."  + avatar.getOriginalFilename().split("\\.")[1];
        String thumbnailPath = "/avatar/" + userId + "/thumbnail." + size + "."  + avatar.getOriginalFilename().split("\\.")[1];
        // 更新头像信息，保存的是缩略图的地址
        userMapper.updateAvatar(userId, thumbnailPath);
        File targetFile = new File(classPath + "/static" + avatarPath);
        File thumbnailFile = new File(classPath + "/static" + thumbnailPath);
        avatar.transferTo(targetFile);
        // 创建头像缩略图
        Thumbnails.of(targetFile).size(200, 200).toFile(thumbnailFile);
        return new BasicResponse(SUCCESS, "头像更换成功!");
    }
}
