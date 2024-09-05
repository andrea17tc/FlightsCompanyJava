package ro.mpp2024.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ro.mpp2024.model.Flight;
import ro.mpp2024.service.IObserver;
import ro.mpp2024.service.IService;
import ro.mpp2024.service.IService;


import javax.swing.*;
import java.io.IOException;

public class SearchController implements IObserver {
    @FXML
    public ComboBox comboBoxDestinations;
    @FXML
    public DatePicker datePicker;
    @FXML
    public Button buttonSearch;
    @FXML
    public Button buttonBook;
    @FXML
    public ListView listView;
    @FXML
    public Label labelAllFlights;

    public BookController bookController;
    IService service;
    Parent parent;
    private int userId;


    public void setService(IService service) {
        this.service = service;
        initData();
    }

    public void setParent(Parent root) {
        this.parent = root;
    }

    public void setBookController(BookController bookCtrl) {
        bookController = bookCtrl;
    }

    public void initialize(){
        comboBoxDestinations.setPromptText("Destinatie");
    }

    public void initData(){
        try{
            comboBoxDestinations.getItems().clear();
            comboBoxDestinations.getItems().addAll(service.findAllFlightDestinations());
            listView.getItems().clear();
            for (Flight flight: service.findAllAvailableFlights())
            {
                listView.getItems().add(flight);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void SearchButtonClicked(ActionEvent actionEvent) {
        String destination = comboBoxDestinations.getValue().toString();
        System.out.println(datePicker.getValue());
        if(destination == null || datePicker.getValue() == null)
        {
            JOptionPane.showMessageDialog(null, "Please select a destination and a date!");
        }
        else{
            if(service.findAllFlightsByDestinationAndDate(destination, datePicker.getValue()).spliterator().getExactSizeIfKnown() == 0)
            {
                listView.visibleProperty().setValue(false);
                labelAllFlights.setText("No available flights for this destination and date!");
                labelAllFlights.textFillProperty().setValue(Color.RED);
                labelAllFlights.fontProperty().setValue(javafx.scene.text.Font.font(20));
                labelAllFlights.visibleProperty().setValue(true);
            }
            else{
                listView.getItems().clear();
                listView.visibleProperty().setValue(true);
                labelAllFlights.visibleProperty().setValue(false);
                for (Flight flight: service.findAllFlightsByDestinationAndDate(destination, datePicker.getValue())) {
                    listView.getItems().add(flight);
                }
            }
        }


    }

    public void BookButtonClicked(ActionEvent actionEvent) throws IOException {
        Flight flight = (Flight) listView.getSelectionModel().getSelectedItem();
        if(flight == null)
        {
            JOptionPane.showMessageDialog(null, "Please select a flight!");
        }
        else{
            openWindow(flight.getId());

        }
    }

    private void openWindow(int flightID) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("BookView.fxml"));
        Parent parent = loader.load();
        BookController bookctrl = loader.<BookController>getController();
        Stage stage = new Stage();
        stage.setTitle("Book flight");
        stage.setScene(new Scene(parent));
        bookctrl.setUserID(userId);
        bookctrl.setFlightId(flightID);
        bookctrl.setService(service);
        stage.show();
    }


    public void LogoutButtonClicked(ActionEvent actionEvent) throws Exception {
        service.logout(userId);
        Stage stage = (Stage) buttonSearch.getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    @Override
    public void updateFlight(int noSeats, int f) {
        Platform.runLater(()->{
            initData();
        });
    }


    public void setUser(int userId) {
        this.userId= userId;
    }
}
