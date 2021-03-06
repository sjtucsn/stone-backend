package com.fanggu.stone.controller;

import com.fanggu.stone.dao.CategoryMapper;
import com.fanggu.stone.model.Category;
import com.fanggu.stone.response.BasicResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

import static com.fanggu.stone.constant.ResultCode.FAIL;
import static com.fanggu.stone.constant.ResultCode.SUCCESS;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private CategoryMapper categoryMapper;

    private static Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @PostMapping("/create")
    public BasicResponse categoryCreateAction(@RequestBody Category category) {
        if (categoryMapper.getCategoryByName(category.getCategoryName()) == null) {
            categoryMapper.insertCategory(category);
            return new BasicResponse(SUCCESS, "创建成功");
        } else {
            return new BasicResponse(FAIL, "类别名称不能重复");
        }
    }

    @PostMapping("/delete")
    public BasicResponse categoryDeleteAction(Integer categoryId) {
        int affectedRows = categoryMapper.deleteCategoryById(categoryId);
        if (affectedRows == 1) {
            return new BasicResponse<>(SUCCESS, "删除类别成功");
        } else {
            return new BasicResponse<>(FAIL, "该条目不存在");
        }
    }

    @GetMapping("/list")
    public BasicResponse<List<Category>> categoryIndexAction() {
        List<Category> categoryList = categoryMapper.listCategories();
        return new BasicResponse<>(SUCCESS, categoryList);
    }
}
