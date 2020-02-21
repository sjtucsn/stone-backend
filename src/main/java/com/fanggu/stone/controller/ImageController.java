package com.fanggu.stone.controller;

import com.fanggu.stone.response.BasicResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static com.fanggu.stone.constant.ResultCode.*;

@RestController
@RequestMapping("/image")
public class ImageController {

    private static Logger logger = LoggerFactory.getLogger(ImageController.class);

    @PostMapping("/upload")
    public BasicResponse imageUploadAction(@RequestParam("image") MultipartFile multipartFile, String userId) {
        if (userId == null || multipartFile == null) {
            return new BasicResponse(PARAM_ERROR, "参数错误");
        }
        if (!multipartFile.getContentType().startsWith("image")) {
            return new BasicResponse(PARAM_ERROR, "不支持该图片格式");
        }
        File dir = new File(getClass().getClassLoader().getResource("").getPath() + "/static/tmp/" + userId);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            multipartFile.transferTo(new File(dir.getPath() + "/" + dir.list().length + "." + multipartFile.getOriginalFilename().split("\\.")[1]));
        } catch (IOException e) {
            e.printStackTrace();
            return new BasicResponse(SYSTEM_ERROR, "图片保存失败");
        }
        return new BasicResponse(SUCCESS, "上传成功");
    }
}
