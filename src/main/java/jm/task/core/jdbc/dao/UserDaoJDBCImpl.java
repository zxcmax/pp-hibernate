package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS user (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255),
                    lastName VARCHAR(255),
                    age TINYINT
                )""";

        try (Connection connection = Util.getConnection(); Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating User table: " + e.getMessage(), e);
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getConnection(); Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE  IF EXISTS user");
        } catch (SQLException e) {
            throw new RuntimeException("Error dropping User table: " + e.getMessage(), e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO user (name, lastName, age) VALUES (?, ?, ?)";

        try (Connection connection = Util.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, lastName);
            stmt.setByte(3, age);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving User table: " + e.getMessage(), e);
        }
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM user WHERE id = ?";

        try (Connection connection = Util.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error removing user in User table: " + e.getMessage(), e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Connection connection = Util.getConnection(); Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM user");
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setLastName(rs.getString("lastName"));
                user.setAge(rs.getByte("age"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all User table: " + e.getMessage(), e);
        }

        return users;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection(); Statement stmt = connection.createStatement()) {
            stmt.execute("TRUNCATE TABLE user");
        } catch (SQLException e) {
            throw new RuntimeException("Error cleaning User table: " + e.getMessage(), e);
        }
    }
}
