package ro.mpp2024.model;

import java.util.Objects;

public class Trip extends Entity<Integer> {
    private Tourist tourist;
    private Purchase purchase;


    public Trip(Tourist tourist, Purchase purchase) {
        this.tourist = tourist;
        this.purchase = purchase;
    }


    public Tourist getTourist() {
        return tourist;
    }

    public void setTourist(Tourist tourist) {
        this.tourist = tourist;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return tourist == trip.tourist && purchase == trip.purchase;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tourist, purchase);
    }

    @Override
    public String toString() {
        return "Trip{" +
                "touristID=" + tourist +
                ", purchaseID=" + purchase +
                '}';
    }
}
