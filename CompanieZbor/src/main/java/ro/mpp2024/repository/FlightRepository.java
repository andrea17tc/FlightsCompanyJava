package ro.mpp2024.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.utils.JdbcUtils;
import ro.mpp2024.model.Flight;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class FlightRepository implements Repository<Integer, Flight>{
    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public FlightRepository(Properties props){
        logger.info("Initializing FlightRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Optional<Flight> findOne(Integer integer) {
        logger.traceEntry();
        Connection con =dbUtils.getConnection();
        try(PreparedStatement statement = con.prepareStatement("select * from flight where id=?;");)
        {
            statement.setInt(1,integer);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                String destination = resultSet.getString("destination");
                String date = resultSet.getString("date");
                LocalDate d = LocalDate.parse(date);
                String airport = resultSet.getString("airport");
                int noSeats = resultSet.getInt("noTotalSeats");
                Flight f = new Flight(destination,d,airport,noSeats);
                f.setId(integer);
                logger.trace("found {} instances", f);
                return Optional.of(f);
            }
        }
        catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }

        logger.traceExit();
        return Optional.empty();

    }

    @Override
    public Iterable<Flight> findAll() {
        logger.traceEntry();
        Connection con =dbUtils.getConnection();
        List<Flight> flights = new ArrayList<>();
        try (PreparedStatement statement = con.prepareStatement("select * from flight");
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next())
            {
                Integer id= resultSet.getInt("id");
                String d= resultSet.getString("date");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate date = LocalDate.parse(d,formatter);
                String destination= resultSet.getString("destination");
                String airport = resultSet.getString("airport");
                int noTotalSeats =resultSet.getInt("noTotalSeats");
                Flight f = new Flight(destination,date,airport,noTotalSeats);
                f.setId(id);
                flights.add(f);

            }
            logger.trace("found all {} instances", flights);
            return flights;

        }catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);

        }
        logger.traceExit(flights);
        return flights;
    }

    public Iterable<Flight> findAllAvailableFlights(){
        logger.traceEntry();
        Connection con =dbUtils.getConnection();
        List<Flight> flights = new ArrayList<>();
        try (PreparedStatement statement = con.prepareStatement("select * from flight where noTotalSeats>0;");
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next())
            {
                Integer id= resultSet.getInt("id");
                String d= resultSet.getString("date");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(d,formatter);
                String destination= resultSet.getString("destination");
                String airport = resultSet.getString("airport");
                int noTotalSeats =resultSet.getInt("noTotalSeats");
                Flight f = new Flight(destination,date,airport,noTotalSeats);
                f.setId(id);
                flights.add(f);

            }
            logger.trace("found all {} instances", flights);
            return flights;

        }catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);

        }
        logger.traceExit(flights);
        return flights;
    }

    public Iterable<Flight> findAllFlightsByDestinationAndDate(String destination, LocalDate date){
        logger.traceEntry();
        Connection con =dbUtils.getConnection();
        List<Flight> flights = new ArrayList<>();
        try (PreparedStatement statement = con.prepareStatement("select * from flight where destination=? and date=?;");
        ) {
            statement.setString(1,destination);
            statement.setString(2,date.toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                Integer id= resultSet.getInt("id");
                String dest= resultSet.getString("destination");
                String airport = resultSet.getString("airport");
                int noTotalSeats =resultSet.getInt("noTotalSeats");
                Flight f = new Flight(dest,date,airport,noTotalSeats);
                f.setId(id);
                flights.add(f);

            }
            logger.trace("found all {} instances", flights);
            return flights;

        }catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);

        }
        logger.traceExit(flights);
        return flights;
    }

    @Override
    public Optional<Flight> save(Flight entity) {
        logger.traceEntry("saving task {}", entity);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into flight(destination, date, airport, noTotalSeats) values (?,?,?,?)")) {
            preparedStatement.setString(1, entity.getDestination());
            preparedStatement.setString(2, entity.getDate().toString());
            preparedStatement.setString(3, entity.getAirport());
            preparedStatement.setInt(4, entity.getNoTotalSeats());
            int result = preparedStatement.executeUpdate();
            logger.trace("saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return Optional.empty();
    }

    @Override
    public Optional<Flight> delete(Integer id) {
        logger.traceEntry("saving task of id {}", id);
        Connection connection= dbUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("delete from flight where id=?"))
        {
            preparedStatement.setInt(1, id);
            int result = preparedStatement.executeUpdate();
            logger.trace("deleted {} instances", result);
        }catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return Optional.empty();
    }

    @Override
    public Optional<Flight> update(Integer id, Flight entity) {
        logger.traceEntry("saving task {}", entity);
        Connection connection= dbUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("update flight set date=?, airport=? where id=?"))
        {
            preparedStatement.setString(1, (entity.getDate().toString()));
            preparedStatement.setString(2,entity.getAirport());
            preparedStatement.setInt(3, id);
            int result = preparedStatement.executeUpdate();
            logger.trace("updated {} instances", result);
        }catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return Optional.empty();
    }

    public void updateSeats(Integer id, int seats) {
        logger.traceEntry("saving task {}", seats);
        Connection connection= dbUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("update flight set noTotalSeats=? where id=?"))
        {
            preparedStatement.setInt(1, seats);
            preparedStatement.setInt(2, id);
            int result = preparedStatement.executeUpdate();
            logger.trace("updated {} instances", result);
        }catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }
}
