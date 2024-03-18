package ro.mpp2024.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

public class TouristRepository implements Repository<Integer,Tourist> {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public TouristRepository(Properties props) {
        logger.info("Initializing TouristRepository with properties: {}", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Tourist> findOne(Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement("SELECT * FROM tourist WHERE id=?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int touristid = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Tourist tourist = new Tourist(name);
                tourist.setId(touristid);
                logger.trace("Found {} instances", tourist);
                return Optional.ofNullable(tourist);
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return Optional.empty();
    }

    @Override
    public Iterable<Tourist> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Tourist> tourists = new ArrayList<>();
        try (PreparedStatement statement = con.prepareStatement("SELECT * FROM tourist");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int touristid = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Tourist tourist = new Tourist(name);
                tourist.setId(touristid);
                tourists.add(tourist);
            }
            logger.trace("Found all {} instances", tourists.size());
            return tourists;
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit(tourists);
        return tourists;
    }

    @Override
    public Optional<Tourist> save(Tourist entity) {
        logger.traceEntry("Saving tourist {}", entity);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into tourist(name) values (?)")) {
            preparedStatement.setString(1, entity.getTouristName());
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
    public Optional<Tourist> delete(Integer id) {
        logger.traceEntry("Deleting tourist with id {}", id);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("delete from tourist where id=?")) {
            preparedStatement.setInt(1, id);
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
    public Optional<Tourist> update(Integer id, Tourist entity) {
        logger.traceEntry("Updating user {}", entity);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("update tourist set name=? where id=?")) {
            preparedStatement.setString(1, entity.getTouristName());
            preparedStatement.setInt(2, id);
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
