/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.dao.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Luis Perez
 */
public class DatabaseConfig {
    
    private DatabaseConfig(){
    }

    public enum Table {
        USERS,
        USERS_CATEGORIES,
        LEVELS_PERCENTAGES,
        PRODUCTS,
        SALES,
        SALES_DETAILS,
        INCOMES,
        BONUSES,
        ADMINS;
    }
    
    public enum UsersCategoriesTableField{
        rowid,
        name;
    }
    
    public enum AdminsTableField{
        rowid,
        admin_id,
        name,
        last_name,
        username,
        password,
        tel,
        cel,
        address;
    }
    

    public enum IncomesTableField {
        rowid,
        sale_id,
        income_date,
        total_sale,
        total_bonuses,
        total_income;
    }

    public enum SalesDetailsTableField {
        rowid,
        sale_id,
        product_id,
        quantity;
    }

    public enum SalesTableField {
        rowid,
        buyer_id,
        sale_date,
        type,
        state,
        total;
    }
    
    public enum SaleType{
        CREDITO,
        CONTADO;
    }
    
    public enum SaleState{
        SOLICITADA,
        ELIMINADA,
        NO_PAGADA,
        PAGADA;
    }

    public enum ProductsTableField {
        rowid,
        name,
        description,
        price;
    }

    public enum LevelsPecentagesTableField {
        rowid,
        percentage;
    }

    public enum UsersTableField {
        rowid,
        user_id,
        name,
        last_name,
        address,
        tel,
        cel,
        adder_id,
        entrance_date,
        category_id;
    }

    public enum BonusesTableField {
        rowid,
        pecentage_id,
        sale_id,
        user_id,
        bonus,
        bonus_date;
    }
    
    private static final String SQL_CREATE_ADMINS_TABLE = "CREATE TABLE IF NOT EXISTS " + Table.ADMINS + "("
            + " " + AdminsTableField.admin_id + " INTEGER NOT NULL UNIQUE, "
            + " " + AdminsTableField.name + " TEXT NOT NULL, "
            + " " + AdminsTableField.last_name + " TEXT NOT NULL, "
            + " " + AdminsTableField.username + " TEXT NOT NULL UNIQUE, "
            + " " + AdminsTableField.password + " TEXT NOT NULL, "
            + " " + AdminsTableField.address + " TEXT, "
            + " " + AdminsTableField.tel + " TEXT, "
            + " " + AdminsTableField.cel + " TEXT "
            + ");";
    private static final String SQL_CREATE_USERS_CATEGORIES_TABLE = "CREATE TABLE IF NOT EXISTS " + Table.USERS_CATEGORIES + "( "
            + UsersCategoriesTableField.name + " TEXT NOT NULL UNIQUE"
            + ");";

    private static final String SQL_CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " + Table.USERS + "("
            + " " + UsersTableField.user_id + " INTEGER NOT NULL UNIQUE, "
            + " " + UsersTableField.name + " TEXT NOT NULL, "
            + " " + UsersTableField.last_name + " TEXT NOT NULL, "
            + " " + UsersTableField.address + " TEXT, "
            + " " + UsersTableField.tel + " TEXT, "
            + " " + UsersTableField.cel + " TEXT, "
            + " " + UsersTableField.adder_id + " INTEGER NOT NULL, "
            + " " + UsersTableField.entrance_date + " INTEGER NOT NULL,"
            + " " + UsersTableField.category_id + " INTEGER NOT NULL"
            + ");";

    private static final String SQL_CREATE_LEVELS_PERCENTAGES_TABLE = "CREATE TABLE IF NOT EXISTS " + Table.LEVELS_PERCENTAGES + "("
            + " " + LevelsPecentagesTableField.percentage + " REAL NOT NULL"
            + ");";

    private static final String SQL_CREATE_INCOMES_TABLE = "CREATE TABLE IF NOT EXISTS " + Table.INCOMES + "("
            + " " + IncomesTableField.sale_id + " INTEGER NOT NULL, "
            + " " + IncomesTableField.income_date + " INTEGER NOT NULL, "
            + " " + IncomesTableField.total_sale + " REAL NOT NULL, "
            + " " + IncomesTableField.total_bonuses + " REAL NOT NULL, "
            + " " + IncomesTableField.total_income + " REAL NOT NULL"
            + ");";

    private static final String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE IF NOT EXISTS " + Table.PRODUCTS + "("
            + " " + ProductsTableField.name + " TEXT NOT NULL, "
            + " " + ProductsTableField.description + " TEXT, "
            + " " + ProductsTableField.price + " REAL NOT NULL"
            + ");";

    private static final String SQL_CREATE_BONUSES_TABLE = "CREATE TABLE IF NOT EXISTS " + Table.BONUSES + "("
            + " " + BonusesTableField.pecentage_id + " INTEGER NOT NULL, "
            + " " + BonusesTableField.sale_id + " INTEGER NOT NULL, "
            + " " + BonusesTableField.user_id + " INTEGER NOT NULL, "
            + " " + BonusesTableField.bonus + " REAL NOT NULL, "
            + " " + BonusesTableField.bonus_date + " INTEGER NOT NULL"
            + ");";

    private static final String SQL_CREATE_SALES_TABLE = "CREATE TABLE IF NOT EXISTS " + Table.SALES + "("
            + " " + SalesTableField.buyer_id + " INTEGER NOT NULL, "
            + " " + SalesTableField.sale_date + " INTEGER NOT NULL, "
            + " " + SalesTableField.type + " TEXT NOT NULL, "
            + " " + SalesTableField.state + " TEXT NOT NULL, "
            + " " + SalesTableField.total + " REAL NOT NULL "
            + ");";

    private static final String SQL_CREATE_SALES_DETAILS_TABLE = "CREATE TABLE IF NOT EXISTS " + Table.SALES_DETAILS + "("
            + " " + SalesDetailsTableField.sale_id + " INTEGER NOT NULL, "
            + " " + SalesDetailsTableField.product_id + " INTEGER NOT NULL, "
            + " " + SalesDetailsTableField.quantity + " INTEGER NOT NULL "
            + ");";

    private static boolean createUsersTable() {
        Connection con = getConnection();
        if (con != null) {
            try {
                PreparedStatement ps = con.prepareStatement(SQL_CREATE_USERS_TABLE);
                ps.execute();
                return true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, DatabaseConfig.class.getName()+"::createUsersTable(): " + ex.getMessage());
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, DatabaseConfig.class.getName()+"::createUsersTable(): No se pudo establecer conexión con la base de datos.");
            return false;
        }
        
    }
    
    private static boolean createUsersCategoriesTable() {
        Connection con = getConnection();
        if (con != null) {
            try {
                PreparedStatement ps = con.prepareStatement(SQL_CREATE_USERS_CATEGORIES_TABLE);
                ps.execute();
                return true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, DatabaseConfig.class.getName()+"::createUsersCategoriesTable(): " + ex.getMessage());
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, DatabaseConfig.class.getName()+"::createUsersCategoriesTable(): No se pudo establecer conexión con la base de datos.");
            return false;
        }
        
    }
    
    private static boolean createAdminsTable() {
        Connection con = getConnection();
        if (con != null) {
            try {
                PreparedStatement ps = con.prepareStatement(SQL_CREATE_ADMINS_TABLE);
                ps.execute();
                return true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, DatabaseConfig.class.getName()+"::createAdminsTable(): " + ex.getMessage());
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, DatabaseConfig.class.getName()+"::createAdminsTable(): No se pudo establecer conexión con la base de datos.");
            return false;
        }
        
    }

    private static boolean createBonusesTable() {
        Connection con = getConnection();
        if (con != null) {
            try {
                PreparedStatement ps = con.prepareStatement(SQL_CREATE_BONUSES_TABLE);
                ps.execute();
                return true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, DatabaseConfig.class.getName()+"::createBonusesTable(): " + ex.getMessage());
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, DatabaseConfig.class.getName()+"::createBonusesTable(): No se pudo establecer conexión con la base de datos.");
            return false;
        }

    }

    private static boolean createIncomesTable() {
        Connection con = getConnection();
        if (con != null) {
            try {
                PreparedStatement ps = con.prepareStatement(SQL_CREATE_INCOMES_TABLE);
                ps.execute();
                return true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, DatabaseConfig.class.getName()+"::createIncomesTable(): " + ex.getMessage());
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, DatabaseConfig.class.getName()+"::createIncomesTable(): No se pudo establecer conexión con la base de datos.");
            return false;
        }

    }

    private static boolean createLevelsPercentagesTable() {
        Connection con = getConnection();
        if (con != null) {
            try {
                PreparedStatement ps = con.prepareStatement(SQL_CREATE_LEVELS_PERCENTAGES_TABLE);
                ps.execute();
                return true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, DatabaseConfig.class.getName()+"::createLevelsPercentagesTable(): " + ex.getMessage());
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, DatabaseConfig.class.getName()+"::createLevelsPercentagesTable(): No se pudo establecer conexión con la base de datos.");
            return false;
        }

    }

    private static boolean createProductsTable() {
        Connection con = getConnection();
        if (con != null) {
            try {
                PreparedStatement ps = con.prepareStatement(SQL_CREATE_PRODUCTS_TABLE);
                ps.execute();
                return true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, DatabaseConfig.class.getName()+"::createProductsTable(): " + ex.getMessage());
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, DatabaseConfig.class.getName()+"::createProductsTable(): No se pudo establecer conexión con la base de datos.");
            return false;
        }

    }

    private static boolean createSalesTable() {
        Connection con = getConnection();
        if (con != null) {
            try {
                PreparedStatement ps = con.prepareStatement(SQL_CREATE_SALES_TABLE);
                ps.execute();
                return true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, DatabaseConfig.class.getName()+"::createSalesTable(): " + ex.getMessage());
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, DatabaseConfig.class.getName()+"::createSalesTable(): No se pudo establecer conexión con la base de datos.");
            return false;
        }

    }

    private static boolean createSalesDetailsTable() {
        Connection con = getConnection();
        if (con != null) {
            try {
                PreparedStatement ps = con.prepareStatement(SQL_CREATE_SALES_DETAILS_TABLE);
                ps.execute();
                return true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, DatabaseConfig.class.getName()+"::createSalesDetailsTable(): " + ex.getMessage());
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, DatabaseConfig.class.getName()+"::createSalesDetailsTable(): No se pudo establecer conexión con la base de datos.");
            return false;
        }

    }

    private static boolean createTables() {
        return createBonusesTable() &&
        createIncomesTable() &&
        createLevelsPercentagesTable() &&
        createProductsTable() &&
        createSalesDetailsTable() &&
        createSalesTable() &&
        createUsersTable() &&
        createUsersCategoriesTable() &&
        createAdminsTable();
    }

    public static boolean initDatabase() {
        return createTables();
    }

    private static Connection conn = null;
    private static final String DATABASE_PATH = System.getProperty("user.dir") + "/test.db";
    private static final String URL_CONNECTION = "jdbc:sqlite:" + DATABASE_PATH;

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection(URL_CONNECTION);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, DatabaseConfig.class.getName()+"::createConnecton(): " + ex.getMessage());
            conn = null;
        }

        return conn;
    }
}
