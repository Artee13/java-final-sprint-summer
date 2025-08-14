package com.gym.dao.jdbc;

import com.gym.dao.WorkoutClassDAO;
import com.gym.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresWorkoutClassDAO implements WorkoutClassDAO {
    public PostgresWorkoutClassDAO() {}

    @Override
    public List<WorkoutClass> findAll() {
        List<WorkoutClass> classes = new ArrayList<>();
        String sql = "SELECT * FROM workout_classes";

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                classes.add(extractClass(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classes;
    }

    @Override
    public List<WorkoutClass> findByTrainer(int trainerId) {
        List<WorkoutClass> classes = new ArrayList<>();
        String sql = "SELECT * FROM workout_classes WHERE trainer_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, trainerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                classes.add(extractClass(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classes;
    }

    @Override
    public WorkoutClass addClass(WorkoutClass c) {
        String sql = "INSERT INTO workout_classes (class_type, class_description, trainer_id) VALUES (?, ?, ?) RETURNING *";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.type);
            stmt.setString(2, c.description);
            stmt.setInt(3, c.trainerId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractClass(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private WorkoutClass extractClass(ResultSet rs) throws SQLException {
        return new WorkoutClass(
            rs.getInt("class_id"),
            rs.getString("class_type"),
            rs.getString("class_description"),
            rs.getInt("trainer_id")
        );
    }
}