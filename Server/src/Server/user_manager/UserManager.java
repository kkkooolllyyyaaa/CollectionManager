package Server.user_manager;

import exceptions.BadPasswordException;
import exceptions.UserExistenceException;
import general.User;

public interface UserManager {
    void insertToRegistered(User user) throws UserExistenceException;

    void deleteFromRegistered(User user) throws UserExistenceException;

    boolean isRegistered(User user) throws BadPasswordException;

    boolean isExist(User user);
}
