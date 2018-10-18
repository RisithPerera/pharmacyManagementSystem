/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pms.base.client.impl;

import com.pms.base.client.CustomerOrderDataClient;
import com.pms.base.file.FileManager;
import com.pms.base.list.ListConnection;
import com.pms.manifest.Data;
import com.pms.manifest.Symbol;
import com.pms.model.child.CustomerOrder;
import com.pms.model.child.CustomerOrderData;
import java.io.IOException;
import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author RISITH-PC
 */
public class CustomerOrderDataClientImpl implements CustomerOrderDataClient{ 
    
    private static CustomerOrderDataClientImpl customerOrderDataClientImpl;
    private static ObservableList<CustomerOrderData> customerOrderDataList;
    private static ObservableList<String> recordList;

    private CustomerOrderDataClientImpl() {
        customerOrderDataList = (ObservableList<CustomerOrderData>) ListConnection.getInstance().getCustomerOrderDataList();
        recordList = (ObservableList<String>) ListConnection.getInstance().getRecordList();
    }
   
    public static CustomerOrderDataClientImpl getInstance() {
        if (customerOrderDataClientImpl == null) {
            customerOrderDataClientImpl = new CustomerOrderDataClientImpl();
        }
        return customerOrderDataClientImpl;
    }
    
  
    @Override
    public boolean add(CustomerOrderData customerOrderData) throws IOException {        
        customerOrderDataList.add(customerOrderData);        
        return FileManager.getInstance().addRecord(customerOrderData, Data.CUSTOMER_ORDER_DATA);
    }

    @Override
    public boolean update(CustomerOrderData customerOrderData) throws IOException {
        int index = customerOrderDataList.indexOf(customerOrderData.getId());
        customerOrderDataList.set(index, customerOrderData);
        return writeAll();
    }

    @Override
    public boolean delete(CustomerOrderData customerOrderData) throws IOException {
        customerOrderDataList.remove(customerOrderData);
        return writeAll();
    }

    @Override
    public CustomerOrderData search(String id) throws IOException {
        CustomerOrderData customerOrderData = new CustomerOrderData();
        customerOrderData.setId(id);
        if (customerOrderDataList.isEmpty()) {
            readAll();
        }
        int index = customerOrderDataList.indexOf(customerOrderData);
        if (index != -1) {
            return customerOrderDataList.get(index);
        }
        return null;
    }

    @Override
    public ObservableList<CustomerOrderData> getAll() throws IOException {
        if (customerOrderDataList.isEmpty()) {
            readAll();
        }
        return customerOrderDataList;
    }
    
    @Override
    public boolean addOrderData(ObservableList<CustomerOrderData> list) throws IOException {
        for (CustomerOrderData customerOrderData : list) {
            customerOrderData.setId(Long.toString(getNextId()));
            customerOrderDataList.add(customerOrderData);
            Collections.sort(customerOrderDataList);
        }       
        return writeAll();
    }

    @Override
    public boolean updateOrderData(CustomerOrder customerOrder, ObservableList<CustomerOrderData> list) throws IOException {
        customerOrderDataList.removeIf(customerOrderData -> customerOrderData.getCustomerOrder().equals(customerOrder));
        return addOrderData(list);
    }

    @Override
    public boolean deleteOrderData(CustomerOrder customerOrder) throws IOException {       
        customerOrderDataList.removeIf(customerOrderData -> customerOrderData.getCustomerOrder().equals(customerOrder));
        return writeAll();
    }
    
    @Override
    public ObservableList<CustomerOrderData> getOrderData(CustomerOrder customerOrder) throws IOException { 
        ObservableList<CustomerOrderData> list = FXCollections.observableArrayList();
        for (CustomerOrderData customerOrderData : customerOrderDataList) {
            if(customerOrderData.getCustomerOrder().equals(customerOrder)){
                list.add(customerOrderData);
            }
        }
        return list;
    }
    
    @Override
    public long getNextId() {
        if (customerOrderDataList.isEmpty()) {
            return 0;
        }
        long preIndex = -1;
        for (CustomerOrderData customerOrderData : customerOrderDataList) {
            if(Long.parseLong(customerOrderData.getId()) - preIndex > 1){
                break;
            }
            preIndex = Long.parseLong(customerOrderData.getId());
        }
        return preIndex+1;
    }
    
    @Override
    public boolean readAll() throws IOException {
        FileManager.getInstance().readAllRecords(Data.CUSTOMER_ORDER_DATA);
        customerOrderDataList.clear();
        try{
            for (String line : recordList) {
                String[] parts = line.split(Symbol.SPLIT_SYMBOL_EXPRESSION);
                System.out.println("Customer Order Data Parts : "+parts.length);
                CustomerOrder customerOrder = CustomerOrderClientImpl.getInstance().search(parts[1]);
                if(customerOrder != null){
                    CustomerOrderData customerOrderData = new CustomerOrderData(parts[0],
                                                                                customerOrder, 
                                                                                parts[2], 
                                                                                parts[3]);
                    customerOrderDataList.add(customerOrderData);
                }else{
                    System.out.println("CustomerOrderId "+parts[1]+" Data is lost.");
                }
            }
        
        }catch(IOException e){
            System.out.println("CustomerOrderData Database is malfunctioned");
        }
        Collections.sort(customerOrderDataList);
        return true;
    }

  
    @Override
    public boolean writeAll() throws IOException {
        recordList.clear();
        if (customerOrderDataList != null) {
            Collections.sort(customerOrderDataList);
            customerOrderDataList.stream().forEach((customerOrderData) -> {
                recordList.add(customerOrderData.toString());
            });
            FileManager.getInstance().writeAllRecords(Data.CUSTOMER_ORDER_DATA);
        }
        return true;
    }
}
