package ro.mpp2024.model;

import java.util.Objects;

public class Purchase extends Entity<Integer> {

    private Flight flight;

    private User user;

    private Tourist tourist;

    private String clientAdress;


    public Purchase(Flight flight, User user, Tourist tourist, String clientAdress) {
        this.flight = flight;
        this.user = user;
        this.tourist = tourist;
        this.clientAdress = clientAdress;
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

    public String getClientAdress() {
        return clientAdress;
    }

    public void setClientAdress(String clientAdress) {
        this.clientAdress = clientAdress;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Purchase purchase = (Purchase) o;
        return  Objects.equals(flight, purchase.flight) && Objects.equals(user, purchase.user) && Objects.equals(tourist, purchase.tourist) && Objects.equals(clientAdress, purchase.clientAdress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), flight, user, tourist, clientAdress);
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "flight=" + flight +
                ", user=" + user +
                ", tourist=" + tourist +
                ", clientAdress='" + clientAdress + '\'' +
                '}';
    }
}
