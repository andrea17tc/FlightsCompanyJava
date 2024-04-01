import org.junit.jupiter.api.Test;
import ro.mpp2024.model.Flight;
import ro.mpp2024.repository.FlightRepository;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Properties;

public class RepoTests {
    @Test
    public void testAdd() throws IOException {
//        Properties props= new Properties();
//        props.load(new FileReader("bd.config"));
//        FlightRepository flightRepository = new FlightRepository(props);
//        LocalDate d = LocalDate.parse("2021-05-05");
//        Flight f = new Flight("Paris", d, "Otopeni", 100);
//        flightRepository.save(f);
//        assert(flightRepository.findOne(f.getId()).get().getDate().equals(LocalDate.parse("2021-05-05")));
    }
}
