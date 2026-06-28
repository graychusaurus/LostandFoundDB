package com.campuslf.dao;

import com.campuslf.database.DatabaseConnection;
import com.campuslf.models.LostItemReport;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LostItemReportDAO {

    // CREATE: Add a lost item report
    public boolean addLostItemReport(LostItemReport report) {
        String sql = "INSERT INTO lost_item_report (admin_id, category_id, item_name, description, " +
                "image_url, complainant_name, complainant_id, complainant_contact_num, is_anonymous, " +
                "location_found, date_found, date_reported, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setObject(1, report.getAdminId());  // NULL allowed
            pstmt.setInt(2, report.getCategoryId());
            pstmt.setString(3, report.getItemName());
            pstmt.setString(4, report.getDescription());
            pstmt.setString(5, report.getImageUrl());
            pstmt.setString(6, report.getComplainantName());
            pstmt.setString(7, report.getComplainantId());
            pstmt.setString(8, report.getComplainantContactNum());
            pstmt.setInt(9, report.isAnonymous() ? 1 : 0);
            pstmt.setString(10, report.getLocationFound());
            pstmt.setDate(11, Date.valueOf(report.getDateFound()));
            pstmt.setTimestamp(12, Timestamp.valueOf(report.getDateReported()));
            pstmt.setString(13, report.getStatus());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        report.setLostReportId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ: Get all lost item reports (optional status filter)
    public List<LostItemReport> getAllLostItemReports(String statusFilter) {
        List<LostItemReport> list = new ArrayList<>();
        String sql = "SELECT * FROM lost_item_report";
        if (statusFilter != null && !statusFilter.isEmpty()) {
            sql += " WHERE status = ?";
        }
        sql += " ORDER BY date_reported DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (statusFilter != null && !statusFilter.isEmpty()) {
                pstmt.setString(1, statusFilter);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToLostItemReport(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // READ: Get lost item report by ID
    public LostItemReport getLostItemReportById(int lostReportId) {
        String sql = "SELECT * FROM lost_item_report WHERE lost_report_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, lostReportId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLostItemReport(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE: Update status of lost item report
    public boolean updateLostItemStatus(int lostReportId, String newStatus) {
        String sql = "UPDATE lost_item_report SET status = ? WHERE lost_report_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, lostReportId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE: Delete a lost item report
    public boolean deleteLostItemReport(int lostReportId) {
        String sql = "DELETE FROM lost_item_report WHERE lost_report_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, lostReportId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper method to map ResultSet to LostItemReport
    private LostItemReport mapResultSetToLostItemReport(ResultSet rs) throws SQLException {
        LostItemReport report = new LostItemReport();
        report.setLostReportId(rs.getInt("lost_report_id"));

        int adminId = rs.getInt("admin_id");
        if (!rs.wasNull()) {
            report.setAdminId(adminId);
        }

        report.setCategoryId(rs.getInt("category_id"));
        report.setItemName(rs.getString("item_name"));
        report.setDescription(rs.getString("description"));
        report.setImageUrl(rs.getString("image_url"));
        report.setComplainantName(rs.getString("complainant_name"));
        report.setComplainantId(rs.getString("complainant_id"));
        report.setComplainantContactNum(rs.getString("complainant_contact_num"));
        report.setAnonymous(rs.getInt("is_anonymous") == 1);
        report.setLocationFound(rs.getString("location_found"));

        // Null-safe date conversions
        Date dateFound = rs.getDate("date_found");
        report.setDateFound(dateFound != null ? dateFound.toLocalDate() : null);

        Timestamp tsReported = rs.getTimestamp("date_reported");
        report.setDateReported(tsReported != null ? tsReported.toLocalDateTime() : null);

        report.setStatus(rs.getString("status"));
        return report;
    }
}