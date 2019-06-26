/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.dao.impl;

import com.loyder.software.model.dao.DeletedUserLogDao;
import com.loyder.software.model.dao.config.DatabaseConfig;
import com.loyder.software.model.dao.config.DatabaseConfig.DeletedUserLogTableField;
import com.loyder.software.model.dao.config.DatabaseConfig.Table;
import com.loyder.software.model.entities.DeletedUserLogRegister;
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
public class IDeletedUserLogDao implements DeletedUserLogDao{
    
    private IDeletedUserLogDao() {
    }

    private static DeletedUserLogDao deletedUserLogDao;

    public static DeletedUserLogDao getDeletedUserLogDao() {
        if (deletedUserLogDao == null) {
            deletedUserLogDao = new IDeletedUserLogDao();
        }
        return deletedUserLogDao;
    }
    
    private static final String SQL_GET_ALL_REGISTERS = String.format("SELECT rowid,* FROM %s", Table.DELETED_USER_LOG);
    private static final String SQL_REMOVE_ALL_REGISTERS = String.format("DELETE FROM %s", Table.DELETED_USER_LOG);
    private static final String SQL_ADD_REGISTER = String.format("INSERT INTO %s (%s,%s,%s) VALUES (?,?,?)", Table.DELETED_USER_LOG, DeletedUserLogTableField.user_id, DeletedUserLogTableField.child_id, DeletedUserLogTableField.parent_id);

    @Override
    public ArrayList<DeletedUserLogRegister> getDeletedUserLogRegisters() {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getDeletedUserLogRegisters(): No se pudo establecer la conexión con la base de datos.");
            return null;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL_REGISTERS)){
            ResultSet rs = pstmt.executeQuery();
            ArrayList<DeletedUserLogRegister> registers = new ArrayList<>();
            while(rs.next()){
                DeletedUserLogRegister p = new DeletedUserLogRegister(
                        rs.getLong(DeletedUserLogTableField.rowid.toString()),
                        rs.getLong(DeletedUserLogTableField.user_id.toString()), 
                        rs.getLong(DeletedUserLogTableField.child_id.toString()), 
                        rs.getLong(DeletedUserLogTableField.parent_id.toString()) );
                registers.add(p);
            }
            return registers;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getDeletedUserLogRegisters(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteAllRegisters() {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::deleteAllRegisters(): No se pudo establecer la conexión con la base de datos.");
            return false;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_REMOVE_ALL_REGISTERS)){
            pstmt.executeUpdate();
            return true;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::deleteAllRegisters(): "+ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean addRegister(DeletedUserLogRegister dulr) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::addRegister(): No se pudo establecer la conexión con la base de datos.");
            return false;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_ADD_REGISTER)){
            pstmt.setLong(1, dulr.getUserId());
            pstmt.setLong(2, dulr.getChildId());
            pstmt.setLong(3, dulr.getParentId());
            pstmt.executeUpdate();
            return true;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::addRegister(): "+ex.getMessage());
        }
        return false;
    }
    
}
