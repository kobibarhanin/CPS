package Screens.AdministratorScreen;



import java.awt.Desktop;
import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class AdministratorScreenController {
	
    String[] _parkinglots;	
    File _Occupancy;
    File _ActivityReport;
    File _PreformanceReport;
    
    
    public void setParkinglots(String[] input) {
    	_parkinglots = input;
    }
    
    @FXML
    public void initialize() {
    	ParkingLotsLst.getItems().addAll(_parkinglots);
    }

    
    
    /*
    Show a PDF file
    */
   public void showpdf (File f) {
		  try {

			if (f.exists()) {

				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(f);
				} else {
					System.out.println("Awt Desktop is not supported!");
				}

			} else {
				System.out.println("File is not exists!");
			}

			System.out.println("Done");

		  } catch (Exception ex) {
			ex.printStackTrace();
		  }

	}



    @FXML // fx:id="ActivityReportBtn"
    private Button ActivityReportBtn; // Value injected by FXMLLoader

    @FXML // fx:id="ParkingLotsLst"
    private ComboBox<String> ParkingLotsLst; // Value injected by FXMLLoader

    @FXML // fx:id="PreformanceReportBtn"
    private Button PreformanceReportBtn; // Value injected by FXMLLoader

    @FXML // fx:id="ShowOccupancyBtn"
    private Button ShowOccupancyBtn; // Value injected by FXMLLoader
    
    
    
    

    @FXML
    void ShowOccupancy(ActionEvent event) {
    	//_Occupancy=getPdf(ParkingLotsLst.getItems().toString());
    	showpdf(_Occupancy);
    }


    @FXML
    void CreatePreformanceReport(ActionEvent event) {
    	//_PreformanceReport=getPdf("Preformance");
    	showpdf(_PreformanceReport);

    }

    @FXML
    void CreateActivityReport(ActionEvent event) {
    	//_ActivityReport=getPdf("Preformance");
    	showpdf(_ActivityReport);

    }

    @FXML
    void ae0101(ActionEvent event) {

    }

}
