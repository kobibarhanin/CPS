package Screens.UserLoginScreen;

import ClientServerMain.Client;
//import ClientServerMain.ClientHandler;
import Screens.CPSRouterScreen.CPSRouterScreenController;
import Screens.CustomerScreen.CustomerScreenController;
import Screens.MessageScreen.MessageScreenController;
import Screens.RegistrationScreen.RegistrationScreenController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Sample Skeleton for 'UserLoginScreen.fxml' Controller Class
 */

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UserLoginScreenController {
	
    Client client;// = new Client(null,0);
	String pass;
	String mail;

    @FXML // fx:id="password"
    private PasswordField password; // Value injected by FXMLLoader

    @FXML // fx:id="loginButton"
    private Button loginButton; // Value injected by FXMLLoader

    @FXML // fx:id="email"
    private TextField email; // Value injected by FXMLLoader

    @FXML // fx:id="regButton"
    private Button regButton; // Value injected by FXMLLoader
    
    @FXML
    private AnchorPane rootPane; // Value injected by FXMLLoader

    @FXML
    void Login(ActionEvent event) {
    	try {
    	    if(email.getText().trim().isEmpty()||password.getText().trim().isEmpty()){
    	        setMessageStage("Please enter credentials.");
    	        return;
            }
            if(client.clientHandler.verifyUser(email.getText(), password.getText())) {
            		Stage stage = (Stage) this.loginButton.getScene().getWindow();
                	stage.close();
                	setCustomerStage(email.getText());
                	setMessageStage("Welcome!\nlogin as " + client.clientHandler.getUserName(email.getText()));
            }
            else {
                setMessageStage("Incorrect Email or Password.\n");
            }
    	}catch(Exception ex)
    	{
    		setMessageStage("Something went\nworng please try again");
    	}
    }

    @FXML
    private Button Back;

    @FXML
    void backToRouter(ActionEvent event) {
        try {
            Stage loginStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Screens/CPSRouterScreen/CPSRouterScreen.fxml"));
            CPSRouterScreenController cpsRouterScreenController = new CPSRouterScreenController();
            loader.setController(cpsRouterScreenController);
            cpsRouterScreenController.setClient(client);
            cpsRouterScreenController.setCarparks(client.clientHandler.getCarparks());
            AnchorPane pane = loader.load();
            loginStage.setScene(new Scene(pane));
            loginStage.setTitle("CPS Router");
            loginStage.setResizable(false);
            loginStage.show();
            Stage stage = (Stage) this.Back.getScene().getWindow();
            stage.close();


        }
        catch (IOException ex){}
    }

    public void setCustomerStage(String Email){

        try {
            Stage customerStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Screens/CustomerScreen/CustomerScreen.fxml"));
            CustomerScreenController customerScreenController = new CustomerScreenController();
            loader.setController(customerScreenController);
            customerScreenController.setClient(client);
            customerScreenController.setOrders(Email);
            customerScreenController.setRegMemberships(Email);
            customerScreenController.setFullMemberships(Email);
            customerScreenController.setParkinglots(client.clientHandler.getCarparks());
            customerScreenController.setCarList(client.clientHandler.getCars());
    		customerScreenController.setUser(client.clientHandler.getUserName(Email));
            customerScreenController.setMail(Email);
            AnchorPane pane = loader.load();
            customerStage.setScene(new Scene(pane));
            customerStage.setTitle("User  = " + client.clientHandler.getUserName(Email));
            customerStage.setResizable(false);
            customerStage.show();

        }
        catch (IOException ex){}
    }
    
    public void setRegistrationStage(){

        try {

            Stage registrationStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Screens/RegistrationScreen/RegistrationScreen.fxml"));
            RegistrationScreenController registrationScreenController = new RegistrationScreenController();
            loader.setController(registrationScreenController);
            registrationScreenController.setClient(client);
            Parent pane = loader.load();
            registrationStage.setScene(new Scene(pane));
            registrationStage.setTitle("Registration Screen");
            registrationStage.setResizable(false);
            registrationStage.show();
        }
        catch (IOException ex){}
    }
    
    public void setMessageStage(String error){

        try {
            Stage messageStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Screens/MessageScreen/MessageScreen.fxml"));
            MessageScreenController messageScreenController = new MessageScreenController();
            messageScreenController.setMessage(error);
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
        client = _client;
    }

    @FXML
    void enterPass(ActionEvent event) {
    	pass = password.getText();
    }

    @FXML
    void enterEmail(ActionEvent event) {
    	mail = email.getText();
    }

    @FXML
    void ae0101(ActionEvent event) { }

    @FXML
    void signUp(ActionEvent event) {
        Stage stage = (Stage)this.loginButton.getScene().getWindow();
        stage.close();
    	setRegistrationStage();
    }

}