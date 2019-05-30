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

public class User{
    
    private Long id;
    private Long userId;
    private String name;
    private String lastName;
    private String address;
    private String tel;
    private String cel;
    private Long adderId;
    private Long entranceDate;
    private Long categoryId;

    public User(){
        
    }

    public User(Long id, Long userId, String name, String lastName, String address, String tel, String cel, Long adderId, Long entranceDate, Long categoryId) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.tel = tel;
        this.cel = cel;
        this.adderId = adderId;
        this.entranceDate = entranceDate;
        this.categoryId = categoryId;
    }
    
    

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    

    public Long getAdderId() {
        return adderId;
    }

    public void setAdderId(Long adderId) {
        this.adderId = adderId;
    }

    public Long getEntranceDate() {
        return entranceDate;
    }

    public void setEntranceDate(Long entranceDate) {
        this.entranceDate = entranceDate;
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCel() {
        return cel;
    }

    public void setCel(String cel) {
        this.cel = cel;
    }
    
}
