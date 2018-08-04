package Screens.CPSRouterScreen;

import ClientServerMain.Client;
import Screens.KioskScreen.KioskScreenController;
import Screens.ManagerLoginScreen.ManagerLoginScreenController;
import Screens.MessageScreen.MessageScreenController;
import Screens.UserLoginScreen.UserLoginScreenController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

//import ClientServerMain.ClientHandler;

/**
 * Sample Skeleton for 'UserLoginScreen.fxml' Controller Class
 */

public class CPSRouterScreenController {
	
    Client client;
    public void setClient(Client _client){
        client = _client;
    }

    String[] carparks;
    public void setCarparks(String[] input) {
        carparks = input;
    }

    @FXML
    public void initialize() {
        carparkSelect.getItems().addAll(carparks);
    }


    @FXML
    private Button customerLogin;

    @FXML
    private Button managerLogin;

    @FXML
    private Button kioskLogin;

    @FXML
    private ComboBox carparkSelect;

    @FXML
    void launchCustomerScreen(){
        Stage stage = (Stage)this.customerLogin.getScene().getWindow();
        stage.close();
        setCustomerLoginStage();
    }

    @FXML
    void launchManagerScreen(){
        Stage stage = (Stage)this.customerLogin.getScene().getWindow();
        stage.close();
        setManagerLoginStage();
    }

    @FXML
    void launchKioskScreen(){
        if(carparkSelect.getSelectionModel().isEmpty()){
            setMessageStage("Please choose Carpark.");
            return;
        }
        Stage stage = (Stage)this.customerLogin.getScene().getWindow();
        stage.close();
        setKioskStage();
    }

    public void setCustomerLoginStage(){
        try {
            Stage loginStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Screens/UserLoginScreen/UserLoginScreen.fxml"));
            UserLoginScreenController userLoginController = new UserLoginScreenController();
            loader.setController(userLoginController);
            userLoginController.setClient(client);
            AnchorPane pane = loader.load();
            loginStage.setScene(new Scene(pane));
            loginStage.setTitle("Client Login");
            loginStage.setResizable(false);
            loginStage.show();
        }
        catch (IOException ex){}
    }

    public void setKioskStage(){
        try {
            Stage kioskStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Screens/KioskScreen/KioskScreen.fxml"));
            KioskScreenController kioskScreenController = new KioskScreenController();
            loader.setController(kioskScreenController);
            kioskScreenController.setClient(client);
            kioskScreenController.setParkName(carparkSelect.getValue().toString());
            AnchorPane pane = loader.load();
            kioskStage.setScene(new Scene(pane));
            kioskStage.setTitle("Kiosk");
            kioskStage.setResizable(false);
            kioskStage.show();
        }
        catch (IOException ex){}
    }

    public void setManagerLoginStage(){
        try {
            Stage loginStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Screens/ManagerLoginScreen/ManagerLoginScreen.fxml"));
            ManagerLoginScreenController managerLoginScreenController = new ManagerLoginScreenController();
            loader.setController(managerLoginScreenController);
            managerLoginScreenController.setClient(client);
            AnchorPane pane = loader.load();
            loginStage.setScene(new Scene(pane));
            loginStage.setTitle("Manager Login");
            loginStage.setResizable(false);
            loginStage.show();
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

}