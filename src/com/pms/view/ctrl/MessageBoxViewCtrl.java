/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pms.view.ctrl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author RISITH
 */
public class MessageBoxViewCtrl implements Initializable {
    
    //--------------------------- FXML Attributes ----------------------------//
    @FXML
    private Label titleLabel, messageLabel;

    //---------------------- Normal Attributes -------------------------------//
    private static Stage primaryStage;
    private static String title,message;
    
//---------------------- Initialize and Startup Actions ----------------------//
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        titleLabel.setText(title);
        messageLabel.setText(message);
    }    
  
    //---------------------- Nodes Events ------------------------------------//    
    @FXML
    public void setStage(MouseEvent event) {
        primaryStage.setX(event.getScreenX()+ Distance.deltaX);
        primaryStage.setY(event.getScreenY()+ Distance.deltaY);
    }

    @FXML
    public void getDelta(MouseEvent event) {
        Distance.deltaX = - event.getX();
        Distance.deltaY = - event.getY();
    }

    @FXML
    public void closeBtnEvent(ActionEvent event) {
        primaryStage.close();
        System.gc();
    }

    @FXML
    public void okMessageBtnEvent(ActionEvent event) {
        primaryStage.close();
        System.gc();
    }

    
    //------------------------- Addtional Methods ----------------------------//
    public static void display(String t, String m){   
        Alert alert = new Alert(AlertType.INFORMATION);        
        alert.setTitle(t);
        alert.setContentText(m);        
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.toFront();
        stage.show();
    }
 
  
    
    public static class Distance{
        static double deltaX,deltaY;
    }
}
