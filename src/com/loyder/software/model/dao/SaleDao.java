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
    public Sale getSaleById(Long id);
    public Sale getSaleByIdAndUserId(Long saleId, Long userId);
    public ArrayList<Sale.Detail> getSaleDetailsBySaleId(Long saleId);
    //Not Filtered Sales
    public ArrayList<Sale> getAllSalesByUserId(Long userId);
    public ArrayList<Sale> getSalesInDateRangeByUserId(Long userId, Date d1, Date d2);
    public ArrayList<Sale> getAllSales();
    public ArrayList<Sale> getSalesInDateRange(Date d1, Date d2);
    //Filtered Sales - Credit All
    public ArrayList<Sale> getAllSalesByUserIdCredit(Long userId);
    public ArrayList<Sale> getSalesInDateRangeByUserIdCredit(Long userId, Date d1, Date d2);
    public ArrayList<Sale> getAllSalesCredit();
    public ArrayList<Sale> getSalesInDateRangeCredit(Date d1, Date d2);
    //Filtered Sales - Credit Not Paid
    public ArrayList<Sale> getAllSalesByUserIdCreditNotPaid(Long userId);
    public ArrayList<Sale> getSalesInDateRangeByUserIdCreditNotPaid(Long userId, Date d1, Date d2);
    public ArrayList<Sale> getAllSalesCreditNotPaid();
    public ArrayList<Sale> getSalesInDateRangeCreditNotPaid(Date d1, Date d2);
    //Filtered Sales - Credit Paid
    public ArrayList<Sale> getAllSalesByUserIdCreditPaid(Long userId);
    public ArrayList<Sale> getSalesInDateRangeByUserIdCreditPaid(Long userId, Date d1, Date d2);
    public ArrayList<Sale> getAllSalesCreditPaid();
    public ArrayList<Sale> getSalesInDateRangeCreditPaid(Date d1, Date d2);
    //Filtered Sales - Cash All
    public ArrayList<Sale> getAllSalesByUserIdCash(Long userId);
    public ArrayList<Sale> getSalesInDateRangeByUserIdCash(Long userId, Date d1, Date d2);
    public ArrayList<Sale> getAllSalesCash();
    public ArrayList<Sale> getSalesInDateRangeCash(Date d1, Date d2);
}
