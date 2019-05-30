/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.dao;

import com.loyder.software.model.entities.Percentage;
import java.util.ArrayList;

/**
 *
 * @author Luis Perez
 */
public interface PercentageDao {
    
    public boolean addPercentage(Double percentage);
    public Percentage getPercentageById(Long id);
    public ArrayList<Percentage> getAllPercentages();
    public boolean removePercentage();
    public boolean updatePercentage(Percentage percentage);
    
}
