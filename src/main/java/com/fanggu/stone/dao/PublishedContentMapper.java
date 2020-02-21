package com.fanggu.stone.dao;

import com.fanggu.stone.model.PublishedContent;
import java.util.List;

public interface PublishedContentMapper {
    int deleteByPrimaryKey(Integer publishedContentId);

    int insert(PublishedContent record);

    PublishedContent selectByPrimaryKey(Integer publishedContentId);

    List<PublishedContent> selectAll();

    int updateByPrimaryKey(PublishedContent record);
}