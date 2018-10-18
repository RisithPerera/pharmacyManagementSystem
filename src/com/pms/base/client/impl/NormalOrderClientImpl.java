/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pms.base.client.impl;

import com.pms.base.client.NormalOrderClient;
import com.pms.base.file.FileManager;
import com.pms.base.list.ListConnection;
import com.pms.manifest.Data;
import com.pms.manifest.Symbol;
import com.pms.model.child.NormalOrder;
import java.io.IOException;
import java.util.Collections;
import javafx.collections.ObservableList;

/**
 *
 * @author RISITH-PC
 */
public class NormalOrderClientImpl implements NormalOrderClient{ 
    
    private static NormalOrderClientImpl normalOrderClientImpl;
    private static ObservableList<NormalOrder> normalOrderList;
    private static ObservableList<String> recordList;

    private NormalOrderClientImpl() {
        normalOrderList = (ObservableList<NormalOrder>) ListConnection.getInstance().getNormalOrderList();
        recordList = (ObservableList<String>) ListConnection.getInstance().getRecordList();
    }
    
    public static NormalOrderClientImpl getInstance() {
        if (normalOrderClientImpl == null) {
            normalOrderClientImpl = new NormalOrderClientImpl();
        }
        return normalOrderClientImpl;
    }
  
    @Override
    public boolean add(NormalOrder normalOrder) throws IOException {
        if(normalOrderList.isEmpty()){
            normalOrderList.add(0, normalOrder);
        }else{
            normalOrderList.add(normalOrderList.size()-1, normalOrder);
        }    
        return FileManager.getInstance().addRecord(normalOrder, Data.NORMAL_ORDER);
    }

    public boolean update(NormalOrder normalOrder) throws IOException {
        if(!normalOrderList.isEmpty()){
            int index = normalOrderList.indexOf(normalOrder.getId());
            normalOrderList.set(index, normalOrder);
            return writeAll();
        }
        return false;
    }

    @Override
    public boolean delete(NormalOrder normalOrder) throws IOException {
        normalOrderList.remove(normalOrder);
        return writeAll();
    }

    @Override
    public NormalOrder search(String id) throws IOException {
        NormalOrder normalOrder = new NormalOrder();
        normalOrder.setId(id);
        if (normalOrderList.isEmpty()) {
            readAll();
        }
        int index = normalOrderList.indexOf(normalOrder);
        if (index != -1) {
            return normalOrderList.get(index);
        }
        return null;
    }

    @Override
    public ObservableList<NormalOrder> getAll() throws IOException {
        if (normalOrderList.isEmpty()) {
            readAll();
        }
        return normalOrderList;
    }

    @Override
    public boolean readAll() throws IOException {
        FileManager.getInstance().readAllRecords(Data.NORMAL_ORDER);
        normalOrderList.clear();
        try{
            for(String line : recordList) {
                String[] parts = line.split(Symbol.SPLIT_SYMBOL_EXPRESSION);
                NormalOrder normalOrder = new NormalOrder(parts[0], parts[1], parts[2]);
                normalOrderList.add(normalOrder);
            }
        }catch(Exception e){
            System.out.println("NormalOrder Database is malfunctioned");
        }
        Collections.sort(normalOrderList);
        return true;
    }

    @Override
    public boolean writeAll() throws IOException {
        recordList.clear();
        if (normalOrderList != null) {
            Collections.sort(normalOrderList);
            normalOrderList.stream().forEach((customDTO) -> {
                recordList.add(customDTO.toString());
            });
            FileManager.getInstance().writeAllRecords(Data.NORMAL_ORDER);
        }
        return true;
    }
    
     @Override
    public long getNextId() {
        if (normalOrderList.isEmpty()) {
            return 0;
        }
        long preIndex = -1;
        for (NormalOrder normalOrder : normalOrderList) {
            if(Long.parseLong(normalOrder.getId()) - preIndex > 1){
                break;
            }
            preIndex = Long.parseLong(normalOrder.getId());
        }
        return preIndex+1;
    }
}
