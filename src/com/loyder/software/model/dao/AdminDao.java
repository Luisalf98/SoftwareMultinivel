/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.dao;

import com.loyder.software.model.entities.Admin;
import java.util.ArrayList;

/**
 *
 * @author Luis Perez
 */
public interface AdminDao {
    public boolean addAdmin(Admin admin);
    public boolean deleteAdminById(Long adminId);
    public Admin getAdminById(Long adminId);
    public Admin getAdminByAdminId(Long adminId);
    public Admin getAdminByUserName(String username);
    public ArrayList<Admin> getAdminByName(String name);
    public ArrayList<Admin> getAllAdmins();
}
