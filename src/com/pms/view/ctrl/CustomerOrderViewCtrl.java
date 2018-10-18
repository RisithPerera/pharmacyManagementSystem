/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pms.view.ctrl;

import com.pms.base.client.impl.CustomerOrderClientImpl;
import com.pms.base.client.impl.CustomerOrderDataClientImpl;
import com.pms.manifest.Data;
import com.pms.manifest.Message;
import com.pms.manifest.View;
import com.pms.model.child.Customer;
import com.pms.model.child.CustomerOrder;
import com.pms.model.child.CustomerOrderData;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
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
public class CustomerOrderViewCtrl implements Initializable {
    
    @FXML
    private TableView<CustomerOrder> orderTable;

    @FXML
    private TableView<CustomerOrderData> orderDataTable;
    
    @FXML
    private TableColumn<CustomerOrder, String> dateCol, timeCol,idCol, customerCol;

    @FXML
    private TableColumn<CustomerOrderData, String> amountCol, rateCol, discountCol, remainderCol;

    @FXML
    private Label totalAmountLabel,finalPriceLabel,discountLabel,pointsLabel;

    @FXML
    private ComboBox<String> yearCombo,monthCombo,dayCombo;
    
    @FXML
    private TextField searchCustomerText;
     
    private ObservableList<CustomerOrder> customerOrderList;
    private ObservableList<CustomerOrderData> customerOrderDataList;
       
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));
        discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        remainderCol.setCellValueFactory(new PropertyValueFactory<>("remainder"));
        
        yearCombo.getItems().setAll(Data.YEARS);
        monthCombo.getItems().setAll(Data.MONTHS);
        dayCombo.getItems().setAll(Data.DAYS);
        
        yearCombo.getSelectionModel().select(null);       
        monthCombo.setDisable(true);
        monthCombo.getSelectionModel().select(null);       
        dayCombo.setDisable(true);
        dayCombo.getSelectionModel().select(null);
        
         searchCustomerText.textProperty().addListener(
            new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldVal, Object newVal) {
                    updateTableDataBySearch((String) oldVal, (String)newVal);
                }
            }
        );
         
        setTableSelection();
        updateOrderDataView();
        setTableMenu();
    }    

    @FXML
    private void addNewBtnEvent(ActionEvent event) {
        try {
            CustomerOrderAddCtrl addCtrl = (CustomerOrderAddCtrl) MainCtrl.getInstance().showContent(String.format(View.PATH, View.CUSTOMER_ORDER_ADD));
            addCtrl.prepareCustomerOrderAddView(null);
        } catch (IOException ex) {
            Logger.getLogger(CustomerViewCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void viewAllBtnEvent(ActionEvent event) {
        System.out.println("view all");
        orderTable.getItems().setAll(customerOrderList);
        yearCombo.getSelectionModel().select(null);
        monthCombo.getSelectionModel().select(null);
        monthCombo.setDisable(true);
        dayCombo.getSelectionModel().select(null);
        dayCombo.setDisable(true);        
   }
       
    @FXML
    void todayBtnEvent(ActionEvent event) {
        Calendar today = Calendar.getInstance();
        yearCombo.getSelectionModel().select(String.valueOf(today.get(Calendar.YEAR)));       
        monthCombo.setDisable(false);
        monthCombo.getSelectionModel().select(today.get(Calendar.MONTH));       
        dayCombo.setDisable(false);
        dayCombo.getSelectionModel().select(String.valueOf(today.get(Calendar.DAY_OF_MONTH)));
        
        updateTableDataByDate(new SimpleDateFormat("yyyy-MM-dd").format(today.getTime()));
    }
    
    @FXML
    void yearComboEvent(ActionEvent event) {
        monthCombo.getSelectionModel().select(null);
        monthCombo.setDisable(false);
        dayCombo.getSelectionModel().select(null);
        dayCombo.setDisable(true);       
        String dateCompare = yearCombo.getSelectionModel().getSelectedItem();       
        updateTableDataByDate(dateCompare);
    }
    
    @FXML
    void monthComboEvent(ActionEvent event) {
        dayCombo.getSelectionModel().select(null);
        dayCombo.setDisable(false);
              
        String dateCompare = yearCombo.getSelectionModel().getSelectedItem()+"-"+
                             String.format("%02d", monthCombo.getSelectionModel().getSelectedIndex()+1);        
        if(monthCombo.getSelectionModel().getSelectedIndex()+1 > 0){
            updateTableDataByDate(dateCompare);
        }
    }
    
    @FXML
    void dayComboEvent(ActionEvent event) {       
        String dateCompare = yearCombo.getSelectionModel().getSelectedItem()+"-"+
                             String.format("%02d", monthCombo.getSelectionModel().getSelectedIndex()+1)+"-"+
                             dayCombo.getSelectionModel().getSelectedItem();
        
        updateTableDataByDate(dateCompare);
    }
    
    private void setTableMenu() {
        orderTable.setRowFactory((TableView<CustomerOrder> tableView) -> {
            TableRow<CustomerOrder> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem updateMenu = new MenuItem("Update Purchase");
            MenuItem deleteMenu = new MenuItem("Delete Purchase");
            
            deleteMenu.setOnAction((ActionEvent event) -> {                         
                try {
                    CustomerOrder selectedCustomerOrder = orderTable.getSelectionModel().getSelectedItem();
                    CustomerOrderDataClientImpl.getInstance().deleteOrderData(selectedCustomerOrder);
                    if (CustomerOrderClientImpl.getInstance().delete(selectedCustomerOrder)){                
                        System.out.println("Safely Deleted....!");
                    }else{
                        System.out.println("Delete Unsucessfull...!");
                    }
                    orderTable.getItems().remove(selectedCustomerOrder);
                } catch (IOException ex) {
                    Logger.getLogger(CustomerViewCtrl.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            updateMenu.setOnAction((ActionEvent event) -> {                                        
                CustomerOrder selectedCustomerOrder = orderTable.getSelectionModel().getSelectedItem();
                try{    
                    CustomerOrderAddCtrl ctrl = (CustomerOrderAddCtrl) MainCtrl.getInstance().showContent(String.format(View.PATH, View.CUSTOMER_ORDER_ADD));
                    ctrl.prepareCustomerOrderUpdateView(selectedCustomerOrder);
                } catch (IOException ex) {
                    Logger.getLogger(CustomerViewCtrl.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            contextMenu.getItems().addAll(deleteMenu,updateMenu);

            row.contextMenuProperty().bind(
                Bindings.when(row.emptyProperty())
                        .then((ContextMenu)null)
                        .otherwise(contextMenu)
                );
                return row ;
            });
        
        orderDataTable.setRowFactory((TableView<CustomerOrderData> tableView) -> {
            TableRow<CustomerOrderData> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteMenu = new MenuItem("Delete Item");
            
            deleteMenu.setOnAction((ActionEvent event) -> {                         
                try {
                    CustomerOrderData selectedCustomerOrderData = orderDataTable.getSelectionModel().getSelectedItem();                   
                    if (CustomerOrderDataClientImpl.getInstance().delete(selectedCustomerOrderData)){                     
                        customerOrderDataList.remove(selectedCustomerOrderData);
                        updateOrderDataView();
                        MessageBoxViewCtrl.display(Message.TITLE,String.format(Message.DELETE, Data.CUSTOMER_ORDER_DATA));
                    }else{
                        MessageBoxViewCtrl.display(Message.TITLE,String.format(Message.UNSUCESS, Data.CUSTOMER_ORDER_DATA));
                    }                   
                } catch (IOException ex) {
                    Logger.getLogger(CustomerViewCtrl.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            contextMenu.getItems().addAll(deleteMenu);
            row.contextMenuProperty().bind(
                Bindings.when(row.emptyProperty())
                        .then((ContextMenu)null)
                        .otherwise(contextMenu)
                );
                return row ;
            });
    }

    private void setTableSelection() {
         try {
            customerOrderList = CustomerOrderClientImpl.getInstance().getAll();
            orderTable.getItems().setAll(customerOrderList);
            orderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != oldSelection) {
                    try {
                        customerOrderDataList = CustomerOrderDataClientImpl.getInstance().getOrderData(newSelection);
                        updateOrderDataView();
                    } catch (IOException ex) {
                        Logger.getLogger(CustomerOrderViewCtrl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(CustomerOrderViewCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void updateTableDataByDate(String dateCompare){
        try{
            orderTable.getItems().setAll(customerOrderList);
            Iterator<CustomerOrder> iterator = orderTable.getItems().iterator();
            while(iterator.hasNext()){  
                if(! iterator.next().getDate().contains(dateCompare)){
                    iterator.remove();
                }
            }
        }catch(NullPointerException exception){}
    }
    
    private void updateTableDataBySearch(String oldText, String newText) {
        if ( oldText != null && (newText.length() < oldText.length()) ) {
            orderTable.getItems().setAll(customerOrderList);
        }
         
        String[] parts = newText.toUpperCase().split(" ");

        ObservableList<CustomerOrder> subentries = FXCollections.observableArrayList();
        for(CustomerOrder entry: orderTable.getItems()){
            boolean match = true;
            for ( String part: parts ) {
                if ( ! entry.getFullName().toUpperCase().contains(part) ) {
                    match = false;
                    break;
                }
            }
            
            if ( match ) {
                subentries.add(entry);
            }
        }
        orderTable.getItems().setAll(subentries);
    }   
    
    public void updateTableDataByCustomer(Customer customer){
        try{
            orderTable.getItems().setAll(customerOrderList);
            Iterator<CustomerOrder> iterator = orderTable.getItems().iterator();
            while(iterator.hasNext()){  
                if(! iterator.next().getFullName().contains(customer.getFullName())){
                    iterator.remove();
                }
            }
        }catch(NullPointerException exception){}
    }
    
    private void updateOrderDataView(){
        double amount = 0, discount = 0, points = 0;
        if(customerOrderDataList != null){
            for (CustomerOrderData customerOrderData : customerOrderDataList) {
                amount += customerOrderData.getAmount();
                discount += customerOrderData.getDiscount();
                points += customerOrderData.getPoints();
            }
            orderDataTable.getItems().setAll(customerOrderDataList);
        }       
        totalAmountLabel.setText(": " +Double.toString(amount));
        finalPriceLabel.setText(": " +Double.toString(amount-discount));
        discountLabel.setText(": " +Double.toString(discount));
        pointsLabel.setText(": " +Double.toString(points));       
    }
}