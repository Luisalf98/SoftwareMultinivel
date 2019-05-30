/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.dao.impl;

import com.loyder.software.model.dao.BonusDao;
import com.loyder.software.model.dao.config.DatabaseConfig;
import com.loyder.software.model.dao.config.DatabaseConfig.BonusesTableField;
import com.loyder.software.model.dao.config.DatabaseConfig.Table;
import com.loyder.software.model.dao.config.DatabaseConnection;
import com.loyder.software.model.entities.Bonus;
import com.loyder.software.model.entities.Sale;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Luis Perez
 */
public class IBonusDao implements BonusDao{
    
    private IBonusDao(){}
    
    private static BonusDao bonusDao;
    
    public static BonusDao getBonusDao(){
        if(bonusDao==null){
            bonusDao = new IBonusDao();
        }
        return bonusDao;
    }
    
    private static final String SQL_ADD_BONUS = String.format("INSERT INTO %s (%s,%s,%s,%s,%s) VALUES (?,?,?,?,?)", Table.BONUSES, BonusesTableField.pecentage_id, BonusesTableField.sale_id, BonusesTableField.user_id, BonusesTableField.bonus, BonusesTableField.bonus_date);
    private static final String SQL_GET_BONUS_BY_ID = String.format("SELECT %s,* FROM %s WHERE %s=?", BonusesTableField.rowid, Table.BONUSES, BonusesTableField.rowid);
    private static final String SQL_GET_BONUS_BY_ID_AND_USER_ID = String.format("SELECT %s,* FROM %s WHERE %s=? AND %s=?", BonusesTableField.rowid, Table.BONUSES, BonusesTableField.rowid, BonusesTableField.user_id);
    private static final String SQL_GET_ALL_BONUSES = String.format("SELECT %s,* FROM %s", BonusesTableField.rowid, Table.BONUSES);
    private static final String SQL_GET_BONUSES_BY_USER_ID = String.format("SELECT %s,* FROM %s WHERE %s=?", BonusesTableField.rowid, Table.BONUSES, BonusesTableField.user_id);
    private static final String SQL_GET_BONUSES_BY_SALE_ID = String.format("SELECT %s,* FROM %s WHERE %s=?", BonusesTableField.rowid, Table.BONUSES, BonusesTableField.sale_id);
    private static final String SQL_GET_BONUSES_IN_DATE_RANGE = String.format("SELECT %s,* FROM %s WHERE %s BETWEEN ? AND ?", BonusesTableField.rowid, Table.BONUSES, BonusesTableField.bonus_date);
    private static final String SQL_GET_BONUSES_IN_DATE_RANGE_BY_USER_ID = String.format("SELECT %s,* FROM %s WHERE (%s BETWEEN ? AND ?) AND %s=?", BonusesTableField.rowid, Table.BONUSES, BonusesTableField.bonus_date, BonusesTableField.user_id);

    @Override
    public boolean addBonus(Bonus bonus) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::addBonus(): No se pudo establecer conexión a la base de datos.");
            return false;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_ADD_BONUS)){
            pstmt.setLong(1, bonus.getPercentageId());
            pstmt.setLong(2, bonus.getSaleId());
            pstmt.setLong(3, bonus.getUserId());
            pstmt.setDouble(4, bonus.getBonus());
            pstmt.setLong(5, bonus.getDate());
            pstmt.executeUpdate();
            return true;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::addBonus(): "+ex.getMessage());
        }
        return false;
    }

    @Override
    public Bonus getBonusById(Long id) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getBonusById(): No se pudo establecer conexión a la base de datos.");
            return null;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_GET_BONUS_BY_ID)){
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                Bonus b = new Bonus();
                b.setId(rs.getLong(BonusesTableField.rowid.toString()));
                b.setBonus(rs.getDouble(BonusesTableField.bonus.toString()));
                b.setDate(rs.getLong(BonusesTableField.bonus_date.toString()));
                b.setSaleId(rs.getLong(BonusesTableField.sale_id.toString()));
                b.setUserId(rs.getLong(BonusesTableField.user_id.toString()));
                b.setPercentageId(rs.getLong(BonusesTableField.pecentage_id.toString()));
                return b;
            }
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getBonusById(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Bonus> getBonusesByUserId(Long id) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getBonusByRecommenderId(): No se pudo establecer conexión a la base de datos.");
            return null;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_GET_BONUSES_BY_USER_ID)){
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Bonus> bonuses = new ArrayList<>();
            while(rs.next()){
                Bonus b = new Bonus();
                b.setId(rs.getLong(BonusesTableField.rowid.toString()));
                b.setBonus(rs.getDouble(BonusesTableField.bonus.toString()));
                b.setDate(rs.getLong(BonusesTableField.bonus_date.toString()));
                b.setSaleId(rs.getLong(BonusesTableField.sale_id.toString()));
                b.setUserId(rs.getLong(BonusesTableField.user_id.toString()));
                b.setPercentageId(rs.getLong(BonusesTableField.pecentage_id.toString()));
                bonuses.add(b);
            }
            return bonuses;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getBonusByRecommenderId(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Bonus> getBonusesBySaleId(Long id) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getBonusBySaleId(): No se pudo establecer conexión a la base de datos.");
            return null;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_GET_BONUSES_BY_SALE_ID)){
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Bonus> bonuses = new ArrayList<>();
            while(rs.next()){
                Bonus b = new Bonus();
                b.setId(rs.getLong(BonusesTableField.rowid.toString()));
                b.setBonus(rs.getDouble(BonusesTableField.bonus.toString()));
                b.setDate(rs.getLong(BonusesTableField.bonus_date.toString()));
                b.setSaleId(rs.getLong(BonusesTableField.sale_id.toString()));
                b.setUserId(rs.getLong(BonusesTableField.user_id.toString()));
                b.setPercentageId(rs.getLong(BonusesTableField.pecentage_id.toString()));
                bonuses.add(b);
            }
            return bonuses;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getBonusBySaleId(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Bonus> getBonusesInDateRange(Date d1, Date d2) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getBonusInDateRange(): No se pudo establecer conexión a la base de datos.");
            return null;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_GET_BONUSES_IN_DATE_RANGE)){
            pstmt.setLong(1, d1.getTime());
            pstmt.setLong(2, d2.getTime());
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Bonus> bonuses = new ArrayList<>();
            while(rs.next()){
                Bonus b = new Bonus();
                b.setId(rs.getLong(BonusesTableField.rowid.toString()));
                b.setBonus(rs.getDouble(BonusesTableField.bonus.toString()));
                b.setDate(rs.getLong(BonusesTableField.bonus_date.toString()));
                b.setSaleId(rs.getLong(BonusesTableField.sale_id.toString()));
                b.setUserId(rs.getLong(BonusesTableField.user_id.toString()));
                b.setPercentageId(rs.getLong(BonusesTableField.pecentage_id.toString()));
                bonuses.add(b);
            }
            return bonuses;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getBonusInDateRange(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Bonus> getBonusesInDateRangeByUserId(Date d1, Date d2, Long recommenderId) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getBonusInDateRangeByRecommenderId(): No se pudo establecer conexión a la base de datos.");
            return null;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_GET_BONUSES_IN_DATE_RANGE_BY_USER_ID)){
            pstmt.setLong(1, d1.getTime());
            pstmt.setLong(2, d2.getTime());
            pstmt.setLong(3, recommenderId);
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Bonus> bonuses = new ArrayList<>();
            while(rs.next()){
                Bonus b = new Bonus();
                b.setId(rs.getLong(BonusesTableField.rowid.toString()));
                b.setBonus(rs.getDouble(BonusesTableField.bonus.toString()));
                b.setDate(rs.getLong(BonusesTableField.bonus_date.toString()));
                b.setSaleId(rs.getLong(BonusesTableField.sale_id.toString()));
                b.setUserId(rs.getLong(BonusesTableField.user_id.toString()));
                b.setPercentageId(rs.getLong(BonusesTableField.pecentage_id.toString()));
                bonuses.add(b);
            }
            return bonuses;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getBonusInDateRangeByRecommenderId(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Bonus> getAllBonuses() {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getAllBonuses(): No se pudo establecer conexión a la base de datos.");
            return null;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL_BONUSES)){
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Bonus> bonuses = new ArrayList<>();
            while(rs.next()){
                Bonus b = new Bonus();
                b.setId(rs.getLong(BonusesTableField.rowid.toString()));
                b.setBonus(rs.getDouble(BonusesTableField.bonus.toString()));
                b.setDate(rs.getLong(BonusesTableField.bonus_date.toString()));
                b.setSaleId(rs.getLong(BonusesTableField.sale_id.toString()));
                b.setUserId(rs.getLong(BonusesTableField.user_id.toString()));
                b.setPercentageId(rs.getLong(BonusesTableField.pecentage_id.toString()));
                bonuses.add(b);
            }
            return bonuses;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getAllBonuses(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public Bonus getBonusByIdAndUserId(Long bonusId, Long recommenderId) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getBonusByIdByRecommenderId(): No se pudo establecer conexión a la base de datos.");
            return null;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_GET_BONUS_BY_ID_AND_USER_ID)){
            pstmt.setLong(1, bonusId);
            pstmt.setLong(2, recommenderId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                Bonus b = new Bonus();
                b.setId(rs.getLong(BonusesTableField.rowid.toString()));
                b.setBonus(rs.getDouble(BonusesTableField.bonus.toString()));
                b.setDate(rs.getLong(BonusesTableField.bonus_date.toString()));
                b.setSaleId(rs.getLong(BonusesTableField.sale_id.toString()));
                b.setUserId(rs.getLong(BonusesTableField.user_id.toString()));
                b.setPercentageId(rs.getLong(BonusesTableField.pecentage_id.toString()));
                return b;
            }
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getBonusByIdByRecommenderId(): "+ex.getMessage());
        }
        return null;
    }

    
}
