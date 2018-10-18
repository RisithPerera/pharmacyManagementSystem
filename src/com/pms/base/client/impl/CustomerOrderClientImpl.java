/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pms.base.client.impl;

import com.pms.base.client.CustomerOrderClient;
import com.pms.base.file.FileManager;
import com.pms.base.list.ListConnection;
import com.pms.manifest.Data;
import com.pms.manifest.Symbol;
import com.pms.model.child.Customer;
import com.pms.model.child.CustomerOrder;
import java.io.IOException;
import java.util.Collections;
import javafx.collections.ObservableList;

/**
 *
 * @author RISITH-PC
 */
public class CustomerOrderClientImpl implements CustomerOrderClient{ 
    
    private static CustomerOrderClientImpl customerOrderClient;
    private static ObservableList<CustomerOrder> customerOrderList;
    private static ObservableList<String> recordList;

    private CustomerOrderClientImpl() {
        customerOrderList = (ObservableList<CustomerOrder>) ListConnection.getInstance().getCustomerOrderList();
        recordList = (ObservableList<String>) ListConnection.getInstance().getRecordList();
    }
  
    public static CustomerOrderClientImpl getInstance() {
        if (customerOrderClient == null) {
            customerOrderClient = new CustomerOrderClientImpl();
        }
        return customerOrderClient;
    }
    
    @Override
    public boolean add(CustomerOrder customerOrder) throws IOException {
        if(customerOrderList.isEmpty()){
            customerOrderList.add(0, customerOrder);
        }else{
            customerOrderList.add(customerOrderList.size()-1, customerOrder);
        }    
        return FileManager.getInstance().addRecord(customerOrder, Data.CUSTOMER_ORDER);
    }

    @Override
    public boolean update(CustomerOrder customerOrder) throws IOException {
        if(!customerOrderList.isEmpty()){
            int index = customerOrderList.indexOf(customerOrder.getId());
            customerOrderList.set(index, customerOrder);
            return writeAll();
        }
        return false;
    }

    @Override
    public boolean delete(CustomerOrder customerOrder) throws IOException {
        customerOrderList.remove(customerOrder);
        return writeAll();
    }

    @Override
    public CustomerOrder search(String id) throws IOException {
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setId(id);
        if (customerOrderList.isEmpty()) {
            readAll();
        }
        int index = customerOrderList.indexOf(customerOrder);
        if (index != -1) {
            return customerOrderList.get(index);
        }
        return null;
    }

    @Override
    public ObservableList<CustomerOrder> getAll() throws IOException {
        if (customerOrderList.isEmpty()) {
            readAll();
        }
        return customerOrderList;
    }
   
    @Override
    public boolean readAll() throws IOException {
        FileManager.getInstance().readAllRecords(Data.CUSTOMER_ORDER);
        customerOrderList.clear();
        try{
            for (String line : recordList) {
                String[] parts = line.split(Symbol.SPLIT_SYMBOL_EXPRESSION);
                System.out.println("Customer Order Parts : "+parts.length);
                Customer customer = CustomerClientImpl.getInstance().search(parts[3]);           
                if(customer != null){
                    CustomerOrder customerOrder = new CustomerOrder(parts[0], 
                                                                    parts[1], 
                                                                    parts[2],
                                                                    customer);
                    customerOrderList.add(customerOrder);
                }else{
                    System.out.println("CustomerId "+parts[3]+" Data is lost.");
                }          
            }
        }catch(Exception e){
            System.out.println("CustomerOrder Database is malfunctioned");
        }
        Collections.sort(customerOrderList);
        return true;
    }

    @Override
    public boolean writeAll() throws IOException {
        recordList.clear();
        if (customerOrderList != null) {
            Collections.sort(customerOrderList);
            customerOrderList.stream().forEach((customerOrder) -> {
                recordList.add(customerOrder.toString());
            });
            FileManager.getInstance().writeAllRecords(Data.CUSTOMER);
        }
        return true;
    }
    
    @Override
    public long getNextId() throws IOException{
        if (customerOrderList.isEmpty()) {
            readAll();
        }
        
        if (customerOrderList.isEmpty()) {
            return 0;
        }
        long preIndex = -1;
        for (CustomerOrder customerOrder : customerOrderList) {
            if(Long.parseLong(customerOrder.getId()) - preIndex > 1){
                break;
            }
            preIndex = Long.parseLong(customerOrder.getId());
        }
        return preIndex+1;
    }
}
