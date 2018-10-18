/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pms.base.client.impl;

import com.pms.base.client.UserClient;
import com.pms.base.file.FileManager;
import com.pms.base.list.ListConnection;
import com.pms.manifest.Data;
import com.pms.manifest.Symbol;
import com.pms.model.child.User;
import java.io.IOException;
import java.util.Collections;
import javafx.collections.ObservableList;

/**
 *
 * @author RISITH-PC
 */
public class UserClientImpl implements UserClient{ 
    
    private static UserClientImpl userClientImpl;
    private static ObservableList<User> userList;
    private static ObservableList<String> recordList;

    public UserClientImpl() {
        userList = (ObservableList<User>) ListConnection.getInstance().getUserList();
        recordList = (ObservableList<String>) ListConnection.getInstance().getRecordList();
    }

    public static UserClientImpl getInstance() {
        if (userClientImpl == null) {
            userClientImpl = new UserClientImpl();
        }
        return userClientImpl;
    }
      
    @Override
    public boolean add(User user) throws IOException {
        if(userList.isEmpty()){
            userList.add(0, user);
        }else{
            userList.add(userList.size()-1, user);
        }  
        return FileManager.getInstance().addRecord(user, Data.USER);
    }

    @Override
    public boolean update(User user) throws IOException {
        int index = userList.indexOf(user.getId());
        userList.set(index, user);
        return writeAll();
    }

    @Override
    public boolean delete(User user) throws IOException {
        userList.remove(user);
        return writeAll();
    }

    @Override
    public User search(String id) throws IOException {
        User user = new User();
        user.setId(id);
        if (userList.isEmpty()) {
            readAll();
        }
        int index = userList.indexOf(user);
        if (index != -1) {
            return userList.get(index);
        }
        return null;
    }

    @Override
    public ObservableList<User> getAll() throws IOException {
        if (userList.isEmpty()) {
            readAll();
        }
        return userList;
    }

    @Override
    public boolean readAll() throws IOException {
        FileManager.getInstance().readAllRecords(Data.USER);
        userList.clear();
        for (String line : recordList) {
            String[] parts = line.split(Symbol.SPLIT_SYMBOL_EXPRESSION);
            User user = new User(parts[0], 
                                 parts[1], 
                                 parts[2], 
                                 parts[3], 
                                 parts[4], 
                                 parts[5]);
            userList.add(user);
        }
        Collections.sort(userList);
        return true;
    }

    @Override
    public boolean writeAll() throws IOException {
        recordList.clear();
        if (userList != null) {
            Collections.sort(userList);
            userList.stream().forEach((customDTO) -> {
                recordList.add(customDTO.toString());
            });
            FileManager.getInstance().writeAllRecords(Data.USER);
        }
        return true;
    }
    
     @Override
    public long getNextId() {
         if (userList.isEmpty()) {
            return 0;
        }
        long preIndex = -1;
        for (User user : userList) {
            if(Long.parseLong(user.getId()) - preIndex > 1){
                break;
            }
            preIndex = Long.parseLong(user.getId());
        }
        return preIndex+1;
    }
}