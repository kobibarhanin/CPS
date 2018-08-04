package Screens.MessageScreen;

import java.awt.Button;
import java.awt.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MessageScreenController {
    String _message;

    @FXML
    private Label message;
    
    @FXML
    private Button OK;

    @FXML
    public void setMessage(String output){
        _message=output;
    }

    @FXML
    public void initialize(){
        message.setText(_message);
    }
    
    @FXML
    void OK(ActionEvent event) {
    	Stage stage = (Stage) this.message.getScene().getWindow();
    	stage.close();
    }

}
