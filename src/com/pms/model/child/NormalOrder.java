/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pms.model.child;


import com.pms.manifest.Symbol;
import com.pms.model.superb.SuperModel;
import java.util.Objects;


/**
 *
 * @author RISITH-PC
 */

public class NormalOrder extends SuperModel implements Comparable<NormalOrder>{
    private String date;
    private String time;
    private String id;


    public NormalOrder() {
    }

    public NormalOrder(String date, String time, String id) {
        this.date = date;
        this.time = time;
        this.id = id;
    }
   
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //------------------------- User Methods ----------------------------//
    public String getDetail() {
        return "**************";
    }
    
    //-----------------------------------------------------------------//
    
    @Override
    public String toString() {
        return  getDate()       + Symbol.SPLIT +
                getTime()       + Symbol.SPLIT +
                getId();
    }      
    
    @Override
    public int compareTo(NormalOrder dto) {
        boolean logic = Integer.parseInt(dto.getId()) > Integer.parseInt(this.getId());
        return  logic ? -1 : !logic ? 1 : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NormalOrder) {
            return ((NormalOrder)obj).getId().equals(this.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {        
        int hash = Objects.hashCode(String.format("%05d", this.getId()));
        return hash;
    }  
}
