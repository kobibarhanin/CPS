package Screens.ManagerLoginScreen;

import ClientServerMain.Client;
import Screens.BranchManagerScreen.BranchManagerScreenController;
import Screens.CPSManagerScreen.CPSManagerScreenController;
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

//import ClientServerMain.ClientHandler;

/**
 * Sample Skeleton for 'UserLoginScreen.fxml' Controller Class
 */

public class ManagerLoginScreenController {
	
    Client client;// = new Client(null,0);
	String pass;
	String mail;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginButton;

    @FXML
    private TextField email;

    @FXML
    private Button regButton;
    
    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button Back;

    @FXML
    void Login(ActionEvent event) {
    	try {
            if(email.getText().trim().isEmpty()||password.getText().trim().isEmpty()){
                setMessageStage("Please enter credentials.");
                return;
            }
            if(client.clientHandler.verifyUser(email.getText(), password.getText())) {
            	if (client.clientHandler.getUserType(email.getText()).equals("CPS_MANAGER"))
            	{
            		Stage stage = (Stage) this.loginButton.getScene().getWindow();
                	stage.close();
                	setCPSManagerStage();
                    setMessageStage("Welcome!\nlogin as " + client.clientHandler.getUserName(email.getText()));
                }
            	if (client.clientHandler.getUserType(email.getText()).equals("BRANCH_MANAGER"))
            	{
            		Stage stage = (Stage) this.loginButton.getScene().getWindow();
                	stage.close();
                	setBranchManagerStage();
                	setMessageStage("Welcome!\nlogin as " + client.clientHandler.getUserName(email.getText()));
            	}
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

    public void setCPSManagerStage(){
        try {
            Stage cpsManagerStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Screens/CPSManagerScreen/CPSManagerScreen.fxml"));
            CPSManagerScreenController cpsManagerScreenController = new CPSManagerScreenController();
            loader.setController(cpsManagerScreenController);
            cpsManagerScreenController.setClient(client);
            cpsManagerScreenController.setComplaints();
            cpsManagerScreenController.setCarparks(client.clientHandler.getCarparks());
            cpsManagerScreenController.setPriceChangeRequests();
            AnchorPane pane = loader.load();
            cpsManagerStage.setScene(new Scene(pane));
            cpsManagerStage.setTitle("CPS Manager");
            cpsManagerStage.setResizable(false);
            cpsManagerStage.show();

        }
        catch (IOException ex){}
    }

    public void setBranchManagerStage(){
        try {
            Stage branchManagerStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Screens/BranchManagerScreen/BranchManagerScreen.fxml"));
            BranchManagerScreenController branchManagerScreenController = new BranchManagerScreenController();
            loader.setController(branchManagerScreenController);
            branchManagerScreenController.setClient(client);
            AnchorPane pane = loader.load();
            branchManagerStage.setScene(new Scene(pane));
            branchManagerStage.setTitle("User  = " + client.clientHandler.getUserName(email.getText()));
            branchManagerStage.setResizable(false);
            branchManagerStage.show();

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



}