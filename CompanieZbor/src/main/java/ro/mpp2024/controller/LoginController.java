package ro.mpp2024.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ro.mpp2024.Main;
import ro.mpp2024.service.Service;

import java.awt.*;
import java.io.IOException;

public class LoginController {

    public Button loginButton;
    public javafx.scene.control.TextField textFieldUsername;
    public PasswordField passwordField;
    Service service;
    public void setService(Service service) {
        this.service = service;
    }

    public void LoginButtonClicked(MouseEvent mouseEvent) throws IOException {
        String username = textFieldUsername.getText();
        String password = passwordField.getText();
        if(service.findUser(username, password)){;
            this.openWindow();
        }
        else{
            System.out.println("Username or password incorrect!");
        }
    }

    private void openWindow() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("SearchView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Search flights");
        SearchController searchController = fxmlLoader.getController();
        searchController.setService(service);
        stage.setScene(scene);
        stage.show();
        //this.closeWindow();

    }
}
