/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pms.manifest;

/**
 *
 * @author RISITH-PC
 */
public interface State {
    public enum ControllerType{
        CUSTOMER_ADD,
        CUSTOMER_UPDATE,
        CUSTOMER_ORDER_ADD,
        CUSTOMER_ORDER_UPDATE,
        NORMAL_ORDER_ADD,
        NORMAL_ORDER_UPDATE;
    }  
}
