package ro.mpp2024.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.model.Flight;
import ro.mpp2024.model.Purchase;
import ro.mpp2024.model.Tourist;
import ro.mpp2024.model.User;
import ro.mpp2024.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class PurchaseRepository implements Repository<Integer,Purchase>{

    private FlightRepository flightRepository;
    private UserRepository userRepository;
    private TouristRepository touristRepository;
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public PurchaseRepository(Properties props, FlightRepository flightRepository, UserRepository userRepository,
                              TouristRepository touristRepository) {
        logger.info("Initializing PurchaseRepository with properties: {}", props);
        dbUtils = new JdbcUtils(props);
        this.flightRepository=flightRepository;
        this.userRepository=userRepository;
        this.touristRepository = touristRepository;

    }
    public Optional<Purchase> findOne(Integer integer){
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement("SELECT * FROM purchase WHERE id=?")) {
            statement.setInt(1, integer);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int flightID = resultSet.getInt("flightID");
                int userID = resultSet.getInt("userID");
                int touristID = resultSet.getInt("touristID");
                String clientAdress = resultSet.getString("clientAdress");
                Flight f = flightRepository.findOne(flightID).get();
                User u = userRepository.findOne(userID).get();
                Tourist t = touristRepository.findOne(touristID).get();
                Purchase p = new Purchase(f,u,t,clientAdress);
                p.setId(integer);
                logger.trace("Found {} instances", p);
                return Optional.of(p);
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return Optional.empty();
    }


    public Iterable<Purchase> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Purchase> purchases = new ArrayList<>();
        try (PreparedStatement statement = con.prepareStatement("SELECT * FROM tourist");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int flightID = resultSet.getInt("flightID");
                int userID = resultSet.getInt("userID");
                int touristID = resultSet.getInt("touristID");
                String clientAdress = resultSet.getString("clientAdress");
                Flight f = flightRepository.findOne(flightID).get();
                User u = userRepository.findOne(userID).get();
                Tourist t = touristRepository.findOne(touristID).get();
                Purchase p = new Purchase(f,u,t,clientAdress);
                p.setId(id);
                purchases.add(p);

            }
            logger.trace("Found all {} instances", purchases.size());
            return purchases;
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return null;
    }


    public Optional<Purchase> save(Purchase entity) {
        logger.traceEntry("Saving purchase {}", entity);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into purchase(flightID,userID,touristID,clientAdress) values (?,?,?,?)")) {
            preparedStatement.setInt(1, entity.getFlight().getId());
            preparedStatement.setInt(2, entity.getUser().getId());
            preparedStatement.setInt(3, entity.getTourist().getId());
            preparedStatement.setString(4, entity.getClientAdress());
            int result = preparedStatement.executeUpdate();
            logger.trace("saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return null;
    }


    public Optional<Purchase> delete(Integer integer) {
        logger.traceEntry("Deleting purchase with id {}", integer);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("delete from purchase where id=?")) {
            preparedStatement.setInt(1, integer);
            int result = preparedStatement.executeUpdate();
            logger.trace("deleted {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return null;
    }


    public Optional<Purchase> update(Integer integer, Purchase entity) {
        logger.traceEntry("Updating purchase {}", entity);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("update purchase set clientAdress=? where id=?")) {
            preparedStatement.setString(1, entity.getClientAdress());
            preparedStatement.setInt(2, integer);
            int result = preparedStatement.executeUpdate();
            logger.trace("updated {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return null;
    }
}
