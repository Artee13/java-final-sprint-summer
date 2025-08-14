package com.gym.dao.jdbc;

import com.gym.dao.MerchandiseDAO;
import com.gym.util.ConnectionManager;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresMerchandiseDAO implements MerchandiseDAO {

    @Override
    public List<Item> findAll() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM gym_merch";

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                items.add(new Item(
                    rs.getInt("merch_id"),
                    rs.getString("merch_name"),
                    rs.getString("merch_type"),
                    rs.getBigDecimal("merch_price"),
                    rs.getInt("quantity_in_stock")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Consider replacing with logger in real app
        }

        return items;
    }

    @Override
    public Item addItem(Item item) {
        String sql = "INSERT INTO gym_merch (merch_name, merch_type, merch_price, quantity_in_stock) " +
                     "VALUES (?, ?, ?, ?) RETURNING *";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.name);
            stmt.setString(2, item.type);
            stmt.setBigDecimal(3, item.price);
            stmt.setInt(4, item.quantity);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Item(
                    rs.getInt("merch_id"),
                    rs.getString("merch_name"),
                    rs.getString("merch_type"),
                    rs.getBigDecimal("merch_price"),
                    rs.getInt("quantity_in_stock")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Consider replacing with logger
        }

        return null;
    }

    @Override
    public BigDecimal totalStockValue() {
        String sql = "SELECT SUM(merch_price * quantity_in_stock) FROM gym_merch";

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getBigDecimal(1); // first column of result
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return BigDecimal.ZERO;
    }
}