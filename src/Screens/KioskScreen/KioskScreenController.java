package Screens.KioskScreen;


import java.io.IOException;

import ClientServerMain.Client;
import Screens.CPSRouterScreen.CPSRouterScreenController;
import Screens.MessageScreen.MessageScreenController;
import Screens.UserLoginScreen.UserLoginScreenController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class KioskScreenController {
	

	Client client;
	String parkName;
	int parkId;
	ObservableList type = FXCollections.observableArrayList();
    ObservableList Time = FXCollections.observableArrayList();

    @FXML
    private Button exitButton;
    
    @FXML
    private ComboBox<?> customerType;

    @FXML
    private ComboBox<?> leaveHour;


    @FXML
    private DatePicker leaveDate;

    @FXML
    private TextField orderOrMemberID;

    @FXML
    private Button enterButton;

    @FXML
    private TextField carID;

    @FXML
    private Button Back;


	@FXML
    public void initialize() {
		type = FXCollections.observableArrayList("Ordering","Member");
        customerType.getItems().addAll(type);
        setTimeComboBox();
        parkId=Integer.parseInt(parkName.substring(4));

    }
    
    @FXML
    void enter(ActionEvent event) {
	    if(customerType.getSelectionModel().isEmpty()) {
            setMessageStage("Must select Customer type.");
            return;
	    }
        if(orderOrMemberID.getText().trim().isEmpty()||carID.getText().trim().isEmpty()||leaveDate.getValue()==null||leaveHour.getSelectionModel().isEmpty()){
            setMessageStage("To enter please fill in all fields.");
            return;
        }

    	if (customerType.getValue().toString() == "Ordering") {
            client.clientHandler.enterParkOrder(Integer.parseInt(carID.getText()), parkId, Integer.parseInt(orderOrMemberID.getText()));
            setMessageStage("Your car is parked\nThank You!");
        }
        if (customerType.getValue().toString() == "Member") {
            String leaveTime = leaveDate.getValue().toString().replaceAll("-", "/") + " " + leaveHour.getValue().toString() + ":00";
            client.clientHandler.enterParkMember(Integer.parseInt(carID.getText()),parkId, Integer.parseInt(orderOrMemberID.getText()), leaveTime);
            setMessageStage("Your car is parked\nThank You!");
        }
    }

    @FXML
    void exit(ActionEvent event) {
	    if(carID.getText().trim().isEmpty()){
            setMessageStage("To exit please enter car number.");
            return;
        }
	    if(client.clientHandler.isaMember(Integer.parseInt(carID.getText()),parkId)!=0){
            client.clientHandler.leavePark(Integer.parseInt(carID.getText()), parkId);
            setMessageStage("Your car is ready for you\nThank You!");
        }
	    else {
            double cost = client.clientHandler.calcCost(Integer.parseInt(carID.getText()));
            client.clientHandler.leavePark(Integer.parseInt(carID.getText()), parkId);
            setMessageStage("Your car is ready for you.\nPlease pay: " + String.format("%.2f", cost) + "\nThank You!");
        }
    }
    


    
    public void setClient(Client _client){
        client = _client;
    }
    
    public void setParkName(String input){
        parkName = input;
    }

    public void setTimeComboBox() {
        for (int i = 0; i < 24; i++) {
            if (i < 10)
                Time = FXCollections.observableArrayList("0" + i + ":00", "0" + i + ":15", "0" + i + ":30", "0" + i + ":45");
            else
                Time = FXCollections.observableArrayList(i + ":00", i + ":15", i + ":30", i + ":45");
            leaveHour.getItems().addAll(Time);
        }
    }

    @FXML
    void backToLogin(ActionEvent event) {
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
