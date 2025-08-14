package com.gym.dao.jdbc;

import com.gym.dao.UserDAO;
import com.gym.entities.*;
import com.gym.util.ConnectionManager;

import java.sql.*;
import java.util.*;

public class PostgresUserDAO implements UserDAO {

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(extractUserFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Replace with logging
        }

        return Optional.empty();
    }

    @Override
    public User create(User user) {
        String sql = "INSERT INTO users (username, password_hash, email, phone_number, address, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getHashedPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getAddress());
            stmt.setString(6, user.getRole().name());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public boolean deleteById(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("user_id");
        String username = rs.getString("username");
        String hashedPassword = rs.getString("password_hash");
        String email = rs.getString("email");
        String phone = rs.getString("phone");
        String address = rs.getString("address");
        Role role = Role.valueOf(rs.getString("role"));

        return switch (role) {
            case MEMBER -> new Member(id, username, hashedPassword, email, phone, address);
            case TRAINER -> new Trainer(id, username, hashedPassword, email, phone, address);
            case ADMIN -> new Admin(id, username, hashedPassword, email, phone, address);
        };
    }
}