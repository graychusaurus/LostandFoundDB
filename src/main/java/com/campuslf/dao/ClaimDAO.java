package com.campuslf.dao;

import com.campuslf.database.DatabaseConnection;
import com.campuslf.models.Claim;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClaimDAO {

    // CREATE: Add a new claim
    public boolean addClaim(Claim claim) {
        String sql = "INSERT INTO claim (lost_report_id, found_report_id, admin_id, claimant_name, " +
                "claimant_id, claimant_contact_number, claimant_signature, verification_notes, " +
                "claim_status, date_claimed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, claim.getLostReportId());
            pstmt.setInt(2, claim.getFoundReportId());
            pstmt.setInt(3, claim.getAdminId());
            pstmt.setString(4, claim.getClaimantName());
            pstmt.setString(5, claim.getClaimantId());  // ✅ Changed from getClaimantSchoolId()
            pstmt.setString(6, claim.getClaimantContactNumber());
            pstmt.setString(7, claim.getClaimantSignature());
            pstmt.setString(8, claim.getVerificationNotes());
            pstmt.setString(9, claim.getClaimStatus());
            pstmt.setTimestamp(10, Timestamp.valueOf(claim.getDateClaimed()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        claim.setClaimId(generatedKeys.getInt(1));
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

    // READ: Get all claims (with optional status filter)
    public List<Claim> getAllClaims(String statusFilter) {
        List<Claim> list = new ArrayList<>();
        String sql = "SELECT * FROM claim";
        if (statusFilter != null && !statusFilter.isEmpty()) {
            sql += " WHERE claim_status = ?";
        }
        sql += " ORDER BY date_claimed DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (statusFilter != null && !statusFilter.isEmpty()) {
                pstmt.setString(1, statusFilter);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToClaim(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // READ: Get claim by ID
    public Claim getClaimById(int claimId) {
        String sql = "SELECT * FROM claim WHERE claim_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, claimId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToClaim(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ: Get claims by lost report ID
    public List<Claim> getClaimsByLostReportId(int lostReportId) {
        List<Claim> list = new ArrayList<>();
        String sql = "SELECT * FROM claim WHERE lost_report_id = ? ORDER BY date_claimed DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, lostReportId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToClaim(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // READ: Get claims by found report ID
    public List<Claim> getClaimsByFoundReportId(int foundReportId) {
        List<Claim> list = new ArrayList<>();
        String sql = "SELECT * FROM claim WHERE found_report_id = ? ORDER BY date_claimed DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, foundReportId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToClaim(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE: Update claim status
    public boolean updateClaimStatus(int claimId, String newStatus) {
        String sql = "UPDATE claim SET claim_status = ? WHERE claim_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, claimId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // UPDATE: Update verification notes
    public boolean updateVerificationNotes(int claimId, String notes) {
        String sql = "UPDATE claim SET verification_notes = ? WHERE claim_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, notes);
            pstmt.setInt(2, claimId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper method to map ResultSet to Claim
    private Claim mapResultSetToClaim(ResultSet rs) throws SQLException {
        Claim claim = new Claim();
        claim.setClaimId(rs.getInt("claim_id"));
        claim.setLostReportId(rs.getInt("lost_report_id"));
        claim.setFoundReportId(rs.getInt("found_report_id"));
        claim.setAdminId(rs.getInt("admin_id"));
        claim.setClaimantName(rs.getString("claimant_name"));
        claim.setClaimantId(rs.getString("claimant_id"));  // ✅ Changed from getClaimantSchoolId()
        claim.setClaimantContactNumber(rs.getString("claimant_contact_number"));
        claim.setClaimantSignature(rs.getString("claimant_signature"));
        claim.setVerificationNotes(rs.getString("verification_notes"));
        claim.setClaimStatus(rs.getString("claim_status"));

        Timestamp tsClaimed = rs.getTimestamp("date_claimed");
        claim.setDateClaimed(tsClaimed != null ? tsClaimed.toLocalDateTime() : null);

        return claim;
    }
}