package ro.mpp2024;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ro.mpp2024.controller.LoginController;
import ro.mpp2024.model.Flight;
import ro.mpp2024.model.User;
import ro.mpp2024.repository.*;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Properties;
import javafx.application.Application;
import ro.mpp2024.service.Service;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main extends Application{

    FlightRepository flightRepository;
    UserRepository userRepository;
    PurchaseRepository purchaseRepository;
    TouristRepository touristRepository;
    TripRepository tripRepository;
    Service service;
    private static Properties props= new Properties();

    public static void main(String[] args) {
        // Press Alt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        //System.out.printf("Hello and welcome!");
;
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }
        launch();


    }

    public void start(Stage stage) throws IOException{
        flightRepository = new FlightRepository(props);
        userRepository= new UserRepository(props);
        touristRepository= new TouristRepository(props);
        purchaseRepository= new PurchaseRepository(props,flightRepository, userRepository,touristRepository);
        tripRepository = new TripRepository(props, touristRepository,purchaseRepository);
        service = new Service(flightRepository, purchaseRepository, touristRepository, tripRepository, userRepository);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Login");
        LoginController loginController = loader.getController();
        loginController.setService(service);
        stage.setScene(scene);
        stage.show();
    }
}