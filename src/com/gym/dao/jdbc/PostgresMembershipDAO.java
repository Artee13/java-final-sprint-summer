package com.gym.dao.jdbc;

import com.gym.dao.MembershipDAO;
import com.gym.util.ConnectionManager;

import java.math.BigDecimal;
import java.sql.*;

public class PostgresMembershipDAO implements MembershipDAO {

    @Override
    public boolean purchaseMembership(int userId, String type, BigDecimal cost, String description) {
        String sql = "INSERT INTO memberships (membership_type, membership_cost, membership_description, user_id) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, type);
            stmt.setBigDecimal(2, cost);
            stmt.setString(3, description);
            stmt.setInt(4, userId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace(); // Replace with logger in production
        }

        return false;
    }

    @Override
    public BigDecimal totalRevenue() {
        String sql = "SELECT SUM(membership_cost) FROM memberships";

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : BigDecimal.ZERO;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal totalSpentByMember(int userId) {
        String sql = "SELECT SUM(membership_cost) FROM memberships WHERE user_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : BigDecimal.ZERO;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return BigDecimal.ZERO;
    }
}