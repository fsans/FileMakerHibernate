package org.hibernate.community.dialect.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestPreparedStatement {
    public static void main(String[] args) {
        String url = "jdbc:filemaker://192.168.0.24/Contacts"; // Update with your DB URL
        String user = "admin"; // Update with your DB username
        String password = "wakawaka"; // Update with your DB password

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO contact (email, name) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, "test@example.com");
                preparedStatement.setString(2, "Test User");
                preparedStatement.executeUpdate();
                System.out.println("Insert successful!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}