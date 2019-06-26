/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.dao;

import com.loyder.software.model.entities.User;
import java.util.ArrayList;

/**
 *
 * @author Luis Perez
 */
public interface UserDao {
    
    public boolean setUserAdder(Long id, Long newAdderId);
    public boolean removeUserById(Long id);
    public boolean addUser(User user);
    public boolean updateUser(User user);
    public User getUserById(Long userId);
    public User getUserByUserId(Long userId);
    public ArrayList<User> getUsersByName(String name);
    public ArrayList<User> getUsersByParentId(Long parentId);
    public ArrayList<User> getAllUsers();
    public User getUserByIdAndCategoryId(Long userId, Long categoryId);
    public User getUserByUserIdAndCategoryId(Long userId, Long categoryId);
    public ArrayList<User> getUsersByNameAndCategoryId(String name, Long categoryId);
    public ArrayList<User> getAllUsersByCategoryId(Long categoryId);
    public ArrayList<User> getUserChildsById(Long id);
    public User getUserChildByIdAndChildId(Long id, Long childId);
    public User getUserChildByIdAndChildUserId(Long id, Long childUserId);
    public ArrayList<User> getUserChildsByIdAndChildName(Long id, String childName);
}
