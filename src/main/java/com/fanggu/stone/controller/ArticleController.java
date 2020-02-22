package com.fanggu.stone.controller;

import com.fanggu.stone.dao.ArticleMapper;
import com.fanggu.stone.model.Article;
import com.fanggu.stone.response.BasicResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.fanggu.stone.constant.ResultCode.FAIL;
import static com.fanggu.stone.constant.ResultCode.SUCCESS;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Resource
    private ArticleMapper articleMapper;

    private static Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @PostMapping("/publish")
    @Transactional(rollbackFor = Exception.class)
    public BasicResponse articleUploadAction(@RequestBody Article article) {
        Integer publisherId = article.getPublisherId();
        String classPath = getClass().getClassLoader().getResource("").getPath();
        File tmpPath = new File(classPath + "/static/tmp/" + publisherId);
        if (tmpPath.exists() && tmpPath.list().length > 0) {
            // 保存上传数据
            articleMapper.insertArticle(article);
            String newPath = "/article/" + article.getArticleId() + "/";
            articleMapper.updateArticleImagePath(article.getArticleId(), newPath);

            // 转移图片位置
            File newFilePath = new File(classPath + "/static" + newPath);
            if (!newFilePath.exists()) {
                newFilePath.mkdirs();
            }
            for (File file : tmpPath.listFiles()) {
                file.renameTo(new File(newFilePath.getPath() + "/" + file.getName()));
            }
        } else {
            // 无图片上传，则直接保存数据
            articleMapper.insertArticle(article);
        }
        return new BasicResponse(SUCCESS, "资源发布成功");
    }

    @PostMapping("/delete")
    public BasicResponse articleDeleteAction(int articleId) {
        int affectedRows = articleMapper.deleteArticleById(articleId);
        if (affectedRows == 1) {
            return new BasicResponse<>(SUCCESS, "删除资源成功");
        } else {
            return new BasicResponse<>(FAIL, "该资源不存在");
        }
    }

    // 获取发布的文章列表，可按上传者Id查看、按资源类别Id查看或者所有见容分页查看
    @GetMapping("/list")
    public BasicResponse<List<Article>> articleIndexAction(Integer pageNo, Integer pageSize, Integer publisherId, Integer categoryId) {
        String classPath = getClass().getClassLoader().getResource("").getPath();
        List<Article> articleList;
        if (publisherId != null) {
            articleList = articleMapper.listArticleByPublisherId(publisherId);
        } else if (categoryId != null) {
            articleList = articleMapper.listArticleByCategoryId(categoryId);
        } else {
            articleList = articleMapper.listArticleByPage(pageNo * pageSize, pageSize);
        }
        for (Article article : articleList) {
            if (article.getImagePath() != null) {
                File articleDir = new File(classPath + "/static" + article.getImagePath());
                if (articleDir.exists()) {
                    String[] fileNameList = articleDir.list();
                    List<String> relativePathNameList = new ArrayList<>();
                    for (String fileName : fileNameList) {
                        relativePathNameList.add(article.getImagePath() + fileName);
                    }
                    // 构造相关图片路径返回给前端
                    article.setImageList(relativePathNameList);
                }
            }
        }
        return new BasicResponse<>(SUCCESS, articleList);
    }
}
