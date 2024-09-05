package ro.mpp2024.proto;

import ro.mpp2024.model.*;
import ro.mpp2024.proto.CompanieZbor;
import ro.mpp2024.proto.ProtoUtils;
import ro.mpp2024.service.*;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ServiceRpcProxyProto implements IService {
    private String host;
    private int port;
    private IObserver client;
    private InputStream input;
    private OutputStream output;
    private Socket connection;
    private BlockingQueue<CompanieZbor.Response> qresponses;
    private volatile boolean finished;

    private int flightID;

    public ServiceRpcProxyProto(String host, int port) {
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
        sendRequest(ProtoUtils.createLoginRequest(user));
        CompanieZbor.Response response = readResponse();
        if (response.getType() == CompanieZbor.Response.ResponseType.OK) {
            this.client = client;
            return response.getId();
        }
        if (response.getType() == CompanieZbor.Response.ResponseType.ERROR) {
            String err = response.getString();
            closeConnection();
            throw new Exception(err);
        }
        return 0;
    }

    @Override
    public void logout(int userID) throws Exception {
        sendRequest(ProtoUtils.createLogoutRequest(userID));
        CompanieZbor.Response response = readResponse();
        closeConnection();
        if (response.getType() == CompanieZbor.Response.ResponseType.ERROR) {
            String err = response.getString();
            throw new Exception(err);
        }
    }

    @Override
    public synchronized Iterable<Flight> findAllAvailableFlights() {
        try {
            sendRequest(ProtoUtils.createFindAllAvailableFlightsRequest());
            CompanieZbor.Response response = readResponse();
            if (response.getType() == CompanieZbor.Response.ResponseType.GET_AVAILABLE_FLIGHTS) {
                return ProtoUtils.createFlightList(response);
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
            sendRequest(ProtoUtils.createFindAllFlightDestinationsRequest());
            CompanieZbor.Response response = readResponse();
            if (response.getType() == CompanieZbor.Response.ResponseType.GET_ALL_FLIGHT_DESTINATIONS) {
                return response.getStringsList();
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
            sendRequest(ProtoUtils.createFindAllFlightsByDestinationAndDateRequest(destination, date.toString()));
            CompanieZbor.Response response = readResponse();
            if (response.getType() == CompanieZbor.Response.ResponseType.GET_FLIGHTS_BY_DESTINATION_AND_DATE) {
                return ProtoUtils.createFlightList(response);
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
            sendRequest(ProtoUtils.createFindAllFlightsRequest());
            CompanieZbor.Response response = readResponse();
            if (response.getType() == CompanieZbor.Response.ResponseType.GET_ALL_FLIGHTS) {
                return ProtoUtils.createFlightList(response);
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
            sendRequest(ProtoUtils.createFindAvailableSeatsRequest(flightID));
            CompanieZbor.Response response = readResponse();
            if (response.getType() == CompanieZbor.Response.ResponseType.GET_AVAILABLE_SEATS) {
                return response.getId();
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
            sendRequest(ProtoUtils.createSaveTouristRequest(touristName));
            CompanieZbor.Response response = readResponse();
            if (response.getType() != CompanieZbor.Response.ResponseType.OK) {
                throw new Exception("Error saving tourist: " + response.getString());
            }
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void saveTourist(String touristName, int purchaseID) {
        try {
            sendRequest(ProtoUtils.createSaveTouristRequest(touristName, purchaseID));
            CompanieZbor.Response response = readResponse();
            if (response.getType() != CompanieZbor.Response.ResponseType.OK) {
                throw new Exception("Error saving tourist: " + response.getString());
            }
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void saveTrip(int touristID, int purchaseID) {
        try {
            sendRequest(ProtoUtils.createSaveTripRequest(touristID, purchaseID));
            CompanieZbor.Response response = readResponse();
            if (response.getType() != CompanieZbor.Response.ResponseType.OK) {
                throw new Exception("Error saving trip: " + response.getString());
            }
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
        }
    }

    @Override
    public synchronized int savePurchase(String clientName, String clientAddress, int userID, int flightID) {
        try {
            sendRequest(ProtoUtils.createSavePurchaseRequest(clientName, clientAddress, userID, flightID));
            CompanieZbor.Response response = readResponse();
            if (response.getType() != CompanieZbor.Response.ResponseType.OK) {
                throw new Exception("Error saving purchase: " + response.getString());
            }
            return response.getId();
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public synchronized void updateFlightSeats(int noSeats, int flightID) {
        try {
            sendRequest(ProtoUtils.createUpdateFlightSeatsRequest(noSeats, flightID));
            CompanieZbor.Response response = readResponse();
            if (response.getType() != CompanieZbor.Response.ResponseType.OK) {
                throw new Exception("Error updating flight seats: " + response.getString());
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

    private void sendRequest(CompanieZbor.Request request) throws Exception {
        try {
            //output.writeObject(request);
            request.writeDelimitedTo(output);
            output.flush();
        } catch (IOException e) {
            throw new Exception("Error sending object " + e);
        }

    }

    private CompanieZbor.Response readResponse() throws Exception {
        CompanieZbor.Response response = null;
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
            output = connection.getOutputStream();
            //output.flush();
            input = connection.getInputStream();
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

    private void handleUpdate(CompanieZbor.Response response) {
        if (response.getType() == CompanieZbor.Response.ResponseType.UPDATE) {
            int noSeats = response.getIntTuplu().getFirst();
            int flightID = response.getIntTuplu().getSecond();
            client.updateFlight(noSeats,flightID);
        }
        // Add more update handling logic here for other response types if needed
    }

    private boolean isUpdate(CompanieZbor.Response response) {
        return response.getType() == CompanieZbor.Response.ResponseType.UPDATE;
        // Add more update detection logic here for other response types if needed
    }


    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    CompanieZbor.Response response = CompanieZbor.Response.parseDelimitedFrom(input);
                    System.out.println("response received " + response);
                    if (isUpdate((CompanieZbor.Response) response)) {
                        handleUpdate((CompanieZbor.Response) response);
                    } else {
                        try {
                            qresponses.put((CompanieZbor.Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
