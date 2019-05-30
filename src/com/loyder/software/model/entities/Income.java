/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.entities;

import java.util.Date;

/**
 *
 * @author Luis Perez
 */
public class Income {
    private Long id;
    private Long saleId;
    private Long date;
    private Double totalSale;
    private Double totalBonus;
    private Double totalIncome;

    public Income(){}
    
    public Income(Long id, Long saleId, Long date, Double totalSale, Double totalBonus, Double totalIncome) {
        this.id = id;
        this.saleId = saleId;
        this.date = date;
        this.totalSale = totalSale;
        this.totalBonus = totalBonus;
        this.totalIncome = totalIncome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Double getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(Double totalSale) {
        this.totalSale = totalSale;
    }

    public Double getTotalBonus() {
        return totalBonus;
    }

    public void setTotalBonus(Double totalBonus) {
        this.totalBonus = totalBonus;
    }

    public Double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
    }
    
    
    
}
