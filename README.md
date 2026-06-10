# Campus Lost and Found Database Layer

JDBC integration with Supabase (PostgreSQL) for the Lost and Found Reporting System.

## Setup
1. Clone the repo
2. Add `config.properties` to `src/main/resources/` (credentials provided separately)
3. Build with Maven
4. Run `DatabaseTest.java` to verify connection

## DAO Methods
- `ItemReportDAO` – CRUD for lost/found items
- `ClaimDAO` – claim processing
- `AdminDAO` – admin authentication
- `ActivityLogDAO` – audit trail
