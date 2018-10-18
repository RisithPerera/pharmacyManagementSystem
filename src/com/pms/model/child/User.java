/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pms.model.child;


import com.pms.model.superb.SuperModel;
import java.util.Objects;
import com.pms.manifest.Symbol;

/**
 *
 * @author RISITH-PC
 */
public class User extends SuperModel implements Comparable<User>{
    
    private String date;
    private String time;
    private String id;    
    private String userName;
    private String password;
    private String type;

    public User() {
    }

    public User(String date, String time, String id, String userName, String password, String type) {
        this.date = date;
        this.time = time;
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    //-----------------------------------------------------------------//
    
    @Override
    public String toString() {
        return  getDate()     + Symbol.SPLIT +
                getTime()     + Symbol.SPLIT +
                getId()       + Symbol.SPLIT +
                getUserName() + Symbol.SPLIT +
                getPassword() + Symbol.SPLIT +
                getType();
    }      
    
    @Override
    public int compareTo(User dto) {
        boolean logic = Integer.parseInt(dto.getId()) > Integer.parseInt(this.getId());
        return  logic ? -1 : !logic ? 1 : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return ((User)obj).getId().equals(this.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {        
        int hash = Objects.hashCode(String.format("%05d", this.getId()));
        return hash;
    }    
}
