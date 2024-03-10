package ro.mpp2024.model;

import java.util.Objects;

public class Tourist {
    private int touristID;
    private String name;

    public Tourist(int touristID, String name) {
        this.touristID = touristID;
        this.name = name;
    }

    public int getTouristID() {
        return touristID;
    }

    public void setTouristID(int touristID) {
        this.touristID = touristID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tourist tourist = (Tourist) o;
        return touristID == tourist.touristID && Objects.equals(name, tourist.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(touristID, name);
    }

    @Override
    public String toString() {
        return "Tourist{" +
                "touristID=" + touristID +
                ", name='" + name + '\'' +
                '}';
    }
}
