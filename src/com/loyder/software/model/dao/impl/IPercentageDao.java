/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.dao.impl;

import com.loyder.software.model.dao.PercentageDao;
import com.loyder.software.model.dao.config.DatabaseConfig;
import com.loyder.software.model.dao.config.DatabaseConfig.LevelsPecentagesTableField;
import com.loyder.software.model.dao.config.DatabaseConfig.Table;
import com.loyder.software.model.entities.Percentage;
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
public class IPercentageDao implements PercentageDao{
    
    private IPercentageDao(){}
    
    private static PercentageDao percentageDao;
    
    public static PercentageDao getPercentageDao(){
        if(percentageDao==null){
            percentageDao = new IPercentageDao();
        }
        return percentageDao;
    }
    
    private static final String SQL_DELETE_LEVEL_PERCENTAGE = String.format("DELETE FROM %s WHERE %s=(SELECT MAX(%s) FROM %s)", Table.LEVELS_PERCENTAGES, LevelsPecentagesTableField.rowid, LevelsPecentagesTableField.rowid, Table.LEVELS_PERCENTAGES);
    private static final String SQL_GET_ALL_LEVEL_PERCENTAGES = String.format("SELECT %s,* FROM %s", LevelsPecentagesTableField.rowid, Table.LEVELS_PERCENTAGES);
    private static final String SQL_ADD_LEVEL_PERCENTAGE = String.format("INSERT INTO %s (%s) VALUES (?);", Table.LEVELS_PERCENTAGES, LevelsPecentagesTableField.percentage);
    private static final String SQL_UPDATE_PERCENTAGE = String.format("UPDATE %s SET %s=? WHERE %s=?", Table.LEVELS_PERCENTAGES, LevelsPecentagesTableField.percentage, LevelsPecentagesTableField.rowid);
    private static final String SQL_GET_LEVEL_PERCENTAGE_BY_ID = String.format("SELECT %s,* FROM %s WHERE %s=?", LevelsPecentagesTableField.rowid, Table.LEVELS_PERCENTAGES, LevelsPecentagesTableField.rowid);
    
    @Override
    public boolean addPercentage(Double percentage) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::addPercentage(): No se pudo establecer conexión a la base de datos.");
            return false;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_ADD_LEVEL_PERCENTAGE)){
            pstmt.setDouble(1, percentage);
            pstmt.executeUpdate();
            return true;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::addPercentage(): "+ex.getMessage());
        }
        return false;
    }

    @Override
    public Percentage getPercentageById(Long id) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getPercentageById(): No se pudo establecer conexión a la base de datos.");
            return null;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_GET_LEVEL_PERCENTAGE_BY_ID)){
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                Percentage p = new Percentage(rs.getLong(LevelsPecentagesTableField.rowid.toString()), rs.getDouble(LevelsPecentagesTableField.percentage.toString()));
                return p;
            }
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getPercentageById(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Percentage> getAllPercentages() {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getAllPercentages(): No se pudo establecer conexión a la base de datos.");
            return null;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL_LEVEL_PERCENTAGES)){
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Percentage> percentages = new ArrayList<>();
            while(rs.next()){
                Percentage p = new Percentage(rs.getLong(LevelsPecentagesTableField.rowid.toString()), rs.getDouble(LevelsPecentagesTableField.percentage.toString()));
                percentages.add(p);
            }
            return percentages;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getAllPercentages(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean removePercentage() {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::removePercentage(): No se pudo establecer conexión a la base de datos.");
            return false;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE_LEVEL_PERCENTAGE)){
            pstmt.executeUpdate();
            return true;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::removePercentage(): "+ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean updatePercentage(Percentage percentage) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::updatePercentage(): No se pudo establecer conexión a la base de datos.");
            return false;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE_PERCENTAGE)){
            pstmt.setDouble(1, percentage.getPercentage());
            pstmt.setLong(2, percentage.getId());
            pstmt.executeUpdate();
            return true;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::updatePercentage(): "+ex.getMessage());
        }
        return false;
    }
    
}
