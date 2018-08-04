package Screens.ClientConnectionScreen;

import ClientServerMain.Client;
import Screens.CPSRouterScreen.CPSRouterScreenController;
import Screens.UserLoginScreen.UserLoginScreenController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ClientConnectionScreenController {

    Client client;
    String _host;


    @FXML
    private TextField host;
    @FXML
    private Button connect;

    @FXML
    public void onButtonClicked(ActionEvent e) {
        if(e.getSource().equals(connect)) {
            if(host.getText()==""){
                _host="127.0.0.1";
            }
            else {
                _host=host.getText();
            }
        }
        Stage stage = (Stage)this.connect.getScene().getWindow();
        stage.close();
        client= new Client(_host, 5555);
        client.clientHandler.printToServer("*Connected to Client*");
        client.clientHandler.buildDb();
        client.clientHandler.printToServer("*Data Base built*");
        setRouterStage();    }

    public void setRouterStage(){

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


        }
        catch (IOException ex){}
    }

}