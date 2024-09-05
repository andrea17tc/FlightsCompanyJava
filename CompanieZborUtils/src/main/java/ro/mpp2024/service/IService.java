package ro.mpp2024.service;

import ro.mpp2024.model.Flight;
import ro.mpp2024.model.User;

import java.time.LocalDate;
import java.util.List;

public interface IService{
    int getFlightID();
    void setFlightID(int flightID);
    void setTouristID(int touristID);

    int login(String username, String password, IObserver client) throws Exception;
    void logout(int userID) throws Exception;
    Iterable<Flight> findAllAvailableFlights();
    List<String> findAllFlightDestinations() throws Exception;
    Iterable<Flight> findAllFlightsByDestinationAndDate(String destination, LocalDate date);
    Iterable<Flight> findAll();
    int findUser(String username, String password, IObserver client);

    int findAvailableSeats(int flightID);
    void saveTourist(String touristName);
    void saveTourist(String touristName, int purchaseID);
    void saveTrip(int touristID, int purchaseID);
    int savePurchase(String clientName, String clientAddress, int userID, int flightID);
    void updateFlightSeats(int noSeats, int flightID);

}