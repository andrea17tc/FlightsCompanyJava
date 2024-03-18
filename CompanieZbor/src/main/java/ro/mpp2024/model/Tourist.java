package ro.mpp2024.model;

import java.util.Objects;

public class Tourist extends Entity<Integer> {
    private String touristName;

    public Tourist(String name) {
        this.touristName = name;
    }


    public String getTouristName() {
        return touristName;
    }

    public void setTouristName(String touristName) {
        this.touristName = touristName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tourist tourist = (Tourist) o;
        return Objects.equals(touristName, tourist.touristName);
    }

    @Override
    public int hashCode() {
        return Objects.hash( touristName);
    }

    @Override
    public String toString() {
        return "Tourist{" +
                ", name='" + touristName + '\'' +
                '}';
    }
}
