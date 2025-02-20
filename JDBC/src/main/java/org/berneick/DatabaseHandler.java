package org.berneick;

import java.sql.*;

public class DatabaseHandler {
    private static final String URL = "jdbc:postgresql://localhost:5432/users_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void readUsers() {
        String query = "SELECT * FROM users";
        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {

            String horizontalLine = "-------------------------------------------------------------------";

            System.out.printf("%-4s | %-20s | %-30s | %-4s%n", "Id", "Name", "Email", "Age");
            System.out.println(horizontalLine);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                int age = resultSet.getInt("age");

                System.out.printf("%-4d | %-20s | %-30s | %-4d%n", id, name, email, age);
            }
            System.out.println(horizontalLine);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUser(User user) {
        String query = "INSERT INTO users (name, email, age) VALUES (?, ?, ?)";
        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setInt(3, user.getAge());

            statement.executeUpdate();
            System.out.println(user + " inserted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(int id, User user) {
        String query = "UPDATE users SET name = ?, email = ?, age = ? WHERE id = ?";

        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setInt(3, user.getAge());
            statement.setInt(4, id);

            int updated = statement.executeUpdate();
            if(updated > 0) {
                System.out.println("Пользователь успешно обновлен.");
            } else {
                System.out.println("Пользователь с ID " + id + " не найден.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int id) {
        String query = "DELETE FROM users WHERE id = ?";
        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            int deleted = statement.executeUpdate();
            if(deleted > 0) {
                System.out.println("Пользователь успешно удален.");
            } else {
                System.out.println("Пользователь с ID " + id + " не найден.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
