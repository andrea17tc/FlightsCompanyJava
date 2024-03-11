package ro.mpp2024.model;

import java.util.Objects;

public class Tourist {
    private int touristID;
    private String touristName;

    public Tourist(int touristID, String name) {
        this.touristID = touristID;
        this.touristName = name;
    }

    public int getTouristID() {
        return touristID;
    }

    public void setTouristID(int touristID) {
        this.touristID = touristID;
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
        return touristID == tourist.touristID && Objects.equals(touristName, tourist.touristName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(touristID, touristName);
    }

    @Override
    public String toString() {
        return "Tourist{" +
                "touristID=" + touristID +
                ", name='" + touristName + '\'' +
                '}';
    }
}
