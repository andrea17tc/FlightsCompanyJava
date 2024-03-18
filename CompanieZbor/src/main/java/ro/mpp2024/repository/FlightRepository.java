package ro.mpp2024.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.utils.JdbcUtils;
import ro.mpp2024.model.Flight;

import java.sql.*;
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
        Flight f;
        Connection con =dbUtils.getConnection();
        try(PreparedStatement preparedStatement=con.prepareStatement("select * from flight where id=?;")){
            preparedStatement.setInt(1, integer);
            int result = preparedStatement.executeUpdate();
            logger.trace("saved {} instances", result);
        }catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);

        }
        logger.traceExit(f);
        return Optional.of(f);
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
                Date date=resultSet.getDate("date");
                String destination= resultSet.getString("destination");
                String aiport = resultSet.getString("airport");
                Integer noTotalSeats =resultSet.getInt("noTotalSeats");
                Flight f = new Flight(destination,date,aiport,noTotalSeats);
                f.setId(id);
                flights.add(f);

            }
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
            preparedStatement.setDate(2, entity.getDate());
            preparedStatement.setString(3, entity.getAirport());
            preparedStatement.setInt(4, entity.getNoTotalSeats());
            int result = preparedStatement.executeUpdate();
            logger.trace("saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Optional<Flight> delete(Integer id) {
        logger.traceEntry("saving task of id {}", id);
        Connection connection= dbUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("delete from flight where id=?"))
        {
            preparedStatement.setInt(1, id);
            int result = preparedStatement.executeUpdate();
            logger.trace("saved {} instances", result);
        }catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Optional<Flight> update(Integer id, Flight entity) {
        logger.traceEntry("saving task {}", entity);
        Connection connection= dbUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("update flight set date=? and airport=? where id=?"))
        {
            preparedStatement.setDate(1, entity.getDate());
            preparedStatement.setString(2,entity.getAirport());
            preparedStatement.setInt(3, id);
            int result = preparedStatement.executeUpdate();
            logger.trace("saved {} instances", result);
        }catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return null;
    }
}
