package org.berneick;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * Класс для работы с пользователями в базе данных.
 */
public class DatabaseHandler {
    private static final String URL = "jdbc:postgresql://localhost:5432/users_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    /**
     * Создает объект {@link Connection} при помощи {@link DriverManager}.
     *
     * @return новый объект {@link Connection} подключения
     * @throws SQLException Если возникает ошибка подключения к бд
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Создает объект {@link User} на основе данных из {@link ResultSet}.
     *
     * @param result Результат выполнения SQL-запроса ({@link ResultSet}),
     *               содержащий данные пользователя.
     * @return Новый объект {@link User}, созданный на основе данных из {@link ResultSet}.
     * @throws SQLException Если возникает ошибка при чтении данных из {@link ResultSet}.
     */
    private User userOf(ResultSet result) throws SQLException {
        String name = result.getString("name");
        String email = result.getString("email");
        int age = result.getInt("age");
        return new User(name, email, age);
    }

    /**
     * Читает всех пользователей из таблицы "users" и выводит их данные в консоль
     * в виде форматированной таблицы.
     */
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
                User user = userOf(resultSet);

                System.out.printf("%-4d | %-20s | %-30s | %-4d%n", id, user.getName(), user.getEmail(), user.getAge());
            }
            System.out.println(horizontalLine);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Добавляет данные в таблицу users на основе данных объекта {@link User}.
     *
     * @param user Объект {@link User}, содержащий данные для добавления в таблицу.
     */
    public void addUser(User user) {
        String query = "INSERT INTO users (name, email, age) VALUES (?, ?, ?)";
        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            setUserParameters(user, statement);

            statement.executeUpdate();
            System.out.println(user + " inserted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Обновляет данные в таблице users на основе данных объекта {@link User}.
     *
     * @param id Идентификатор пользователя в таблице.
     * @param user Объект {@link User}, содержащий данные для обновления.
     */
    public void updateUser(int id, User user) {
        String query = "UPDATE users SET name = ?, email = ?, age = ? WHERE id = ?";

        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            setUserParameters(user, statement);
            statement.setInt(4, id);

            printUpdateStatus(id, statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Устанавливает параметры в {@link PreparedStatement} на основе данных объекта {@link User}.
     *
     * @param user      Объект {@link User}, содержащий данные для установки параметров.
     * @param statement Подготовленный SQL-запрос ({@link PreparedStatement}),
     *                  в который будут установлены параметры.
     * @throws SQLException Если возникает ошибка при работе с базой данных.
     */
    private void setUserParameters(User user, PreparedStatement statement) throws SQLException {
        statement.setString(1, user.getName());
        statement.setString(2, user.getEmail());
        statement.setInt(3, user.getAge());
    }

    /**
     * Удаляет пользователя из таблицы users по его идентификатору
     *
     * @param id Идентификатор пользователя в таблице.
     */
    public void deleteUser(int id) {
        String query = "DELETE FROM users WHERE id = ?";
        try(Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            printUpdateStatus(id, statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Обрабатывает результат выполнения SQL-операции (например, UPDATE или DELETE)
     * и выводит сообщение о статусе операции.
     *
     * @param id        Идентификатор пользователя, для которого выполнялась операция.
     * @param statement Подготовленный SQL-запрос ({@link PreparedStatement}),
     *                  результат выполнения которого обрабатывается.
     * @throws SQLException Если возникает ошибка при работе с базой данных.
     */
    private static void printUpdateStatus(int id, PreparedStatement statement) throws SQLException {
        int updated = statement.executeUpdate();
        if(updated > 0) {
            System.out.println("Пользователь успешно обновлен.");
        } else {
            System.out.println("Пользователь с ID " + id + " не найден.");
        }
    }
}
