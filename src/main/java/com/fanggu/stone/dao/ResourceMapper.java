package com.fanggu.stone.dao;

import com.fanggu.stone.model.Resource;
import java.util.List;

public interface ResourceMapper {
    int insertResource(Resource resource);

    int deleteResourceById(Integer resourceId);

    List<Resource> listResourceByUploaderId(Integer uploaderId);

    List<Resource> listResourceByCategoryId(Integer categoryId);

    List<Resource> listResourceByPage(Integer offset, Integer pageSize);
}