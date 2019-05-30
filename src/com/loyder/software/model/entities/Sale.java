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
public class Sale {
    
    private Long id;
    private Long buyerId;
    private Long saleDate;
    private String state;
    private String type;
    private Double total;
    
    public Sale(){}

    public Sale(Long id, Long buyerId, Long saleDate, Double total, String type, String state) {
        this.id = id;
        this.buyerId = buyerId;
        this.saleDate = saleDate;
        this.total = total;    
        this.state = state;
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Long getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Long saleDate) {
        this.saleDate = saleDate;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
    
    public static class Detail{
        private Long quantity;
        private Long productId;
        private Long saleId;
        
        public Detail(){}

        public Detail(Long quantity, Long productId, Long saleId) {
            this.quantity = quantity;
            this.productId = productId;
            this.saleId = saleId;
        }

        public Long getSaleId() {
            return saleId;
        }

        public void setSaleId(Long saleId) {
            this.saleId = saleId;
        }
        
        
        
        public Long getQuantity() {
            return quantity;
        }

        public void setQuantity(Long quantity) {
            this.quantity = quantity;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }
        
        
    } 
}
