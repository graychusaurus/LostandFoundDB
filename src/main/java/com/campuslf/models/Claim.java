package com.campuslf.models;

import java.time.LocalDateTime;

public class Claim {
    private int claimId;
    private int lostReportId;
    private int foundReportId;
    private int adminId;
    private String claimantName;
    private String claimantId;           // ✅ Renamed from claimantSchoolId
    private String claimantContactNumber;
    private String claimantSignature;
    private String verificationNotes;
    private String claimStatus;
    private LocalDateTime dateClaimed;

    public Claim() {}

    // Getters and Setters
    public int getClaimId() { return claimId; }
    public void setClaimId(int claimId) { this.claimId = claimId; }

    public int getLostReportId() { return lostReportId; }
    public void setLostReportId(int lostReportId) { this.lostReportId = lostReportId; }

    public int getFoundReportId() { return foundReportId; }
    public void setFoundReportId(int foundReportId) { this.foundReportId = foundReportId; }

    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }

    public String getClaimantName() { return claimantName; }
    public void setClaimantName(String claimantName) { this.claimantName = claimantName; }

    public String getClaimantId() { return claimantId; }                    // ✅ Renamed
    public void setClaimantId(String claimantId) { this.claimantId = claimantId; }  // ✅ Renamed

    public String getClaimantContactNumber() { return claimantContactNumber; }
    public void setClaimantContactNumber(String claimantContactNumber) { this.claimantContactNumber = claimantContactNumber; }

    public String getClaimantSignature() { return claimantSignature; }
    public void setClaimantSignature(String claimantSignature) { this.claimantSignature = claimantSignature; }

    public String getVerificationNotes() { return verificationNotes; }
    public void setVerificationNotes(String verificationNotes) { this.verificationNotes = verificationNotes; }

    public String getClaimStatus() { return claimStatus; }
    public void setClaimStatus(String claimStatus) { this.claimStatus = claimStatus; }

    public LocalDateTime getDateClaimed() { return dateClaimed; }
    public void setDateClaimed(LocalDateTime dateClaimed) { this.dateClaimed = dateClaimed; }
}