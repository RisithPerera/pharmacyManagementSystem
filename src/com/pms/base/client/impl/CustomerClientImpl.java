/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pms.base.client.impl;

import com.pms.base.client.CustomerClient;
import com.pms.base.file.FileManager;
import com.pms.base.list.ListConnection;
import com.pms.manifest.Data;
import com.pms.manifest.Symbol;
import com.pms.model.child.Customer;
import java.io.IOException;
import java.util.Collections;
import javafx.collections.ObservableList;

/**
 *
 * @author RISITH-PC
 */
public class CustomerClientImpl implements CustomerClient{ 
    
    private static CustomerClientImpl customerClient;
    private static ObservableList<Customer> customerList;
    private static ObservableList<String> recordList;

    private CustomerClientImpl() {
        customerList = (ObservableList<Customer>) ListConnection.getInstance().getCustomerList();
        recordList = (ObservableList<String>) ListConnection.getInstance().getRecordList();
    }

    public static CustomerClientImpl getInstance() {
        if (customerClient == null) {
            customerClient = new CustomerClientImpl();
        }
        return customerClient;
    }
    
    @Override
    public  boolean add(Customer customer) throws IOException {
        customerList.add(customer);
        return FileManager.getInstance().addRecord(customer, Data.CUSTOMER);
    }

    @Override
    public  boolean update(Customer customer) throws IOException {
        int index = customerList.indexOf(customer);
        if (index != -1) {
            customerList.set(index, customer);
            return writeAll();
        }
        return false;
    }

    @Override
    public  boolean delete(Customer customer) throws IOException {
        customerList.remove(customer);
        return writeAll();
    }

    @Override
    public  Customer search(String id) throws IOException {
        Customer customer = new Customer();
        customer.setId(id);
        if (customerList.isEmpty()) {
            readAll();
        }
        int index = customerList.indexOf(customer);
        if (index != -1) {
            return customerList.get(index);
        }
        return null;
    }

    @Override
    public  ObservableList<Customer> getAll() throws IOException {
        if (customerList.isEmpty()) {
            readAll();
        }
        return customerList;
    }

    @Override
    public  boolean readAll() throws IOException {
        FileManager.getInstance().readAllRecords(Data.CUSTOMER);       
        customerList.clear();
        System.out.println("dddd");
        try{
            for (String line : recordList) {               
                String[] parts = line.split(Symbol.SPLIT_SYMBOL_EXPRESSION);
                System.out.println("Customer Parts : "+parts.length);
                Customer customer = new Customer(parts[0], 
                                                 parts[1], 
                                                 parts[2], 
                                                 parts[3], 
                                                 parts[4], 
                                                 parts[5], 
                                                 parts[6], 
                                                 parts[7], 
                                                 parts[8], 
                                                 parts[9], 
                                                 parts[10], 
                                                 parts[11], 
                                                 parts[12], 
                                                 parts[13], 
                                                 parts[14], 
                                                 parts[15], 
                                                 parts[16],
                                                 parts[17]
                                               );
                customerList.add(customer);
            }
        }catch(Exception e){
            System.out.println("Customer Database is malfunctioned");
        }
        Collections.sort(customerList);
        return true;
    }

    @Override
    public boolean writeAll() throws IOException {
        recordList.clear();
        if (customerList != null) {
            Collections.sort(customerList);
            customerList.stream().forEach((customDTO) -> {
                recordList.add(customDTO.toString());
            });
            FileManager.getInstance().writeAllRecords(Data.CUSTOMER);
        }
        return true;
    }

    @Override
    public long getNextId() {
         if (customerList.isEmpty()) {
            return 0;
        }
        long preIndex = -1;
        for (Customer customer : customerList) {
            if(Long.parseLong(customer.getId()) - preIndex > 1){
                break;
            }
            preIndex = Long.parseLong(customer.getId());
        }
        return preIndex+1;
    }
}
