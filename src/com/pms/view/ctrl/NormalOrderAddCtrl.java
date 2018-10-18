/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pms.view.ctrl;

import com.pms.base.client.impl.CustomerClientImpl;
import com.pms.base.client.impl.NormalOrderClientImpl;
import com.pms.base.client.impl.NormalOrderDataClientImpl;
import com.pms.manifest.Data;
import com.pms.manifest.Message;
import com.pms.manifest.State;
import com.pms.manifest.View;
import com.pms.model.child.Customer;
import com.pms.model.child.NormalOrderData;
import com.pms.model.child.NormalOrder;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author RISITH-PC
 */
public class NormalOrderAddCtrl implements Initializable {

    @FXML
    private TextField idText, amountText,searchItemText;

    @FXML
    private ChoiceBox<String> discountChoice;
     
    @FXML
    private Button printBtn;
    
    @FXML
    private TableView<NormalOrderData> orderDataTable;
      
    @FXML
    private TableColumn<NormalOrderData, String> amountCol, rateCol, discountCol, remainderCol;
   
    @FXML
    private Label totalAmountLabel, finalPriceLabel, discountLabel, pointsLabel;
    
    @FXML
    private ListView<?> itemListView;
     
    private ObservableList<NormalOrderData> normalOrderDataList;
    private NormalOrder selectedNormalOrder;
    private State.ControllerType controllerType;
       
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));
        discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        remainderCol.setCellValueFactory(new PropertyValueFactory<>("remainder"));
        
        discountChoice.getItems().setAll("0","1","2","3","4","5","6","7","8","9");
        
        setTableMenu();
    }    

    @FXML
    private void showPurchasesBtnEvent(ActionEvent event) {
        try {
            MainCtrl.getInstance().showContent(String.format(View.PATH, View.NORMAL_ORDER_VIEW));
        } catch (IOException ex) {
            Logger.getLogger(CustomerViewCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addBtnEvent(ActionEvent event) {
        NormalOrderData normalOrderData = new NormalOrderData();
        normalOrderData.setId("0");
        normalOrderData.setNormalOrder(selectedNormalOrder);
        normalOrderData.setAmount(amountText.getText());
        normalOrderData.setRate(discountChoice.getSelectionModel().getSelectedItem());
        
        normalOrderDataList.add(normalOrderData);
        updateOrderDataView();
        discountChoice.getSelectionModel().select(0);
        amountText.clear();       
    }

    @FXML
    private void printBtnEvent(ActionEvent event) {
        try {
            switch(controllerType){
                case NORMAL_ORDER_ADD :
                    createNormalOrderAdd();
                    break;
                case NORMAL_ORDER_UPDATE :
                    createNormalOrderUpdate();
            }
            clearFields();
            MainCtrl.getInstance().showContent(String.format(View.PATH, View.NORMAL_ORDER_VIEW));
        } catch (IOException ex) {
            Logger.getLogger(NormalOrderAddCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void cancelBtnEvent(ActionEvent event) {
        clearFields();
        try {
            switch(controllerType){
                case NORMAL_ORDER_ADD :
                    MainCtrl.getInstance().showContent(String.format(View.PATH, View.DASHBOARD));                    
                    break;
                case NORMAL_ORDER_UPDATE :        
                    MainCtrl.getInstance().showContent(String.format(View.PATH, View.NORMAL_ORDER_VIEW));
                    break;
            }  
        } catch (IOException ex) {
               Logger.getLogger(CustomerAddCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    public void prepareNormalOrderAddView(){
        controllerType = State.ControllerType.NORMAL_ORDER_ADD;
        printBtn.setText("Print");
        normalOrderDataList = FXCollections.observableArrayList();            
        selectedNormalOrder = new NormalOrder();
        idText.setText(Long.toString(NormalOrderClientImpl.getInstance().getNextId()));
        updateOrderDataView();
    }
    
    public void prepareNormalOrderUpdateView(NormalOrder normalOrder) {  
        try {
            controllerType = State.ControllerType.NORMAL_ORDER_UPDATE;
            printBtn.setText("Update");
            normalOrderDataList = NormalOrderDataClientImpl.getInstance().getOrderData(normalOrder);
            selectedNormalOrder = normalOrder;     
            idText.setText(normalOrder.getId());
            updateOrderDataView();
        } catch (IOException ex) {
            Logger.getLogger(NormalOrderAddCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void createNormalOrderAdd() {
        try {            
            selectedNormalOrder.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            selectedNormalOrder.setTime(new SimpleDateFormat("hh:mm:ss").format(new Date()));
            selectedNormalOrder.setId(Long.toString(NormalOrderClientImpl.getInstance().getNextId()));
            NormalOrderClientImpl.getInstance().add(selectedNormalOrder);
            NormalOrderDataClientImpl.getInstance().addOrderData(normalOrderDataList);
            MessageBoxViewCtrl.display(Message.TITLE,String.format(Message.ADD, Data.NORMAL_ORDER));           
        } catch (IOException ex) {
            Logger.getLogger(NormalOrderAddCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createNormalOrderUpdate() {
         try {
            NormalOrderDataClientImpl.getInstance().updateOrderData(selectedNormalOrder, normalOrderDataList);
            MessageBoxViewCtrl.display(Message.TITLE,String.format(Message.UPDATE, Data.NORMAL_ORDER));
        } catch (IOException ex) {
            Logger.getLogger(NormalOrderAddCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
    private void clearFields(){
        normalOrderDataList.clear();
        selectedNormalOrder = null;
        idText.clear();
        amountText.clear();
        discountChoice.getSelectionModel().select(0);
        totalAmountLabel.setText(Integer.toString(0));
        finalPriceLabel.setText(Integer.toString(0));
        discountLabel.setText(Integer.toString(0));
        pointsLabel.setText(Integer.toString(0));
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
    
    private void setTableMenu() {
        orderDataTable.setRowFactory((TableView<NormalOrderData> tableView) -> {
            TableRow<NormalOrderData> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteMenu = new MenuItem("Delete");
            
            deleteMenu.setOnAction((ActionEvent event) -> {                                                  
                normalOrderDataList.remove(orderDataTable.getSelectionModel().getSelectedItem());
                updateOrderDataView();
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
}