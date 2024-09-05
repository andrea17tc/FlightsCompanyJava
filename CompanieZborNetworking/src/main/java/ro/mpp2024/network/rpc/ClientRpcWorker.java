package ro.mpp2024.network.rpc;

import ro.mpp2024.model.Flight;
import ro.mpp2024.model.User;
import ro.mpp2024.service.IObserver;
import ro.mpp2024.service.IService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;


public class ClientRpcWorker implements Runnable, IObserver {
    private IService server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientRpcWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            //output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request) {
        Response response = null;
        if (request.type() == RequestType.LOGIN) {
            System.out.println("Login request ..." + request.type());
            User u = (User) request.data();
            try {
                int id = server.login(u.getUsername(), u.getPassword(), this);
                return new Response.Builder().type(ResponseType.OK).data(id).build();
            } catch (Exception e) {
                connected = false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.LOGOUT) {
            System.out.println("Logout request");
            // LogoutRequest logReq=(LogoutRequest)request;
            int userID = (int) request.data();
            try {
                server.logout(userID);
                connected = false;
                return okResponse;

            } catch (Exception e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.FIND_ALL_AVAILABLE_FLIGHTS) {
            System.out.println("Find all available flights request ...");
            try {
                Iterable<Flight> flights = server.findAllAvailableFlights();
                return new Response.Builder().type(ResponseType.GET_AVAILABLE_FLIGHTS).data(flights).build();
            } catch (Exception e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
            if (request.type() == RequestType.FIND_ALL_FLIGHT_DESTINATIONS) {
                System.out.println("GetLoggedFriends Request ...");
                try {
                    Iterable<String> destinations = server.findAllFlightDestinations();
                    return new Response.Builder().type(ResponseType.GET_ALL_FLIGHT_DESTINATIONS).data(destinations).build();
                } catch (Exception e) {
                    return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
                }
            }

            if (request.type() == RequestType.FIND_ALL_FLIGHTS_BY_DESTINATION_AND_DATE) {
                System.out.println("Find all flights by destination and date request ...");
                Object[] data = (Object[]) request.data();
                String destination = (String) data[0];
                String date = (String) data[1];
                try {
                    Iterable<Flight> flights = server.findAllFlightsByDestinationAndDate(destination, LocalDate.parse(date));
                    return new Response.Builder().type(ResponseType.GET_FLIGHTS_BY_DESTINATION_AND_DATE).data(flights).build();
                } catch (Exception e) {
                    return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
                }
            }

            if (request.type() == RequestType.FIND_ALL_FLIGHTS) {
                System.out.println("Find all flights request ...");
                try {
                    Iterable<Flight> flights = server.findAll();
                    return new Response.Builder().type(ResponseType.GET_ALL_FLIGHTS).data(flights).build();
                } catch (Exception e) {
                    return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
                }
            }

            if (request.type() == RequestType.FIND_AVAILABLE_SEATS) {
                System.out.println("Find available seats request ...");
                int flightID = (int) request.data();
                try {
                    int seats = server.findAvailableSeats(flightID);
                    return new Response.Builder().type(ResponseType.GET_AVAILABLE_SEATS).data(seats).build();
                } catch (Exception e) {
                    return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
                }
            }

            if (request.type() == RequestType.SAVE_TOURIST) {
                System.out.println("Save tourist request ...");
                Object[] data = (Object[]) request.data();
                String name = (String) data[0];
                int purchaseID = (int) data[1];
                try {
                    server.saveTourist(name,purchaseID);
                    return okResponse;
                } catch (Exception e) {
                    return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
                }
            }

            if (request.type() == RequestType.SAVE_TRIP) {
                System.out.println("Save trip request ...");
                Object[] data = (Object[]) request.data();
                int flightID = (int) data[0];
                int touristID = (int) data[1];
                try {
                    server.saveTrip(flightID, touristID);
                    return okResponse;
                } catch (Exception e) {
                    return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
                }
            }
            if(request.type() == RequestType.SAVE_PURCHASE){
                System.out.println("Save purchase request ...");
                Object[] data = (Object[]) request.data();
                String clientName = (String) data[0];
                String clientAdress = (String) data[1];
                int userID = (int) data[2];
                int flightID = (int) data[3];
                try {
                    int id = server.savePurchase(clientName, clientAdress, userID,flightID);
                    return new Response.Builder().type(ResponseType.OK).data(id).build();
                } catch (Exception e) {
                    return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
                }
            }

            if (request.type() == RequestType.UPDATE_FLIGHT_SEATS) {
                System.out.println("Update flight seats request ...");
                Object[] data = (Object[]) request.data();
                int noSeats = (int) data[0];
                int flightID = (int) data[1];
                try {
                    server.updateFlightSeats(noSeats, flightID);
                    return okResponse;
                } catch (Exception e) {
                    return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
                }
            }

        return response;
    }

    private void sendResponse (Response response) throws IOException {
        System.out.println("sending response " + response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }

    @Override
    public void updateFlight(int noSeats, int flightID) {
        Object[] data = new Object[] { noSeats, flightID };
        Response response = new Response.Builder().type(ResponseType.UPDATE).data(data).build();
        try {
            sendResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

