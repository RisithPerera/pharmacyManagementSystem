/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pms.base.client.impl;

import com.pms.base.client.NormalOrderDataClient;
import com.pms.base.file.FileManager;
import com.pms.base.list.ListConnection;
import com.pms.manifest.Data;
import com.pms.manifest.Symbol;
import com.pms.model.child.NormalOrder;
import com.pms.model.child.NormalOrderData;
import java.io.IOException;
import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author RISITH-PC
 */

public class NormalOrderDataClientImpl implements NormalOrderDataClient{ 
    
    private static NormalOrderDataClientImpl normalOrderDataClientImpl;
    private static ObservableList<NormalOrderData> normalOrderDataList;
    private static ObservableList<String> recordList;

    private NormalOrderDataClientImpl() {
        normalOrderDataList = (ObservableList<NormalOrderData>) ListConnection.getInstance().getNormalOrderDataList();
        recordList = (ObservableList<String>) ListConnection.getInstance().getRecordList();
    }
    
    public static NormalOrderDataClientImpl getInstance() {
        if (normalOrderDataClientImpl == null) {
            normalOrderDataClientImpl = new NormalOrderDataClientImpl();
        }
        return normalOrderDataClientImpl;
    }
    
    @Override
    public boolean add(NormalOrderData normalOrderData) throws IOException {
        normalOrderDataList.add(normalOrderData);
        return FileManager.getInstance().addRecord(normalOrderData, Data.NORMAL_ORDER_DATA);
    }
    
    @Override
    public boolean update(NormalOrderData normalOrderData) throws IOException {
        int index = normalOrderDataList.indexOf(normalOrderData.getId());
        normalOrderDataList.set(index, normalOrderData);
        return writeAll();
    }
    
    @Override
    public boolean delete(NormalOrderData normalOrderData) throws IOException {
        normalOrderDataList.remove(normalOrderData);
        return writeAll();
    }
    
    @Override
    public NormalOrderData search(String id) throws IOException {
        NormalOrderData normalOrderData = new NormalOrderData();
        normalOrderData.setId(id);
        if (normalOrderDataList.isEmpty()) {
            readAll();
        }
        int index = normalOrderDataList.indexOf(normalOrderData);
        if (index != -1) {
            return normalOrderDataList.get(index);
        }
        return null;
    }
    
    @Override
    public ObservableList<NormalOrderData> getAll() throws IOException {
        if (normalOrderDataList.isEmpty()) {
            readAll();
        }
        return normalOrderDataList;
    }
    
    @Override
    public boolean addOrderData(ObservableList<NormalOrderData> list) throws IOException {
        for (NormalOrderData normalOrderData : list) {
            normalOrderData.setId(Long.toString(getNextId()));
            normalOrderDataList.add(normalOrderData);
            Collections.sort(normalOrderDataList);
        }       
        return writeAll();
    }

    @Override
    public boolean updateOrderData(NormalOrder normalOrder, ObservableList<NormalOrderData> list) throws IOException {
        normalOrderDataList.removeIf(normalOrderData -> normalOrderData.getNormalOrder().equals(normalOrder));
        return addOrderData(list);
    }

    @Override
    public boolean deleteOrderData(NormalOrder normalOrder) throws IOException {       
        normalOrderDataList.removeIf(normalOrderData -> normalOrderData.getNormalOrder().equals(normalOrder));
        System.out.println("deleted List : "+ normalOrderDataList);
        return writeAll();
    }
    
    @Override
    public ObservableList<NormalOrderData> getOrderData(NormalOrder normalOrder) throws IOException { 
        ObservableList<NormalOrderData> list = FXCollections.observableArrayList();
        for (NormalOrderData normalOrderData : normalOrderDataList) {
            if(normalOrderData.getNormalOrder().equals(normalOrder)){
                list.add(normalOrderData);
            }
        }
        return list;
    }
    
    @Override
    public long getNextId() {
        if (normalOrderDataList.isEmpty()) {
            return 0;
        }
        long preIndex = -1;
        for (NormalOrderData normalOrderData : normalOrderDataList) {
            if(Long.parseLong(normalOrderData.getId()) - preIndex > 1){
                break;
            }
            preIndex = Long.parseLong(normalOrderData.getId());
        }
        return preIndex+1;
    }
    
    @Override
    public boolean readAll() throws IOException {
        FileManager.getInstance().readAllRecords(Data.NORMAL_ORDER_DATA);
        normalOrderDataList.clear();
        try{
            for (String line : recordList) {
                String[] parts = line.split(Symbol.SPLIT_SYMBOL_EXPRESSION);
                NormalOrder normalOrder = NormalOrderClientImpl.getInstance().search(parts[1]);
                if(normalOrder != null){
                    NormalOrderData normalOrderData = new NormalOrderData(parts[0],
                                                                          normalOrder, 
                                                                          parts[2], 
                                                                          parts[3]);
                    normalOrderDataList.add(normalOrderData);
                }else{
                    System.out.println("NormalOrderDataId "+parts[1]+" Data is lost.");
                }
            }
        }catch(Exception e){
            System.out.println("NormalOrderData Database is malfunctioned");
        }
        Collections.sort(normalOrderDataList);
        return true;
    }

    @Override
    public boolean writeAll() throws IOException {
        recordList.clear();
        if (normalOrderDataList != null) {
            Collections.sort(normalOrderDataList);
            normalOrderDataList.stream().forEach((customDTO) -> {
                recordList.add(customDTO.toString());
            });
            FileManager.getInstance().writeAllRecords(Data.NORMAL_ORDER_DATA);
        }
        return true;
    }
}
