/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.entities;

import java.util.Objects;

/**
 *
 * @author Luis Perez
 */
public class Category{
    
    private Long id;
    private String name;

    public Category() {
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Category){
            Category c2 = (Category)obj;
            return Objects.equals(this.id, c2.id); //To change body of generated methods, choose Tools | Templates.
        }
        return false;
    }

}
