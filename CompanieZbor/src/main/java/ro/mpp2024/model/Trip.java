package ro.mpp2024.model;

import java.util.Objects;

public class Trip {
    private int touristID;
    private int purchaseID;


    public Trip(int touristID, int purchaseID) {
        this.touristID = touristID;
        this.purchaseID = purchaseID;
    }


    public int getTouristID() {
        return touristID;
    }

    public void setTouristID(int touristID) {
        this.touristID = touristID;
    }

    public int getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(int purchaseID) {
        this.purchaseID = purchaseID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return touristID == trip.touristID && purchaseID == trip.purchaseID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(touristID, purchaseID);
    }

    @Override
    public String toString() {
        return "Trip{" +
                "touristID=" + touristID +
                ", purchaseID=" + purchaseID +
                '}';
    }
}
