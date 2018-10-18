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

public class CustomerOrder extends SuperModel implements Comparable<CustomerOrder>{
    private String date;
    private String time;
    private String id;
    private Customer customer;

    public CustomerOrder() {
    }

    public CustomerOrder(String date, String time, String id, Customer customer) {
        this.date = date;
        this.time = time;
        this.id = id;
        this.customer = customer;
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

    public Customer getCustomer() {
        return customer;
    }

    public String getCustomerId() {
        return customer.getId();
    }
     
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
   
    //------------------------- User Methods ----------------------------//
    public String getFullName() {
        return customer.getFullName();
    }
    
    public double getFinal() {
        return 0;//Double.parseDouble(total) - Double.parseDouble(discount);
    }
    
    //------------------------------------------------------------------//
    @Override
    public String toString() {
        return  getDate()       + Symbol.SPLIT +
                getTime()       + Symbol.SPLIT +
                getId()         + Symbol.SPLIT +
                getCustomerId();
    }      
    
    @Override
    public int compareTo(CustomerOrder dto) {
        boolean logic = Integer.parseInt(dto.getId()) > Integer.parseInt(this.getId());
        return  logic ? -1 : !logic ? 1 : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CustomerOrder) {
            return ((CustomerOrder)obj).getId().equals(this.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {        
        int hash = Objects.hashCode(String.format("%05d", this.getId()));
        return hash;
    }  
}
