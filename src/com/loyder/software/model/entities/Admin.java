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
public class Admin {
    private Long id;
    private Long adminId;
    private String name;
    private String lastName;
    private String username;
    private String password;
    private String address;
    private String tel;
    private String cel;
    
    public Admin(){
        
    }

    public Admin(Long id, Long adminId, String name, String lastName, String username, String password, String address, String tel, String cel) {
        this.id = id;
        this.adminId = adminId;
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.address = address;
        this.tel = tel;
        this.cel = cel;
    }

    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
