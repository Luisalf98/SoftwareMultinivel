/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.dao.config;

import com.loyder.software.model.dao.AdminDao;
import com.loyder.software.model.dao.BonusDao;
import com.loyder.software.model.dao.CategoryDao;
import com.loyder.software.model.dao.IncomeDao;
import com.loyder.software.model.dao.PercentageDao;
import com.loyder.software.model.dao.ProductDao;
import com.loyder.software.model.dao.SaleDao;
import com.loyder.software.model.dao.UserDao;
import com.loyder.software.model.dao.impl.IAdminDao;
import com.loyder.software.model.dao.impl.IBonusDao;
import com.loyder.software.model.dao.impl.ICategoryDao;
import com.loyder.software.model.dao.impl.IIncomeDao;
import com.loyder.software.model.dao.impl.IPercentageDao;
import com.loyder.software.model.dao.impl.IProductDao;
import com.loyder.software.model.dao.impl.ISaleDao;
import com.loyder.software.model.dao.impl.IUserDao;

/**
 *
 * @author Luis Perez
 */
public class DatabaseConnection {
    
    private DatabaseConnection(){}
    
    public static UserDao getUserDao(){
        return IUserDao.getUserDao();
    }
    public static AdminDao getAdminDao(){
        return IAdminDao.getAdminDao();
    }
    public static ProductDao getProductDao(){
        return IProductDao.getProductDao();
    }
    public static SaleDao getSaleDao(){
        return ISaleDao.getSaleDao();
    }
    public static PercentageDao getPercentageDao(){
        return IPercentageDao.getPercentageDao();
    }
    public static BonusDao getBonusDao(){
        return IBonusDao.getBonusDao();
    }
    public static IncomeDao getIncomeDao(){
        return IIncomeDao.getIncomeDao();
    }
    public static CategoryDao getCategoryDao(){
        return ICategoryDao.getCategoryDao();
    }
}
