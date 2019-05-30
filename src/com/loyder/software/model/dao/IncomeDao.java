/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.dao;

import com.loyder.software.model.entities.Income;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Luis Perez
 */
public interface IncomeDao {
    public boolean addIncome(Income income);
    public Income getIncomeById(Long incomeId);
    public ArrayList<Income> getAllIncomes();
    public ArrayList<Income> getIncomesInDateRange(Date d1, Date d2);
}
