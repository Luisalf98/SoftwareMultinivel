/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.dao.impl;

import com.loyder.software.model.dao.IncomeDao;
import com.loyder.software.model.dao.config.DatabaseConfig;
import com.loyder.software.model.dao.config.DatabaseConfig.Table;
import com.loyder.software.model.dao.config.DatabaseConfig.IncomesTableField;
import com.loyder.software.model.dao.config.DatabaseConnection;
import com.loyder.software.model.entities.Income;
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
public class IIncomeDao implements IncomeDao{
    
    private IIncomeDao(){}
    
    private static IncomeDao incomeDao;
    
    public static IncomeDao getIncomeDao(){
        if(incomeDao==null){
            incomeDao = new IIncomeDao();
        }
        return incomeDao;
    }
    
    private static final String SQL_ADD_INCOME = String.format("INSERT INTO %s (%s,%s,%s,%s,%s) VALUES (?,?,?,?,?)", Table.INCOMES, IncomesTableField.sale_id, IncomesTableField.income_date, IncomesTableField.total_sale, IncomesTableField.total_bonuses, IncomesTableField.total_income);
    private static final String SQL_GET_INCOME_BY_ID = String.format("SELECT %s,* FROM %s WHERE %s=?", IncomesTableField.rowid, Table.INCOMES, IncomesTableField.rowid);
    private static final String SQL_GET_ALL_INCOMES = String.format("SELECT %s,* FROM %s", IncomesTableField.rowid, Table.INCOMES);
    private static final String SQL_GET_INCOMES_IN_DATE_RANGE = String.format("SELECT %s,* FROM %s WHERE %s BETWEEN ? AND ?", IncomesTableField.rowid, Table.INCOMES, IncomesTableField.income_date);
    
    @Override
    public boolean addIncome(Income income) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::addIncome(): No se pudo establecer conexión a la base de datos.");
            return false;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_ADD_INCOME)){
            pstmt.setLong(1, income.getSaleId());
            pstmt.setLong(2, income.getDate());
            pstmt.setDouble(3, income.getTotalSale());
            pstmt.setDouble(4, income.getTotalBonus());
            pstmt.setDouble(5, income.getTotalIncome());
            pstmt.executeUpdate();
            return true;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::addIncome(): "+ex.getMessage());
        }
        return false;
    }

    @Override
    public Income getIncomeById(Long incomeId) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getIncomeById(): No se pudo establecer conexión a la base de datos.");
            return null;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_GET_INCOME_BY_ID)){
            pstmt.setLong(1, incomeId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                Income b = new Income();
                b.setId(rs.getLong(IncomesTableField.rowid.toString()));
                b.setDate(rs.getLong(IncomesTableField.income_date.toString()));
                b.setSaleId(rs.getLong(IncomesTableField.sale_id.toString()));
                b.setTotalBonus(rs.getDouble(IncomesTableField.total_bonuses.toString()));
                b.setTotalIncome(rs.getDouble(IncomesTableField.total_income.toString()));
                b.setTotalSale(rs.getDouble(IncomesTableField.total_sale.toString()));
                return b;
            }
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getIncomeById(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Income> getAllIncomes() {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getAllIncomes(): No se pudo establecer conexión a la base de datos.");
            return null;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL_INCOMES)){
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Income> incomes = new ArrayList<>();
            while(rs.next()){
                Income b = new Income();
                b.setId(rs.getLong(IncomesTableField.rowid.toString()));
                b.setDate(rs.getLong(IncomesTableField.income_date.toString()));
                b.setSaleId(rs.getLong(IncomesTableField.sale_id.toString()));
                b.setTotalBonus(rs.getDouble(IncomesTableField.total_bonuses.toString()));
                b.setTotalIncome(rs.getDouble(IncomesTableField.total_income.toString()));
                b.setTotalSale(rs.getDouble(IncomesTableField.total_sale.toString()));
                incomes.add(b);
            }
            return incomes;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getAllIncomes(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Income> getIncomesInDateRange(Date d1, Date d2) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getIncomesInDateRange(): No se pudo establecer conexión a la base de datos.");
            return null;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_GET_INCOMES_IN_DATE_RANGE)){
            pstmt.setLong(1, d1.getTime());
            pstmt.setLong(2, d2.getTime());
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Income> incomes = new ArrayList<>();
            while(rs.next()){
                Income b = new Income();
                b.setId(rs.getLong(IncomesTableField.rowid.toString()));
                b.setDate(rs.getLong(IncomesTableField.income_date.toString()));
                b.setSaleId(rs.getLong(IncomesTableField.sale_id.toString()));
                b.setTotalBonus(rs.getDouble(IncomesTableField.total_bonuses.toString()));
                b.setTotalIncome(rs.getDouble(IncomesTableField.total_income.toString()));
                b.setTotalSale(rs.getDouble(IncomesTableField.total_sale.toString()));
                incomes.add(b);
            }
            return incomes;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::getIncomesInDateRange(): "+ex.getMessage());
        }
        return null;
    }

}
