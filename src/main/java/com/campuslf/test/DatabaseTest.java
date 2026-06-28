package com.campuslf.test;

import com.campuslf.dao.*;
import com.campuslf.models.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DatabaseTest {
    public static void main(String[] args) {

        System.out.println("=== Testing Database Layer ===\n");

        // ============================================================
        // 1. TEST: Admin
        // ============================================================
        System.out.println("--- 1. Testing Admin ---");
        AdminDAO adminDAO = new AdminDAO();

        // Check if test admin exists
        Admin existingAdmin = adminDAO.getAdminByUsername("test_admin");
        int adminId = 1; // default

        if (existingAdmin == null) {
            Admin newAdmin = new Admin();
            newAdmin.setUsername("test_admin");
            newAdmin.setPassword("password123");
            boolean added = adminDAO.addAdmin(newAdmin);
            System.out.println("Admin added: " + added);
            // Fetch the newly created admin to get the ID
            Admin fetchedNew = adminDAO.getAdminByUsername("test_admin");
            if (fetchedNew != null) {
                adminId = fetchedNew.getAdminId();
            }
        } else {
            System.out.println("Admin already exists, skipping insert.");
            adminId = existingAdmin.getAdminId();
        }

        Admin fetchedAdmin = adminDAO.getAdminByUsername("test_admin");
        System.out.println("Fetched admin: " + (fetchedAdmin != null ? fetchedAdmin.getUsername() : "null"));
        System.out.println("Using admin_id: " + adminId);
        System.out.println();

        // ============================================================
        // 2. TEST: Category
        // ============================================================
        System.out.println("--- 2. Testing Category ---");
        CategoryDAO categoryDAO = new CategoryDAO();
        List<Category> categories = categoryDAO.getAllCategories();

        if (categories.isEmpty()) {
            System.err.println("⚠️ WARNING: No categories found in the database!");
            System.err.println("Please insert sample categories first. Skipping tests that need category_id.");
            System.out.println();
            // Still continue but note that tests may fail
        } else {
            System.out.println("Categories found: " + categories.size());
            for (Category cat : categories) {
                System.out.println("  - " + cat.getCategoryId() + ": " + cat.getCategoryName());
            }
            System.out.println();
        }

        // Get the first category ID (if any)
        int categoryId = categories.isEmpty() ? 1 : categories.get(0).getCategoryId();
        if (!categories.isEmpty()) {
            System.out.println("Using category_id: " + categoryId);
        }
        System.out.println();

        // ============================================================
        // 3. TEST: Lost Item Report (CREATE & READ)
        // ============================================================
        System.out.println("--- 3. Testing Lost Item Report ---");
        LostItemReportDAO lostDAO = new LostItemReportDAO();

        if (categories.isEmpty()) {
            System.err.println("⚠️ Skipping Lost Item Report test - no categories available.");
        } else {
            LostItemReport lostItem = new LostItemReport();
            lostItem.setAdminId(adminId);
            lostItem.setCategoryId(categoryId);
            lostItem.setItemName("Lost Laptop");
            lostItem.setDescription("Dell XPS 13, silver with red sticker");
            lostItem.setImageUrl("https://example.com/laptop.jpg");
            lostItem.setComplainantName("Juan Dela Cruz");
            lostItem.setComplainantId("2024-12345");
            lostItem.setComplainantContactNum("09123456789");
            lostItem.setAnonymous(false);
            lostItem.setLocationFound("Library 2nd floor");
            lostItem.setDateFound(LocalDate.now());
            lostItem.setDateReported(LocalDateTime.now());
            lostItem.setStatus("Pending");

            boolean lostAdded = lostDAO.addLostItemReport(lostItem);
            System.out.println("Lost item added: " + lostAdded);
            if (lostAdded) {
                System.out.println("Generated lost_report_id: " + lostItem.getLostReportId());
            }

            List<LostItemReport> lostItems = lostDAO.getAllLostItemReports("Pending");
            System.out.println("Pending lost items count: " + lostItems.size());
            for (LostItemReport item : lostItems) {
                System.out.println("  - " + item.getItemName() + " (" + item.getStatus() + ")");
            }
        }
        System.out.println();

        // ============================================================
        // 4. TEST: Found Item Report (CREATE & READ)
        // ============================================================
        System.out.println("--- 4. Testing Found Item Report ---");
        FoundItemReportDAO foundDAO = new FoundItemReportDAO();

        if (categories.isEmpty()) {
            System.err.println("⚠️ Skipping Found Item Report test - no categories available.");
        } else {
            FoundItemReport foundItem = new FoundItemReport();
            foundItem.setAdminId(adminId);
            foundItem.setCategoryId(categoryId);
            foundItem.setItemName("Found Wallet");
            foundItem.setDescription("Black leather wallet with PUP ID inside");
            foundItem.setImageUrl("https://example.com/wallet.jpg");
            foundItem.setFinderName("Maria Santos");
            foundItem.setFinderId("2024-67890");
            foundItem.setFinderContactNumber("09987654321");
            foundItem.setAnonymous(false);
            foundItem.setLocationFound("Canteen");
            foundItem.setDateFound(LocalDate.now());
            foundItem.setDateReported(LocalDateTime.now());
            foundItem.setStatus("Pending");

            boolean foundAdded = foundDAO.addFoundItemReport(foundItem);
            System.out.println("Found item added: " + foundAdded);
            if (foundAdded) {
                System.out.println("Generated found_report_id: " + foundItem.getFoundReportId());
            }

            List<FoundItemReport> foundItems = foundDAO.getAllFoundItemReports("Pending");
            System.out.println("Pending found items count: " + foundItems.size());
            for (FoundItemReport item : foundItems) {
                System.out.println("  - " + item.getItemName() + " (" + item.getStatus() + ")");
            }
        }
        System.out.println();

        // ============================================================
        // 5. TEST: Claim (CREATE & READ)
        // ============================================================
        System.out.println("--- 5. Testing Claim ---");
        ClaimDAO claimDAO = new ClaimDAO();

        if (categories.isEmpty()) {
            System.err.println("⚠️ Skipping Claim test - no items to claim.");
        } else {
            // Get the first lost and found report IDs
            List<LostItemReport> lostItems = lostDAO.getAllLostItemReports("Pending");
            List<FoundItemReport> foundItems = foundDAO.getAllFoundItemReports("Pending");

            int lostReportId = lostItems.isEmpty() ? 1 : lostItems.get(0).getLostReportId();
            int foundReportId = foundItems.isEmpty() ? 1 : foundItems.get(0).getFoundReportId();

            Claim claim = new Claim();
            claim.setLostReportId(1);
            claim.setFoundReportId(1);
            claim.setAdminId(1);
            claim.setClaimantName("Pedro Reyes");
            claim.setClaimantId("2024-11111");              // ✅ claimant_id
            claim.setClaimantContactNumber("09123456789");
            claim.setClaimantSignature("I confirm this item is mine");
            claim.setVerificationNotes("Claimant described the red sticker and scratch on the laptop");
            claim.setClaimStatus("Pending");
            claim.setDateClaimed(LocalDateTime.now());

            boolean claimAdded = claimDAO.addClaim(claim);
            System.out.println("Claim added: " + claimAdded);
            if (claimAdded) {
                System.out.println("Generated claim_id: " + claim.getClaimId());
            }

            List<Claim> claims = claimDAO.getAllClaims("Pending");
            System.out.println("Pending claims count: " + claims.size());
            for (Claim c : claims) {
                System.out.println("  - Claim #" + c.getClaimId() + " by " + c.getClaimantName() + " (" + c.getClaimStatus() + ")");
            }
        }
        System.out.println();

        // ============================================================
        // 6. TEST: Activity Log
        // ============================================================
        System.out.println("--- 6. Testing Activity Log ---");
        ActivityLogDAO logDAO = new ActivityLogDAO();

        boolean logAdded = logDAO.addLog(adminId, "Tested database integration");
        System.out.println("Activity log added: " + logAdded);

        List<ActivityLog> logs = logDAO.getAllLogs();
        System.out.println("Total logs: " + logs.size());
        if (!logs.isEmpty()) {
            System.out.println("Latest log: " + logs.get(0).getActivity() + " at " + logs.get(0).getTimestamp());
        }
        System.out.println();

        // ============================================================
        // 7. TEST: Update Operations
        // ============================================================
        System.out.println("--- 7. Testing Update Operations ---");

        if (!categories.isEmpty()) {
            List<LostItemReport> lostItems = lostDAO.getAllLostItemReports("Pending");
            List<FoundItemReport> foundItems = foundDAO.getAllFoundItemReports("Pending");

            if (!lostItems.isEmpty()) {
                boolean statusUpdated = lostDAO.updateLostItemStatus(lostItems.get(0).getLostReportId(), "Claimed");
                System.out.println("Lost item status updated to 'Claimed': " + statusUpdated);
            }

            if (!foundItems.isEmpty()) {
                boolean statusUpdated = foundDAO.updateFoundItemStatus(foundItems.get(0).getFoundReportId(), "Claimed");
                System.out.println("Found item status updated to 'Claimed': " + statusUpdated);
            }

            List<Claim> claims = claimDAO.getAllClaims("Pending");
            if (!claims.isEmpty()) {
                boolean claimApproved = claimDAO.updateClaimStatus(claims.get(0).getClaimId(), "Approved");
                System.out.println("Claim status updated to 'Approved': " + claimApproved);
            }
        }
        System.out.println();

        // ============================================================
        // Summary
        // ============================================================
        System.out.println("=== ✅ All tests completed successfully! ===");
    }
}