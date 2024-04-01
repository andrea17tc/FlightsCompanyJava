package ro.mpp2024.service;
import ro.mpp2024.repository.*;
import ro.mpp2024.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Service {
    FlightRepository flightRepository;
    PurchaseRepository purchaseRepository;
    TouristRepository touristRepository;
    TripRepository tripRepository;
    UserRepository userRepository;
    int userID;
    int flightID=0;
    int touristID=0;

    public Service(FlightRepository flightRepository, PurchaseRepository purchaseRepository, TouristRepository touristRepository, TripRepository tripRepository, UserRepository userRepository) {
        this.flightRepository = flightRepository;
        this.purchaseRepository = purchaseRepository;
        this.touristRepository = touristRepository;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }

    public void setTouristID(int touristID) {
        this.touristID = touristID;
    }


    public Iterable<Flight> findAllAvailableFlights(){
        return flightRepository.findAllAvailableFlights();
    }

    public List<String> findAllFlightDestinations(){
        List<String> destinations = new ArrayList<>();
        for (Flight flight: findAllAvailableFlights()) {
            if(!destinations.contains(flight.getDestination()))
                destinations.add(flight.getDestination());
        }
        return destinations;
    }

    public Iterable<Flight> findAllFlightsByDestinationAndDate(String destination, LocalDate date){
        return flightRepository.findAllFlightsByDestinationAndDate(destination,date);
    }

    public Iterable<Flight> findAll(){
        return flightRepository.findAll();
    }

    public boolean findUser(String username, String password){
        User user = userRepository.findUserByUsername(username).get();
        if(userRepository.findUserByUsername(username).isEmpty())
            return false;
        if(Objects.equals(user.getPassword(), password)){
            userID=user.getId();
            return true;
        }
        return false;
    }

    public int findAvailableSeats(int flightID){
        Flight flight = flightRepository.findOne(flightID).get();
        return flight.getNoTotalSeats();
    }

    public void saveTourist(String touristName, int purchaseID){
        saveTourist(touristName);
        saveTrip(touristID,purchaseID);
    }

    private void saveTourist(String touristName){
        Tourist tourist = new Tourist(touristName);
        if(touristRepository.findTouristByName(touristName).isEmpty()) {
            touristRepository.save(tourist);
            touristID = touristRepository.findTouristByName(touristName).get().getId();
        }
        else
            touristID = touristRepository.findTouristByName(touristName).get().getId();
    }

    public void saveTrip(int touristID, int purchaseID) {
        try{
            Tourist t= touristRepository.findOne(touristID).get();
            Purchase p = purchaseRepository.findOne(purchaseID).get();
            Trip trip = new Trip(t,p);
            tripRepository.save(trip);
        }
        catch(Exception e){
            System.err.println("Error saving trip" + e);
        }
    }

    public void updateFlightSeats(int noSeats){
        Flight flight = flightRepository.findOne(flightID).get();
        int nr = flight.getNoTotalSeats()-noSeats;
        flightRepository.updateSeats(flightID,nr);
    }


}
