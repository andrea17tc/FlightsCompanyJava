package ro.mpp2024.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ro.mpp2024.Main;
import ro.mpp2024.model.Flight;
import ro.mpp2024.service.Service;
import ro.mpp2024.utils.Observer;

import javax.swing.*;
import java.io.IOException;

public class SearchController implements Observer {
    public ComboBox comboBoxDestinations;
    public DatePicker datePicker;
    public Button buttonSearch;
    public Button buttonBook;
    public ListView listView;
    public Label labelAllFlights;
    Service service;
    public void setService(Service service) {
        this.service = service;
        service.addObserver(this);
        initData();
    }

    public void initialize(){
        comboBoxDestinations.setPromptText("Destinatie");
    }

    public void initData(){
        comboBoxDestinations.getItems().addAll(service.findAllFlightDestinations());
        listView.getItems().clear();
        for (Flight flight: service.findAllAvailableFlights())
        {
         listView.getItems().add(flight);
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
            service.setFlightID(flight.getId());
            openWindow();

        }
    }

    private void openWindow() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("BookView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Book flight");
        BookController bookController = fxmlLoader.getController();
        bookController.setService(service);
        stage.setScene(scene);
        stage.show();
        //this.closeWindow();

    }

    public void LogoutButtonClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) buttonSearch.getScene().getWindow();
        stage.close();
    }

    @Override
    public void update() {
        initData();
    }
}
