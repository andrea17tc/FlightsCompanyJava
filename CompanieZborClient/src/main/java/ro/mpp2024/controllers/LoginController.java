package ro.mpp2024.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ro.mpp2024.service.IService;

import java.awt.*;
import java.io.IOException;

public class LoginController {

    public TextField textFieldUsername;
    private IService service;
    @FXML
    public PasswordField passwordField;

    public SearchController searchController;

    Parent parent;
    public void setService(IService service) {
        this.service = service;
    }

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
    }

    public void setParent(Parent root) {
        this.parent = root;
    }

    public void LoginButtonClicked(MouseEvent mouseEvent) throws IOException {
        String username = textFieldUsername.getText();
        String password = passwordField.getText();
        try{
            int userId = service.login(username, password, searchController);
            searchController.setService(service);
            searchController.setUser(userId);
            openWindow();
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    private void openWindow() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Search flights");
        stage.setScene(new Scene(parent));
        stage.show();
        //close current window
        ((Node)(textFieldUsername)).getScene().getWindow().hide();
    }

}
