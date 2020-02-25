package com.fanggu.stone.dao;

import com.fanggu.stone.model.Resource;
import java.util.List;

public interface ResourceMapper {
    int insertResource(Resource resource);

    int deleteResourceById(Integer resourceId);

    List<Resource> listResourceByPage(Integer uploaderId, Integer categoryId, Integer offset, Integer pageSize);
}