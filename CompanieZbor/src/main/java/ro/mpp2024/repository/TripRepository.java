package ro.mpp2024.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

public class TripRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public TripRepository(Properties props) {
        logger.info("Initializing TripRepository with properties: {}", props);
        dbUtils = new JdbcUtils(props);
    }

    public List<String> findOneTrip(Integer integer) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<String> datas = new ArrayList<>();
        try (PreparedStatement statement = con.prepareStatement("SELECT * FROM trip WHERE id=?")) {
            statement.setInt(1, integer);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int touristID = resultSet.getInt("touristID");
                int purchaseID = resultSet.getInt("purchaseID");
                datas.add(String.valueOf(touristID));
                datas.add(String.valueOf(purchaseID));

                logger.trace("Found {} instances", datas);
                return datas;
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return datas;
    }
    public List<List<String>> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<String> datas = new ArrayList<>();
        List<List<String>> allDatas = new ArrayList<>();
        try (PreparedStatement statement = con.prepareStatement("SELECT * FROM trip");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int touristID = resultSet.getInt("touristID");
                int purchaseID = resultSet.getInt("purchaseID");
                datas.clear();
                datas.add(String.valueOf(touristID));
                datas.add(String.valueOf(purchaseID));
                allDatas.add(datas);

            }
            logger.trace("Found all {} instances", allDatas.size());
            return allDatas;
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return allDatas;
    }
}
