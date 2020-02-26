package com.fanggu.stone.dao;

import com.fanggu.stone.model.Category;
import java.util.List;

public interface CategoryMapper {
    int insertCategory(Category category);

    Category getCategoryByName(String categoryName);

    int deleteCategoryById(Integer categoryId);

    List<Category> listCategories();
}