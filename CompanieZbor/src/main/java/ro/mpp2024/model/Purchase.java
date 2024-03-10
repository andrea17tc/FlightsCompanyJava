package ro.mpp2024.model;

import java.util.Objects;

public class Purchase {

    private int purchaseID;

    private int flightID;

    private int userID;

    private String clientName;

    private String clientAddress;

    private int noSeats;

    public Purchase(int purchaseID, int flightID, int userID, String clientName, String clientAddress, int noSeats) {
        this.purchaseID = purchaseID;
        this.flightID = flightID;
        this.userID = userID;
        this.clientName = clientName;
        this.clientAddress = clientAddress;
        this.noSeats = noSeats;
    }

    public int getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(int purchaseID) {
        this.purchaseID = purchaseID;
    }

    public int getFlightID() {
        return flightID;
    }

    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
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
        Purchase purchase = (Purchase) o;
        return purchaseID == purchase.purchaseID && flightID == purchase.flightID && userID == purchase.userID && noSeats == purchase.noSeats && Objects.equals(clientName, purchase.clientName) && Objects.equals(clientAddress, purchase.clientAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(purchaseID, flightID, userID, clientName, clientAddress, noSeats);
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "purchaseID=" + purchaseID +
                ", flightID=" + flightID +
                ", userID=" + userID +
                ", clientName='" + clientName + '\'' +
                ", clientAddress='" + clientAddress + '\'' +
                ", noSeats=" + noSeats +
                '}';
    }
}
