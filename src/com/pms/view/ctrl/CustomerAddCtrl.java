/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pms.view.ctrl;

import com.pms.base.client.impl.CustomerClientImpl;
import com.pms.manifest.Data;
import com.pms.manifest.Message;
import com.pms.manifest.State;
import com.pms.manifest.View;
import com.pms.model.child.Customer;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author RISITH-PC
 */
public class CustomerAddCtrl implements Initializable {

    @FXML
    private TextField idText, fNameText, lNameText, streetText, cityText;
  
    @FXML
    private TextField nicNoText, licNoText, teleNoText, whatsappText, viberText, emailText;
    
    @FXML
    private DatePicker dobText, issueDateText, expDateText;
    
    @FXML
    private Button saveBtn;
    
    @FXML
    private ChoiceBox<String> districtChoice;

    /**
     * Initializes the controller class.
     */
    
    State.ControllerType controllerType;
    private Customer selectedCustomer;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        districtChoice.getItems().setAll(Data.DISTRICTS);
    }    

    @FXML
    private void showCustomerBtnEvent(ActionEvent event) {
        clearFields();
        try {
            MainCtrl.getInstance().showContent(String.format(View.PATH, View.CUSTOMER_VIEW));
        } catch (IOException ex) {
            Logger.getLogger(CustomerViewCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void saveBtnEvent(ActionEvent event){
        switch(controllerType){
            case CUSTOMER_ADD :
                createCustomerAdd();
                break;
            case CUSTOMER_UPDATE :
                createCustomerUpdate();
        } 
        clearFields();
    }

    @FXML
    private void cancelBtnEvent(ActionEvent event) {
        clearFields();
        try {
            switch(controllerType){
                case CUSTOMER_ADD :
                    MainCtrl.getInstance().showContent(String.format(View.PATH, View.DASHBOARD));                    
                    break;
                case CUSTOMER_UPDATE :        
                    MainCtrl.getInstance().showContent(String.format(View.PATH, View.CUSTOMER_VIEW));
                    break;
            }  
        } catch (IOException ex) {
               Logger.getLogger(CustomerAddCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    public void prepareCustomerAddView() { 
        controllerType = State.ControllerType.CUSTOMER_ADD;
        selectedCustomer = null;
        saveBtn.setText("Save");
        
        idText.setText(Long.toString(CustomerClientImpl.getInstance().getNextId()));
        districtChoice.getSelectionModel().select(0);
        dobText.setValue(LocalDate.of(1990, 01, 01));
        LocalDate localDate = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        issueDateText.setValue(localDate);
        expDateText.setValue(localDate.plusDays(180));     
    }
    
    public void prepareCustomerUpdateView(Customer customer) {  
        controllerType = State.ControllerType.CUSTOMER_UPDATE;
        selectedCustomer = customer;
        saveBtn.setText("Update"); 
        
        idText.setText(customer.getId());    
        fNameText.setText(customer.getFName()); 
        lNameText.setText(customer.getLName());      
        streetText.setText(customer.getStreet());    
        cityText.setText(customer.getCity());   
        districtChoice.getSelectionModel().select(customer.getDistrict());
        dobText.setValue(LocalDate.parse(customer.getDate(), DateTimeFormatter.ISO_DATE));
        nicNoText.setText(customer.getNicNo());     
        licNoText.setText(customer.getLicNo());     
        teleNoText.setText(customer.getTeleNo()); 
        whatsappText.setText(customer.getWhatsappNo()); 
        viberText.setText(customer.getViberNo());   
        emailText.setText(customer.getEmail());     
        issueDateText.setValue(LocalDate.parse(customer.getIssueDate(), DateTimeFormatter.ISO_DATE));
        expDateText.setValue(LocalDate.parse(customer.getExpireDate(), DateTimeFormatter.ISO_DATE));
    }
    
    private void createCustomerAdd() {
        selectedCustomer = new Customer();
        selectedCustomer.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));   
        selectedCustomer.setTime(new SimpleDateFormat("hh:mm:ss").format(new Date()));    
        selectedCustomer.setId(Long.toString(CustomerClientImpl.getInstance().getNextId())); 
        selectedCustomer.setFName(fNameText.getText()); 
        selectedCustomer.setLName(lNameText.getText());      
        selectedCustomer.setStreet(streetText.getText());    
        selectedCustomer.setCity(cityText.getText());      
        selectedCustomer.setDistrict(districtChoice.getSelectionModel().getSelectedItem());  
        selectedCustomer.setDob(dobText.getValue().toString());       
        selectedCustomer.setNicNo(nicNoText.getText());     
        selectedCustomer.setLicNo(licNoText.getText());     
        selectedCustomer.setTeleNo(teleNoText.getText());     
        selectedCustomer.setWhatsappNo(whatsappText.getText()); 
        selectedCustomer.setViberNo(viberText.getText());   
        selectedCustomer.setEmail(emailText.getText());     
        selectedCustomer.setIssueDate(issueDateText.getValue().toString());
        selectedCustomer.setExpireDate(expDateText.getValue().toString());
        selectedCustomer.setPoints(Integer.toString(0));
        try {
            CustomerClientImpl.getInstance().add(selectedCustomer);
            MessageBoxViewCtrl.display(Message.TITLE,String.format(Message.ADD, Data.CUSTOMER));
            MainCtrl.getInstance().showContent(String.format(View.PATH, View.CUSTOMER_VIEW));
        } catch (IOException ex) {
            Logger.getLogger(CustomerAddCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createCustomerUpdate() {
        if(selectedCustomer != null){
            selectedCustomer.setFName(fNameText.getText()); 
            selectedCustomer.setLName(lNameText.getText());      
            selectedCustomer.setStreet(streetText.getText());    
            selectedCustomer.setCity(cityText.getText());      
            selectedCustomer.setDistrict(districtChoice.getSelectionModel().getSelectedItem());  
            selectedCustomer.setDob(dobText.getValue().toString());       
            selectedCustomer.setNicNo(nicNoText.getText());     
            selectedCustomer.setLicNo(licNoText.getText());     
            selectedCustomer.setTeleNo(teleNoText.getText());     
            selectedCustomer.setWhatsappNo(whatsappText.getText()); 
            selectedCustomer.setViberNo(viberText.getText());   
            selectedCustomer.setEmail(emailText.getText());     
            selectedCustomer.setIssueDate(issueDateText.getValue().toString());
            selectedCustomer.setExpireDate(expDateText.getValue().toString()); 
            try {
                CustomerClientImpl.getInstance().update(selectedCustomer);
                MessageBoxViewCtrl.display(Message.TITLE,String.format(Message.UPDATE, Data.CUSTOMER));
                MainCtrl.getInstance().showContent(String.format(View.PATH, View.CUSTOMER_VIEW));
            } catch (IOException ex) {
                Logger.getLogger(CustomerAddCtrl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            MessageBoxViewCtrl.display(Message.TITLE,"Selected Customer Is Null Object");
        }
    }
    
    private void clearFields(){
        selectedCustomer = null;
        fNameText.clear();
        lNameText.clear();
        streetText.clear();
        cityText.clear();
        districtChoice.getSelectionModel().select(0);
        dobText.setValue(LocalDate.of(1990, 01, 01));
        nicNoText.clear();
        licNoText.clear();
        teleNoText.clear();
        whatsappText.clear();
        viberText.clear();
        emailText.clear();
        issueDateText.setValue(null);
        expDateText.setValue(null);
    }

}
