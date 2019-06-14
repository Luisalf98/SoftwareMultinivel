/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.dao.impl;

import com.loyder.software.model.dao.SaleDao;
import com.loyder.software.model.dao.config.DatabaseConfig;
import com.loyder.software.model.dao.config.DatabaseConfig.BonusesTableField;
import com.loyder.software.model.dao.config.DatabaseConfig.IncomesTableField;
import com.loyder.software.model.dao.config.DatabaseConfig.ProductsTableField;
import com.loyder.software.model.dao.config.DatabaseConfig.SaleState;
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
    private static final String SQL_DELETE_SALE_BY_ID = String.format("DELETE FROM %s WHERE %s=?", Table.SALES, SalesTableField.rowid);
    private static final String SQL_DELETE_SALE_DETAILS_BY_SALE_ID = String.format("DELETE FROM %s WHERE %s=?", Table.SALES_DETAILS, SalesDetailsTableField.sale_id);
    private static final String SQL_DELETE_BONUSES_BY_SALE_ID = String.format("DELETE FROM %s WHERE %s=?", Table.BONUSES, BonusesTableField.sale_id);
    private static final String SQL_DELETE_INCOME_BY_SALE_ID = String.format("DELETE FROM %s WHERE %s=?", Table.INCOMES, IncomesTableField.sale_id);
    
    private static final String SQL_GET_ALL_SALES_BY_USER_ID = String.format("SELECT %s,* FROM %s WHERE %s=? AND (\"%s\" LIKE ?) AND (\"%s\" LIKE ?)", SalesTableField.rowid, Table.SALES, SalesTableField.buyer_id, SalesTableField.state, SalesTableField.type);
    private static final String SQL_GET_SALES_BY_USER_ID_IN_DATE_RANGE = String.format("SELECT %s,* FROM %s WHERE %s=? AND (%s BETWEEN ? AND ?) AND (\"%s\" LIKE ?) AND (\"%s\" LIKE ?)", SalesTableField.rowid, Table.SALES, SalesTableField.buyer_id, SalesTableField.sale_date, SalesTableField.state, SalesTableField.type);
    private static final String SQL_GET_ALL_SALES = String.format("SELECT %s,* FROM %s WHERE (\"%s\" LIKE ?) AND (\"%s\" LIKE ?)", SalesTableField.rowid, Table.SALES, SalesTableField.state, SalesTableField.type);
    private static final String SQL_GET_SALES_IN_DATE_RANGE = String.format("SELECT %s,* FROM %s WHERE (%s BETWEEN ? AND ?) AND (\"%s\" LIKE ?) AND (\"%s\" LIKE ?)", SalesTableField.rowid, Table.SALES, SalesTableField.sale_date, SalesTableField.state, SalesTableField.type);

    private static final String SQL_GET_ALL_SOLD_PRODUCTS = String.format("SELECT SUM(s.%s) as product_quantity, s.%s as product_id, p.\"%s\" as product_name, p.%s as product_price FROM %s s, %s p, %s sa WHERE s.%s == p.%s AND s.%s = sa.%s AND sa.%s = '%s' GROUP BY s.%s", SalesDetailsTableField.quantity, SalesDetailsTableField.product_id, ProductsTableField.name, ProductsTableField.price, Table.SALES_DETAILS, Table.PRODUCTS, Table.SALES, SalesDetailsTableField.product_id, ProductsTableField.rowid, SalesDetailsTableField.sale_id, SalesTableField.rowid, SalesTableField.state, SaleState.PAGADA, SalesDetailsTableField.product_id);
    private static final String SQL_GET_SOLD_PRODUCT_BY_ID = String.format("SELECT SUM(s.%s) as product_quantity, s.%s as product_id, p.\"%s\" as product_name, p.%s as product_price FROM %s s, %s p, %s sa WHERE s.%s == p.%s AND s.%s = ? AND s.%s = sa.%s AND sa.%s = '%s' GROUP BY s.%s", SalesDetailsTableField.quantity, SalesDetailsTableField.product_id, ProductsTableField.name, ProductsTableField.price,Table.SALES_DETAILS, Table.PRODUCTS, Table.SALES, SalesDetailsTableField.product_id, ProductsTableField.rowid, SalesDetailsTableField.product_id, SalesDetailsTableField.sale_id, SalesTableField.rowid, SalesTableField.state, SaleState.PAGADA, SalesDetailsTableField.product_id);
    private static final String SQL_GET_ALL_SOLD_PRODUCTS_IN_DATE_RANGE = String.format("SELECT SUM(s.%s) as product_quantity, s.%s as product_id, p.\"%s\" as product_name, p.%s as product_price FROM %s s, %s p, %s sa WHERE s.%s == p.%s  AND sa.%s == s.%s AND (sa.%s BETWEEN ? AND ?) AND sa.%s = '%s' GROUP BY s.%s", SalesDetailsTableField.quantity, SalesDetailsTableField.product_id, ProductsTableField.name, ProductsTableField.price, Table.SALES_DETAILS, Table.PRODUCTS, Table.SALES, SalesDetailsTableField.product_id, ProductsTableField.rowid, SalesTableField.rowid, SalesDetailsTableField.sale_id, SalesTableField.sale_date, SalesTableField.state, SaleState.PAGADA, SalesDetailsTableField.product_id);
    
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
    public ArrayList<Sale> getAllSalesByUserId(Long id, String state, String type) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSalesByUserId(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL_SALES_BY_USER_ID)) {
            pstmt.setLong(1, id);
            pstmt.setString(2, state);
            pstmt.setString(3, type);
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
    public ArrayList<Sale> getAllSales(String state, String type) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSales(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL_SALES)) {
            pstmt.setString(1, state);
            pstmt.setString(2, type);
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
    public ArrayList<Sale> getSalesInDateRange(Date d1, Date d2, String state, String type) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSalesInDateRange(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_SALES_IN_DATE_RANGE)) {
            pstmt.setLong(1, d1.getTime());
            pstmt.setLong(2, d2.getTime());
            pstmt.setString(3, state);
            pstmt.setString(4, type);
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
    public ArrayList<Sale> getSalesInDateRangeByUserId(Long id, Date d1, Date d2, String state, String type) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSalesInDateRangeByUserId(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_SALES_BY_USER_ID_IN_DATE_RANGE)) {
            pstmt.setLong(1, id);
            pstmt.setLong(2, d1.getTime());
            pstmt.setLong(3, d2.getTime());
            pstmt.setString(4, state);
            pstmt.setString(5, type);
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

    @Override
    public boolean removeSale(Long id) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, ISaleDao.class.getName() + "::removeSale(): No se pudo establecer conexión con la base de datos.");
            return false;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE_SALE_BY_ID)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ISaleDao.class.getName() + "::removeSale(): " + ex.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean removeSaleDetailsBySaleId(Long id) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, ISaleDao.class.getName() + "::removeSaleDetails(): No se pudo establecer conexión con la base de datos.");
            return false;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE_SALE_DETAILS_BY_SALE_ID)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ISaleDao.class.getName() + "::removeSaleDetails(): " + ex.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean removeBonusesBySaleId(Long id) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, ISaleDao.class.getName() + "::removeBonusesBySaleId(): No se pudo establecer conexión con la base de datos.");
            return false;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE_BONUSES_BY_SALE_ID)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ISaleDao.class.getName() + "::removeBonusesBySaleId(): " + ex.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean removeIncomeBySaleId(Long id) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, ISaleDao.class.getName() + "::removeIncomeBySaleId(): No se pudo establecer conexión con la base de datos.");
            return false;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE_INCOME_BY_SALE_ID)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ISaleDao.class.getName() + "::removeIncomeBySaleId(): " + ex.getMessage());
        }
        return false;
    }

    @Override
    public ArrayList<Sale.SoldProduct> getAllSoldProducts() {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSoldProducts(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL_SOLD_PRODUCTS)) {
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale.SoldProduct> sales = new ArrayList<>();
            while (rs.next()) {
                Sale.SoldProduct s = new Sale.SoldProduct();
                s.setProductId(rs.getLong("product_id"));
                s.setPrice(rs.getDouble("product_price"));
                s.setProductName(rs.getString("product_name"));
                s.setQuantity(rs.getLong("product_quantity"));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSoldProducts(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Sale.SoldProduct> getAllSoldProductsInDateRange(Long d1, Long d2) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSoldProductsInDateRange(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL_SOLD_PRODUCTS_IN_DATE_RANGE)) {
            pstmt.setLong(1, d1);
            pstmt.setLong(2, d2);
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale.SoldProduct> sales = new ArrayList<>();
            while (rs.next()) {
                Sale.SoldProduct s = new Sale.SoldProduct();
                s.setProductId(rs.getLong("product_id"));
                s.setPrice(rs.getDouble("product_price"));
                s.setProductName(rs.getString("product_name"));
                s.setQuantity(rs.getLong("product_quantity"));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getAllSoldProductsInDateRange(): " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Sale.SoldProduct> getSoldProductById(Long id) {
        Connection conn = DatabaseConfig.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSoldProductById(): No se pudo establecer conexión con la base de datos.");
            return null;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_GET_SOLD_PRODUCT_BY_ID)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Sale.SoldProduct> sales = new ArrayList<>();
            while (rs.next()) {
                Sale.SoldProduct s = new Sale.SoldProduct();
                s.setProductId(rs.getLong("product_id"));
                s.setPrice(rs.getDouble("product_price"));
                s.setProductName(rs.getString("product_name"));
                s.setQuantity(rs.getLong("product_quantity"));
                sales.add(s);
            }
            return sales;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "::getSoldProductById(): " + ex.getMessage());
        }
        return null;
    }

}
