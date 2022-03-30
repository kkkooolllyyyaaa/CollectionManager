package Server.datebase;

import exceptions.SQLNoDataException;
import exceptions.SQLUniqueException;
import general.User;

import java.util.ArrayList;

public interface UserDataBase {
    User getUser(User user) throws SQLNoDataException;

    void insertUser(User user) throws SQLUniqueException;

    void deleteUser(User user) throws SQLNoDataException;

    ArrayList<User> getAll() throws SQLNoDataException;
}
