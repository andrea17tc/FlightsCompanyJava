package ro.mpp2024.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ro.mpp2024.service.IService;

import javax.swing.*;
import java.util.List;

public class BookController {

    public TextField textFieldClientName;
    public TextField textFieldClientAdress;
    
    public Spinner spinnerNumeric;
    public Label labelNoSeatsAvailable;
    
    public Button buttonBook;
    public Button buttonCancel;
    public TextField textFieldTouristName1;
    public TextField textFieldTouristName2;
    public TextField textFieldTouristName3;
    public TextField textFieldTouristName4;
    public TextField textFieldTouristName5;
    IService service;
    Parent parent;

    private int userId;
    private int flightId;

    public BookController() {
        System.out.println("Constructor BookController");
    }

    public void setService(IService service) {
        this.service = service;
        initData();
    }

    public void setParent(Parent root) {
        this.parent = root;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public void initialize(){
        List<TextField> textFields = List.of(textFieldTouristName1, textFieldTouristName2, textFieldTouristName3, textFieldTouristName4, textFieldTouristName5);
        for (TextField textField : textFields) {
            textField.visibleProperty().setValue(false);
        }

    }
    public void initData(){
        int availableSeats = service.findAvailableSeats(flightId);
        if(availableSeats<5){
            spinnerNumeric.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, availableSeats-1));
            labelNoSeatsAvailable.setText("Only " + availableSeats + " seats available!");
            labelNoSeatsAvailable.setStyle("-fx-text-fill: red;");
            System.out.println("spinner initialized to " + availableSeats);
        }
        else{
            spinnerNumeric.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5));;
            System.out.println("spinner initialized to 5");
        }

    }

    public void BookButtonClicked(ActionEvent actionEvent) {
        List<TextField> textFields = List.of(textFieldTouristName1, textFieldTouristName2, textFieldTouristName3, textFieldTouristName4, textFieldTouristName5);
        if(textFieldClientName.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Please enter the client name!");
        }
        else if(textFieldClientAdress.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Please enter the client address!");
        }
        else{
            for (int i = 0; i < (int) spinnerNumeric.getValue(); i++) {
                if(textFields.get(i).getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter the tourist name!");
                    return;
                }
            }
            int purchaseID = service.savePurchase(textFieldClientName.getText(), textFieldClientAdress.getText(), userId, flightId);
            int noSeats = 1;
            for (int i = 0; i < (int) spinnerNumeric.getValue(); i++) {
                service.saveTourist(textFields.get(i).getText(), purchaseID);
                noSeats++;
            }
            service.updateFlightSeats(noSeats, flightId);
            JOptionPane.showMessageDialog(null, "Purchase added successfully!");
            this.closeWindow();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) buttonCancel.getScene().getWindow();
        stage.close();
    }

    public void CancelButtonClicked(ActionEvent actionEvent) {
        this.closeWindow();
    }

    public void SpinnerValueChanged(MouseEvent mouseEvent) {
        int value = (int) spinnerNumeric.getValue();
        List<TextField> textFields = List.of(textFieldTouristName1, textFieldTouristName2, textFieldTouristName3, textFieldTouristName4, textFieldTouristName5);
        for (int i = 0; i < value; i++) {
            textFields.get(i).visibleProperty().setValue(true);
        }
        for (int i = value; i < 5; i++) {
            textFields.get(i).visibleProperty().setValue(false);
        }
    }

    public void setUserID(int userId) {
        this.userId= userId;
    }
}
