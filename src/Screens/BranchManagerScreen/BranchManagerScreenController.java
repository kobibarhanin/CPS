package Screens.BranchManagerScreen;



import CarPark.ParkDetails;
import CarPark.Struct;
import ClientServerMain.ReportBuilder;
import Screens.CPSRouterScreen.CPSRouterScreenController;
import Screens.ManagerLoginScreen.ManagerLoginScreenController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import Screens.CustomerScreen.CustomerScreenController.MyRow;
import Screens.MessageScreen.MessageScreenController;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import ClientServerMain.Client;


public class BranchManagerScreenController {


    String[] _reports = {"Orders Report","Complaints Report","Corrupt Parking Space Report"};
    String[] status;
    Client client;
    int carpark;



    @FXML
    private Button sendPrices;

    @FXML
    private TextField inplacePrice;

    @FXML
    private TextField aheadPrice;

    @FXML
    private TextField occupancy;

    @FXML
    private TextField orders;

    @FXML
    private TextField corrupt;



    public void setStatus(String []_status){
        status=_status;
    }

    @FXML
    public void initialize() {
        carpark = Character.getNumericValue(client.clientHandler.getEmail().charAt(4));
        setStatus(client.clientHandler.getCarparkInfo(carpark));
        occupancy.setText(status[0]);
        orders.setText(status[1]);
        corrupt.setText(status[2]);
        reportsBox.getItems().addAll(Struct.report.ORDER.toString(),Struct.report.COMPLAINT.toString());

    }


    @FXML
    private Button Back;

    @FXML
    void backToManagerLogin(ActionEvent event) {
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


            Stage stage = (Stage) this.Back.getScene().getWindow();
            stage.close();


        }
        catch (IOException ex){}
    }

    public void setMessageStage(String msg){

        try {
            Stage messageStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Screens/MessageScreen/MessageScreen.fxml"));
            MessageScreenController messageScreenController = new MessageScreenController();
            messageScreenController.setMessage(msg);
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



    @FXML
    void sendPricesChangeRequest(){
        if(client.clientHandler.addPriceChangeRequest(carpark,Integer.parseInt(inplacePrice.getText()),Integer.parseInt(aheadPrice.getText()) )){
            setMessageStage("Request sent.\nPrices will update if approved.");
        }
        else setMessageStage("Something went worng\n please try again");
    }


    @FXML
    private ComboBox reportsBox;

    @FXML
    private ComboBox reportsBuild;



    @FXML
    void reportsBuildAct(){

        ReportBuilder builder = new ReportBuilder();
        if(reportsBox.getValue().toString()==Struct.report.ORDER.toString())
            builder.builldORDERReport(client.clientHandler.createParkAdminReport(Struct.report.ORDER.toString(),carpark));
        if(reportsBox.getValue().toString()==Struct.report.COMPLAINT.toString())
            builder.builldCOMPLAINTReport(client.clientHandler.createParkAdminReport(Struct.report.COMPLAINT.toString(),carpark));


    }


    public void setClient(Client _client){
        client = _client;
    }




}



