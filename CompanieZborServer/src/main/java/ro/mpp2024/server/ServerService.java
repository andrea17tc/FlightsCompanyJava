package ro.mpp2024.server;

import ro.mpp2024.model.*;
import ro.mpp2024.service.IObserver;
import ro.mpp2024.service.IService;
import ro.mpp2024.repository.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


public class ServerService implements IService {

    private FlightRepository flightRepository;
    private PurchaseRepository purchaseRepository;
    private TouristRepository touristRepository;
    private TripRepository tripRepository;
    private UserRepository userRepository;

    private Map<String, IObserver> loggedClients;

    private int touristID;

    public ServerService(FlightRepository flightRepository, PurchaseRepository purchaseRepository, TouristRepository touristRepository, TripRepository tripRepository, UserRepository userRepository) {
        this.flightRepository = flightRepository;
        this.purchaseRepository = purchaseRepository;
        this.touristRepository = touristRepository;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized Iterable<Flight> findAllAvailableFlights() {
        return flightRepository.findAllAvailableFlights();
    }

    @Override
    public List<String> findAllFlightDestinations() {
        List<String> destinations = new ArrayList<>();
        for (Flight flight : findAllAvailableFlights()) {
            if (!destinations.contains(flight.getDestination()))
                destinations.add(flight.getDestination());
        }
        return destinations;
    }

    @Override
    public synchronized Iterable<Flight> findAllFlightsByDestinationAndDate(String destination, LocalDate date) {
        return flightRepository.findAllFlightsByDestinationAndDate(destination, date);
    }

    @Override
    public synchronized Iterable<Flight> findAll() {
        return flightRepository.findAll();
    }

    @Override
    public int getFlightID() {
        return 0;
    }

    @Override
    public void setFlightID(int flightID) {

    }

    @Override
    public void setTouristID(int touristID) {

    }

    public synchronized int login(String username, String password, IObserver searchCtrl) throws Exception {
        if (findUser(username, password, searchCtrl) != 0) {
            if (loggedClients.get(username) != null)
                throw new Exception("User already logged in.");
            loggedClients.put(username, searchCtrl);
            int id = findUser(username, password, searchCtrl);
            return id;
        } else {
            throw new Exception("Authentication failed.");
        }
    }

    @Override
    public void logout(int userID) throws Exception {
        User user = userRepository.findOne(userID).get();
        if (loggedClients.get(user.getUsername()) != null) {
            loggedClients.remove(user.getUsername());
        } else
            throw new Exception("User not logged in.");
    }

    @Override
    public synchronized int findUser(String username, String password, IObserver client) {
        User user = userRepository.findUserByUsername(username).get();
        if (userRepository.findUserByUsername(username).isEmpty())
            return 0;
        if (Objects.equals(user.getPassword(), password)) {
            return user.getId();
        }
        return 0;
    }

    @Override
    public synchronized int findAvailableSeats(int flightID) {
        return flightRepository.findOne(flightID).get().getNoTotalSeats();
    }

    @Override
    public synchronized void saveTourist(String touristName) {
        try {
            Tourist tourist = new Tourist(touristName);
            if (touristRepository.findTouristByName(touristName).isEmpty()) {
                touristRepository.save(tourist);
                touristID = touristRepository.findTouristByName(touristName).get().getId();
            } else
                touristID = touristRepository.findTouristByName(touristName).get().getId();
        } catch (Exception e) {
            System.err.println("Error saving tourist" + e);
        }
    }

    @Override
    public void saveTourist(String touristName, int purchaseID) {
        saveTourist(touristName);
        saveTrip(touristID, purchaseID);
    }

    @Override
    public synchronized void saveTrip(int touristID, int purchaseID) {
        try {
            Tourist t = touristRepository.findOne(touristID).get();
            Purchase p = purchaseRepository.findOne(purchaseID).get();
            Trip trip = new Trip(t, p);
            tripRepository.save(trip);
        } catch (Exception e) {
            System.err.println("Error saving trip" + e);
        }
    }

    @Override
    public synchronized int savePurchase(String clientName, String clientAddress, int userID, int flightID) {
        try {
            saveTourist(clientName);
            int touristID = touristRepository.findTouristByName(clientName).get().getId();
            Flight flight = flightRepository.findOne(flightID).get();
            User user = userRepository.findOne(userID).get();
            Tourist tourist = touristRepository.findOne(touristID).get();
            Purchase purchase = new Purchase(flight, user, tourist, clientAddress);
            purchaseRepository.save(purchase);
            int purchaseID = purchaseRepository.findByClientAndFlight(flightID, touristID).get().getId();
            return purchaseID;
        } catch (Exception e) {
            System.err.println("Error saving purchase" + e);
            return 0;
        }
    }

    @Override
    public synchronized void updateFlightSeats(int noSeats, int flightID) {
        try {
            Flight flight = flightRepository.findOne(flightID).get();
            int nr = flight.getNoTotalSeats() - noSeats;
            flightRepository.updateSeats(flightID, nr);
            notifyAll(noSeats, flightID);
        } catch (Exception e) {
            System.err.println("Error updating flight seats" + e);
        }
    }

    public void notifyAll(int noSeats, int flightID) {
        for (IObserver client : loggedClients.values()) {
            if (client != null)
                new Thread(() -> {
                    try {
                        client.updateFlight(noSeats, flightID);
                    } catch (Exception e) {
                        System.err.println("Error notifying client" + e);
                    }
                }).start();
        }
    }

}
