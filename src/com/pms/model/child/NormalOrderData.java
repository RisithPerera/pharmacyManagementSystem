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

public class NormalOrderData extends SuperModel implements Comparable<NormalOrderData>{
   
    private String id;
    private NormalOrder normalOrder;
    private String amount;
    private String rate;

    public NormalOrderData() {
    }

    public NormalOrderData(String id, NormalOrder normalOrder, String amount, String rate) {
        this.id = id;
        this.normalOrder = normalOrder;
        this.amount = amount;
        this.rate = rate;
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NormalOrder getNormalOrder() {
        return normalOrder;
    }

    public String getNormalOrderId() {
        return normalOrder.getId();
    }
    
    public void setNormalOrder(NormalOrder normalOrder) {
        this.normalOrder = normalOrder;
    }

    public double getAmount() {
        return Double.parseDouble(amount);
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public double getRate() {
        return Double.parseDouble(rate);
    }

    public void setRate(String discountRate) {
        this.rate = discountRate;
    }
    
    //-------------------------- Calculatons ----------------------------------//
   
    public double getDiscount() {
        return getAmount() * getRate()/100;
    }
    
    public double getRemainder() {
        return getAmount() - getDiscount();
    }
    
    public double getPoints() {
        return getAmount() * 0.01;
    }
    
    //---------------------------- Override Methods ---------------------------//
    
    @Override
    public String toString() {
        return  getId()               + Symbol.SPLIT +
                getNormalOrderId()    + Symbol.SPLIT +
                getAmount()           + Symbol.SPLIT +
                getRate();              
    }      
    
    @Override
    public int compareTo(NormalOrderData dto) {
        boolean logic = Integer.parseInt(dto.getId()) > Integer.parseInt(this.getId());
        return  logic ? -1 : !logic ? 1 : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NormalOrderData) {
            return ((NormalOrderData)obj).getId().equals(this.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {        
        int hash = Objects.hashCode(String.format("%05d", this.getId()));
        return hash;
    }  
}
