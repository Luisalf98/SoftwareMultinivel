/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.dao;

import com.loyder.software.model.dao.config.DatabaseConfig.SaleState;
import com.loyder.software.model.entities.Sale;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Luis Perez
 */
public interface SaleDao {
    
    public Long addSale(Sale sale, ArrayList<Sale.Detail> details);
    public boolean updateSaleState(Long id, SaleState state);
    public boolean removeSale(Long id);
    public boolean removeSaleDetailsBySaleId(Long id);
    public boolean removeBonusesBySaleId(Long id);
    public boolean removeIncomeBySaleId(Long id);
    public Sale getSaleById(Long id);
    public Sale getSaleByIdAndUserId(Long saleId, Long userId);
    public ArrayList<Sale.Detail> getSaleDetailsBySaleId(Long saleId);
    //Not Filtered Sales
    public ArrayList<Sale> getAllSalesByUserId(Long userId, String stateWildcard, String typeWildcard);
    public ArrayList<Sale> getSalesInDateRangeByUserId(Long userId, Date d1, Date d2, String stateWildcard, String typeWildcard);
    public ArrayList<Sale> getAllSales(String stateWildcard, String typeWildcard);
    public ArrayList<Sale> getSalesInDateRange(Date d1, Date d2, String stateWildcard, String typeWildcard);
    
    public ArrayList<Sale.SoldProduct> getAllSoldProducts();
    public ArrayList<Sale.SoldProduct> getAllSoldProductsInDateRange(Long d1, Long d2);
    public ArrayList<Sale.SoldProduct> getSoldProductById(Long id);
}
