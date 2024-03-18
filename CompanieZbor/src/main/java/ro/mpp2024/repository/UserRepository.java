package ro.mpp2024.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

public class UserRepository implements Repository<Integer, User> {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public UserRepository(Properties props) {
        logger.info("Initializing UserRepository with properties: {}", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<User> findOne(Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement("SELECT * FROM user WHERE id=?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int userid = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                User user = new User(username, password);
                user.setId(id);
                logger.trace("Found {} instances", user);
                return Optional.ofNullable(user);
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return Optional.empty();
    }

    @Override
    public Iterable<User> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = con.prepareStatement("SELECT * FROM user");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int userid = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                User user = new User(username, password);
                user.setId(userid);
                users.add(user);
            }
            logger.trace("Found all {} instances", users.size());
            return users;
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit(users);
        return users;
    }

    @Override
    public Optional<User> save(User entity) {
        logger.traceEntry("Saving user {}", entity);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into user(username,password) values (?,?)")) {
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getPassword());
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
    public Optional<User> delete(Integer id) {
        logger.traceEntry("Deleting user with id {}", id);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("delete from user where id=?")) {
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
    public Optional<User> update(Integer id, User entity) {
        logger.traceEntry("Updating user {}", entity);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("update user set password=? where id=?")) {
            preparedStatement.setString(1, entity.getPassword());
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
