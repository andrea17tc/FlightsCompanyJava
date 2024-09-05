package ro.mpp2024;

import ro.mpp2024.repository.*;
import ro.mpp2024.server.ServerService;
import ro.mpp2024.utils.AbstractServer;
import ro.mpp2024.utils.RpcConcurrentServer;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {
        // UserRepository userRepo=new UserRepositoryMock();
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties "+e);
            return;
        }
        UserRepository userRepo=new UserRepository(serverProps);
        FlightRepository flightRepository = new FlightRepository(serverProps);
        TouristRepository touristRepository = new TouristRepository(serverProps);
        PurchaseRepository purchaseRepository = new PurchaseRepository(serverProps, flightRepository, userRepo, touristRepository);
        TripRepository tripRepository = new TripRepository(serverProps, touristRepository, purchaseRepository);
        ServerService service = new ServerService(flightRepository, purchaseRepository, touristRepository, tripRepository, userRepo);

        int serverPort=defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+serverPort);
        AbstractServer server = new RpcConcurrentServer(serverPort, service);
        try {
            server.start();
        }
        catch (Exception e) {
            System.err.println("Error starting the server" + e.getMessage());
        }finally {
            try {
                server.stop();
            }catch(Exception e){
                System.err.println("Error stopping server "+e.getMessage());
            }
        }
    }
}
