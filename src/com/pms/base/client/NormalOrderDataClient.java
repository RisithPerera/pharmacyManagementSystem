/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pms.base.client;


import com.pms.base.client.superb.SuperClient;
import com.pms.model.child.NormalOrder;
import com.pms.model.child.NormalOrderData;
import java.io.IOException;
import javafx.collections.ObservableList;


/**
 *
 * @author RISITH-PC
 */
public interface NormalOrderDataClient extends SuperClient<NormalOrderData>{
    public boolean addOrderData(ObservableList<NormalOrderData> list) throws IOException;
    public boolean updateOrderData(NormalOrder t, ObservableList<NormalOrderData> list) throws IOException;
    public boolean deleteOrderData(NormalOrder t) throws IOException;
    public ObservableList<NormalOrderData> getOrderData(NormalOrder t) throws IOException;

}
