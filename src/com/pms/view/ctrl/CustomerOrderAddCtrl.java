/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pms.view.ctrl;

import com.pms.base.client.impl.CustomerClientImpl;
import com.pms.base.client.impl.CustomerOrderClientImpl;
import com.pms.base.client.impl.CustomerOrderDataClientImpl;
import com.pms.manifest.Data;
import com.pms.manifest.Message;
import com.pms.manifest.State;
import com.pms.manifest.View;
import com.pms.model.child.Customer;
import com.pms.model.child.CustomerOrder;
import com.pms.model.child.CustomerOrderData;
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
public class CustomerOrderAddCtrl implements Initializable {

    @FXML
    private TextField idText, amountText, customerText;

    @FXML
    private ChoiceBox<String> discountChoice;
    
    @FXML
    private TextField searchCustomerText;
   
    @FXML
    private Button addBtn;
    
    @FXML
    private Button printBtn;
    
    @FXML
    private Button cancelBtn;
    
    @FXML
    private TableView<CustomerOrderData> orderDataTable;
      
    @FXML
    private TableColumn<CustomerOrderData, String> amountCol, rateCol, discountCol, remainderCol;
   
    @FXML
    private Label totalAmountLabel, finalPriceLabel, discountLabel, pointsLabel;
    
    @FXML
    private ListView<CustomerTemp> customerListView,itemListView;
     
    private ObservableList<CustomerOrderData> customerOrderDataList;
    private ObservableList<CustomerTemp> customerTempList;
    
    private Customer selectedCustomer;
    private CustomerOrder selectedCustomerOrder;
    private State.ControllerType controllerType;
       
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));
        discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        remainderCol.setCellValueFactory(new PropertyValueFactory<>("remainder"));
        
        discountChoice.getItems().setAll("0","1","2","3","4","5","6","7","8","9");
        
        searchCustomer();
        setTableMenu();
    }    

    @FXML
    private void showPurchasesBtnEvent(ActionEvent event) {
        try {
            MainCtrl.getInstance().showContent(String.format(View.PATH, View.CUSTOMER_ORDER_VIEW));
        } catch (IOException ex) {
            Logger.getLogger(CustomerViewCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addBtnEvent(ActionEvent event) {
        CustomerOrderData customerOrderData = new CustomerOrderData();
        customerOrderData.setId("0");
        customerOrderData.setCustomerOrder(selectedCustomerOrder);
        customerOrderData.setAmount(amountText.getText());
        customerOrderData.setRate(discountChoice.getSelectionModel().getSelectedItem());
        
        customerOrderDataList.add(customerOrderData);
        updateOrderDataView();
        discountChoice.getSelectionModel().select(0);
        amountText.clear();       
    }

    @FXML
    private void printBtnEvent(ActionEvent event) {
        try {
            switch(controllerType){
                case CUSTOMER_ORDER_ADD :
                    createCustomerOrderAdd();
                    break;
                case CUSTOMER_ORDER_UPDATE :
                    createCustomerOrderUpdate();
            }
            clearFields();
            MainCtrl.getInstance().showContent(String.format(View.PATH, View.CUSTOMER_ORDER_VIEW));
        } catch (IOException ex) {
            Logger.getLogger(CustomerOrderAddCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void customerListEvent(MouseEvent event) {
        if(! customerListView.getSelectionModel().isEmpty()){
            selectedCustomer = customerListView.getSelectionModel().getSelectedItem().getCustomer();
            customerText.setText(selectedCustomer.getIdFullName());
        }
    }
    
    @FXML
    private void cancelBtnEvent(ActionEvent event) {
        clearFields();
        try {
            switch(controllerType){
                case CUSTOMER_ORDER_ADD :
                    MainCtrl.getInstance().showContent(String.format(View.PATH, View.DASHBOARD));                    
                    break;
                case CUSTOMER_ORDER_UPDATE :        
                    MainCtrl.getInstance().showContent(String.format(View.PATH, View.CUSTOMER_ORDER_VIEW));
                    break;
            }  
        } catch (IOException ex) {
               Logger.getLogger(CustomerAddCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void searchCustomer() {
        customerTempList = FXCollections.observableArrayList();
        try {
            for (Customer customer : CustomerClientImpl.getInstance().getAll()) {
                customerTempList.add(new CustomerTemp(customer));
            }
        } catch (IOException ex) {
            Logger.getLogger(CustomerOrderAddCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
        customerListView.setItems(customerTempList);
        searchCustomerText.textProperty().addListener(
            new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldVal, Object newVal) {
                    if ( oldVal != null && (newVal.toString().length() < oldVal.toString().length()) ) {
                        customerListView.setItems(customerTempList);
                    }

                    String[] parts = newVal.toString().toUpperCase().split(" ");

                    ObservableList<CustomerTemp> subentries = FXCollections.observableArrayList();
                    for(CustomerTemp entry: customerListView.getItems()){
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
                    customerListView.setItems(subentries); 
                }
            }
        ); 
    }   
     
    public void prepareCustomerOrderAddView(Customer customer){        
        try {
            controllerType = State.ControllerType.CUSTOMER_ORDER_ADD;
            printBtn.setText("Print");
            customerOrderDataList = FXCollections.observableArrayList();            
            selectedCustomerOrder = new CustomerOrder();
            selectedCustomer = customer;
            if(selectedCustomer == null){
                customerText.clear();
            }else{
                customerText.setText(selectedCustomer.getIdFullName());
            }
            idText.setText(Long.toString(CustomerOrderClientImpl.getInstance().getNextId()));
            customerListView.getSelectionModel().select(null);
            updateOrderDataView();
        } catch (IOException ex) {
            Logger.getLogger(CustomerOrderAddCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    
    public void prepareCustomerOrderUpdateView(CustomerOrder customerOrder) {  
        try {
            controllerType = State.ControllerType.CUSTOMER_ORDER_UPDATE;
            printBtn.setText("Update");
            customerOrderDataList = CustomerOrderDataClientImpl.getInstance().getOrderData(customerOrder);
            selectedCustomerOrder = customerOrder;
            selectedCustomer = customerOrder.getCustomer();           
            idText.setText(customerOrder.getId());
            customerText.setText(customerOrder.getCustomer().getIdFullName());
            updateOrderDataView();
        } catch (IOException ex) {
            Logger.getLogger(CustomerOrderAddCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void createCustomerOrderAdd() {
        try { 
            selectedCustomerOrder.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            selectedCustomerOrder.setTime(new SimpleDateFormat("hh:mm:ss").format(new Date()));
            selectedCustomerOrder.setId(Long.toString(CustomerOrderClientImpl.getInstance().getNextId()));
            selectedCustomerOrder.setCustomer(selectedCustomer);
            CustomerOrderClientImpl.getInstance().add(selectedCustomerOrder);
            CustomerOrderDataClientImpl.getInstance().addOrderData(customerOrderDataList);
            MessageBoxViewCtrl.display(Message.TITLE,String.format(Message.ADD, Data.CUSTOMER_ORDER));
            
        } catch (IOException ex) {
            Logger.getLogger(CustomerOrderAddCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createCustomerOrderUpdate() {
         try {
            selectedCustomerOrder.setCustomer(selectedCustomer);
            System.out.println("aaaaaa");
            CustomerOrderClientImpl.getInstance().update(selectedCustomerOrder);
            System.out.println("bbbb");
            CustomerOrderDataClientImpl.getInstance().updateOrderData(selectedCustomerOrder, customerOrderDataList);
            System.out.println("ccccccc");
            MessageBoxViewCtrl.display(Message.TITLE,String.format(Message.UPDATE, Data.CUSTOMER_ORDER));
        } catch (IOException ex) {
            Logger.getLogger(CustomerOrderAddCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
    private void clearFields(){
        customerOrderDataList.clear();
        selectedCustomerOrder = null;
        selectedCustomer = null;
        idText.clear();
        customerText.clear();
        amountText.clear();
        discountChoice.getSelectionModel().select(0);
        totalAmountLabel.setText(Integer.toString(0));
        finalPriceLabel.setText(Integer.toString(0));
        discountLabel.setText(Integer.toString(0));
        pointsLabel.setText(Integer.toString(0));
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
    
    private void setTableMenu() {
        orderDataTable.setRowFactory((TableView<CustomerOrderData> tableView) -> {
            TableRow<CustomerOrderData> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteMenu = new MenuItem("Delete");
            
            deleteMenu.setOnAction((ActionEvent event) -> {                                                  
                customerOrderDataList.remove(orderDataTable.getSelectionModel().getSelectedItem());
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
    
    private class CustomerTemp{
        private Customer customer;

        public CustomerTemp(Customer customer) {
            this.customer = customer;
        }
        
        public Customer getCustomer() {
            return customer;
        }

        public void setCustomer(Customer customer) {
            this.customer = customer;
        }
               
        @Override
        public String toString() {
            return customer.getId()+": "+customer.getFullName() +": "+ customer.getDistrict() ;
        }       
    }
    
}