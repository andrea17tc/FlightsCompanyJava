package ro.mpp2024;

import ro.mpp2024.controllers.*;
import ro.mpp2024.network.rpc.ServicesRpcProxy;
import ro.mpp2024.proto.ServiceRpcProxyProto;
import ro.mpp2024.service.IService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;


public class StartRpcClient extends Application {
    private Stage primaryStage;
    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";


    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClient.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IService server = new ServiceRpcProxyProto(serverIP, serverPort);

        //LOGIN
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("LoginView.fxml"));
        Parent root=loader.load();


        LoginController ctrl = loader.<LoginController>getController();
        ctrl.setService(server);


        //SEARCH
        FXMLLoader searchLoader = new FXMLLoader(getClass().getClassLoader().getResource("SearchView.fxml"));
        Parent searchRoot=searchLoader.load();


        SearchController searchCtrl = searchLoader.<SearchController>getController();
        //searchCtrl.setService(server);

        ctrl.setSearchController(searchCtrl);
        ctrl.setParent(searchRoot);


        //STAGE
        primaryStage.setTitle("Login Window");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }


}



