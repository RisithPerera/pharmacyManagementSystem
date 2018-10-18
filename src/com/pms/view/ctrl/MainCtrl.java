package com.pms.view.ctrl;


import com.pms.main.Main;
import com.pms.manifest.View;
import com.pms.model.child.User;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;


public class MainCtrl {

    //--------------------------- FXML Attributes ----------------------------//
    @FXML
    private AnchorPane contentPane;

    @FXML
    private VBox menuList;

    @FXML
    private Label currentTimeText;
    
    @FXML
    private Label userTypeText, userNameText, loginTimeText, loginDateText;
    
    //---------------------- Normal Attributes -------------------------------//
    private static MainCtrl mainCtrl;   
    private Stage primaryStage;
    
    public static MainCtrl getInstance() {
        return mainCtrl;
    }
    
    //---------------------- Initialize and Startup Actions ------------------//
    public void initialize() {        
        mainCtrl = this;
        primaryStage = Main.primaryStage;
        currentTimeText.textProperty().bind(Main.timeTask.messageProperty());
        
        //-------------------------- Add Menu List Event ---------------------------//
        for(Node menuItem:menuList.getChildren()){  
            menuItem.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    menuListEvent(menuItem.getId());
                }
            });
        }
        menuListEvent("home");
    }

    //---------------------- Nodes Events ------------------------------------//
    void menuListEvent(String item){
        try {
            switch (item) {
                case "home":
                    showContent(String.format(View.PATH, View.DASHBOARD));
                    break;
                case "purchaseNormal":
                    showContent(String.format(View.PATH, View.NORMAL_ORDER_VIEW));
                    break;
                case "purchaseCustomer":
                    showContent(String.format(View.PATH, View.CUSTOMER_ORDER_VIEW));
                    break;
                case "customer":
                    showContent(String.format(View.PATH, View.CUSTOMER_VIEW));
                    break;
                case "analysis":
                    showContent(String.format(View.PATH, View.ANALYSIS));
                    break;
                case "settings":
                    showContent(String.format(View.PATH, View.SETTINGS));
                    break;
                case "about":
                    showAbout(String.format(View.PATH, View.ABOUT));
            }
        } catch (IOException ex) {
            Logger.getLogger(MainCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void logoutBtnEvent(ActionEvent event) {
        try {
            primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource(String.format(View.PATH, View.LOGIN)))));
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //------------------------- Addtional Methods ----------------------------//
    public Object showContent(String path) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane pane = fxmlLoader.load(getClass().getResource(path).openStream());
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setTopAnchor(pane, 0.0);
        contentPane.getChildren().setAll(pane);
        return fxmlLoader.getController();
    }

    private void showAbout(String path) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(path));        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.getIcons().add(new Image(View.IMAGE_ABOUT));
        stage.initOwner(primaryStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    
    public void updateLoginContent(User user){
        switch(user.getType()){
            case "0": userTypeText.setText("Login As : ADMINISTRATOR"); break;
            case "1": userTypeText.setText("Login As : CLERK");         break;
            case "2": userTypeText.setText("Login As : AUDIT");
        }
        
        userNameText.setText("Current User : " + user.getUserName());
        loginDateText.setText("Date Logged In : " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));   
        loginTimeText.setText("Time Logged In : " + new SimpleDateFormat("hh:mm:ss a").format(new Date()));       
    }
}
