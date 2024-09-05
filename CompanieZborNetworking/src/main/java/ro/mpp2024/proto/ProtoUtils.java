package ro.mpp2024.proto;

import ro.mpp2024.model.Flight;
import ro.mpp2024.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class ProtoUtils {
    public static User createUser(CompanieZbor.Request request) {
        return new User(request.getUser().getUsername(), request.getUser().getPassword());
    }

    public static List<Flight> createFlightList(CompanieZbor.Response response) {
        List<Flight> flights = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy h:mm:ss a");
        for (CompanieZbor.Flight flight : response.getFlightsList()) {
            LocalDateTime localDateTime = LocalDateTime.parse(flight.getDate(), formatter);
            LocalDate localDate = localDateTime.toLocalDate();
            Flight f = new Flight(flight.getDestination(), localDate, flight.getAirport(), flight.getSeats());
            f.setId(flight.getId());
            flights.add(f);
        }
        return flights;
    }

    public static CompanieZbor.Response createOkResponse() {
        CompanieZbor.Response response = CompanieZbor.Response.newBuilder().setType(CompanieZbor.Response.ResponseType.OK).build();
        return response;
    }

    public static CompanieZbor.Response createOkResponse(int id) {
        CompanieZbor.Response response = CompanieZbor.Response.newBuilder().setType(CompanieZbor.Response.ResponseType.OK).setId(id).build();
        return response;
    }
    public static CompanieZbor.Response createErrorResponse(String text) {
        CompanieZbor.Response response = CompanieZbor.Response.newBuilder().setType(CompanieZbor.Response.ResponseType.ERROR).setString(text).build();
        return response;
    }

    public static CompanieZbor.Response createGetAvailableFlightsResponse(List<Flight> flights) {
        CompanieZbor.Response.Builder response = CompanieZbor.Response.newBuilder().setType(CompanieZbor.Response.ResponseType.GET_AVAILABLE_FLIGHTS);
        for (Flight flight : flights) {
            CompanieZbor.Flight.Builder flightBuilder = CompanieZbor.Flight.newBuilder();
            flightBuilder.setId(flight.getId());
            flightBuilder.setDestination(flight.getDestination());
            flightBuilder.setDate(flight.getDate().toString());
            flightBuilder.setAirport(flight.getAirport());
            flightBuilder.setSeats(flight.getNoTotalSeats());
            response.addFlights(flightBuilder);
        }
        return response.build();
    }

    public static CompanieZbor.Response createGetAllFlightDestinationsResponse(List<String> destinations) {
        CompanieZbor.Response.Builder response = CompanieZbor.Response.newBuilder().setType(CompanieZbor.Response.ResponseType.GET_ALL_FLIGHT_DESTINATIONS);
        for (String destination : destinations) {
            response.addStrings(destination);
        }
        return response.build();
    }

    public static CompanieZbor.Response createGetFlightsByDestinationAndDate(List<Flight> flights){
        CompanieZbor.Response.Builder response = CompanieZbor.Response.newBuilder().setType(CompanieZbor.Response.ResponseType.GET_FLIGHTS_BY_DESTINATION_AND_DATE);
        for (Flight flight : flights) {
            CompanieZbor.Flight.Builder flightBuilder = CompanieZbor.Flight.newBuilder();
            flightBuilder.setId(flight.getId());
            flightBuilder.setDestination(flight.getDestination());
            flightBuilder.setDate(flight.getDate().toString());
            flightBuilder.setAirport(flight.getAirport());
            flightBuilder.setSeats(flight.getNoTotalSeats());
            response.addFlights(flightBuilder);
        }
        return response.build();
    }


    public static CompanieZbor.Response createGetFlightsByDestinationAndDateResponse(List<Flight> flights) {
        CompanieZbor.Response.Builder response = CompanieZbor.Response.newBuilder().setType(CompanieZbor.Response.ResponseType.GET_FLIGHTS_BY_DESTINATION_AND_DATE);
        for (Flight flight : flights) {
            CompanieZbor.Flight.Builder flightBuilder = CompanieZbor.Flight.newBuilder();
            flightBuilder.setId(flight.getId());
            flightBuilder.setDestination(flight.getDestination());
            flightBuilder.setDate(flight.getDate().toString());
            flightBuilder.setAirport(flight.getAirport());
            flightBuilder.setSeats(flight.getNoTotalSeats());
            response.addFlights(flightBuilder);
        }
        return response.build();
    }

    public static CompanieZbor.Response createGetAllFlightsResponse(List<Flight> flights) {
        CompanieZbor.Response.Builder response = CompanieZbor.Response.newBuilder().setType(CompanieZbor.Response.ResponseType.GET_ALL_FLIGHTS);
        for (Flight flight : flights) {
            CompanieZbor.Flight.Builder flightBuilder = CompanieZbor.Flight.newBuilder();
            flightBuilder.setId(flight.getId());
            flightBuilder.setDestination(flight.getDestination());
            flightBuilder.setDate(flight.getDate().toString());
            flightBuilder.setAirport(flight.getAirport());
            flightBuilder.setSeats(flight.getNoTotalSeats());
            response.addFlights(flightBuilder);
        }
        return response.build();
    }

    public static CompanieZbor.Response createGetAvailableSeatsResponse(int seats) {
        CompanieZbor.Response response = CompanieZbor.Response.newBuilder().setType(CompanieZbor.Response.ResponseType.GET_AVAILABLE_SEATS).setId(seats).build();
        return response;
    }

    public static CompanieZbor.Response createUpdateResponse(){
        CompanieZbor.Response response = CompanieZbor.Response.newBuilder().setType(CompanieZbor.Response.ResponseType.UPDATE).build();
        return response;
    }

    public static CompanieZbor.Request createLoginRequest(User user){
        CompanieZbor.User.Builder userBuilder = CompanieZbor.User.newBuilder();
        userBuilder.setUsername(user.getUsername());
        userBuilder.setPassword(user.getPassword());
        CompanieZbor.Request request = CompanieZbor.Request.newBuilder().setType(CompanieZbor.Request.RequestType.LOGIN).setUser(userBuilder).build();
        return request;
    }

    public static CompanieZbor.Request createLogoutRequest(int userID){
        CompanieZbor.Request request = CompanieZbor.Request.newBuilder().setType(CompanieZbor.Request.RequestType.LOGOUT).setId(userID).build();
        return request;
    }

    public static CompanieZbor.Request createFindAllAvailableFlightsRequest(){
        CompanieZbor.Request request = CompanieZbor.Request.newBuilder().setType(CompanieZbor.Request.RequestType.FIND_ALL_AVAILABLE_FLIGHTS).build();
        return request;
    }

    public static CompanieZbor.Request createFindAllFlightDestinationsRequest(){
        CompanieZbor.Request request = CompanieZbor.Request.newBuilder().setType(CompanieZbor.Request.RequestType.FIND_ALL_FLIGHT_DESTINATIONS).build();
        return request;
    }

    public static CompanieZbor.Request createFindAllFlightsByDestinationAndDateRequest(String destination, String date){
        CompanieZbor.StringTuplu.Builder stringTuplu = CompanieZbor.StringTuplu.newBuilder();
        stringTuplu.setFirst(destination);
        stringTuplu.setSecond(date);
        CompanieZbor.Request request = CompanieZbor.Request.newBuilder().setType(CompanieZbor.Request.RequestType.FIND_ALL_FLIGHTS_BY_DESTINATION_AND_DATE).setStringTuplu(stringTuplu).build();
        return request;
    }

    public static CompanieZbor.Request createFindAllFlightsRequest(){
        CompanieZbor.Request request = CompanieZbor.Request.newBuilder().setType(CompanieZbor.Request.RequestType.FIND_ALL_FLIGHTS).build();
        return request;
    }

    public static CompanieZbor.Request createFindAvailableSeatsRequest(int flightID){
        CompanieZbor.Request request = CompanieZbor.Request.newBuilder().setType(CompanieZbor.Request.RequestType.FIND_AVAILABLE_SEATS).setId(flightID).build();
        return request;
    }

    public static CompanieZbor.Request createSaveTouristRequest(String name){
        CompanieZbor.Request request = CompanieZbor.Request.newBuilder().setType(CompanieZbor.Request.RequestType.SAVE_TOURIST).setString(name).build();
        return request;
    }

    public static CompanieZbor.Request createSaveTouristRequest(String name, int purchaseID){
        CompanieZbor.StringIntTuplu.Builder stringIntTuplu = CompanieZbor.StringIntTuplu.newBuilder();
        stringIntTuplu.setFirst(name);
        stringIntTuplu.setSecond(purchaseID);
        CompanieZbor.Request request = CompanieZbor.Request.newBuilder().setType(CompanieZbor.Request.RequestType.SAVE_TOURIST).setStringIntTuplu(stringIntTuplu).build();
        return request;
    }

    public static CompanieZbor.Request createSaveTripRequest(int touristID, int flightID){
        CompanieZbor.IntTuplu.Builder intTuplu = CompanieZbor.IntTuplu.newBuilder();
        intTuplu.setFirst(touristID);
        intTuplu.setSecond(flightID);
        CompanieZbor.Request request = CompanieZbor.Request.newBuilder().setType(CompanieZbor.Request.RequestType.SAVE_TRIP).setIntTuplu(intTuplu).build();
        return request;
    }

    public static CompanieZbor.Request createSavePurchaseRequest(String clientName, String clientAddress, int userID, int flightID){
        CompanieZbor.StringTuplu.Builder stringTuplu = CompanieZbor.StringTuplu.newBuilder();
        stringTuplu.setFirst(clientName);
        stringTuplu.setSecond(clientAddress);
        CompanieZbor.IntTuplu.Builder intTuplu = CompanieZbor.IntTuplu.newBuilder();
        intTuplu.setFirst(userID);
        intTuplu.setSecond(flightID);
        CompanieZbor.MixList.Builder mixList = CompanieZbor.MixList.newBuilder();
        mixList.setData(stringTuplu);
        mixList.setList(intTuplu);
        CompanieZbor.Request request = CompanieZbor.Request.newBuilder().setType(CompanieZbor.Request.RequestType.SAVE_PURCHASE).setMixList(mixList).build();
        return request;
    }

    public static CompanieZbor.Request createUpdateFlightSeatsRequest(int seats, int flightID){
        CompanieZbor.IntTuplu.Builder intTuplu = CompanieZbor.IntTuplu.newBuilder();
        intTuplu.setFirst(seats);
        intTuplu.setSecond(flightID);
        CompanieZbor.Request request = CompanieZbor.Request.newBuilder().setType(CompanieZbor.Request.RequestType.UPDATE_FLIGHT_SEATS).setIntTuplu(intTuplu).build();
        return request;
    }
}
