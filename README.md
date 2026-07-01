# Campus Lost and Found Database Layer

JDBC integration with **SQLite** for the Campus Lost and Found Reporting System.  
Provides a complete data access layer using DAO pattern for `lost_item_report`, `found_item_report`, `claim`, `admin`, `category`, and `activity_logs` tables.

---

## 📦 Technologies
- Java 17+
- Maven
- **SQLite** (local database)
- JDBC

---

## 🔧 Setup for Developers

1. Clone the Repository

```bash
git clone https://github.com/grayuschaurus/LostandFoundDB.git
cd LostandFoundDB

2. Open in IntelliJ
* Open IntelliJ IDEA
* Select Open and choose the project folder
* Wait for Maven to download dependencies

3. Run the Test
* Navigate to src/main/java/com/campuslf/test/DatabaseTest.java
* Right-click → Run 'DatabaseTest.main()'

Expected output:
=== Testing Database Layer ===

--- 1. Testing Admin ---
Admin already exists, skipping insert.
Fetched admin: test_admin

--- 2. Testing Category ---
Categories found: 10

--- 3. Testing Lost Item Report ---
Lost item added: true

--- 4. Testing Found Item Report ---
Found item added: true

--- 5. Testing Claim ---
Claim added: true

--- 6. Testing Activity Log ---
Activity log added: true

--- 7. Testing Update Operations ---
Lost item status updated to 'Claimed': true
Found item status updated to 'Claimed': true
Claim status updated to 'Approved': true

=== ✅ All tests completed successfully! ===

No setup required! The database file LostAndFound.db is included in the repository.

📂 Project Structure
src/main/java/com/campuslf/
├── database/
│   └── DatabaseConnection.java      # SQLite connection
├── models/
│   ├── Admin.java
│   ├── Category.java
│   ├── LostItemReport.java
│   ├── FoundItemReport.java
│   ├── Claim.java
│   └── ActivityLog.java
├── dao/
│   ├── AdminDAO.java
│   ├── CategoryDAO.java
│   ├── LostItemReportDAO.java
│   ├── FoundItemReportDAO.java
│   ├── ClaimDAO.java
│   └── ActivityLogDAO.java
└── test/
    ├── TestConnection.java
    └── DatabaseTest.java

📝 DAO Methods Overview
* LostItemReportDAO
-addLostItemReport()
-getAllLostItemReports()
-updateLostItemStatus()

* FoundItemReportDAO
-addFoundItemReport()
-getAllFoundItemReports()
-updateFoundItemStatus()

* ClaimDAO
-addClaim()
-getAllClaims()
-updateClaimStatus()
-updateVerificationNotes()

* AdminDAO
-getAdminByUsername()
-addAdmin()

* CategoryDAO
-getAllCategories()
-getCategoryById()

* ActivityLogDAO
-addLog()
-getAllLogs()


Usage Example (JavaFX Controller)
// Get all pending lost items
LostItemReportDAO lostDAO = new LostItemReportDAO();
List<LostItemReport> pendingLost = lostDAO.getAllLostItemReports("Pending");

// Add a new found item
FoundItemReport foundItem = new FoundItemReport();
foundItem.setAdminId(1);
foundItem.setCategoryId(1);
foundItem.setItemName("Black Wallet");
foundItem.setDescription("Leather wallet with PUP ID");
foundItem.setFinderName("Juan Dela Cruz");
foundItem.setFinderId("2024-12345");
foundItem.setFinderContactNumber("09123456789");
foundItem.setLocationFound("Canteen");
foundItem.setDateFound(LocalDate.now());
foundItem.setDateReported(LocalDateTime.now());
foundItem.setStatus("Pending");

FoundItemReportDAO foundDAO = new FoundItemReportDAO();
boolean success = foundDAO.addFoundItemReport(foundItem);

Database Schema
admin	- Admin accounts (login)
category	- Item categories (Electronics, Bag/Wallet, etc.)
lost_item_report	- Reports of lost items
found_item_report	- Reports of found items
claim	- Claim verification and processing
activity_logs	- Audit trail of admin actions

Key Relationships
* admin (1) → lost_item_report (many)
* admin (1) → found_item_report (many)
* admin (1) → claim (many)
* category (1) → lost_item_report (many)
* category (1) → found_item_report (many)
* lost_item_report (1) → claim (many)
* found_item_report (1) → claim (many)

🔧 Troubleshooting
* No suitable driver found	- Make sure SQLite JDBC dependency is in pom.xml and Maven is refreshed
* SQL error or missing database (no such table)	- The database file LostAndFound.db must be in the project root folder
* SQL error or missing database (no such column)	- Check column names against the schema above
* Java warnings about restricted methods	- Safe to ignore – it's a Java 25 warning

Ready for integration with JavaFX frontend. Use the DAO methods directly – no manual JDBC handling needed.
