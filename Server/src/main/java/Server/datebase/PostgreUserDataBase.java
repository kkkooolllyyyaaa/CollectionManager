package Server.datebase;

import exceptions.SQLNoDataException;
import exceptions.SQLUniqueException;
import general.User;

import java.sql.*;
import java.util.ArrayList;

public class PostgreUserDataBase implements UserDataBase {
    @Override
    public User getUser(User user) throws SQLNoDataException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Users_prog\" WHERE username = ?")) {
            statement.setString(1, user.getUserName());
            ResultSet rs = statement.executeQuery();
            rs.next();
            return new User(rs.getString(1), rs.getString(2));
        } catch (SQLException e) {
            throw new SQLNoDataException();
        }
    }

    @Override
    public void insertUser(User user) throws SQLUniqueException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO \"Users_prog\" VALUES(?,?)")) {

            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());
            statement.execute();
        } catch (SQLException unknownError) {
            unknownError.printStackTrace();
            throw new SQLUniqueException();
        }
    }

    @Override
    public void deleteUser(User user) throws SQLNoDataException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM \"Users_prog\" WHERE username = ?")) {
            statement.setString(1, user.getUserName());
            statement.execute();
        } catch (SQLException e) {
            throw new SQLNoDataException();
        }
    }

    @Override
    public ArrayList<User> getAll() throws SQLNoDataException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Users_prog\"")) {
            ArrayList<User> arrayList = new ArrayList<>();
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String log = rs.getString(1);
                String pass = rs.getString(2);
                User user = new User(log);
                user.setPassword(pass);
                arrayList.add(user);
            }
            return arrayList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLNoDataException();
        }
    }

    private Connection getConnection() throws SQLException {

        return DataBaseConnector.getConnection();
    }
}
