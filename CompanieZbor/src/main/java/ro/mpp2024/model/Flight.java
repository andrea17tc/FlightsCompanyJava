package ro.mpp2024.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

public class Flight extends Entity<Integer> {

    private String destination;

    private LocalDate date;

    private String airport;

    private int noTotalSeats;

    public Flight(String destination, LocalDate date, String airport, int noSeats) {
        this.destination = destination;
        this.date = date;
        this.airport = airport;
        this.noTotalSeats = noSeats;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public int getNoTotalSeats() {
        return noTotalSeats;
    }

    public void setNoTotalSeats(int noTotalSeats) {
        this.noTotalSeats = noTotalSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return noTotalSeats == flight.noTotalSeats && Objects.equals(destination, flight.destination) && Objects.equals(date, flight.date) && Objects.equals(airport, flight.airport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destination, date, airport, noTotalSeats);
    }

    @Override
    public String toString() {
        return "Flight to "  + destination +" on " + date + " from " + airport + ". Seats available: "+ noTotalSeats;
    }
}
