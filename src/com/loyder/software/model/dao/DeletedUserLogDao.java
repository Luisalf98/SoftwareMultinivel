/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.model.dao;

import com.loyder.software.model.entities.DeletedUserLogRegister;
import java.util.ArrayList;

/**
 *
 * @author Luis Perez
 */
public interface DeletedUserLogDao {
    
    
    public boolean deleteAllRegisters();
    public boolean addRegister(DeletedUserLogRegister dulr);
    public ArrayList<DeletedUserLogRegister> getDeletedUserLogRegisters();
}
