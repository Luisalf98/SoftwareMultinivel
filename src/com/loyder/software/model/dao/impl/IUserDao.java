/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.dao.impl;

import com.loyder.software.main.ApplicationStarter;
import com.loyder.software.model.dao.UserDao;
import com.loyder.software.model.dao.config.DatabaseConfig;
import com.loyder.software.model.dao.config.DatabaseConfig.Table;
import com.loyder.software.model.dao.config.DatabaseConfig.UsersTableField;
import com.loyder.software.model.dao.config.DatabaseConnection;
import com.loyder.software.model.entities.User;
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
public class IUserDao implements UserDao {
    
    private static final String SQL_ADD_USER = "INSERT INTO "+Table.USERS+" ("+UsersTableField.user_id+","+UsersTableField.name+","+UsersTableField.last_name+","+UsersTableField.address+","+UsersTableField.tel+","+UsersTableField.cel+","+UsersTableField.adder_id+","+UsersTableField.entrance_date+","+UsersTableField.category_id+") VALUES(?,?,?,?,?,?,?,?,?)";
    private static final String SQL_GET_USER_BY_ID = "SELECT rowid,* FROM "+Table.USERS+" WHERE "+UsersTableField.rowid+"=?";
    private static final String SQL_GET_USER_BY_USER_ID = "SELECT rowid,* FROM "+Table.USERS+" WHERE "+UsersTableField.user_id+"=?";
    private static final String SQL_GET_ALL_USERS = "SELECT rowid,* FROM "+Table.USERS;
    private static final String SQL_GET_USERS_BY_NAME = "SELECT rowid,* FROM "+Table.USERS+" WHERE "+UsersTableField.name+" LIKE ? OR "+UsersTableField.last_name+" LIKE ?";
    private static final String SQL_GET_USER_BY_ID_AND_CATEGORY_ID = "SELECT rowid,* FROM "+Table.USERS+" WHERE "+UsersTableField.rowid+"=? AND "+UsersTableField.category_id+"=?";
    private static final String SQL_GET_USER_BY_USER_ID_AND_CATEGORY_ID = "SELECT rowid,* FROM "+Table.USERS+" WHERE "+UsersTableField.user_id+"=? AND "+UsersTableField.category_id+"=?";
    private static final String SQL_GET_ALL_USERS_BY_CATEGORY_ID = "SELECT rowid,* FROM "+Table.USERS+" WHERE "+UsersTableField.category_id+"=?";
    private static final String SQL_GET_USERS_BY_NAME_AND_CATEGORY_ID = "SELECT rowid,* FROM "+Table.USERS+" WHERE ("+UsersTableField.name+" LIKE ? OR "+UsersTableField.last_name+" LIKE ?) AND "+UsersTableField.category_id+"=?";
    private static final String SQL_REMOVE_USER_BY_ID = "UPDATE "+Table.USERS+" SET "+UsersTableField.adder_id+" = ? WHERE "+UsersTableField.rowid+" = ?";
    private static final String SQL_REASSIGN_REMOVED_USER_CHILDS = "UPDATE "+Table.USERS+" SET "+UsersTableField.adder_id+" = ? WHERE "+UsersTableField.adder_id+" = ?";
    private static final String SQL_GET_USER_CHILDS_BY_ID = "SELECT "+UsersTableField.rowid+",* FROM "+Table.USERS+" WHERE "+UsersTableField.adder_id+"=?";
    private static final String SQL_GET_USER_CHILDS_BY_ID_AND_CHILD_NAME = "SELECT "+UsersTableField.rowid+",* FROM "+Table.USERS+" WHERE "+UsersTableField.adder_id+"=? AND ("+UsersTableField.name+" LIKE ? OR "+UsersTableField.last_name+" LIKE ?)";
    private static final String SQL_GET_USER_CHILD_BY_ID_AND_CHILD_ID = "SELECT "+UsersTableField.rowid+",* FROM "+Table.USERS+" WHERE "+UsersTableField.adder_id+"=? AND "+UsersTableField.rowid+"=?";
    private static final String SQL_GET_USER_CHILD_BY_ID_AND_CHILD_USER_ID = "SELECT "+UsersTableField.rowid+",* FROM "+Table.USERS+" WHERE "+UsersTableField.adder_id+"=? AND "+UsersTableField.user_id+"=?";
    private static final String SQL_UPDATE_USER = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?", Table.USERS, UsersTableField.user_id, UsersTableField.name, UsersTableField.last_name, UsersTableField.address, UsersTableField.tel, UsersTableField.cel, UsersTableField.category_id, UsersTableField.rowid);
    
    private IUserDao(){}
    
    private static UserDao userDao;
    
    public static UserDao getUserDao(){
        if(userDao==null){
            userDao = new IUserDao();
        }
        return userDao;
    }
    
    @Override
    public boolean addUser(User user) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn == null){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::addUser(): No se pudo establecer conexión con la base de datos.");
            return false;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_ADD_USER, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setLong(1, user.getUserId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getLastName());
            pstmt.setString(4, user.getAddress());
            pstmt.setString(5, user.getTel());
            pstmt.setString(6, user.getCel());
            pstmt.setLong(7, user.getAdderId());
            pstmt.setLong(8, user.getEntranceDate());
            pstmt.setLong(9, user.getCategoryId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::addUser(): "+ex.getMessage());
        }
        return false;
    }

    @Override
    public User getUserById(Long rowId) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn == null){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getUserById(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        User u = null;
        try(PreparedStatement ps = conn.prepareStatement(SQL_GET_USER_BY_ID)){
            ps.setLong(1, rowId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                u = new User(rs.getLong(UsersTableField.rowid.toString()), rs.getLong(UsersTableField.user_id.toString()), rs.getString(UsersTableField.name.toString()), rs.getString(UsersTableField.last_name.toString()), rs.getString(UsersTableField.address.toString()), rs.getString(UsersTableField.tel.toString()), rs.getString(UsersTableField.cel.toString()), rs.getLong(UsersTableField.adder_id.toString()), rs.getLong(UsersTableField.entrance_date.toString()), rs.getLong(UsersTableField.category_id.toString()));
            }
            return u;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getUserById(): "+ex.getMessage());
        }
        return null;
    }
    
    @Override
    public User getUserByUserId(Long userId) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn == null){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getUserByUserId(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        User u = null;
        try(PreparedStatement ps = conn.prepareStatement(SQL_GET_USER_BY_USER_ID)){
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                u = new User(rs.getLong(UsersTableField.rowid.toString()), rs.getLong(UsersTableField.user_id.toString()), rs.getString(UsersTableField.name.toString()), rs.getString(UsersTableField.last_name.toString()), rs.getString(UsersTableField.address.toString()), rs.getString(UsersTableField.tel.toString()), rs.getString(UsersTableField.cel.toString()), rs.getLong(UsersTableField.adder_id.toString()), rs.getLong(UsersTableField.entrance_date.toString()), rs.getLong(UsersTableField.category_id.toString()));
            }
            return u;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getUserByUserId(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<User> getUserChildsById(Long id) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn == null){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getUserChildsById(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        User user = (id.equals(ApplicationStarter.COMPANY_ROOT.getId()))?ApplicationStarter.COMPANY_ROOT:DatabaseConnection.getUserDao().getUserById(id);
        if(user == null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getUserChildsById(): No fue posible encontrar al usuario.");
            return null;
        }
        ArrayList<User> users = new ArrayList<>();
        try(PreparedStatement ps = conn.prepareStatement(SQL_GET_USER_CHILDS_BY_ID)){
            ps.setLong(1, user.getId());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                User u = new User(rs.getLong(UsersTableField.rowid.toString()), rs.getLong(UsersTableField.user_id.toString()), rs.getString(UsersTableField.name.toString()), rs.getString(UsersTableField.last_name.toString()), rs.getString(UsersTableField.address.toString()), rs.getString(UsersTableField.tel.toString()), rs.getString(UsersTableField.cel.toString()), rs.getLong(UsersTableField.adder_id.toString()), rs.getLong(UsersTableField.entrance_date.toString()), rs.getLong(UsersTableField.category_id.toString()));
                users.add(u);
            }
            return users;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getUserNetworkChildsById(): "+ex.getMessage());
        }
        return null;
    }
    
    private boolean setUserAdder(Long id, Long newAdderId){
        Connection conn = DatabaseConfig.getConnection();
        if(conn == null){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::setUserAdder(): No se pudo establecer conexión con la base de datos.");
            return false;
        }
        try(PreparedStatement ps = conn.prepareStatement(SQL_REMOVE_USER_BY_ID)){
            ps.setLong(1, newAdderId);
            ps.setLong(2, id);
            ps.executeUpdate();
            return true;            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::setUserAdder(): "+ex.getMessage());
        }
        return false;
    }
    
    private boolean reassignRemovedUserChilds(Long oldAdderId, Long newAdderId){
        Connection conn = DatabaseConfig.getConnection();
        if(conn == null){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::reassignRemovedUserChilds(): No se pudo establecer conexión con la base de datos.");
            return false;
        }
        try(PreparedStatement ps = conn.prepareStatement(SQL_REASSIGN_REMOVED_USER_CHILDS)){
            ps.setLong(1, newAdderId);
            ps.setLong(2, oldAdderId);
            ps.executeUpdate();
            return true;            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::reassignRemovedUserChilds(): "+ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean removeUserById(Long id) {
        User user = getUserById(id);
        return setUserAdder(id, -1L) && reassignRemovedUserChilds(id, user.getAdderId());
    }

    @Override
    public ArrayList<User> getUsersByName(String name) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn == null){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getUsersByName(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try(PreparedStatement ps = conn.prepareStatement(SQL_GET_USERS_BY_NAME)){
            String nameExp = "%"+name+"%";
            ps.setString(1, nameExp);
            ps.setString(2, nameExp);
            ResultSet rs = ps.executeQuery();
            ArrayList<User> users = new ArrayList<>();
            while(rs.next()){
                User u = new User(rs.getLong(UsersTableField.rowid.toString()), rs.getLong(UsersTableField.user_id.toString()), rs.getString(UsersTableField.name.toString()), rs.getString(UsersTableField.last_name.toString()), rs.getString(UsersTableField.address.toString()), rs.getString(UsersTableField.tel.toString()), rs.getString(UsersTableField.cel.toString()), rs.getLong(UsersTableField.adder_id.toString()), rs.getLong(UsersTableField.entrance_date.toString()), rs.getLong(UsersTableField.category_id.toString()));
                users.add(u);
            }
            return users;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getUsersByName(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<User> getAllUsers() {
        Connection conn = DatabaseConfig.getConnection();
        if(conn == null){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getAllUsers(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try(PreparedStatement ps = conn.prepareStatement(SQL_GET_ALL_USERS)){
            ResultSet rs = ps.executeQuery();
            ArrayList<User> users = new ArrayList<>();
            while(rs.next()){
                User u = new User(rs.getLong(UsersTableField.rowid.toString()), rs.getLong(UsersTableField.user_id.toString()), rs.getString(UsersTableField.name.toString()), rs.getString(UsersTableField.last_name.toString()), rs.getString(UsersTableField.address.toString()), rs.getString(UsersTableField.tel.toString()), rs.getString(UsersTableField.cel.toString()), rs.getLong(UsersTableField.adder_id.toString()), rs.getLong(UsersTableField.entrance_date.toString()), rs.getLong(UsersTableField.category_id.toString()));
                users.add(u);
            }
            return users;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getAllUsers(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public User getUserChildByIdAndChildId(Long id, Long childId) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn == null){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getUserChildByIdAndChildId(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        User adder = (id.equals(ApplicationStarter.COMPANY_ROOT.getId()))?ApplicationStarter.COMPANY_ROOT:DatabaseConnection.getUserDao().getUserById(id);
        if(adder == null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getUserChildByIdAndChildId(): No fue posible encontrar al usuario.");
            return null;
        }
        User user = null;
        try(PreparedStatement ps = conn.prepareStatement(SQL_GET_USER_CHILD_BY_ID_AND_CHILD_ID)){
            ps.setLong(1, id);
            ps.setLong(2, childId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                user = new User(rs.getLong(UsersTableField.rowid.toString()), rs.getLong(UsersTableField.user_id.toString()), rs.getString(UsersTableField.name.toString()), rs.getString(UsersTableField.last_name.toString()), rs.getString(UsersTableField.address.toString()), rs.getString(UsersTableField.tel.toString()), rs.getString(UsersTableField.cel.toString()), rs.getLong(UsersTableField.adder_id.toString()), rs.getLong(UsersTableField.entrance_date.toString()), rs.getLong(UsersTableField.category_id.toString()));
            }
            return user;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getUserChildByIdAndChildId(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public User getUserChildByIdAndChildUserId(Long id, Long childUserId) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn == null){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getUserChildByIdAndChildUserId(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        User adder = (id.equals(ApplicationStarter.COMPANY_ROOT.getId()))?ApplicationStarter.COMPANY_ROOT:DatabaseConnection.getUserDao().getUserById(id);
        if(adder == null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getUserChildByIdAndChildUserId(): No fue posible encontrar al usuario.");
            return null;
        }
        User user = null;
        try(PreparedStatement ps = conn.prepareStatement(SQL_GET_USER_CHILD_BY_ID_AND_CHILD_USER_ID)){
            ps.setLong(1, id);
            ps.setLong(2, childUserId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                user = new User(rs.getLong(UsersTableField.rowid.toString()), rs.getLong(UsersTableField.user_id.toString()), rs.getString(UsersTableField.name.toString()), rs.getString(UsersTableField.last_name.toString()), rs.getString(UsersTableField.address.toString()), rs.getString(UsersTableField.tel.toString()), rs.getString(UsersTableField.cel.toString()), rs.getLong(UsersTableField.adder_id.toString()), rs.getLong(UsersTableField.entrance_date.toString()), rs.getLong(UsersTableField.category_id.toString()));
            }
            return user;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getUserChildByIdAndChildUserId(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<User> getUserChildsByIdAndChildName(Long id, String childName) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn == null){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getUserChildsByIdAndChildName(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        User user = (id.equals(ApplicationStarter.COMPANY_ROOT.getId()))?ApplicationStarter.COMPANY_ROOT:DatabaseConnection.getUserDao().getUserById(id);
        if(user == null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getUserChildsByIdAndChildName(): No fue posible encontrar al usuario.");
            return null;
        }
        ArrayList<User> users = new ArrayList<>();
        try(PreparedStatement ps = conn.prepareStatement(SQL_GET_USER_CHILDS_BY_ID_AND_CHILD_NAME)){
            ps.setLong(1, user.getId());
            ps.setString(2, "%"+childName+"%");
            ps.setString(3, "%"+childName+"%");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                User u = new User(rs.getLong(UsersTableField.rowid.toString()), rs.getLong(UsersTableField.user_id.toString()), rs.getString(UsersTableField.name.toString()), rs.getString(UsersTableField.last_name.toString()), rs.getString(UsersTableField.address.toString()), rs.getString(UsersTableField.tel.toString()), rs.getString(UsersTableField.cel.toString()), rs.getLong(UsersTableField.adder_id.toString()), rs.getLong(UsersTableField.entrance_date.toString()), rs.getLong(UsersTableField.category_id.toString()));
                users.add(u);
            }
            return users;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getUserChildsByIdAndChildName(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public User getUserByIdAndCategoryId(Long userId, Long categoryId) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn == null){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getUserByIdAndCategoryId(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        User u = null;
        try(PreparedStatement ps = conn.prepareStatement(SQL_GET_USER_BY_ID_AND_CATEGORY_ID)){
            ps.setLong(1, userId);
            ps.setLong(2, categoryId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                u = new User(rs.getLong(UsersTableField.rowid.toString()), rs.getLong(UsersTableField.user_id.toString()), rs.getString(UsersTableField.name.toString()), rs.getString(UsersTableField.last_name.toString()), rs.getString(UsersTableField.address.toString()), rs.getString(UsersTableField.tel.toString()), rs.getString(UsersTableField.cel.toString()), rs.getLong(UsersTableField.adder_id.toString()), rs.getLong(UsersTableField.entrance_date.toString()), rs.getLong(UsersTableField.category_id.toString()));
            }
            return u;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getUserByIdAndCategoryId(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public User getUserByUserIdAndCategoryId(Long userId, Long categoryId) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn == null){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getUserByUserIdAndCategoryId(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        User u = null;
        try(PreparedStatement ps = conn.prepareStatement(SQL_GET_USER_BY_USER_ID_AND_CATEGORY_ID)){
            ps.setLong(1, userId);
            ps.setLong(2, categoryId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                u = new User(rs.getLong(UsersTableField.rowid.toString()), rs.getLong(UsersTableField.user_id.toString()), rs.getString(UsersTableField.name.toString()), rs.getString(UsersTableField.last_name.toString()), rs.getString(UsersTableField.address.toString()), rs.getString(UsersTableField.tel.toString()), rs.getString(UsersTableField.cel.toString()), rs.getLong(UsersTableField.adder_id.toString()), rs.getLong(UsersTableField.entrance_date.toString()), rs.getLong(UsersTableField.category_id.toString()));
            }
            return u;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getUserByUserIdAndCategoryId(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<User> getUsersByNameAndCategoryId(String name, Long categoryId) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn == null){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getUsersByNameAndCategoryId(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try(PreparedStatement ps = conn.prepareStatement(SQL_GET_USERS_BY_NAME_AND_CATEGORY_ID)){
            String nameExp = "%"+name+"%";
            ps.setString(1, nameExp);
            ps.setString(2, nameExp);
            ps.setLong(3, categoryId);
            ResultSet rs = ps.executeQuery();
            ArrayList<User> users = new ArrayList<>();
            while(rs.next()){
                User u = new User(rs.getLong(UsersTableField.rowid.toString()), rs.getLong(UsersTableField.user_id.toString()), rs.getString(UsersTableField.name.toString()), rs.getString(UsersTableField.last_name.toString()), rs.getString(UsersTableField.address.toString()), rs.getString(UsersTableField.tel.toString()), rs.getString(UsersTableField.cel.toString()), rs.getLong(UsersTableField.adder_id.toString()), rs.getLong(UsersTableField.entrance_date.toString()), rs.getLong(UsersTableField.category_id.toString()));
                users.add(u);
            }
            return users;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getUsersByNameAndCategoryId(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<User> getAllUsersByCategoryId(Long categoryId) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn == null){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getAllUsersByCategoryId(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try(PreparedStatement ps = conn.prepareStatement(SQL_GET_ALL_USERS_BY_CATEGORY_ID)){
            ps.setLong(1, categoryId);
            ResultSet rs = ps.executeQuery();
            ArrayList<User> users = new ArrayList<>();
            while(rs.next()){
                User u = new User(rs.getLong(UsersTableField.rowid.toString()), rs.getLong(UsersTableField.user_id.toString()), rs.getString(UsersTableField.name.toString()), rs.getString(UsersTableField.last_name.toString()), rs.getString(UsersTableField.address.toString()), rs.getString(UsersTableField.tel.toString()), rs.getString(UsersTableField.cel.toString()), rs.getLong(UsersTableField.adder_id.toString()), rs.getLong(UsersTableField.entrance_date.toString()), rs.getLong(UsersTableField.category_id.toString()));
                users.add(u);
            }
            return users;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::getAllUsersByCategoryId(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean updateUser(User user) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn == null){
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::updateUser(): No se pudo establecer conexión con la base de datos.");
            return false;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE_USER)) {
            pstmt.setLong(1, user.getUserId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getLastName());
            pstmt.setString(4, user.getAddress());
            pstmt.setString(5, user.getTel());
            pstmt.setString(6, user.getCel());
            pstmt.setLong(7, user.getCategoryId());
            pstmt.setLong(8, user.getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, IUserDao.class.getName()+"::updateUser(): "+ex.getMessage());
        }
        return false;
    }


}
