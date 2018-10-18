/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pms.base.client.superb;


import com.pms.model.superb.SuperModel;
import java.io.IOException;
import javafx.collections.ObservableList;

/**
 *
 * @author Pahansith
 * @param <T>
 */
public interface SuperClient<T extends SuperModel>{    
    public boolean add(T t) throws IOException;
    public boolean update(T t) throws IOException;
    public T search(String t) throws IOException;
    public boolean delete(T t) throws IOException;  
    public ObservableList<T> getAll() throws IOException;
    public long getNextId() throws IOException;
    public boolean readAll() throws IOException;
    public boolean writeAll() throws IOException;
}
