import org.junit.jupiter.api.Test;
import ro.mpp2024.repository.*;
import ro.mpp2024.service.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ServiceTests {
    FlightRepository flightRepository;
    UserRepository userRepository;
    PurchaseRepository purchaseRepository;
    TouristRepository touristRepository;
    TripRepository tripRepository;
    Service service;

    private static Properties props= new Properties();
    @Test
    public void testAdd() throws IOException {
        props.load(new FileReader("bd.config"));
        FlightRepository flightRepository = new FlightRepository(props);
        UserRepository userRepository= new UserRepository(props);
        TouristRepository touristRepository= new TouristRepository(props);
        PurchaseRepository purchaseRepository= new PurchaseRepository(props,flightRepository, userRepository,touristRepository);
        TripRepository tripRepository = new TripRepository(props, touristRepository,purchaseRepository);
        Service service = new Service(flightRepository, purchaseRepository, touristRepository, tripRepository, userRepository);
        service.setFlightID(1);
        int availableSeats = service.findAvailableSeats(service.getFlightID());
        assert(availableSeats == 150);
    }
}
