package com.fanggu.stone.dao;

import com.fanggu.stone.model.Article;

import java.util.List;

public interface ArticleMapper {
    int insertArticle(Article article);

    int updateArticleImagePath(Integer articleId, String imagePath);

    int deleteArticleById(Integer resourceId);

    List<Article> listArticleByPublisherId(Integer publisherId);

    List<Article> listArticleByCategoryId(Integer categoryId);

    List<Article> listArticleByPage(Integer offset, Integer pageSize);
}