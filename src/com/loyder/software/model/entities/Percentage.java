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
public class Percentage {
    
    private Long id;
    private Double percentage;

    public Percentage(Long id, Double percentage) {
        this.id = id;
        this.percentage = percentage;
    }
    
    public Percentage(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
    
    
    
}
