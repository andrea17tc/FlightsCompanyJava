package ro.mpp2024.model;

import java.util.Objects;

public class Purchase extends Entity<Integer> {

    private Flight flight;

    private User user;

    private Tourist tourist;

    private String clientAddress;

    private int noBookedSeats;

    public Purchase(Flight flight, User user, Tourist tourist, String clientAddress, int noBookedSeats) {
        this.flight = flight;
        this.user = user;
        this.tourist = tourist;
        this.clientAddress = clientAddress;
        this.noBookedSeats = noBookedSeats;
    }


    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tourist getTourist() {
        return tourist;
    }

    public void setTourist(Tourist tourist) {
        this.tourist = tourist;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public int getNoBookedSeats() {
        return noBookedSeats;
    }

    public void setNoBookedSeats(int noBookedSeats) {
        this.noBookedSeats = noBookedSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Purchase purchase = (Purchase) o;
        return noBookedSeats == purchase.noBookedSeats && Objects.equals(flight, purchase.flight) && Objects.equals(user, purchase.user) && Objects.equals(tourist, purchase.tourist) && Objects.equals(clientAddress, purchase.clientAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), flight, user, tourist, clientAddress, noBookedSeats);
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "flight=" + flight +
                ", user=" + user +
                ", tourist=" + tourist +
                ", clientAddress='" + clientAddress + '\'' +
                ", noBookedSeats=" + noBookedSeats +
                '}';
    }
}
