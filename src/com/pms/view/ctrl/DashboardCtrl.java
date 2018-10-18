/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pms.view.ctrl;

import com.pms.manifest.View;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author RISITH-PC
 */
public class DashboardCtrl implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    void addNewCustomerOrderBtnEvent(ActionEvent event) {
        try {
            CustomerOrderAddCtrl addCtrl = (CustomerOrderAddCtrl) MainCtrl.getInstance().showContent(String.format(View.PATH, View.CUSTOMER_ORDER_ADD));
            addCtrl.prepareCustomerOrderAddView(null);
        } catch (IOException ex) {
            Logger.getLogger(CustomerViewCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void addNewNormalOrderBtnEvent(ActionEvent event) {
        try {
            NormalOrderAddCtrl addCtrl = (NormalOrderAddCtrl) MainCtrl.getInstance().showContent(String.format(View.PATH, View.NORMAL_ORDER_ADD));
            addCtrl.prepareNormalOrderAddView();
        } catch (IOException ex) {
            Logger.getLogger(CustomerViewCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
