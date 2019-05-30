/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.dao;

import com.loyder.software.model.entities.Bonus;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Luis Perez
 */
public interface BonusDao {
    public boolean addBonus(Bonus bonus);
    public Bonus getBonusById(Long id);
    public Bonus getBonusByIdAndUserId(Long bonusId, Long recommenderId);
    public ArrayList<Bonus> getBonusesByUserId(Long id);
    public ArrayList<Bonus> getBonusesBySaleId(Long id);
    public ArrayList<Bonus> getBonusesInDateRange(Date d1, Date d2);
    public ArrayList<Bonus> getBonusesInDateRangeByUserId(Date d1, Date d2, Long recommenderId);
    public ArrayList<Bonus> getAllBonuses();
    
}
