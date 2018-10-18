/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pms.view.ctrl;

import com.pms.base.client.impl.NormalOrderClientImpl;
import com.pms.base.client.impl.NormalOrderDataClientImpl;
import com.pms.manifest.Data;
import com.pms.manifest.Message;
import com.pms.manifest.View;
import com.pms.model.child.NormalOrder;
import com.pms.model.child.NormalOrderData;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
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
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author RISITH-PC
 */
public class NormalOrderViewCtrl implements Initializable {
    
    @FXML
    private TableView<NormalOrder> orderTable;

    @FXML
    private TableView<NormalOrderData> orderDataTable;
    
    @FXML
    private TableColumn<NormalOrder, String> dateCol, timeCol,idCol, detailCol;

    @FXML
    private TableColumn<NormalOrderData, String> amountCol, rateCol, discountCol, remainderCol;

    @FXML
    private Label totalAmountLabel,finalPriceLabel,discountLabel,pointsLabel;

    @FXML
    private ComboBox<String> yearCombo,monthCombo,dayCombo;
    
    private ObservableList<NormalOrder> normalOrderList;
    private ObservableList<NormalOrderData> normalOrderDataList;
       
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        detailCol.setCellValueFactory(new PropertyValueFactory<>("detail"));
        
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
        
         
        setTableSelection();
        updateOrderDataView();
        setTableMenu();
    }    

    @FXML
    private void addNewBtnEvent(ActionEvent event) {
        try {
            NormalOrderAddCtrl addCtrl = (NormalOrderAddCtrl) MainCtrl.getInstance().showContent(String.format(View.PATH, View.NORMAL_ORDER_ADD));
            addCtrl.prepareNormalOrderAddView();
        } catch (IOException ex) {
            Logger.getLogger(CustomerViewCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void viewAllBtnEvent(ActionEvent event) {
        System.out.println("view all");
        orderTable.getItems().setAll(normalOrderList);
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
        orderTable.setRowFactory((TableView<NormalOrder> tableView) -> {
            TableRow<NormalOrder> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem updateMenu = new MenuItem("Update Purchase");
            MenuItem deleteMenu = new MenuItem("Delete Purchase");
            
            deleteMenu.setOnAction((ActionEvent event) -> {                         
                try {
                    NormalOrder selectedNormalOrder = orderTable.getSelectionModel().getSelectedItem();
                    NormalOrderDataClientImpl.getInstance().deleteOrderData(selectedNormalOrder);
                    if (NormalOrderClientImpl.getInstance().delete(selectedNormalOrder)){                
                        System.out.println("Safely Deleted....!");
                    }else{
                        System.out.println("Delete Unsucessfull...!");
                    }
                    orderTable.getItems().remove(selectedNormalOrder);
                } catch (IOException ex) {
                    Logger.getLogger(CustomerViewCtrl.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            updateMenu.setOnAction((ActionEvent event) -> {                                        
                NormalOrder selectedNormalOrder = orderTable.getSelectionModel().getSelectedItem();
                try{    
                    NormalOrderAddCtrl ctrl = (NormalOrderAddCtrl) MainCtrl.getInstance().showContent(String.format(View.PATH, View.NORMAL_ORDER_ADD));
                    ctrl.prepareNormalOrderUpdateView(selectedNormalOrder);
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
        
        orderDataTable.setRowFactory((TableView<NormalOrderData> tableView) -> {
            TableRow<NormalOrderData> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteMenu = new MenuItem("Delete Item");
            
            deleteMenu.setOnAction((ActionEvent event) -> {                         
                try {
                    NormalOrderData selectedNormalOrderData = orderDataTable.getSelectionModel().getSelectedItem();                   
                    if (NormalOrderDataClientImpl.getInstance().delete(selectedNormalOrderData)){
                        normalOrderDataList.remove(selectedNormalOrderData);
                        updateOrderDataView();
                        MessageBoxViewCtrl.display(Message.TITLE,String.format(Message.DELETE, Data.NORMAL_ORDER_DATA));
                    }else{
                        MessageBoxViewCtrl.display(Message.TITLE,String.format(Message.UNSUCESS, Data.NORMAL_ORDER_DATA));
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
            normalOrderList = NormalOrderClientImpl.getInstance().getAll();
            orderTable.getItems().setAll(normalOrderList);
            orderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != oldSelection) {
                    try {
                        normalOrderDataList = NormalOrderDataClientImpl.getInstance().getOrderData(newSelection);
                        updateOrderDataView();
                    } catch (IOException ex) {
                        Logger.getLogger(NormalOrderViewCtrl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(NormalOrderViewCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void updateTableDataByDate(String dateCompare){
        try{
            orderTable.getItems().setAll(normalOrderList);
            Iterator<NormalOrder> iterator = orderTable.getItems().iterator();
            while(iterator.hasNext()){  
                if(! iterator.next().getDate().contains(dateCompare)){
                    iterator.remove();
                }
            }
        }catch(NullPointerException exception){}
    }
    
    private void updateOrderDataView(){
        double amount = 0, discount = 0, points = 0;
        if(normalOrderDataList != null){
            for (NormalOrderData customerOrderData : normalOrderDataList) {
                amount += customerOrderData.getAmount();
                discount += customerOrderData.getDiscount();
                points += customerOrderData.getPoints();
            }
            orderDataTable.getItems().setAll(normalOrderDataList);
        }       
        totalAmountLabel.setText(": " +Double.toString(amount));
        finalPriceLabel.setText(": " +Double.toString(amount-discount));
        discountLabel.setText(": " +Double.toString(discount));
        pointsLabel.setText(": " +Double.toString(points));       
    }

}