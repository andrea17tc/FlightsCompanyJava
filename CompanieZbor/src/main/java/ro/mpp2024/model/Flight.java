package ro.mpp2024.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Flight {
    private int flightID;

    private String destination;

    private LocalDateTime dateTime;

    private String airport;

    private int noSeats;

    public Flight(int flightID, String destination, LocalDateTime dateTime, String airport, int noSeats) {
        this.flightID = flightID;
        this.destination = destination;
        this.dateTime = dateTime;
        this.airport = airport;
        this.noSeats = noSeats;
    }

    public int getFlightID() {
        return flightID;
    }

    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public int getNoSeats() {
        return noSeats;
    }

    public void setNoSeats(int noSeats) {
        this.noSeats = noSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return flightID == flight.flightID && noSeats == flight.noSeats && Objects.equals(destination, flight.destination) && Objects.equals(dateTime, flight.dateTime) && Objects.equals(airport, flight.airport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightID, destination, dateTime, airport, noSeats);
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightID=" + flightID +
                ", destination='" + destination + '\'' +
                ", dateTime=" + dateTime +
                ", airport='" + airport + '\'' +
                ", noSeats=" + noSeats +
                '}';
    }
}
