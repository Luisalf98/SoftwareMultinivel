/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.dao;

import com.loyder.software.model.entities.Category;
import java.util.ArrayList;

/**
 *
 * @author Luis Perez
 */
public interface CategoryDao {
    
    public boolean addCategory(Category category);
    public boolean removeCategoryById(Long id);
    public boolean updateCategory(Category category);
    public ArrayList<Category> getAllCategories();
    public ArrayList<Category> getCategoriesByName(String name);
    public Category getCategoryById(Long id);
}
