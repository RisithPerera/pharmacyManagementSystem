/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pms.base.list;


import com.pms.model.child.Customer;
import com.pms.model.child.CustomerOrder;
import com.pms.model.child.CustomerOrderData;
import com.pms.model.child.NormalOrder;
import com.pms.model.child.NormalOrderData;
import com.pms.model.child.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author risit
 */
public class ListConnection {

    private static ListConnection listConnection;

    private final ObservableList<Customer> customerList;
    private final ObservableList<CustomerOrder> customerOrderList;
    private final ObservableList<CustomerOrderData> customerOrderDataList;
    private final ObservableList<NormalOrder> normalOrderList;
    private final ObservableList<NormalOrderData> normalOrderDataList;
    private final ObservableList<User> userList;
   
    private final ObservableList<String> recordList;

    private ListConnection() {
        this.customerList = FXCollections.observableArrayList();
        this.customerOrderList = FXCollections.observableArrayList();
        this.customerOrderDataList = FXCollections.observableArrayList();
        this.normalOrderList = FXCollections.observableArrayList();
        this.normalOrderDataList = FXCollections.observableArrayList();
        this.userList = FXCollections.observableArrayList();
        
        this.recordList = FXCollections.observableArrayList();
    }

    public static ListConnection getInstance() {
        if (listConnection == null) {
            listConnection = new ListConnection();
        }
        return listConnection;
    }

    public ObservableList<Customer> getCustomerList() {
        return customerList;
    }

    public ObservableList<CustomerOrder> getCustomerOrderList() {
        return customerOrderList;
    }

    public ObservableList<CustomerOrderData> getCustomerOrderDataList() {
        return customerOrderDataList;
    }

    public ObservableList<NormalOrder> getNormalOrderList() {
        return normalOrderList;
    }

    public ObservableList<NormalOrderData> getNormalOrderDataList() {
        return normalOrderDataList;
    }

    public ObservableList<User> getUserList() {
        return userList;
    }
    
     public ObservableList<String> getRecordList() {
        return recordList;
    }
}
