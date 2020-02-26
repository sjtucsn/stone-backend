package com.fanggu.stone.controller;

import com.fanggu.stone.dao.ResourceMapper;
import com.fanggu.stone.model.Resource;
import com.fanggu.stone.response.BasicResponse;
import com.fanggu.stone.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.fanggu.stone.constant.ResultCode.FAIL;
import static com.fanggu.stone.constant.ResultCode.SUCCESS;

@RestController
@RequestMapping("/resource")
public class ResourceController {
    @javax.annotation.Resource
    private ResourceMapper resourceMapper;

    private static Logger logger = LoggerFactory.getLogger(ResourceController.class);

    @PostMapping("/upload")
    public BasicResponse resourceUploadAction(@RequestBody Resource resource) {
        Integer uploaderId = resource.getUploaderId();
        if (uploaderId == null) {
            return new BasicResponse(FAIL, "资源发布失败");
        }
        String classPath = getClass().getClassLoader().getResource("").getPath();
        File tmpPath = new File(classPath + "/static/tmp/" + uploaderId);
        try {
            if (tmpPath.exists() && tmpPath.list().length > 0) {
                // 保存上传数据
                String newPath = "/image/" + uploaderId + "/" + Utils.getCurrentDateTime() + "/";
                resource.setImagePath(newPath);
                resourceMapper.insertResource(resource);

                // 转移图片位置，用户上传图片保存在/static/image/uploaderId/currentTime/目录下
                File newFilePath = new File(classPath + "/static" + newPath);
                if (!newFilePath.exists()) {
                    newFilePath.mkdirs();
                }
                for (File file : tmpPath.listFiles()) {
                    file.renameTo(new File(newFilePath.getPath() + "/" + file.getName()));
                }
            } else {
                // 无图片上传，则直接保存数据
                resourceMapper.insertResource(resource);
            }
            return new BasicResponse(SUCCESS, "资源上传成功");
        } catch (Exception e) {
            for (File file : tmpPath.listFiles()) {
                file.delete();
            }
            return new BasicResponse(FAIL, "资源上传失败");
        }
    }

    @PostMapping("/delete")
    public BasicResponse resourceDeleteAction(Integer resourceId) {
        int affectedRows = resourceMapper.deleteResourceById(resourceId);
        if (affectedRows == 1) {
            return new BasicResponse<>(SUCCESS, "删除资源成功");
        } else {
            return new BasicResponse<>(FAIL, "该资源不存在");
        }
    }

    // 获取资源列表，可按上传者Id查看、按资源类别Id查看或者所有见容分页查看
    @GetMapping("/list")
    public BasicResponse<List<Resource>> resourceIndexAction(Integer offset, Integer pageSize, Integer uploaderId, Integer categoryId) {
        String classPath = getClass().getClassLoader().getResource("").getPath();
        List<Resource> resourceList = resourceMapper.listResourceByPage(uploaderId, categoryId, offset, pageSize);
        for (Resource resource : resourceList) {
            if (resource.getImagePath() != null) {
                File resourceDir = new File(classPath + "/static" + resource.getImagePath());
                if (resourceDir.exists()) {
                    String[] fileNameList = resourceDir.list();
                    // 文件名按顺序排好，保证原图和缩略图顺序一致
                    Arrays.sort(fileNameList);
                    List<String> imageList = new ArrayList<>();
                    List<String> thumbnailList = new ArrayList<>();
                    for (String fileName : fileNameList) {
                        if (fileName.startsWith("thumbnail.")) {
                            thumbnailList.add(resource.getImagePath() + fileName);
                        } else {
                            imageList.add(resource.getImagePath() + fileName);
                        }
                    }
                    // 构造相关图片路径返回给前端
                    resource.setThumbnailList(thumbnailList);
                    resource.setImageList(imageList);
                }
            }
        }
        return new BasicResponse<>(SUCCESS, resourceList);
    }
}
