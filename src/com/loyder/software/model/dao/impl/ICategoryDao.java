/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.dao.impl;

import com.loyder.software.model.dao.CategoryDao;
import com.loyder.software.model.dao.config.DatabaseConfig;
import com.loyder.software.model.dao.config.DatabaseConfig.Table;
import com.loyder.software.model.dao.config.DatabaseConfig.UsersCategoriesTableField;
import com.loyder.software.model.entities.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Luis Perez
 */
public class ICategoryDao implements CategoryDao{
    
    private ICategoryDao(){}
    
    private static CategoryDao categoryDao;
    
    public static CategoryDao getCategoryDao(){
        if(categoryDao==null){
            categoryDao = new ICategoryDao();
        }
        return categoryDao;
    }
    
    private static final String SQL_UPDATE_CATEGORY = String.format("UPDATE %s SET %s=? WHERE %s=?", Table.USERS_CATEGORIES, UsersCategoriesTableField.name, UsersCategoriesTableField.rowid);
    private static final String SQL_ADD_CATEGORY = String.format("INSERT INTO %s (%s) VALUES (?)", Table.USERS_CATEGORIES, UsersCategoriesTableField.name);
    private static final String SQL_REMOVE_CATEGOY_BY_ID = String.format("DELETE FROM %s WHERE %s=?", Table.USERS_CATEGORIES, UsersCategoriesTableField.rowid);
    private static final String SQL_GET_ALL_CATEGORIES = String.format("SELECT %s,* FROM %s", UsersCategoriesTableField.rowid, Table.USERS_CATEGORIES);
    private static final String SQL_GET_CATEGORY_BY_ID = String.format("SELECT %s,* FROM %s WHERE %s=?", UsersCategoriesTableField.rowid, Table.USERS_CATEGORIES, UsersCategoriesTableField.rowid);
    private static final String SQL_GET_CATEGORIES_BY_NAME = String.format("SELECT %s,* FROM %s WHERE %s LIKE ?", UsersCategoriesTableField.rowid, Table.USERS_CATEGORIES, UsersCategoriesTableField.name);
    

    @Override
    public boolean addCategory(Category category) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::addCategory(): No se pudo establecer conexión a la base de datos.");
            return false;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_ADD_CATEGORY)){
            pstmt.setString(1, category.getName());
            pstmt.executeUpdate();
            return true;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::addCategory(): "+ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean removeCategoryById(Long id) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::removeCategoryById(): No se pudo establecer conexión a la base de datos.");
            return false;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_REMOVE_CATEGOY_BY_ID)){
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            return true;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::removeCategoryById(): "+ex.getMessage());
        }
        return false;
    }

    @Override
    public ArrayList<Category> getAllCategories() {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getAllCategories(): No se pudo establecer conexión a la base de datos.");
            return null;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL_CATEGORIES)){
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Category> categories = new ArrayList<>();
            while(rs.next()){
                Category c = new Category(rs.getLong(UsersCategoriesTableField.rowid.toString()), rs.getString(UsersCategoriesTableField.name.toString()));
                categories.add(c);
            }
            return categories;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getAllCategories(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Category> getCategoriesByName(String name) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getCategoriesByName(): No se pudo establecer conexión a la base de datos.");
            return null;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_GET_CATEGORIES_BY_NAME)){
            pstmt.setString(1, "%"+name+"%");
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Category> categories = new ArrayList<>();
            while(rs.next()){
                Category c = new Category(rs.getLong(UsersCategoriesTableField.rowid.toString()), rs.getString(UsersCategoriesTableField.name.toString()));
                categories.add(c);
            }
            return categories;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getCategoriesByName(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public Category getCategoryById(Long id) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getCategoryById(): No se pudo establecer conexión a la base de datos.");
            return null;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_GET_CATEGORY_BY_ID)){
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                Category c = new Category(rs.getLong(UsersCategoriesTableField.rowid.toString()), rs.getString(UsersCategoriesTableField.name.toString()));
                return c;
            }
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getCategoryById(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean updateCategory(Category category) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::updateCategory(): No se pudo establecer conexión a la base de datos.");
            return false;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE_CATEGORY)){
            pstmt.setString(1, category.getName());
            pstmt.setLong(2, category.getId());
            pstmt.executeUpdate();
            return true;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::updateCategory(): "+ex.getMessage());
        }
        return false;
    }
    
}
