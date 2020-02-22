package com.fanggu.stone.controller;

import com.fanggu.stone.dao.UserMapper;
import com.fanggu.stone.model.User;
import com.fanggu.stone.response.BasicResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.fanggu.stone.constant.ResultCode.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserMapper userMapper;

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    public BasicResponse userRegisterAction(@RequestBody User user) {
        if (userMapper.getUserByTel(user.getUserTel()) != null) {
            user.setUserType(0);
            userMapper.insertUser(user);
            return new BasicResponse(SUCCESS, "注册成功");
        } else {
            return new BasicResponse(FAIL, "该手机号已注册！");
        }
    }

    @PostMapping("/login")
    public BasicResponse<User> userLoginAction(String userTel, String password) {
        User user = userMapper.getUserByLogin(userTel, password);
        if (user == null) {
            return new BasicResponse<>(FAIL, "用户名或密码错误");
        } else {
            return new BasicResponse<>(SUCCESS, user);
        }
    }

    @PostMapping("/update")
    public BasicResponse<User> userUpdateAction(@RequestBody User user) {
        userMapper.updateUser(user);
        user = userMapper.getUserById(user.getUserId());
        return new BasicResponse<>(SUCCESS, user);
    }
}
