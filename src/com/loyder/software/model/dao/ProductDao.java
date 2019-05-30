/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.dao;

import com.loyder.software.model.entities.Product;
import java.util.ArrayList;

/**
 *
 * @author Luis Perez
 */
public interface ProductDao {
    public boolean addProduct(Product product);
    public Product getProductById(Long id);
    public ArrayList<Product> getProductByName(String name);
    public ArrayList<Product> getAllProducts();
    public boolean removeProductById(Long id);
    public boolean updateProduct(Product product);
}
