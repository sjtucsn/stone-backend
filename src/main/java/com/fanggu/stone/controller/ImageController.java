package com.fanggu.stone.controller;

import com.fanggu.stone.response.BasicResponse;
import net.coobird.thumbnailator.Thumbnails;
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
    public BasicResponse imageUploadAction(@RequestParam("image") MultipartFile multipartFile, String userId, Integer index) {
        if (userId == null || multipartFile == null) {
            return new BasicResponse(PARAM_ERROR, "参数错误");
        }
        if (!multipartFile.getContentType().startsWith("image")) {
            return new BasicResponse(PARAM_ERROR, "不支持该图片格式");
        }
        // 临时图片均保存在/static/tmp/userId/目录下，缩略图以thumbnail.开头
        File dir = new File(getClass().getClassLoader().getResource("").getPath() + "/static/tmp/" + userId);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            File targetFile = new File(dir.getPath() + "/" + index + "." + multipartFile.getOriginalFilename().split("\\.")[1]);
            File thumbnailFile = new File(dir.getPath() + "/thumbnail." + index + "." + multipartFile.getOriginalFilename().split("\\.")[1]);
            multipartFile.transferTo(targetFile);
            // 创建缩略图
            Thumbnails.of(targetFile).size(320, 320).toFile(thumbnailFile);
        } catch (IOException e) {
            e.printStackTrace();
            return new BasicResponse(SYSTEM_ERROR, "图片保存失败");
        }
        return new BasicResponse(SUCCESS, "上传成功");
    }
}
