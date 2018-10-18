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

public interface Data {
    
    public static String PATH = "database/%sBase/%s.txt";
        
    public static final String CUSTOMER = "Customer";
    public static final String CUSTOMER_ORDER = "CustomerOrder";
    public static final String CUSTOMER_ORDER_DATA = "CustomerOrderData";
    public static final String NORMAL_ORDER = "NormalOrder";
    public static final String NORMAL_ORDER_DATA  = "NormalOrderData";
    public static final String USER = "User";
    
    public static final String DISTRICTS[] = {"Kalutara","Colombo","Ampara","Anuradhapura","Badulla","Batticaloa","Galle","Gampaha","Hambantota","Jaffna","Kandy","Kegalle","Kurunegala","Mannar","Matale","Matara","Moneragala","Mullaitivu","Nuwara Eliya","Polonnaruwa","Puttalam","Ratnapura","Trincomalee","Vavuniya"};
    public static final String USER_TYPE[] = {"Admin","Clerk","Audit"};
    public static final String YEARS[] = {"2018","2019"};    
    public static final String MONTHS[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};   
    public static final String DAYS[] = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};   
    
}
