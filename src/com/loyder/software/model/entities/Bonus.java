/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.entities;


/**
 *
 * @author Luis Perez
 */
public class Bonus {
    private Long id;
    private Long percentageId;
    private Long saleId;
    private Double bonus;
    private Long userId;
    private Long date;
    
    public Bonus(){}

    public Bonus(Long id, Long percentageId, Long saleId, Double bonus, Long userId, Long date) {
        this.id = id;
        this.percentageId = percentageId;
        this.saleId = saleId;
        this.bonus = bonus;
        this.userId = userId;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPercentageId() {
        return percentageId;
    }

    public void setPercentageId(Long percentageId) {
        this.percentageId = percentageId;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public Double getBonus() {
        return bonus;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
    
    
}
