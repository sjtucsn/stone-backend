package com.fanggu.stone.dao;

import com.fanggu.stone.model.UploadedContent;
import java.util.List;

public interface UploadedContentMapper {
    int deleteByPrimaryKey(Integer uploadedContentId);

    int insert(UploadedContent record);

    UploadedContent selectByPrimaryKey(Integer uploadedContentId);

    List<UploadedContent> selectAll();

    int updateByPrimaryKey(UploadedContent record);
}