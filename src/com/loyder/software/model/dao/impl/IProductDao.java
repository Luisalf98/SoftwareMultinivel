/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.dao.impl;

import com.loyder.software.model.dao.ProductDao;
import com.loyder.software.model.dao.config.DatabaseConfig;
import com.loyder.software.model.dao.config.DatabaseConfig.ProductsTableField;
import com.loyder.software.model.dao.config.DatabaseConfig.Table;
import com.loyder.software.model.entities.Product;
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
public class IProductDao implements ProductDao{
    
    private static ProductDao productDao;
    private IProductDao(){}
    
    public static ProductDao getProductDao(){
        if(productDao==null){
            productDao = new IProductDao();
        }
        return productDao;
    }
    
    private static final String SQL_ADD_PRODUCT = String.format("INSERT INTO %s (%s,%s,%s) VALUES (?,?,?)", Table.PRODUCTS, ProductsTableField.name, ProductsTableField.description, ProductsTableField.price);
    private static final String SQL_GET_PRODUCT_BY_ID = String.format("SELECT %s,* FROM %s WHERE %s=?", ProductsTableField.rowid, Table.PRODUCTS, ProductsTableField.rowid);
    private static final String SQL_GET_PRODUCT_BY_NAME = String.format("SELECT %s,* FROM %s WHERE %s LIKE ?", ProductsTableField.rowid, Table.PRODUCTS, ProductsTableField.name);
    private static final String SQL_GET_ALL_PRODUCTS = String.format("SELECT %s,* FROM %s", ProductsTableField.rowid, Table.PRODUCTS);
    private static final String SQL_REMOVE_PRODUCT_BY_ID = String.format("DELETE FROM %s WHERE %s = ?", Table.PRODUCTS, ProductsTableField.rowid);
    private static final String SQL_UPDATE_PRODUCT = String.format("UPDATE %s SET %s=?, %s=?, %s=? WHERE %s=?", Table.PRODUCTS, ProductsTableField.name, ProductsTableField.description, ProductsTableField.price, ProductsTableField.rowid);
    
    @Override
    public boolean addProduct(Product product) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::addProduct(): No se pudo establecer la conexión con la base de datos.");
            return false;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_ADD_PRODUCT)){
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setDouble(3, product.getPrice());
            pstmt.executeUpdate();
            return true;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::addProduct(): "+ex.getMessage());
        }
        return false;
    }

    @Override
    public Product getProductById(Long id) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getProductById(): No se pudo establecer la conexión con la base de datos.");
            return null;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_GET_PRODUCT_BY_ID)){
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                Product p = new Product(rs.getLong(ProductsTableField.rowid.toString()), rs.getString(ProductsTableField.name.toString()), rs.getString(ProductsTableField.description.toString()), rs.getDouble(ProductsTableField.price.toString()));
                return p;
            }
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getProductById(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Product> getProductByName(String name) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getProductByName(): No se pudo establecer la conexión con la base de datos.");
            return null;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_GET_PRODUCT_BY_NAME)){
            pstmt.setString(1, "%"+name+"%");
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Product> products = new ArrayList<>();
            while(rs.next()){
                Product p = new Product(rs.getLong(ProductsTableField.rowid.toString()), rs.getString(ProductsTableField.name.toString()), rs.getString(ProductsTableField.description.toString()), rs.getDouble(ProductsTableField.price.toString()));
                products.add(p);
            }
            return products;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getProductByName(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean removeProductById(Long id) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::removeProductById(): No se pudo establecer la conexión con la base de datos.");
            return false;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_REMOVE_PRODUCT_BY_ID)){
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            return true;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::removeProductById(): "+ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateProduct(Product product) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::updateProduct(): No se pudo establecer la conexión con la base de datos.");
            return false;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE_PRODUCT)){
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setLong(4, product.getId());
            pstmt.executeUpdate();
            return true;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::updateProduct(): "+ex.getMessage());
        }
        return false;
    }

    @Override
    public ArrayList<Product> getAllProducts() {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getAllProducts(): No se pudo establecer la conexión con la base de datos.");
            return null;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL_PRODUCTS)){
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Product> products = new ArrayList<>();
            while(rs.next()){
                Product p = new Product(rs.getLong(ProductsTableField.rowid.toString()), rs.getString(ProductsTableField.name.toString()), rs.getString(ProductsTableField.description.toString()), rs.getDouble(ProductsTableField.price.toString()));
                products.add(p);
            }
            return products;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getAllProducts(): "+ex.getMessage());
        }
        return null;
    }
}
