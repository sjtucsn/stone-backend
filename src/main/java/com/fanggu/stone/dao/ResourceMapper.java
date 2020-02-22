package com.fanggu.stone.dao;

import com.fanggu.stone.model.Resource;
import java.util.List;

public interface ResourceMapper {
    int insertResource(Resource resource);

    int deleteResourceById(Integer resourceId);

    List<Resource> listContentsByUploaderId(Integer uploaderId);

    List<Resource> listContentsByCategoryId(Integer categoryId);

    List<Resource> listContentsByPage(Integer offset, Integer pageSize);
}