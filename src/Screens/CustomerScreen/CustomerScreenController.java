package Screens.CustomerScreen;

import ClientServerMain.Client;
import Screens.MessageScreen.MessageScreenController;
import Screens.OrderTuple;
import Screens.RegistrationScreen.RegistrationScreenController;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Sample Skeleton for 'CustomerScreen.fxml' Controller Class
 */


public class CustomerScreenController {
	
	public class MyRow 
	{
		
		/** The Reservation id. */
		String OrderId;
		
		/** The Parking lot name. */
		String CarParkId;
		
		/** The Status. */
		String CarId;
		
		/** The Time. */
		String orderTimeFrom;

        String orderTimeTo;


        /**
		 * Instantiates a new my row.
		 *
		 * @param a the a
		 * @param b the b
		 * @param c the c
		 * @param d the d
		 */
		public MyRow(String a, String b, String c, String d, String e)
		{
			OrderId = a;
			CarId = b;
			CarParkId = c;
			orderTimeFrom = d;
            orderTimeTo = e;

        }
		
		/**
		 * Gets the order id.
		 *
		 * @return the order id
		 */
		public String getOrderId() {return OrderId;}
		
		/**
		 * Gets the status.
		 *
		 * @return the status
		 */
		public String getCarId() {return CarId;}
		
		/**
		 * Gets the parking lot name.
		 *
		 * @return the parking lot name
		 */
		public String getCarParkId() {return CarParkId;}

        public String getOrderTimeFrom() {
            return orderTimeFrom;
        }

        public String getOrderTimeTo() {
            return orderTimeTo;
        }
    }

    public class regMemberType
    {


        /** The Reservation id. */
        String RegMemID;

        /** The Status. */
        String RegMemCarID;

        String RegMemStartDate;

        /** The Parking lot name. */
        String RegMemCarpark;

        /** The Time. */
        String RegMemLeaveTime;


        public regMemberType(String a, String b, String c, String d, String e)
        {
            RegMemID = a;
            RegMemCarID = b;
            RegMemStartDate = c;
            RegMemCarpark = d;
            RegMemLeaveTime = e;
        }

        public String getRegMemID() {
            return RegMemID;
        }

        public String getRegMemCarID() {
            return RegMemCarID;
        }

        public String getRegMemStartDate() {
            return RegMemStartDate;
        }

        public String getRegMemCarpark() {
            return RegMemCarpark;
        }

        public String getRegMemLeaveTime() {
            return RegMemLeaveTime;
        }
    }

    public class fullMemberType
    {

        /** The Reservation id. */
        String FullMemID;

        /** The Status. */
        String FullMemCarID;


        String FullMemStartDate;

        public fullMemberType(String fullMemID, String fullMemCarID, String fullMemStartDate) {
            FullMemID = fullMemID;
            FullMemCarID = fullMemCarID;
            FullMemStartDate = fullMemStartDate;
        }

        public String getFullMemID() {
            return FullMemID;
        }

        public String getFullMemCarID() {
            return FullMemCarID;
        }

        public String getFullMemStartDate() {
            return FullMemStartDate;
        }
    }

    Client client;
    String userMail;
    String userName;
    String selectedMember;
    String selectedDate;
    String selectedParkingLot;
    String selectedStartTime;
    String selectedEndTime;
    String selectedCar;
    
    String[] carList;
    String[] orders;
    String[] regMemberships;
    String[] fullMemberships;

    //ObservableList carList = FXCollections.observableArrayList();
    String[] parkinglots;
    ObservableList Time = FXCollections.observableArrayList();
    ObservableList Members = FXCollections.observableArrayList("Full Membership", "Normal Membership");
    ObservableList<MyRow> row = FXCollections.observableArrayList();
    ObservableList<regMemberType> regMemRow = FXCollections.observableArrayList();
    ObservableList<fullMemberType> fullMemRow = FXCollections.observableArrayList();

    public void setClient(Client _client) {
        client = _client;
    }

    public void setUser(String user) {
        userName = user;
    }
    
    public void setMail(String mail) {
        userMail = mail;
    }

    public void setCarList(String[] input) {
    	//ObservableList carList = FXCollections.observableArrayList(input);
    	carList = input;
    }
    public void setParkinglots(String[] input) {
    	parkinglots = input;
    }
    
    public void setOrders(String email) {
    	orders = client.clientHandler.getOrders(email);
    }

    public void setRegMemberships(String email) {
        regMemberships = client.clientHandler.getRegmemberships(email);
    }
    public void setFullMemberships(String email) {
        fullMemberships = client.clientHandler.getFullmemberships(email);
    }


    @FXML
    private TextArea complaintText;

    @FXML
    private Button complaintSend;

    //----------------------------------

    @FXML
    private TableView<fullMemberType> fullMemTable;

    @FXML
    private TableColumn<fullMemberType, String> FullMemID;

    @FXML
    private TableColumn<fullMemberType, String> FullMemCarID;

    @FXML
    private TableColumn<fullMemberType, String> FullMemStartDate;


    //--------------------------

    @FXML
    private TableView<regMemberType> regMemTable;

    @FXML
    private TableColumn<regMemberType, String> RegMemID;

    @FXML
    private TableColumn<regMemberType, String> RegMemCarID;

    @FXML
    private TableColumn<regMemberType, String> RegMemStartDate;

    @FXML
    private TableColumn<regMemberType, String> RegMemCarpark;

    @FXML
    private TableColumn<regMemberType, String> RegMemLeaveTime;

    //----------------------

    @FXML // fx:id="MembershipList"
    private ComboBox<?> MembershipList; // Value injected by FXMLLoader

    @FXML // fx:id="EndTime"
    private ComboBox<?> EndTime; // Value injected by FXMLLoader

    @FXML // fx:id="InsertCarID"
    private TextField InsertCarID; // Value injected by FXMLLoader
    
    @FXML // fx:id="UserHell"
    private TextField UserHello;

    @FXML // fx:id="addNewCar"
    private Button addNewCar; // Value injected by FXMLLoader

    @FXML // fx:id="StartTime"
    private ComboBox<?> StartTime; // Value injected by FXMLLoader

    @FXML // fx:id="CarParkId"
    private TableColumn<MyRow, String> CarParkId; // Value injected by FXMLLoader

    @FXML // fx:id="OrderId"
    private TableColumn<MyRow, String> OrderId; // Value injected by FXMLLoader
    
    @FXML // fx:id="OrderId"
    private TableColumn<MyRow, String> orderTimeFrom;

    @FXML // fx:id="OrderId"
    private TableColumn<MyRow, String> orderTimeTo;
    
    @FXML // fx:id="table"
    private TableView<MyRow> table; // Value injected by FXMLLoader

    @FXML // fx:id="placeOrderButton"
    private Button placeOrderButton; // Value injected by FXMLLoader

    @FXML // fx:id="CarsComboBox"
    private ComboBox<?> CarsComboBox; // Value injected by FXMLLoader

    @FXML // fx:id="SelectCar1"
    private ComboBox SelectCar1; // Value injected by FXMLLoader

    @FXML // fx:id="SelectCar"
    private ComboBox<?> SelectCar; // Value injected by FXMLLoader

    @FXML // fx:id="CarId"
    private TableColumn<MyRow, String> CarId; // Value injected by FXMLLoader

    @FXML // fx:id="SelectCar11"
    private ComboBox SelectCar11; // Value injected by FXMLLoader

    @FXML // fx:id="CancelOrderButton"
    private Button CancelOrderButton; // Value injected by FXMLLoader
    
    @FXML
    private Button Back;

    @FXML // fx:id="SelectCarPark"
    private ComboBox SelectCarPark; // Value injected by FXMLLoader
    
    @FXML
    private ComboBox parkLots;

    @FXML // fx:id="SelectDate"
    private DatePicker SelectDate; // Value injected by FXMLLoader
    
    @FXML // fx:id="SelectDate"
    private DatePicker SelectDate1; // Value injected by FXMLLoader

    @FXML // fx:id="printUsersButton"
    private Button printUsersButton; // Value injected by FXMLLoader
    
    @FXML
    private ComboBox<?> chooseStart;
    
    @FXML
    private ComboBox<?> chooseEnd;


    public void setFullMembershipsTable(){
        if(fullMemberships.length  >0)
        {
            fullMemTable.setEditable(true);
            for(int i = 0; i < fullMemberships.length; i++)
            {
                String[] parts = fullMemberships[i].split(",");
                fullMemRow.add(new fullMemberType(parts[0], parts[1], parts[2]));
                fullMemTable.setItems(fullMemRow);
            }
            fullMemTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("FullMemID"));
            fullMemTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("FullMemCarID"));
            fullMemTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("FullMemStartDate"));

        }
    }


    public void setRegMembershipsTable(){
        if(regMemberships.length >0)
        {
            regMemTable.setEditable(true);
            for(int i = 0; i < regMemberships.length; i++)
            {
                String[] parts = regMemberships[i].split(",");
                regMemRow.add(new regMemberType(parts[0], parts[1], parts[2], parts[3] , parts[4]));
                regMemTable.setItems(regMemRow);
            }
            regMemTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("RegMemID"));
            regMemTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("RegMemCarID"));
            regMemTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("RegMemStartDate"));
            regMemTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("RegMemCarpark"));
            regMemTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("RegMemLeaveTime"));
        }
    }





    @FXML
    public void initialize() {

        setRegMembershipsTable();
        setFullMembershipsTable();

        if(orders.length >0)
    	{
	    	table.setEditable(true);
	    	for(int i = 0; i < orders.length; i++)
	    	{
	    		String[] parts = orders[i].split(",");
		    	row.add(new MyRow(parts[0], parts[1], parts[4], parts[2] , parts[3]));
		    	table.setItems(row);
	    	}
	    	table.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("OrderId"));
	    	table.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("CarId"));
	    	table.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("CarParkId"));
	    	table.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("orderTimeFrom"));
            table.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("orderTimeTo"));

        }


    	UserHello.setText(client.clientHandler.getUserName(client.clientHandler.userEmail));
    	SelectCar1.getItems().addAll(carList);
    	SelectCar11.getItems().addAll(carList);
		SelectCarPark.getItems().addAll(parkinglots);
		parkLots.getItems().addAll(parkinglots);
		MembershipList.getItems().addAll(Members);

        carParkSelector.getItems().addAll(parkinglots);

		for(int i = 0; i<24; i++)
		{
			if (i<10)
				Time = FXCollections.observableArrayList("0"+i+":00", "0"+i+":15", "0"+i+":30", "0"+i+":45");
			else
				Time = FXCollections.observableArrayList(i+":00", i+":15", i+":30", i+":45");
			StartTime.getItems().addAll(Time);
			EndTime.getItems().addAll(Time);
			chooseStart.getItems().addAll(Time);
			chooseEnd.getItems().addAll(Time);
		}

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
    
    
    @FXML
    void InsertCarID(ActionEvent event) {
    
    }

    @FXML
    void AddCar(ActionEvent event) {
        if(InsertCarID.getText().trim().isEmpty()){
            setMessageStage("Enter car number.");
            return;
        }
    	client.clientHandler.addCar(InsertCarID.getText(), userMail);
    	setCarList(client.clientHandler.getCars());
    	SelectCar1.getItems().clear();
    	SelectCar11.getItems().clear();
    	SelectCar1.getItems().addAll(carList);
    	SelectCar11.getItems().addAll(carList);
    	setMessageStage("Car " + InsertCarID.getText() + " was added");
    	//add car function with "CarID"
    }

    @FXML
    void ChooseMembership(ActionEvent event) {
    	if(MembershipList.getValue().toString() == "Normal Membership")
    	{
    		parkLots.setDisable(false);
    		chooseStart.setDisable(false);
    		chooseEnd.setDisable(false);
    	}
    	else
    	{
    		parkLots.setDisable(true);
    		chooseStart.setDisable(true);
    		chooseEnd.setDisable(true);
    	}
    }


    @FXML
    void ChooseParkLot(ActionEvent event) {

    }


    @FXML
    void addMember(ActionEvent event) {
        if(SelectCar11.getSelectionModel().isEmpty()||MembershipList.getSelectionModel().isEmpty()){
            setMessageStage("Please choose a car and membership type.");
            return;
        }
    	if(MembershipList.getValue().toString() == "Normal Membership")
    	{
    	    if(parkLots.getSelectionModel().isEmpty()||chooseEnd.getSelectionModel().isEmpty()){
    	        setMessageStage("Choose Carpark and End time");
            }
    		try {
    			String End = chooseEnd.getValue().toString() + ":00";
    			client.clientHandler.addRegMembership(SelectCar11.getValue().toString(), userMail, parkLots.getValue().toString(), End);
    			setMessageStage("Normal Membership added");
    		}catch(Exception ex)
    		{
    			setMessageStage("Unable to add Membership\nplease try again");
    		}
    	}
    	if(MembershipList.getValue().toString() == "Full Membership")
    	{
    		try {
    			client.clientHandler.addFullMembership(SelectCar11.getValue().toString(), userMail);
    			setMessageStage("Full Membership added");
    		}catch(Exception ex)
    		{
    			setMessageStage("Unable to add Membership\nplease try again");
    		}
    	}
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



    @FXML
    void placeOrder(ActionEvent event) {
    	//System.out.println(Integer.parseInt(SelectCar1.getAccessibleText()) + userMail + StartTime.getAccessibleText() + EndTime.getAccessibleText() + SelectCarPark.getAccessibleText());
			try {
			    if(SelectCar1.getSelectionModel().isEmpty()||SelectCarPark.getSelectionModel().isEmpty()||StartTime.getSelectionModel().isEmpty()||EndTime.getSelectionModel().isEmpty()||SelectDate.getValue()==null||SelectDate1.getValue()==null){
			        setMessageStage("Enter Order information - all fields are required.");
			        return;
                }
				String dateST = SelectDate.getValue().toString().replaceAll("-", "/");
				String dateEN = SelectDate1.getValue().toString().replaceAll("-", "/");
				String Start = dateST + " " + StartTime.getValue().toString() + ":00";
				String End = dateEN + " " + EndTime.getValue().toString() + ":00";
				if(client.clientHandler.addOrder(Integer.parseInt(SelectCar1.getValue().toString()), userMail, Start, End, 1,"AHEAD"))
					setMessageStage("Order Added\nThank You!");
				else
					setMessageStage("Something went worng\nplease try again");
			} catch (Exception e) {
				setMessageStage("Something went worng\nplease try again");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    }


    @FXML
    void EndTime(ActionEvent event) {

    }

    @FXML
    void SelectCar(ActionEvent event) {

    }

    @FXML
    void CancelOrder(ActionEvent event) {
    	try {
			if(client.clientHandler.cancelOrder(Integer.parseInt(table.getSelectionModel().getSelectedItem().getOrderId())))
				setMessageStage("Order " + table.getSelectionModel().getSelectedItem().getOrderId() + " Canceld");
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
    private ComboBox carParkSelector;

    @FXML
    void sendComplaint(ActionEvent event){
        if(client.clientHandler.addComplaint(complaintText.getText(),Integer.parseInt(carParkSelector.getValue().toString().substring(4))))
            setMessageStage("Your Complaint is recieved.\nWe will be in touch with you shortly via Email.");
        else setMessageStage("Error - Complaint not submitted.\nTry again.");
    }

}
