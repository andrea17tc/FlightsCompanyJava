package ro.mpp2024.network.rpc;

import ro.mpp2024.model.*;
import ro.mpp2024.service.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ServicesRpcProxy implements IService {
    private String host;
    private int port;
    private IObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    private int flightID;

    public ServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<>();
    }

    @Override
    public int getFlightID() {
        return flightID;
    }

    @Override
    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }


    @Override
    public void setTouristID(int touristID) {

    }

    @Override
    public int login(String username,String password, IObserver client) throws Exception {
        initializeConnection();
        User user = new User(username, password);
        Request req = new Request.Builder().type(RequestType.LOGIN).data(user).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            this.client = client;
            return (int) response.data();
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new Exception(err);
        }
        return 0;
    }

    @Override
    public void logout(int userID) throws Exception {
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(userID).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new Exception(err);
        }
    }

    @Override
    public synchronized Iterable<Flight> findAllAvailableFlights() {
        try {
            Request request = new Request.Builder().type(RequestType.FIND_ALL_AVAILABLE_FLIGHTS).build();
            sendRequest(request);
            Response response = readResponse();
            if (response.type() == ResponseType.GET_AVAILABLE_FLIGHTS) {
                return (Iterable<Flight>) response.data();
            } else {
                throw new Exception("Error retrieving available flights");
            }
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public List<String> findAllFlightDestinations() throws Exception {
        //initializeConnection();
        try {
            Request request = new Request.Builder().type(RequestType.FIND_ALL_FLIGHT_DESTINATIONS).build();
            sendRequest(request);
            Response response = readResponse();
            if (response.type() == ResponseType.GET_ALL_FLIGHT_DESTINATIONS) {
                return (List<String>) response.data();
            } else {
                throw new Exception("Error retrieving flight destinations");
            }
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public synchronized Iterable<Flight> findAllFlightsByDestinationAndDate(String destination, LocalDate date) {
        try {
            Object[] requestData = {destination, date.toString()};
            Request request = new Request.Builder().type(RequestType.FIND_ALL_FLIGHTS_BY_DESTINATION_AND_DATE).data(requestData).build();
            sendRequest(request);
            Response response = readResponse();
            if (response.type() == ResponseType.GET_FLIGHTS_BY_DESTINATION_AND_DATE) {
                return (Iterable<Flight>) response.data();
            } else {
                throw new Exception("Error retrieving flights by destination and date");
            }
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public synchronized Iterable<Flight> findAll() {
        try {
            Request request = new Request.Builder().type(RequestType.FIND_ALL_FLIGHTS).build();
            sendRequest(request);
            Response response = readResponse();
            if (response.type() == ResponseType.GET_ALL_FLIGHTS) {
                return (Iterable<Flight>) response.data();
            } else {
                throw new Exception("Error retrieving all flights");
            }
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public int findUser(String username, String password, IObserver client) {
        return 0;
    }

    @Override
    public synchronized int findAvailableSeats(int flightID) {
        try {
            Request request = new Request.Builder().type(RequestType.FIND_AVAILABLE_SEATS).data(flightID).build();
            sendRequest(request);
            Response response = readResponse();
            if (response.type() == ResponseType.GET_AVAILABLE_SEATS) {
                return (int) response.data();
            } else {
                throw new Exception("Error retrieving available seats");
            }
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public synchronized void saveTourist(String touristName) {
        try {
            Request request = new Request.Builder().type(RequestType.SAVE_TOURIST).data(touristName).build();
            sendRequest(request);
            Response response = readResponse();
            if (response.type() != ResponseType.OK) {
                throw new Exception("Error saving tourist: " + response.data());
            }
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void saveTourist(String touristName, int purchaseID) {
        try {
            Object[] data = new Object[] { touristName, purchaseID };
            Request request = new Request.Builder().type(RequestType.SAVE_TOURIST).data(data).build();
            sendRequest(request);
            Response response = readResponse();
            if (response.type() != ResponseType.OK) {
                throw new Exception("Error saving tourist: " + response.data());
            }
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void saveTrip(int touristID, int purchaseID) {
        try {
            Object[] data = new Object[] { touristID, purchaseID };
            Request request = new Request.Builder().type(RequestType.SAVE_TRIP).data(data).build();
            sendRequest(request);
            Response response = readResponse();
            if (response.type() != ResponseType.OK) {
                throw new Exception("Error saving trip: " + response.data());
            }
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
        }
    }

    @Override
    public synchronized int savePurchase(String clientName, String clientAddress, int userID, int flightID) {
        try {
            Object[] data = new Object[] { clientName, clientAddress, userID, flightID};
            Request request = new Request.Builder().type(RequestType.SAVE_PURCHASE).data(data).build();
            sendRequest(request);
            Response response = readResponse();
            if (response.type() != ResponseType.OK) {
                throw new Exception("Error saving purchase: " + response.data());
            }
            return (int) response.data();
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public synchronized void updateFlightSeats(int noSeats, int flightID) {
        try {
            Object[] data = new Object[] { noSeats, flightID };
            Request request = new Request.Builder().type(RequestType.UPDATE_FLIGHT_SEATS).data(data).build();
            sendRequest(request);
            Response response = readResponse();
            if (response.type() != ResponseType.OK) {
                throw new Exception("Error updating flight seats: " + response.data());
            }
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
        }
    }


    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request) throws Exception {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new Exception("Error sending object " + e);
        }

    }

    private Response readResponse() throws Exception {
        Response response = null;
        try {
            response = qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws Exception {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(Response response) {
        if (response.type() == ResponseType.UPDATE) {
            Object[] data = (Object[]) response.data();
            int noSeats = (int) data[0];
            int flightID = (int) data[1];
            client.updateFlight(noSeats,flightID);
        }
        // Add more update handling logic here for other response types if needed
    }

    private boolean isUpdate(Response response) {
        return response.type() == ResponseType.UPDATE;
        // Add more update detection logic here for other response types if needed
    }


    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (isUpdate((Response) response)) {
                        handleUpdate((Response) response);
                    } else {
                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
