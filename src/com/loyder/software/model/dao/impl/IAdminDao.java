/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.dao.impl;

import com.loyder.software.model.dao.AdminDao;
import com.loyder.software.model.dao.config.DatabaseConfig;
import com.loyder.software.model.dao.config.DatabaseConfig.Table;
import com.loyder.software.model.dao.config.DatabaseConfig.AdminsTableField;
import com.loyder.software.model.entities.Admin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Luis Perez
 */
public class IAdminDao implements AdminDao {

    private static final String SQL_ADD_ADMIN = "INSERT INTO " + Table.ADMINS + " (" + AdminsTableField.admin_id + "," + AdminsTableField.name + "," + AdminsTableField.last_name + "," + AdminsTableField.address + "," + AdminsTableField.tel + "," + AdminsTableField.cel + "," + AdminsTableField.username + "," + AdminsTableField.password + ") VALUES(?,?,?,?,?,?,?,?)";
    private static final String SQL_GET_ADMIN_BY_ID = "SELECT rowid,* FROM " + Table.ADMINS + " WHERE " + AdminsTableField.rowid + "=?";
    private static final String SQL_GET_ADMIN_BY_ADMINID = "SELECT rowid,* FROM " + Table.ADMINS + " WHERE " + AdminsTableField.admin_id + "=?";
    private static final String SQL_GET_ADMIN_BY_USERNAME = "SELECT rowid,* FROM " + Table.ADMINS + " WHERE " + AdminsTableField.username + "=?";
    private static final String SQL_GET_ALL_ADMINS = "SELECT rowid,* FROM " + Table.ADMINS;
    private static final String SQL_GET_ADMIN_BY_NAME = "SELECT rowid,* FROM " + Table.ADMINS + " WHERE " + AdminsTableField.name + " LIKE '%?%' OR " + AdminsTableField.last_name + " LIKE %?%";
    private static final String SQL_DELETE_ADMIN_BY_ID = "DELETE FROM " + Table.ADMINS + " WHERE " + AdminsTableField.rowid + "=?";

    private IAdminDao() {
    }

    private static AdminDao adminDao;

    public static AdminDao getAdminDao() {
        if (adminDao == null) {
            adminDao = new IAdminDao();
        }
        return adminDao;
    }

    @Override
    public boolean addAdmin(Admin admin) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, IAdminDao.class.getName() + "::addAdmin(): No se pudo establecer conexión con la base de datos.");
            return false;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_ADD_ADMIN, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, admin.getAdminId());
            pstmt.setString(2, admin.getName());
            pstmt.setString(3, admin.getLastName());
            pstmt.setString(4, admin.getAddress());
            pstmt.setString(5, admin.getTel());
            pstmt.setString(6, admin.getCel());
            pstmt.setString(7, admin.getUsername());
            pstmt.setString(8, admin.getPassword());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, IAdminDao.class.getName() + "::addAdmin(): " + ex.getMessage());
        }
        return false;
    }

    @Override
    public Admin getAdminById(Long rowId) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, IAdminDao.class.getName() + "::getAdminById(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        Admin u = null;
        try (PreparedStatement ps = conn.prepareStatement(SQL_GET_ADMIN_BY_ID)) {
            ps.setLong(1, rowId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                u = new Admin(rs.getLong(AdminsTableField.rowid.toString()), rs.getLong(AdminsTableField.admin_id.toString()), rs.getString(AdminsTableField.name.toString()), rs.getString(AdminsTableField.last_name.toString()), rs.getString(AdminsTableField.username.toString()), rs.getString(AdminsTableField.password.toString()), rs.getString(AdminsTableField.address.toString()), rs.getString(AdminsTableField.tel.toString()), rs.getString(AdminsTableField.cel.toString()));
            }
            return u;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, IAdminDao.class.getName() + "::getAdminById(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public Admin getAdminByAdminId(Long adminId) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, IAdminDao.class.getName() + "::getAdminByAdminId(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        Admin u = null;
        try (PreparedStatement ps = conn.prepareStatement(SQL_GET_ADMIN_BY_ADMINID)) {
            ps.setLong(1, adminId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                u = new Admin(rs.getLong(AdminsTableField.rowid.toString()), rs.getLong(AdminsTableField.admin_id.toString()), rs.getString(AdminsTableField.name.toString()), rs.getString(AdminsTableField.last_name.toString()), rs.getString(AdminsTableField.username.toString()), rs.getString(AdminsTableField.password.toString()), rs.getString(AdminsTableField.address.toString()), rs.getString(AdminsTableField.tel.toString()), rs.getString(AdminsTableField.cel.toString()));
            }
            return u;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, IAdminDao.class.getName() + "::getAdminByAdminId(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Admin> getAllAdmins() {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, IAdminDao.class.getName() + "::getAllAdmins(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        ArrayList<Admin> admins = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQL_GET_ALL_ADMINS)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Admin u = new Admin(rs.getLong(AdminsTableField.rowid.toString()), rs.getLong(AdminsTableField.admin_id.toString()), rs.getString(AdminsTableField.name.toString()), rs.getString(AdminsTableField.last_name.toString()), rs.getString(AdminsTableField.username.toString()), rs.getString(AdminsTableField.password.toString()), rs.getString(AdminsTableField.address.toString()), rs.getString(AdminsTableField.tel.toString()), rs.getString(AdminsTableField.cel.toString()));
                admins.add(u);
            }
            return admins;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, IAdminDao.class.getName() + "::getAllAdmins(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Admin> getAdminByName(String name) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, IAdminDao.class.getName() + "::getAdminByName(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        ArrayList<Admin> admins = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQL_GET_ADMIN_BY_NAME)) {
            ps.setString(1, name);
            ps.setString(2, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Admin u = new Admin(rs.getLong(AdminsTableField.rowid.toString()), rs.getLong(AdminsTableField.admin_id.toString()), rs.getString(AdminsTableField.name.toString()), rs.getString(AdminsTableField.last_name.toString()), rs.getString(AdminsTableField.username.toString()), rs.getString(AdminsTableField.password.toString()), rs.getString(AdminsTableField.address.toString()), rs.getString(AdminsTableField.tel.toString()), rs.getString(AdminsTableField.cel.toString()));
                admins.add(u);
            }
            return admins;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, IAdminDao.class.getName() + "::getAdminByName(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public Admin getAdminByUserName(String username) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, IAdminDao.class.getName() + "::getAdminByUsername(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        Admin u = null;
        try (PreparedStatement ps = conn.prepareStatement(SQL_GET_ADMIN_BY_USERNAME)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                u = new Admin(rs.getLong(AdminsTableField.rowid.toString()), rs.getLong(AdminsTableField.admin_id.toString()), rs.getString(AdminsTableField.name.toString()), rs.getString(AdminsTableField.last_name.toString()), rs.getString(AdminsTableField.username.toString()), rs.getString(AdminsTableField.password.toString()), rs.getString(AdminsTableField.address.toString()), rs.getString(AdminsTableField.tel.toString()), rs.getString(AdminsTableField.cel.toString()));
            }
            return u;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, IAdminDao.class.getName() + "::getAdminByUsername(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteAdminById(Long adminId) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, IAdminDao.class.getName() + "::addAdmin(): No se pudo establecer conexión con la base de datos.");
            return false;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE_ADMIN_BY_ID)) {

            pstmt.setLong(1, adminId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, IAdminDao.class.getName() + "::deleteAdmin(): " + ex.getMessage());
        }
        return false;
    }

}
