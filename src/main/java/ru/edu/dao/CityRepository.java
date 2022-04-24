package ru.edu.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.edu.service.CityInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Component
public class CityRepository {

    private Connection connection;

    @Autowired
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Get all.
     */
    public List<CityInfo> getAll() {

        List<CityInfo> cities = new ArrayList<>();
        String sql = "SELECT * FROM cities";

        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    String climate = resultSet.getString("climate");
                    int population = resultSet.getInt("population");
                    double latitude = resultSet.getDouble("latitude");
                    double longitude = resultSet.getDouble("longitude");

                    cities.add(new CityInfo(id, name, description, climate, population, latitude, longitude));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to getAll()", ex);
        }

        return cities;
    }

    /**
     * Get city by id. Returns null if not found.
     *
     * @param cityId - item id
     */
    public CityInfo getCityById(String cityId) {

        CityInfo cityInfo;
        String sql = "SELECT * FROM cities where id=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cityId);

            try (ResultSet resultSet = statement.executeQuery()) {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String climate = resultSet.getString("climate");
                int population = resultSet.getInt("population");
                double latitude = resultSet.getDouble("latitude");
                double longitude = resultSet.getDouble("longitude");
                cityInfo = new CityInfo(cityId, name, description, climate, population, latitude, longitude);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return cityInfo;
    }

    /**
     * Get city by name. Returns null if not found.
     *
     * @param cityName - item name
     */
    public CityInfo getCityByName(String cityName) {

        CityInfo cityInfo;
        String sql = "SELECT * FROM cities where name=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cityName);

            try (ResultSet resultSet = statement.executeQuery()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String climate = resultSet.getString("climate");
                int population = resultSet.getInt("population");
                double latitude = resultSet.getDouble("latitude");
                double longitude = resultSet.getDouble("longitude");
                cityInfo = new CityInfo(id, name, description, climate, population, latitude, longitude);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return cityInfo;
    }

    /**
     * Create new city.
     *
     * @param info - city without id
     */
    public CityInfo create(CityInfo info) {

        String sql = "INSERT INTO cities (name, description, climate, population, latitude, longitude) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, info.getName());
            statement.setString(2, info.getDescription());
            statement.setString(3, info.getClimate());
            statement.setString(4, String.valueOf(info.getPopulation()));
            statement.setString(5, String.valueOf(info.getLatitude()));
            statement.setString(6, String.valueOf(info.getLongitude()));

            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return getCityByName(info.getName());
    }

    /**
     * Update existing city. Don't change id
     */
    public CityInfo update(CityInfo info) {

        String sql = "UPDATE cities SET name = ?, description = ?, climate = ?, population = ?, " +
                "latitude = ?, longitude = ? WHERE id = ?";

        String id = info.getId();
        if (id == null) {
            id = getCityByName(info.getName()).getId();
        }
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, info.getName());
            statement.setString(2, info.getDescription());
            statement.setString(3, info.getClimate());
            statement.setString(4, String.valueOf(info.getPopulation()));
            statement.setString(5, String.valueOf(info.getLatitude()));
            statement.setString(6, String.valueOf(info.getLongitude()));
            statement.setString(7, id);

            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return getCityById(id);
    }

    /**
     * Delete city by id.
     */
    public CityInfo delete(String cityId) {

        CityInfo cityInfo = getCityById(cityId);
        String sql = "DELETE FROM cities WHERE id=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cityId);
            statement.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to delete() city with id=" + cityId, ex);
        }

        return cityInfo;
    }
}
