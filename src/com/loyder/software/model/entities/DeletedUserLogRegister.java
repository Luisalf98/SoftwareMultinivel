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
public class DeletedUserLogRegister {
    
    private Long rowId;
    private Long userId;
    private Long childId;
    private Long parentId;
    
    public DeletedUserLogRegister(){}

    public DeletedUserLogRegister(Long rowId, Long userId, Long childId, Long parentId) {
        this.rowId = rowId;
        this.userId = userId;
        this.childId = childId;
        this.parentId = parentId;
    }
    
    public Long getRowId() {
        return rowId;
    }

    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    
}
