package ro.mpp2024.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.model.Purchase;
import ro.mpp2024.model.Tourist;
import ro.mpp2024.model.Trip;
import ro.mpp2024.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class TripRepository implements Repository<Integer, Trip> {

    private TouristRepository touristRepository;
    private PurchaseRepository purchaseRepository;
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public TripRepository(Properties props, TouristRepository touristRepository, PurchaseRepository purchaseRepository) {
        logger.info("Initializing TripRepository with properties: {}", props);
        dbUtils = new JdbcUtils(props);
        this.touristRepository = touristRepository;
        this.purchaseRepository = purchaseRepository;
    }

    public Optional<Trip> findOne(Integer integer) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<String> datas = new ArrayList<>();
        try (PreparedStatement statement = con.prepareStatement("SELECT * FROM trip WHERE id=?")) {
            statement.setInt(1, integer);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int touristID = resultSet.getInt("touristID");
                int purchaseID = resultSet.getInt("purchaseID");
                Tourist t = touristRepository.findOne(touristID).get();
                Purchase p = purchaseRepository.findOne(purchaseID).get();
                Trip trip = new Trip(t,p);
                trip.setId(integer);
                logger.trace("Found {} instances",trip );
                return Optional.of(trip);
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return Optional.empty();
    }
    public Iterable<Trip> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Trip> trips = new ArrayList<>();
        try (PreparedStatement statement = con.prepareStatement("SELECT * FROM trip");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int touristID = resultSet.getInt("touristID");
                int purchaseID = resultSet.getInt("purchaseID");
                Tourist t = touristRepository.findOne(touristID).get();
                Purchase p = purchaseRepository.findOne(purchaseID).get();
                Trip trip = new Trip(t,p);
                trip.setId(id);
                trips.add(trip);

            }
            logger.trace("Found all {} instances", trips.size());
            return trips;
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return trips;
    }

    @Override
    public Optional<Trip> save(Trip entity) {
        logger.traceEntry("Saving trip {}", entity);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into trip(touristID,purchaseID) values (?,?)")) {
            preparedStatement.setInt(1, entity.getTourist().getId());
            preparedStatement.setInt(2, entity.getPurchase().getId());
            int result = preparedStatement.executeUpdate();
            logger.trace("saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return Optional.of(entity);
    }

    @Override
    public Optional<Trip> delete(Integer integer) {
        logger.traceEntry("Deleting trip with id {}", integer);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("delete from trip where id=?")) {
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

    @Override
    public Optional<Trip> update(Integer integer, Trip entity) {
        logger.traceEntry("Updating trip {}", entity);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("update trip set touristID=? where id=?")) {
            preparedStatement.setInt(1, entity.getTourist().getId());
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

