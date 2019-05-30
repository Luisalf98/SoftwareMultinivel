/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.dao.impl;

import com.loyder.software.model.dao.SaleDao;
import com.loyder.software.model.dao.config.DatabaseConfig;
import com.loyder.software.model.dao.config.DatabaseConfig.SaleState;
import com.loyder.software.model.dao.config.DatabaseConfig.SaleType;
import com.loyder.software.model.dao.config.DatabaseConfig.SalesDetailsTableField;
import com.loyder.software.model.dao.config.DatabaseConfig.SalesTableField;
import com.loyder.software.model.dao.config.DatabaseConfig.Table;
import com.loyder.software.model.entities.Sale;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Luis Perez
 */
public class ISaleDao implements SaleDao {

    private static SaleDao saleDao;

    private ISaleDao() {
    }

    public static SaleDao getSaleDao() {
        if (saleDao == null) {
            saleDao = new ISaleDao();
        }
        return saleDao;
    }
    
    private static final String SQL_UPDATE_SALE_STATE = String.format("UPDATE %s SET %s=? WHERE %s=?", Table.SALES, SalesTableField.state, SalesTableField.rowid);
    private static final String SQL_ADD_SALE = String.format("INSERT INTO %s (%s,%s,%s,%s,%s) VALUES (?,?,?,?,?)", Table.SALES, SalesTableField.buyer_id, SalesTableField.sale_date, SalesTableField.total, SalesTableField.type, SalesTableField.state);
    private static final String SQL_ADD_SALE_DETAIL = String.format("INSERT INTO %s (%s,%s,%s) VALUES (?,?,?)", Table.SALES_DETAILS, SalesDetailsTableField.sale_id, SalesDetailsTableField.product_id, SalesDetailsTableField.quantity);
    private static final String SQL_GET_SALE_BY_ID = String.format("SELECT %s,* FROM %s WHERE %s=?", SalesTableField.rowid, Table.SALES, SalesTableField.rowid);
    private static final String SQL_GET_SALE_DETAIL_BY_SALE_ID = String.format("SELECT %s,* FROM %s WHERE %s=?", SalesDetailsTableField.rowid, Table.SALES_DETAILS, SalesDetailsTableField.sale_id);
    private static final String SQL_GET_SALE_BY_ID_AND_USER_ID = String.format("SELECT %s,* FROM %s WHERE %s=? AND %s=?", SalesTableField.rowid, Table.SALES, SalesTableField.buyer_id, SalesTableField.rowid);
    //Not filtered
    private static final String SQL_GET_ALL_SALES_BY_USER_ID = String.format("SELECT %s,* FROM %s WHERE %s=?", SalesTableField.rowid, Table.SALES, SalesTableField.buyer_id);
    private static final String SQL_GET_SALES_BY_USER_ID_IN_DATE_RANGE = String.format("SELECT %s,* FROM %s WHERE %s=? AND (%s BETWEEN ? AND ?)", SalesTableField.rowid, Table.SALES, SalesTableField.buyer_id, SalesTableField.sale_date);
    private static final String SQL_GET_ALL_SALES = String.format("SELECT %s,* FROM %s", SalesTableField.rowid, Table.SALES);
    private static final String SQL_GET_SALES_IN_DATE_RANGE = String.format("SELECT %s,* FROM %s WHERE %s BETWEEN ? AND ?", SalesTableField.rowid, Table.SALES, SalesTableField.sale_date);
    //Filtered - Credit All
    private static final String SQL_GET_ALL_SALES_BY_USER_ID_CREDIT = String.format("SELECT %s,* FROM %s WHERE %s=? AND %s=?", SalesTableField.rowid, Table.SALES, SalesTableField.buyer_id, SalesTableField.type);
    private static final String SQL_GET_SALES_BY_USER_ID_IN_DATE_RANGE_CREDIT = String.format("SELECT %s,* FROM %s WHERE %s=? AND (%s BETWEEN ? AND ?) AND %s=?", SalesTableField.rowid, Table.SALES, SalesTableField.buyer_id, SalesTableField.sale_date, SalesTableField.type);
    private static final String SQL_GET_ALL_SALES_CREDIT = String.format("SELECT %s,* FROM %s WHERE %s=?", SalesTableField.rowid, Table.SALES, SalesTableField.type);
    private static final String SQL_GET_SALES_IN_DATE_RANGE_CREDIT = String.format("SELECT %s,* FROM %s WHERE (%s BETWEEN ? AND ?) AND %s=?", SalesTableField.rowid, Table.SALES, SalesTableField.sale_date, SalesTableField.type);
    //Filtered Credit Paid
    private static final String SQL_GET_ALL_SALES_BY_USER_ID_CREDIT_PAID = String.format("SELECT %s,* FROM %s WHERE %s=? AND %s=? AND %s=?", SalesTableField.rowid, Table.SALES, SalesTableField.buyer_id, SalesTableField.type, SalesTableField.state);
    private static final String SQL_GET_SALES_BY_USER_ID_IN_DATE_RANGE_CREDIT_PAID = String.format("SELECT %s,* FROM %s WHERE %s=? AND (%s BETWEEN ? AND ?) AND %s=? AND %s=?", SalesTableField.rowid, Table.SALES, SalesTableField.buyer_id, SalesTableField.sale_date, SalesTableField.type, SalesTableField.state);
    private static final String SQL_GET_ALL_SALES_CREDIT_PAID = String.format("SELECT %s,* FROM %s WHERE %s=? AND %s=?", SalesTableField.rowid, Table.SALES, SalesTableField.type, SalesTableField.state);
    private static final String SQL_GET_SALES_IN_DATE_RANGE_CREDIT_PAID = String.format("SELECT %s,* FROM %s WHERE (%s BETWEEN ? AND ?) AND %s=? AND %s=?", SalesTableField.rowid, Table.SALES, SalesTableField.sale_date, SalesTableField.type, SalesTableField.state);
    //Filtered Credit Not Paid
    private static final String SQL_GET_ALL_SALES_BY_USER_ID_CREDIT_NOT_PAID = String.format("SELECT %s,* FROM %s WHERE %s=? AND %s=? AND %s=?", SalesTableField.rowid, Table.SALES, SalesTableField.buyer_id, SalesTableField.type, SalesTableField.state);
    private static final String SQL_GET_SALES_BY_USER_ID_IN_DATE_RANGE_CREDIT_NOT_PAID = String.format("SELECT %s,* FROM %s WHERE %s=? AND (%s BETWEEN ? AND ?) AND %s=? AND %s=?", SalesTableField.rowid, Table.SALES, SalesTableField.buyer_id, SalesTableField.sale_date, SalesTableField.type, SalesTableField.state);
    private static final String SQL_GET_ALL_SALES_CREDIT_NOT_PAID = String.format("SELECT %s,* FROM %s WHERE %s=? AND %s=?", SalesTableField.rowid, Table.SALES, SalesTableField.type, SalesTableField.state);
    private static final String SQL_GET_SALES_IN_DATE_RANGE_CREDIT_NOT_PAID = String.format("SELECT %s,* FROM %s WHERE (%s BETWEEN ? AND ?) AND %s=? AND %s=?", SalesTableField.rowid, Table.SALES, SalesTableField.sale_date, SalesTableField.type, SalesTableField.state);
    //Filtered Cash All
    private static final String SQL_GET_ALL_SALES_BY_USER_ID_CASH = String.format("SELECT %s,* FROM %s WHERE %s=? AND %s=?", SalesTableField.rowid, Table.SALES, SalesTableField.buyer_id, SalesTableField.type);
    private static final String SQL_GET_SALES_BY_USER_ID_IN_DATE_RANGE_CASH = String.format("SELECT %s,* FROM %s WHERE %s=? AND (%s BETWEEN ? AND ?) AND %s=?", SalesTableField.rowid, Table.SALES, SalesTableField.buyer_id, SalesTableField.sale_date, SalesTableField.type);
    private static final String SQL_GET_ALL_SALES_CASH = String.format("SELECT %s,* FROM %s WHERE %s=?", SalesTableField.rowid, Table.SALES, SalesTableField.type);
    private static final String SQL_GET_SALES_IN_DATE_RANGE_CASH = String.format("SELECT %s,* FROM %s WHERE (%s BETWEEN ? AND ?) AND %s=?", SalesTableField.rowid, Table.SALES, SalesTableField.sale_date, SalesTableField.type);

    @Override
    public Long addSale(Sale sale, ArrayList<Sale.Detail> details) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, ISaleDao.class.getName() + "::addSale(): No se pudo establecer conexión con la base de datos.");
            return -1L;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_ADD_SALE, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, sale.getBuyerId());
            pstmt.setLong(2, sale.getSaleDate());
            pstmt.setDouble(3, sale.getTotal());
            pstmt.setString(4, sale.getType());
            pstmt.setString(5, sale.getState());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            final Long saleId;
            if (rs.next()) {
                saleId = rs.getLong(1);
                details.forEach((detail) -> {
                    detail.setSaleId(saleId);
                    addSaleDetail(detail);
                });
                return saleId;
            }else{
                JOptionPane.showMessageDialog(null, this.getClass().getName()+"::addSale(): No se pudo agregar la venta.");
                return -1L;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ISaleDao.class.getName() + "::addSale(): " + ex.getMessage());
        }
        return -1L;

    }

    private boolean addSaleDetail(Sale.Detail detail) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, ISaleDao.class.getName() + "::addSaleDetail(): No se pudo establecer conexión con la base de datos.");
            return false;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_ADD_SALE_DETAIL)) {
            pstmt.setLong(1, detail.getSaleId());
            pstmt.setLong(2, detail.getProductId());
            pstmt.setLong(3, detail.getQuantity());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ISaleDao.class.getName() + "::addSaleDetail(): " + ex.getMessage());
        }
        return false;
    }

    @Override
    public Sale getSaleById(Long id) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSaleById(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_SALE_BY_ID)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getLong(SalesTableField.rowid.toString()));
                s.setSaleDate(rs.getLong(SalesTableField.sale_date.toString()));
                s.setBuyerId(rs.getLong(SalesTableField.buyer_id.toString()));
                s.setTotal(rs.getDouble(SalesTableField.total.toString()));
                s.setState(rs.getString(SalesTableField.state.toString()));
                s.setType(rs.getString(SalesTableField.type.toString()));
                return s;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSaleById(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Sale.Detail> getSaleDetailsBySaleId(Long saleId) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSaleDetailsBySaleId(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_SALE_DETAIL_BY_SALE_ID)) {
            pstmt.setLong(1, saleId);
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale.Detail> details = new ArrayList<>();
            while (rs.next()) {
                Sale.Detail d = new Sale.Detail();
                d.setProductId(rs.getLong(SalesDetailsTableField.product_id.toString()));
                d.setQuantity(rs.getLong(SalesDetailsTableField.quantity.toString()));
                d.setSaleId(rs.getLong(SalesDetailsTableField.sale_id.toString()));
                details.add(d);
            }
            return details;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSaleDetailsBySaleId(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Sale> getAllSalesByUserId(Long id) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSalesByUserId(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL_SALES_BY_USER_ID)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale> sales = new ArrayList<>();
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getLong(SalesTableField.rowid.toString()));
                s.setSaleDate(rs.getLong(SalesTableField.sale_date.toString()));
                s.setBuyerId(rs.getLong(SalesTableField.buyer_id.toString()));
                s.setTotal(rs.getDouble(SalesTableField.total.toString()));
                s.setState(rs.getString(SalesTableField.state.toString()));
                s.setType(rs.getString(SalesTableField.type.toString()));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSalesByUserId(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Sale> getAllSales() {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSales(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL_SALES)) {
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale> sales = new ArrayList<>();
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getLong(SalesTableField.rowid.toString()));
                s.setSaleDate(rs.getLong(SalesTableField.sale_date.toString()));
                s.setBuyerId(rs.getLong(SalesTableField.buyer_id.toString()));
                s.setTotal(rs.getDouble(SalesTableField.total.toString()));
                s.setState(rs.getString(SalesTableField.state.toString()));
                s.setType(rs.getString(SalesTableField.type.toString()));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSales(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Sale> getSalesInDateRange(Date d1, Date d2) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSalesInDateRange(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_SALES_IN_DATE_RANGE)) {
            pstmt.setLong(1, d1.getTime());
            pstmt.setLong(2, d2.getTime());
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale> sales = new ArrayList<>();
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getLong(SalesTableField.rowid.toString()));
                s.setSaleDate(rs.getLong(SalesTableField.sale_date.toString()));
                s.setBuyerId(rs.getLong(SalesTableField.buyer_id.toString()));
                s.setTotal(rs.getDouble(SalesTableField.total.toString()));
                s.setState(rs.getString(SalesTableField.state.toString()));
                s.setType(rs.getString(SalesTableField.type.toString()));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSalesInDateRange(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Sale> getSalesInDateRangeByUserId(Long id, Date d1, Date d2) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSalesInDateRangeByUserId(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_SALES_BY_USER_ID_IN_DATE_RANGE)) {
            pstmt.setLong(1, id);
            pstmt.setLong(2, d1.getTime());
            pstmt.setLong(3, d2.getTime());
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale> sales = new ArrayList<>();
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getLong(SalesTableField.rowid.toString()));
                s.setSaleDate(rs.getLong(SalesTableField.sale_date.toString()));
                s.setBuyerId(rs.getLong(SalesTableField.buyer_id.toString()));
                s.setTotal(rs.getDouble(SalesTableField.total.toString()));
                s.setState(rs.getString(SalesTableField.state.toString()));
                s.setType(rs.getString(SalesTableField.type.toString()));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSalesInDateRangeByUserId(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public Sale getSaleByIdAndUserId(Long saleId, Long recommenderId) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSaleByIdAndUserId(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_SALE_BY_ID_AND_USER_ID)) {
            pstmt.setLong(1, recommenderId);
            pstmt.setLong(2, saleId);
            ResultSet rs = pstmt.executeQuery();
            Sale s = null;
            if (rs.next()) {
                s = new Sale();
                s.setId(rs.getLong(SalesTableField.rowid.toString()));
                s.setSaleDate(rs.getLong(SalesTableField.sale_date.toString()));
                s.setBuyerId(rs.getLong(SalesTableField.buyer_id.toString()));
                s.setTotal(rs.getDouble(SalesTableField.total.toString()));
                s.setState(rs.getString(SalesTableField.state.toString()));
                s.setType(rs.getString(SalesTableField.type.toString()));
            }
            return s;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSaleByIdAndUserId(): " + ex.getMessage());
        }
        return null;
    }
    
    
    //Filtered Credit
    @Override
    public ArrayList<Sale> getAllSalesByUserIdCredit(Long id) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSalesByUserIdCredit(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL_SALES_BY_USER_ID_CREDIT)) {
            pstmt.setLong(1, id);
            pstmt.setString(2, SaleType.CREDITO.toString());
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale> sales = new ArrayList<>();
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getLong(SalesTableField.rowid.toString()));
                s.setSaleDate(rs.getLong(SalesTableField.sale_date.toString()));
                s.setBuyerId(rs.getLong(SalesTableField.buyer_id.toString()));
                s.setTotal(rs.getDouble(SalesTableField.total.toString()));
                s.setState(rs.getString(SalesTableField.state.toString()));
                s.setType(rs.getString(SalesTableField.type.toString()));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSalesByUserIdCredit(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Sale> getAllSalesCredit() {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSalesCredit(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL_SALES_CREDIT)) {
            pstmt.setString(1, SaleType.CREDITO.toString());
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale> sales = new ArrayList<>();
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getLong(SalesTableField.rowid.toString()));
                s.setSaleDate(rs.getLong(SalesTableField.sale_date.toString()));
                s.setBuyerId(rs.getLong(SalesTableField.buyer_id.toString()));
                s.setTotal(rs.getDouble(SalesTableField.total.toString()));
                s.setState(rs.getString(SalesTableField.state.toString()));
                s.setType(rs.getString(SalesTableField.type.toString()));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSalesCredit(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Sale> getSalesInDateRangeCredit(Date d1, Date d2) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSalesInDateRangeCredit(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_SALES_IN_DATE_RANGE_CREDIT)) {
            pstmt.setLong(1, d1.getTime());
            pstmt.setLong(2, d2.getTime());
            pstmt.setString(3, SaleType.CREDITO.toString());
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale> sales = new ArrayList<>();
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getLong(SalesTableField.rowid.toString()));
                s.setSaleDate(rs.getLong(SalesTableField.sale_date.toString()));
                s.setBuyerId(rs.getLong(SalesTableField.buyer_id.toString()));
                s.setTotal(rs.getDouble(SalesTableField.total.toString()));
                s.setState(rs.getString(SalesTableField.state.toString()));
                s.setType(rs.getString(SalesTableField.type.toString()));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSalesInDateRangeCredit(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Sale> getSalesInDateRangeByUserIdCredit(Long id, Date d1, Date d2) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSalesInDateRangeByUserIdCredit(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_SALES_BY_USER_ID_IN_DATE_RANGE_CREDIT)) {
            pstmt.setLong(1, id);
            pstmt.setLong(2, d1.getTime());
            pstmt.setLong(3, d2.getTime());
            pstmt.setString(4, SaleType.CREDITO.toString());
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale> sales = new ArrayList<>();
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getLong(SalesTableField.rowid.toString()));
                s.setSaleDate(rs.getLong(SalesTableField.sale_date.toString()));
                s.setBuyerId(rs.getLong(SalesTableField.buyer_id.toString()));
                s.setTotal(rs.getDouble(SalesTableField.total.toString()));
                s.setState(rs.getString(SalesTableField.state.toString()));
                s.setType(rs.getString(SalesTableField.type.toString()));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSalesInDateRangeByUserIdCredit(): " + ex.getMessage());
        }
        return null;
    }
    
    //Filtered Credit Paid
    @Override
    public ArrayList<Sale> getAllSalesByUserIdCreditPaid(Long id) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSalesByUserIdCreditPaid(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL_SALES_BY_USER_ID_CREDIT_PAID)) {
            pstmt.setLong(1, id);
            pstmt.setString(2, SaleType.CREDITO.toString());
            pstmt.setString(3, SaleState.PAGADA.toString());
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale> sales = new ArrayList<>();
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getLong(SalesTableField.rowid.toString()));
                s.setSaleDate(rs.getLong(SalesTableField.sale_date.toString()));
                s.setBuyerId(rs.getLong(SalesTableField.buyer_id.toString()));
                s.setTotal(rs.getDouble(SalesTableField.total.toString()));
                s.setState(rs.getString(SalesTableField.state.toString()));
                s.setType(rs.getString(SalesTableField.type.toString()));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSalesByUserIdCreditPaid(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Sale> getAllSalesCreditPaid() {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSalesCreditPaid(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL_SALES_CREDIT_PAID)) {
            pstmt.setString(1, SaleType.CREDITO.toString());
            pstmt.setString(2, SaleState.PAGADA.toString());
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale> sales = new ArrayList<>();
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getLong(SalesTableField.rowid.toString()));
                s.setSaleDate(rs.getLong(SalesTableField.sale_date.toString()));
                s.setBuyerId(rs.getLong(SalesTableField.buyer_id.toString()));
                s.setTotal(rs.getDouble(SalesTableField.total.toString()));
                s.setState(rs.getString(SalesTableField.state.toString()));
                s.setType(rs.getString(SalesTableField.type.toString()));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSalesCreditPaid(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Sale> getSalesInDateRangeCreditPaid(Date d1, Date d2) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSalesInDateRangeCreditPaid(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_SALES_IN_DATE_RANGE_CREDIT_PAID)) {
            pstmt.setLong(1, d1.getTime());
            pstmt.setLong(2, d2.getTime());
            pstmt.setString(3, SaleType.CREDITO.toString());
            pstmt.setString(4, SaleState.PAGADA.toString());
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale> sales = new ArrayList<>();
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getLong(SalesTableField.rowid.toString()));
                s.setSaleDate(rs.getLong(SalesTableField.sale_date.toString()));
                s.setBuyerId(rs.getLong(SalesTableField.buyer_id.toString()));
                s.setTotal(rs.getDouble(SalesTableField.total.toString()));
                s.setState(rs.getString(SalesTableField.state.toString()));
                s.setType(rs.getString(SalesTableField.type.toString()));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSalesInDateRangeCreditPaid(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Sale> getSalesInDateRangeByUserIdCreditPaid(Long id, Date d1, Date d2) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSalesInDateRangeByUserIdCreditPaid(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_SALES_BY_USER_ID_IN_DATE_RANGE_CREDIT_PAID)) {
            pstmt.setLong(1, id);
            pstmt.setLong(2, d1.getTime());
            pstmt.setLong(3, d2.getTime());
            pstmt.setString(4, SaleType.CREDITO.toString());
            pstmt.setString(5, SaleState.PAGADA.toString());
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale> sales = new ArrayList<>();
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getLong(SalesTableField.rowid.toString()));
                s.setSaleDate(rs.getLong(SalesTableField.sale_date.toString()));
                s.setBuyerId(rs.getLong(SalesTableField.buyer_id.toString()));
                s.setTotal(rs.getDouble(SalesTableField.total.toString()));
                s.setState(rs.getString(SalesTableField.state.toString()));
                s.setType(rs.getString(SalesTableField.type.toString()));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSalesInDateRangeByUserIdCreditPaid(): " + ex.getMessage());
        }
        return null;
    }
    
    //Filtered Credit Not Paid
    @Override
    public ArrayList<Sale> getAllSalesByUserIdCreditNotPaid(Long id) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSalesByUserIdCreditNotPaid(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL_SALES_BY_USER_ID_CREDIT_NOT_PAID)) {
            pstmt.setLong(1, id);
            pstmt.setString(2, SaleType.CREDITO.toString());
            pstmt.setString(3, SaleState.NO_PAGADA.toString());
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale> sales = new ArrayList<>();
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getLong(SalesTableField.rowid.toString()));
                s.setSaleDate(rs.getLong(SalesTableField.sale_date.toString()));
                s.setBuyerId(rs.getLong(SalesTableField.buyer_id.toString()));
                s.setTotal(rs.getDouble(SalesTableField.total.toString()));
                s.setState(rs.getString(SalesTableField.state.toString()));
                s.setType(rs.getString(SalesTableField.type.toString()));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSalesByUserIdCreditNotPaid(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Sale> getAllSalesCreditNotPaid() {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSalesCreditNotPaid(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL_SALES_CREDIT_NOT_PAID)) {
            pstmt.setString(1, SaleType.CREDITO.toString());
            pstmt.setString(2, SaleState.NO_PAGADA.toString());
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale> sales = new ArrayList<>();
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getLong(SalesTableField.rowid.toString()));
                s.setSaleDate(rs.getLong(SalesTableField.sale_date.toString()));
                s.setBuyerId(rs.getLong(SalesTableField.buyer_id.toString()));
                s.setTotal(rs.getDouble(SalesTableField.total.toString()));
                s.setState(rs.getString(SalesTableField.state.toString()));
                s.setType(rs.getString(SalesTableField.type.toString()));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSalesCreditNotPaid(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Sale> getSalesInDateRangeCreditNotPaid(Date d1, Date d2) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSalesInDateRangeCreditNotPaid(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_SALES_IN_DATE_RANGE_CREDIT_NOT_PAID)) {
            pstmt.setLong(1, d1.getTime());
            pstmt.setLong(2, d2.getTime());
            pstmt.setString(3, SaleType.CREDITO.toString());
            pstmt.setString(4, SaleState.NO_PAGADA.toString());
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale> sales = new ArrayList<>();
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getLong(SalesTableField.rowid.toString()));
                s.setSaleDate(rs.getLong(SalesTableField.sale_date.toString()));
                s.setBuyerId(rs.getLong(SalesTableField.buyer_id.toString()));
                s.setTotal(rs.getDouble(SalesTableField.total.toString()));
                s.setState(rs.getString(SalesTableField.state.toString()));
                s.setType(rs.getString(SalesTableField.type.toString()));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSalesInDateRangeCreditNotPaid(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Sale> getSalesInDateRangeByUserIdCreditNotPaid(Long id, Date d1, Date d2) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSalesInDateRangeByUserIdCreditNotPaid(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_SALES_BY_USER_ID_IN_DATE_RANGE_CREDIT_NOT_PAID)) {
            pstmt.setLong(1, id);
            pstmt.setLong(2, d1.getTime());
            pstmt.setLong(3, d2.getTime());
            pstmt.setString(4, SaleType.CREDITO.toString());
            pstmt.setString(5, SaleState.NO_PAGADA.toString());
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale> sales = new ArrayList<>();
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getLong(SalesTableField.rowid.toString()));
                s.setSaleDate(rs.getLong(SalesTableField.sale_date.toString()));
                s.setBuyerId(rs.getLong(SalesTableField.buyer_id.toString()));
                s.setTotal(rs.getDouble(SalesTableField.total.toString()));
                s.setState(rs.getString(SalesTableField.state.toString()));
                s.setType(rs.getString(SalesTableField.type.toString()));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSalesInDateRangeByUserIdCreditNotPaid(): " + ex.getMessage());
        }
        return null;
    }
    
    //Filtered Cash
    @Override
    public ArrayList<Sale> getAllSalesByUserIdCash(Long id) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSalesByUserIdCash(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL_SALES_BY_USER_ID_CASH)) {
            pstmt.setLong(1, id);
            pstmt.setString(2, SaleType.CONTADO.toString());
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale> sales = new ArrayList<>();
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getLong(SalesTableField.rowid.toString()));
                s.setSaleDate(rs.getLong(SalesTableField.sale_date.toString()));
                s.setBuyerId(rs.getLong(SalesTableField.buyer_id.toString()));
                s.setTotal(rs.getDouble(SalesTableField.total.toString()));
                s.setState(rs.getString(SalesTableField.state.toString()));
                s.setType(rs.getString(SalesTableField.type.toString()));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSalesByUserIdCash(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Sale> getAllSalesCash() {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSalesCash(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL_SALES_CASH)) {
            pstmt.setString(1, SaleType.CONTADO.toString());
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale> sales = new ArrayList<>();
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getLong(SalesTableField.rowid.toString()));
                s.setSaleDate(rs.getLong(SalesTableField.sale_date.toString()));
                s.setBuyerId(rs.getLong(SalesTableField.buyer_id.toString()));
                s.setTotal(rs.getDouble(SalesTableField.total.toString()));
                s.setState(rs.getString(SalesTableField.state.toString()));
                s.setType(rs.getString(SalesTableField.type.toString()));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSalesCash(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Sale> getSalesInDateRangeCash(Date d1, Date d2) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSalesInDateRangeCash(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_SALES_IN_DATE_RANGE_CASH)) {
            pstmt.setLong(1, d1.getTime());
            pstmt.setLong(2, d2.getTime());
            pstmt.setString(3, SaleType.CONTADO.toString());
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale> sales = new ArrayList<>();
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getLong(SalesTableField.rowid.toString()));
                s.setSaleDate(rs.getLong(SalesTableField.sale_date.toString()));
                s.setBuyerId(rs.getLong(SalesTableField.buyer_id.toString()));
                s.setTotal(rs.getDouble(SalesTableField.total.toString()));
                s.setState(rs.getString(SalesTableField.state.toString()));
                s.setType(rs.getString(SalesTableField.type.toString()));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSalesInDateRangeCash(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Sale> getSalesInDateRangeByUserIdCash(Long id, Date d1, Date d2) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSalesInDateRangeByUserIdCash(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_SALES_BY_USER_ID_IN_DATE_RANGE_CASH)) {
            pstmt.setLong(1, id);
            pstmt.setLong(2, d1.getTime());
            pstmt.setLong(3, d2.getTime());
            pstmt.setString(4, SaleType.CONTADO.toString());
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale> sales = new ArrayList<>();
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getLong(SalesTableField.rowid.toString()));
                s.setSaleDate(rs.getLong(SalesTableField.sale_date.toString()));
                s.setBuyerId(rs.getLong(SalesTableField.buyer_id.toString()));
                s.setTotal(rs.getDouble(SalesTableField.total.toString()));
                s.setState(rs.getString(SalesTableField.state.toString()));
                s.setType(rs.getString(SalesTableField.type.toString()));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSalesInDateRangeByUserIdCash(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean updateSaleState(Long id, SaleState state) {
        Connection conn = DatabaseConfig.getConnection();
        if(conn==null){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::updateSaleState(): No se pudo establecer conexión a la base de datos.");
            return false;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE_SALE_STATE)){
            pstmt.setString(1, state.toString());
            pstmt.setLong(2, id);
            pstmt.executeUpdate();
            return true;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, this.getClass().getName()+"::updateSaleState(): "+ex.getMessage());
        }
        return false;
    }

}
