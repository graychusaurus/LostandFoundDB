package com.campuslf.dao;

import com.campuslf.database.DatabaseConnection;
import com.campuslf.models.Admin;

import java.sql.*;
import java.time.LocalDateTime;

public class AdminDAO {

    public Admin getAdminByUsername(String username) {
        String sql = "SELECT * FROM admin WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Admin admin = new Admin();
                    admin.setAdminId(rs.getInt("admin_id"));
                    admin.setUsername(rs.getString("username"));
                    admin.setPassword(rs.getString("password"));

                    // Null-safe timestamp
                    Timestamp ts = rs.getTimestamp("created_at");
                    admin.setCreatedAt(ts != null ? ts.toLocalDateTime() : null);

                    return admin;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addAdmin(Admin admin) {
        // Let the database handle created_at with DEFAULT CURRENT_TIMESTAMP
        String sql = "INSERT INTO admin (username, password) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, admin.getUsername());
            pstmt.setString(2, admin.getPassword());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}