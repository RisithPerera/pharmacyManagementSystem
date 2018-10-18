/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pms.view.ctrl;

import com.pms.base.client.impl.CustomerClientImpl;
import com.pms.manifest.Data;
import com.pms.manifest.Message;
import com.pms.manifest.View;
import com.pms.model.child.Customer;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author RISITH-PC
 */
public class CustomerViewCtrl implements Initializable {

    @FXML
    private TableView<Customer> customerTable;
    
    @FXML
    private TableColumn<Customer, String> dateCol, idCol,  nameCol, addressCol, ageCol, identityCol;
    
    @FXML
    private TableColumn<Customer, String> contactsCol, periodCol, pointsCol;
    
    @FXML
    private TextField searchCustomerText;
   
    private ObservableList<Customer> customerList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
            ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
            identityCol.setCellValueFactory(new PropertyValueFactory<>("identity"));
            contactsCol.setCellValueFactory(new PropertyValueFactory<>("contacts"));
            periodCol.setCellValueFactory(new PropertyValueFactory<>("period"));
            pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));
            
            customerList = CustomerClientImpl.getInstance().getAll();
            customerTable.getItems().setAll(customerList);
            // TODO
        } catch (IOException ex) {
            Logger.getLogger(CustomerViewCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        searchCustomerText.textProperty().addListener(
            new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldVal, Object newVal) {
                    searchCustomer((String)oldVal, (String)newVal);
                }
            }
        );
        
        setTableMenu();
    }    

    @FXML
    private void addNewBtnEvent(ActionEvent event) {
        try {
            CustomerAddCtrl customerAddCtrl = (CustomerAddCtrl) MainCtrl.getInstance().showContent(String.format(View.PATH, View.CUSTOMER_ADD));
            customerAddCtrl.prepareCustomerAddView();
        } catch (IOException ex) {
            Logger.getLogger(CustomerViewCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void searchCustomer(String oldText, String newText) {
        if ( oldText != null && (newText.length() < oldText.length()) ) {
            customerTable.getItems().setAll(customerList);
        }
         
        String[] parts = newText.toUpperCase().split(" ");

        ObservableList<Customer> subentries = FXCollections.observableArrayList();
        for(Customer entry: customerTable.getItems()){
            boolean match = true;
            for ( String part: parts ) {
                if ( ! entry.toString().toUpperCase().contains(part) ) {
                    match = false;
                    break;
                }
            }
            
            if ( match ) {
                subentries.add(entry);
            }
        }
        customerTable.getItems().setAll(subentries);
    }   
    
    private void setTableMenu() {
        customerTable.setRowFactory((TableView<Customer> tableView) -> {
            TableRow<Customer> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem addMenu = new MenuItem("Add Purchase");
            MenuItem viewMenu = new MenuItem("View Purchases");
            MenuItem editMenu = new MenuItem("Edit Customer");
            MenuItem deleteMenu = new MenuItem("Delete Customer");
            
            addMenu.setOnAction((ActionEvent event) -> {                         
                try{    
                    Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
                    CustomerOrderAddCtrl ctrl = (CustomerOrderAddCtrl) MainCtrl.getInstance().showContent(String.format(View.PATH, View.CUSTOMER_ORDER_ADD));
                    ctrl.prepareCustomerOrderAddView(selectedCustomer);
                } catch (IOException ex) {
                    Logger.getLogger(CustomerViewCtrl.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            viewMenu.setOnAction((ActionEvent event) -> {                         
                try{    
                    Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
                    CustomerOrderViewCtrl ctrl = (CustomerOrderViewCtrl) MainCtrl.getInstance().showContent(String.format(View.PATH, View.CUSTOMER_ORDER_VIEW));
                    ctrl.updateTableDataByCustomer(selectedCustomer);
                } catch (IOException ex) {
                    Logger.getLogger(CustomerViewCtrl.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            deleteMenu.setOnAction((ActionEvent event) -> {                         
                try {
                    Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();                    
                    if (CustomerClientImpl.getInstance().delete(selectedCustomer)) {
                        MessageBoxViewCtrl.display(Message.TITLE,String.format(Message.DELETE, Data.CUSTOMER));
                    }else{
                        MessageBoxViewCtrl.display(Message.TITLE,String.format(Message.UNSUCESS, Data.CUSTOMER));
                    }
                    customerTable.getItems().setAll(CustomerClientImpl.getInstance().getAll());
                } catch (IOException ex) {
                    Logger.getLogger(CustomerViewCtrl.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            editMenu.setOnAction((ActionEvent event) -> {                                        
                try{    
                    Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
                    CustomerAddCtrl ctrl = (CustomerAddCtrl) MainCtrl.getInstance().showContent(String.format(View.PATH, View.CUSTOMER_ADD));
                    ctrl.prepareCustomerUpdateView(selectedCustomer);
                } catch (IOException ex) {
                    Logger.getLogger(CustomerViewCtrl.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            contextMenu.getItems().addAll(addMenu,viewMenu,deleteMenu,editMenu);

            row.contextMenuProperty().bind(
                Bindings.when(row.emptyProperty())
                        .then((ContextMenu)null)
                        .otherwise(contextMenu)
                );
                return row ;
            });
    }
    
}



   