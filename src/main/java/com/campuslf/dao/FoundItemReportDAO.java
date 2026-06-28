package com.campuslf.dao;

import com.campuslf.database.DatabaseConnection;
import com.campuslf.models.FoundItemReport;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FoundItemReportDAO {

    // CREATE: Add a found item report
    public boolean addFoundItemReport(FoundItemReport report) {
        String sql = "INSERT INTO found_item_report (admin_id, category_id, item_name, description, " +
                "image_url, finder_name, finder_id, finder_contact_number, is_anonymous, " +
                "location_found, date_found, date_reported, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setObject(1, report.getAdminId());  // NULL allowed
            pstmt.setInt(2, report.getCategoryId());
            pstmt.setString(3, report.getItemName());
            pstmt.setString(4, report.getDescription());
            pstmt.setString(5, report.getImageUrl());
            pstmt.setString(6, report.getFinderName());
            pstmt.setString(7, report.getFinderId());
            pstmt.setString(8, report.getFinderContactNumber());
            pstmt.setInt(9, report.isAnonymous() ? 1 : 0);
            pstmt.setString(10, report.getLocationFound());
            pstmt.setDate(11, Date.valueOf(report.getDateFound()));
            pstmt.setTimestamp(12, Timestamp.valueOf(report.getDateReported()));
            pstmt.setString(13, report.getStatus());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        report.setFoundReportId(generatedKeys.getInt(1));
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

    // READ: Get all found item reports (optional status filter)
    public List<FoundItemReport> getAllFoundItemReports(String statusFilter) {
        List<FoundItemReport> list = new ArrayList<>();
        String sql = "SELECT * FROM found_item_report";
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
                    list.add(mapResultSetToFoundItemReport(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // READ: Get found item report by ID
    public FoundItemReport getFoundItemReportById(int foundReportId) {
        String sql = "SELECT * FROM found_item_report WHERE found_report_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, foundReportId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFoundItemReport(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE: Update status of found item report
    public boolean updateFoundItemStatus(int foundReportId, String newStatus) {
        String sql = "UPDATE found_item_report SET status = ? WHERE found_report_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, foundReportId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE: Delete a found item report
    public boolean deleteFoundItemReport(int foundReportId) {
        String sql = "DELETE FROM found_item_report WHERE found_report_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, foundReportId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper method to map ResultSet to FoundItemReport
    private FoundItemReport mapResultSetToFoundItemReport(ResultSet rs) throws SQLException {
        FoundItemReport report = new FoundItemReport();
        report.setFoundReportId(rs.getInt("found_report_id"));

        int adminId = rs.getInt("admin_id");
        if (!rs.wasNull()) {
            report.setAdminId(adminId);
        }

        report.setCategoryId(rs.getInt("category_id"));
        report.setItemName(rs.getString("item_name"));
        report.setDescription(rs.getString("description"));
        report.setImageUrl(rs.getString("image_url"));
        report.setFinderName(rs.getString("finder_name"));
        report.setFinderId(rs.getString("finder_id"));
        report.setFinderContactNumber(rs.getString("finder_contact_number"));
        report.setAnonymous(rs.getInt("is_anonymous") == 1);
        report.setLocationFound(rs.getString("location_found"));

        Date dateFound = rs.getDate("date_found");
        report.setDateFound(dateFound != null ? dateFound.toLocalDate() : null);

        Timestamp tsReported = rs.getTimestamp("date_reported");
        report.setDateReported(tsReported != null ? tsReported.toLocalDateTime() : null);

        report.setStatus(rs.getString("status"));
        return report;
    }
}