package ro.mpp2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mpp2024.model.Flight;
import ro.mpp2024.repository.*;

import java.util.Collection;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("service/flights-request")
public class ServiceRest {
    @Autowired
    private FlightRepository flightRepository;
    @PostMapping
    public Flight create(@RequestBody Flight flightRequest){
        System.out.println("Creating flightRequest");
        return flightRepository.save(flightRequest).get();
    }

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Flight> getAll(){
        System.out.println("Getting flightRequests");
        Collection<Flight> all = StreamSupport.stream(flightRepository.findAll().spliterator(), false).toList();
        return all;

    }

    @RequestMapping(value = "/available", method = RequestMethod.GET)
    public Collection<Flight> filterAvailable(){
        Collection<Flight> all;
        System.out.println("Getting available flights");
        all= StreamSupport.stream(flightRepository.findAllAvailableFlights().spliterator(), false).toList();
        return all;
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends Object> getById(@PathVariable Integer id){
        System.out.println("Get by id "+id);
        Flight flight =flightRepository.findOne(id).get();
        if (flight==null)
            return new ResponseEntity<String>("Entity not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Flight>(flight, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Flight flight){
        System.out.println("Updating flight "+id);
        flight.setId(id);
        Flight f = flightRepository.update(id,flight).get();
        return new ResponseEntity<Flight>(f, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        System.out.println("Deleting flight "+id);
        flightRepository.delete(id);
        return new ResponseEntity<String>("Entity deleted", HttpStatus.OK);
    }
}
