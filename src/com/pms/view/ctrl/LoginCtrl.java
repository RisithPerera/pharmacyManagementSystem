package com.pms.view.ctrl;

import com.pms.base.client.impl.UserClientImpl;
import com.pms.main.Main;
import com.pms.manifest.Message;
import com.pms.manifest.View;
import com.pms.model.child.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginCtrl implements Initializable {
    
    @FXML
    private Label currentTimeText;

    @FXML
    private TextField usernameText;
    
    @FXML
    private Button loginBtn;
    
    @FXML
    private PasswordField passwordText;
    
    private Stage primaryStage;
    private ObservableList<User> userList;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        primaryStage = Main.primaryStage;
        currentTimeText.textProperty().bind(Main.timeTask.messageProperty());
        try {
            userList = UserClientImpl.getInstance().getAll();
        } catch (IOException ex) {
            Logger.getLogger(LoginCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        loginBtnEnable();
        validationNodes();
    }

    @FXML
    void loginBtnEvent(ActionEvent event) {        
        try {            
            for (User user : userList) {
                if(user.getUserName().equals(usernameText.getText())){
                    if(user.getPassword().equals(passwordText.getText())){
                        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource(String.format(View.PATH, View.MAIN)))));
                        MainCtrl.getInstance().updateLoginContent(user);
                    }else{
                        MessageBoxViewCtrl.display(Message.TITLE,"Password is incorrect!");
                    }
                    clearFields();
                    return;
                }
            }
            clearFields();
            MessageBoxViewCtrl.display(Message.TITLE,"No Such Username!");
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void closeBtnEvent(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    //------------------------- Addtional Methods ----------------------------//
    
    private void clearFields(){
        usernameText.clear();
        passwordText.clear();
    }
    
    private void validationNodes() {
        usernameText.setOnKeyTyped((KeyEvent event) -> {
            String str = event.getCharacter();
            if(!str.matches("[A-Za-z]")) {
                event.consume();
            }
        });
  
        usernameText.setOnKeyReleased((KeyEvent event) -> {
             loginBtnEnable(); 
        });
        
        passwordText.setOnKeyReleased((KeyEvent event) -> {
             loginBtnEnable(); 
        });
    }
    
    private void loginBtnEnable() {
        boolean action1 = usernameText.getText().trim().equals("");
        boolean action2 = passwordText.getText().trim().equals("");
        
        if(action1 || action2){
            loginBtn.setDisable(true);
        }else{
            loginBtn.setDisable(false);
        }
    }
}
