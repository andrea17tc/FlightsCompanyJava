package ro.mpp2024.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import ro.mpp2024.model.Flight;
import ro.mpp2024.service.Service;

public class SearchController {
    public ComboBox comboBoxDestinations;
    public DatePicker datePicker;
    public Button buttonSearch;
    public Button buttonBook;
    public ListView listView;
    public Label labelAllFlights;
    Service service;
    public void setService(Service service) {
        this.service = service;
        initData();
    }

    public void initialize(){
        comboBoxDestinations.setPromptText("Destinatie");
    }

    public void initData(){
        comboBoxDestinations.getItems().addAll(service.findAllFlightDestinations());
        for (Flight flight: service.findAllAvailableFlights())
        {
         listView.getItems().add(flight);
        }
    }

    public void SearchButtonClicked(ActionEvent actionEvent) {
        String destination = comboBoxDestinations.getValue().toString();
        System.out.println(datePicker.getValue());
        if(service.findAllFlightsByDestinationAndDate(destination, datePicker.getValue()).spliterator().getExactSizeIfKnown() == 0)
        {
            listView.visibleProperty().setValue(false);
            labelAllFlights.visibleProperty().setValue(true);
            Label label = new Label("No flights available for this destination and date");
            labelAllFlights.setLayoutX(64);
            labelAllFlights.setLayoutY(129);
            labelAllFlights.setPrefHeight(17);
            //add label to the pane
            labelAllFlights.getChildrenUnmodifiable().add(label);

        }
        else
            listView.getItems().setAll(service.findAllFlightsByDestinationAndDate(destination, datePicker.getValue()));

    }

    public void BookButtonClicked(ActionEvent actionEvent) {

    }
}
