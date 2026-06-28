package com.campuslf.test;

import com.campuslf.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        System.out.println("🔌 Testing SQLite connection...");
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("✅ Connected to SQLite successfully!");
            System.out.println("Database URL: " + conn.getMetaData().getURL());
        } catch (SQLException e) {
            System.err.println("❌ Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}