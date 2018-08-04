package Screens.CPSManagerScreen;

import CarPark.ParkDetails;
import ClientServerMain.Client;
import ClientServerMain.ReportBuilder;
import Screens.ManagerLoginScreen.ManagerLoginScreenController;
import Screens.MessageScreen.MessageScreenController;
import Screens.UserLoginScreen.UserLoginScreenController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CPSManagerScreenController {


    public class complaintType{

        String ComplaintId;

        String CustomerEmail;

        String Complaint;

        String ComplaintStatus;

        public complaintType(String complaintId, String customerEmail, String complaint, String complaintStatus) {
            ComplaintId = complaintId;
            CustomerEmail = customerEmail;
            Complaint = complaint;
            ComplaintStatus = complaintStatus;
        }

        public String getComplaintId() {
            return ComplaintId;
        }

        public String getCustomerEmail() {
            return CustomerEmail;
        }

        public String getComplaint() {
            return Complaint;
        }

        public String getComplaintStatus() {
            return ComplaintStatus;
        }
    }

    public class priceChangeType{

        String requestID;
        String requestPark;
        String aheadPrice;
        String inplacePrice;
        String requestStatus;

        public priceChangeType(String requestID, String requestPark, String aheadPrice, String inplacePrice, String requestStatus) {
            this.requestID = requestID;
            this.requestPark = requestPark;
            this.aheadPrice = aheadPrice;
            this.inplacePrice = inplacePrice;
            this.requestStatus = requestStatus;
        }

        public String getRequestID() {
            return requestID;
        }

        public String getRequestPark() {
            return requestPark;
        }

        public String getAheadPrice() {
            return aheadPrice;
        }

        public String getInplacePrice() {
            return inplacePrice;
        }

        public String getRequestStatus() {
            return requestStatus;
        }
    }

    Client client;

    String[] complaints;
    String[] priceChangeRequests;

    ObservableList<complaintType> complaintRow = FXCollections.observableArrayList();
    ObservableList<priceChangeType> priceChangeRow = FXCollections.observableArrayList();

    public void setClient(Client _client) {
        client = _client;
    }

    public void setComplaints() {
        complaints = client.clientHandler.getComplaints();
    }
    public void setPriceChangeRequests() {
        priceChangeRequests = client.clientHandler.getPriceChangeRequests();
    }

    @FXML
    private TableView<complaintType> complaintTable;

    @FXML
    private TableColumn<complaintType, String> ComplaintId;

    @FXML
    private TableColumn<complaintType, String> CustomerEmail;

    @FXML
    private TableColumn<complaintType, String> Complaint;

    @FXML
    private TableColumn<complaintType, String> ComplaintStatus;


    @FXML
    private TableView<priceChangeType> requestsTable;

    @FXML
    private TableColumn<priceChangeType, String> requestID;

    @FXML
    private TableColumn<priceChangeType, String> requestPark;

    @FXML
    private TableColumn<priceChangeType, String> aheadPrice;

    @FXML
    private TableColumn<priceChangeType, String> inplacePrice;

    @FXML
    private TableColumn<priceChangeType, String> requestStatus;



    public void setComplaintsTable(){
            if(complaints.length > 0)
        {
            complaintTable.setEditable(true);
            for(int i = 0; i < complaints.length; i++)
            {
                String[] parts = complaints[i].split(",");
                complaintRow.add(new complaintType(parts[0], parts[1], parts[2],parts[3]));
                complaintTable.setItems(complaintRow);
            }
            complaintTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("ComplaintId"));
            complaintTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("CustomerEmail"));
            complaintTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("Complaint"));
            complaintTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("ComplaintStatus"));
        }
    }

    public void setPriceChangeRequestsTable(){
        if(priceChangeRequests.length > 0)
        {
            requestsTable.setEditable(true);
            for(int i = 0; i < priceChangeRequests.length; i++)
            {
                String[] parts = priceChangeRequests[i].split(",");
                priceChangeRow.add(new priceChangeType(parts[0], parts[1], parts[2],parts[3],parts[4]));
                requestsTable.setItems(priceChangeRow);
            }
            requestsTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("requestID"));
            requestsTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("requestPark"));
            requestsTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("aheadPrice"));
            requestsTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("inplacePrice"));
            requestsTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("requestStatus"));

        }
    }

    @FXML
    void closeComplaint(ActionEvent event) {
        try {
            if(client.clientHandler.closeComplaint(Integer.parseInt(complaintTable.getSelectionModel().getSelectedItem().getComplaintId()))){
                setMessageStage("Complaint " +complaintTable.getSelectionModel().getSelectedItem().getComplaintId() + " Closed");
            }
            else setMessageStage("Something went worng\n please try again");
        } catch (NumberFormatException e) {
            setMessageStage("Something went worng\n please try again");
            e.printStackTrace();
        } catch (Exception e) {
            setMessageStage("Something went worng\n please try again");
            e.printStackTrace();
        }
    }

    @FXML
    void approveChange(ActionEvent event){
        try {
            int requestId=Integer.parseInt(requestsTable.getSelectionModel().getSelectedItem().getRequestID());
            int park=Integer.parseInt(requestsTable.getSelectionModel().getSelectedItem().getRequestPark());
            int inplace=Integer.parseInt(requestsTable.getSelectionModel().getSelectedItem().getInplacePrice());
            int ahead=Integer.parseInt(requestsTable.getSelectionModel().getSelectedItem().getAheadPrice());

            if(client.clientHandler.approveSetPrices(requestId,park,inplace,ahead)){
                setMessageStage("Request " +requestId + " Approved");
            }
            else setMessageStage("Something went worng\n please try again");
        } catch (NumberFormatException e) {
            setMessageStage("Something went worng\n please try again");
            e.printStackTrace();
        } catch (Exception e) {
            setMessageStage("Something went worng\n please try again");
            e.printStackTrace();
        }
    }
    @FXML
    void rejectChange(ActionEvent event){
        try {
            int requestId=Integer.parseInt(requestsTable.getSelectionModel().getSelectedItem().getRequestID());

            if(client.clientHandler.rejectSetPrices(requestId)){
                setMessageStage("Request " + requestId + " Rejected");
            }
            else setMessageStage("Something went worng\n please try again");
        } catch (NumberFormatException e) {
            setMessageStage("Something went worng\n please try again");
            e.printStackTrace();
        } catch (Exception e) {
            setMessageStage("Something went worng\n please try again");
            e.printStackTrace();
        }
    }


    @FXML
    public void initialize() {

        setComplaintsTable();
        setPriceChangeRequestsTable();
        parkSelector.getItems().addAll(carparks);
        reportSelector.getItems().addAll(ParkDetails.type.PERIOD.toString(),ParkDetails.type.STATISTICS.toString());
        parkingLot.getItems().addAll(carparks);


        m.put(0, c00);
        m.put(1, c01);
        m.put(2, c02);
        m.put(3, c03);
        m.put(4, c04);
        m.put(5, c05);
        m.put(6, c06);
        m.put(7, c07);
        m.put(8, c10);
        m.put(9, c11);
        m.put(10, c12);
        m.put(11, c13);
        m.put(12, c14);
        m.put(13, c15);
        m.put(14, c16);
        m.put(15, c17);
        m.put(16, c20);
        m.put(17, c21);
        m.put(18, c22);
        m.put(19, c23);
        m.put(20, c24);
        m.put(21, c25);
        m.put(22, c26);
        m.put(23, c27);

    }


    @FXML
    private Button Back;

    @FXML
    private Button parkView;

    @FXML
    private Button parkReport;

    @FXML
    private ComboBox parkSelector;

    @FXML
    private ComboBox reportSelector;

    @FXML
    private TextField monthlyMem;

    @FXML
    private TextField monthlyMemCar;





    @FXML
    void parkReportAct(){

        ReportBuilder builder = new ReportBuilder();

        if(reportSelector.getValue().toString()==ParkDetails.type.STATISTICS.toString())
            builder.builldSTATISTICSReport(client.clientHandler.createAdminReport(ParkDetails.type.STATISTICS.toString(),selectedCarpark));
        if(reportSelector.getValue().toString()==ParkDetails.type.PERIOD.toString())
            builder.builldPERIODReport(client.clientHandler.createAdminReport(ParkDetails.type.PERIOD.toString(),selectedCarpark));
    }

    int selectedCarpark=-1;
    @FXML
    void parkViewAct(){
    }

    @FXML
    void parkSelectorAct(){
        selectedCarpark = Integer.parseInt(parkSelector.getValue().toString().substring(4));
        String [] input=client.clientHandler.createAdminReport(ParkDetails.type.PERFORMANCE.toString(),selectedCarpark).split(",");
        monthlyMem.setText(input[0]);
        monthlyMemCar.setText(input[1]);
    }

    String[] carparks;

    public void setCarparks(String[] input) {
        carparks = input;
    }


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


    Map<Integer, Rectangle> m = new HashMap<Integer, Rectangle>();




    @FXML // fx:id="c11"
    private Rectangle c11; // Value injected by FXMLLoader

    @FXML // fx:id="parkingLot"
    private ComboBox<String> parkingLot; // Value injected by FXMLLoader

    @FXML // fx:id="c10"
    private Rectangle c10; // Value injected by FXMLLoader

    @FXML // fx:id="RepTxt"
    private TextArea RepTxt; // Value injected by FXMLLoader

    @FXML // fx:id="c13"
    private Rectangle c13; // Value injected by FXMLLoader

    @FXML // fx:id="c12"
    private Rectangle c12; // Value injected by FXMLLoader

    @FXML // fx:id="c15"
    private Rectangle c15; // Value injected by FXMLLoader

    @FXML // fx:id="c14"
    private Rectangle c14; // Value injected by FXMLLoader

    @FXML // fx:id="c17"
    private Rectangle c17; // Value injected by FXMLLoader

    @FXML // fx:id="c16"
    private Rectangle c16; // Value injected by FXMLLoader

    @FXML // fx:id="reportType"
    private ComboBox<String> reportType; // Value injected by FXMLLoader

    @FXML // fx:id="c20"
    private Rectangle c20; // Value injected by FXMLLoader

    @FXML // fx:id="c00"
    private Rectangle c00; // Value injected by FXMLLoader

    @FXML // fx:id="c22"
    private Rectangle c22; // Value injected by FXMLLoader

    @FXML // fx:id="c21"
    private Rectangle c21; // Value injected by FXMLLoader

    @FXML // fx:id="c02"
    private Rectangle c02; // Value injected by FXMLLoader

    @FXML // fx:id="c24"
    private Rectangle c24; // Value injected by FXMLLoader

    @FXML // fx:id="c01"
    private Rectangle c01; // Value injected by FXMLLoader

    @FXML // fx:id="c23"
    private Rectangle c23; // Value injected by FXMLLoader

    @FXML // fx:id="c04"
    private Rectangle c04; // Value injected by FXMLLoader

    @FXML // fx:id="c26"
    private Rectangle c26; // Value injected by FXMLLoader

    @FXML // fx:id="c03"
    private Rectangle c03; // Value injected by FXMLLoader

    @FXML // fx:id="c25"
    private Rectangle c25; // Value injected by FXMLLoader

    @FXML // fx:id="c06"
    private Rectangle c06; // Value injected by FXMLLoader

    @FXML // fx:id="c05"
    private Rectangle c05; // Value injected by FXMLLoader

    @FXML // fx:id="c27"
    private Rectangle c27; // Value injected by FXMLLoader

    @FXML // fx:id="c07"
    private Rectangle c07; // Value injected by FXMLLoader

    @FXML // fx:id="ParkingLotsLst1"
    private ComboBox<String> ParkingLotsLst1; // Value injected by FXMLLoader



    String map,map2;
    int width;
    String[] status = null;








    ///////////////////////////////////////////////////////////////////////////////////////////////







    void showmap (int i) {
        int j = 0;
        int temp = i;

        for (; i < 3*width + temp; i++, j++)
        {
            if (status[i].equals("EMPTY"))
                m.get(j).setFill(Color.GREEN);

            else if (status[i].equals("OCCUPIED"))
                m.get(j).setFill(Color.BLUE);

            else if (status[i].equals("CORRUPT"))
                m.get(j).setFill(Color.RED);

            if((i+1)%width == 0)
                j+= 8-width;

        }
    }




    @FXML
    void showFirstFloor(ActionEvent event) {
        if (status != null)
            showmap(0);
    }

    @FXML
    void showSecondFloor(ActionEvent event) {
        if (status != null)
            showmap(width*3);
    }

    @FXML
    void showThirdFloor(ActionEvent event) {
        if (status != null)
            showmap(width*6);
    }




    @FXML
    void chooseParkingLot(ActionEvent event) {
        for(int i = 0; i < m.size(); i++)
            m.get(i).setFill(Color.TRANSPARENT);

//        System.out.println(Integer.parseInt(parkingLot.getValue().toString().substring(4)));

        map = client.clientHandler.createAdminReport(ParkDetails.type.POSITIONS.toString(), Integer.parseInt(parkingLot.getValue().toString().substring(4)));
//        map = ("park11#3#4#3#11,0,CORRUPT,0.0,0;12,0,Empty,0.0,0;13,0,Empty,0.0,0;14,0,Empty,1.0,0;15,0,Empty,1.0,0;16,0,Empty,1.0,0;17,0,Empty,2.0,0;18,0,Empty,2.0,0;19,0,Empty,2.0,0;21,0,Empty,1.0,0;22,0,Empty,1.0,0;23,0,Empty,1.0,0;24,0,Empty,2.0,0;25,0,Empty,2.0,0;26,0,Empty,2.0,0;27,0,Empty,3.0,0;28,0,Empty,3.0,0;29,0,Empty,3.0,0;31,0,Empty,2.0,0;32,0,Empty,2.0,0;33,0,Empty,2.0,0;34,0,Empty,3.0,0;35,0,Empty,3.0,0;36,0,Empty,3.0,0;37,0,Empty,4.0,0;38,0,Empty,4.0,0;39,0,Empty,4.0,0;39,0,Empty,4.0,0;39,0,Empty,4.0,0;39,0,Empty,4.0,0;39,0,Empty,4.0,0;39,0,Empty,4.0,0;39,0,Empty,4.0,0;39,0,Empty,4.0,0;39,0,Empty,4.0,0;39,0,Empty,4.0,0");

        String[] first = map.split("#");


        width = Integer.parseInt(first[2]);
        String []first1 = first[4].split(";");
        status = new String[first1.length];
        for (int i = 0; i < first1.length; i++)
        {
            status[i] = (first1[i].split(","))[2];
            System.out.println(status[i]);
        }

    }


}
