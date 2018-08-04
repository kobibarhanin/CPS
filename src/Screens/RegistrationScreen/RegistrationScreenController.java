package Screens.RegistrationScreen;

import ClientServerMain.Client;
import Screens.MessageScreen.MessageScreenController;
import Screens.UserLoginScreen.UserLoginScreenController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Sample Skeleton for 'RegistrationScreen.fxml' Controller Class
 */

//package Screens;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class RegistrationScreenController {
	
	
	Client client;
	
	
    @FXML // fx:id="password"
    private TextField password; // Value injected by FXMLLoader

    @FXML // fx:id="name"
    private TextField name; // Value injected by FXMLLoader

    @FXML // fx:id="id"
    private TextField id; // Value injected by FXMLLoader

    @FXML // fx:id="password1"
    private TextField password1; // Value injected by FXMLLoader

    @FXML // fx:id="email"
    private TextField email; // Value injected by FXMLLoader

    @FXML // fx:id="regButton"
    private Button regButton; // Value injected by FXMLLoader
    
    @FXML // fx:id="regButton"
    private Button Back; // Value injected by FXMLLoader

    @FXML
    void onButtonClicked(ActionEvent event) {
        if(email.getText().trim().isEmpty()||password.getText().trim().isEmpty()||id.getText().trim().isEmpty()||name.getText().trim().isEmpty()){
            setMessageStage("Please enter credentials.");
            return;
        }
    	if(event.getSource().equals(regButton)) {
    		String a = password.getText();
    		String b = password1.getText();
    		if (a.equals(b))
    		{
    			client.clientHandler.addUser(name.getText(), id.getText(), password.getText(), email.getText());
            	Stage stage = (Stage)this.regButton.getScene().getWindow();
            	stage.close();
            	setLoginStage();
            	setMessageStage(name.getText());
    		}
    		else
    		{
    			setErrorStage("password not identical please re enter password");
    			password.setText("");
    			password1.setText("");
    		}
    			
        }
    }
    public void setLoginStage(){

        try {
            Stage loginStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../UserLoginScreen/UserLoginScreen.fxml"));
            UserLoginScreenController userLoginController = new UserLoginScreenController();
            loader.setController(userLoginController);
            userLoginController.setClient(client);
            Parent pane = loader.load();
            loginStage.setScene(new Scene(pane));
            loginStage.setTitle("Client Login");
            loginStage.setResizable(false);
            loginStage.show();


        }
        catch (IOException ex){}
    }

    @FXML
    void backToLogin(ActionEvent event) {
        try {
            Stage loginStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Screens/UserLoginScreen/UserLoginScreen.fxml"));
            UserLoginScreenController userLoginController = new UserLoginScreenController();
            loader.setController(userLoginController);
            userLoginController.setClient(client);
            Parent pane = loader.load();
            loginStage.setScene(new Scene(pane));
            loginStage.setTitle("Client Login");
            loginStage.setResizable(false);
            loginStage.show();
            Stage stage = (Stage) this.Back.getScene().getWindow();
            stage.close();
        }
        catch (IOException ex){}
}
    
    public void setMessageStage(String name){

        try {
            Stage messageStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Screens/MessageScreen/MessageScreen.fxml"));
            MessageScreenController messageScreenController = new MessageScreenController();
            messageScreenController.setMessage("User " + name + " has been added.");
            loader.setController(messageScreenController);
            Pane root = (Pane)loader.load();
            messageStage.setScene(new Scene(root));
            messageStage.setResizable(false);
            messageStage.show();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
    
    public void setErrorStage(String name){

        try {
            Stage messageStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Screens/MessageScreen/MessageScreen.fxml"));
            MessageScreenController messageScreenController = new MessageScreenController();
            messageScreenController.setMessage(name);
            loader.setController(messageScreenController);
            Pane root = (Pane)loader.load();
            messageStage.setScene(new Scene(root));
            messageStage.setResizable(false);
            messageStage.show();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
    
    public void setClient(Client _client){
        client=_client;
    }

    public void initialize(){
    }


}

