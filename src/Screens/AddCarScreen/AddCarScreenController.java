package Screens.AddCarScreen;

import ClientServerMain.Client;
import Screens.MessageScreen.MessageScreenController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class AddCarScreenController {

    Client client;

    @FXML
    private TextField carID;
    @FXML
    private Button addCarButton;



    @FXML
    public void onButtonClicked(ActionEvent e) {
        if(e.getSource().equals(addCarButton)) {
            client.clientHandler.addCar(carID.getText(), client.clientHandler.userEmail);
            messageStage(carID.getText());
            Stage stage = (Stage)this.addCarButton.getScene().getWindow();
            stage.close();
        }

    }

    public void messageStage(String carId){

        try {
            Stage addedStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Screens/MessageScreen/MessageScreen.fxml"));
            MessageScreenController messageScreenController = new MessageScreenController();
            messageScreenController.setMessage("Car " + carId + " has been added.");
            loader.setController(messageScreenController);
            Pane root = (Pane)loader.load();
            addedStage.setScene(new Scene(root));
            addedStage.setResizable(false);
            addedStage.show();
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

