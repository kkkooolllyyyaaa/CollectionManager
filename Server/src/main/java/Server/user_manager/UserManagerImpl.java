package Server.user_manager;

import Server.datebase.UserDataBase;
import exceptions.SQLNoDataException;
import exceptions.SQLUniqueException;
import exceptions.UserExistenceException;
import general.User;

import java.util.HashSet;

public class UserManagerImpl implements UserManager {
    private final HashSet<User> registered;
    private final UserDataBase userDataBase;

    public UserManagerImpl(UserDataBase userDataBase) {
        this.userDataBase = userDataBase;
        registered = new HashSet<>();
        try {
            registered.addAll(userDataBase.getAll());
        } catch (SQLNoDataException notExcepted) {
            notExcepted.printStackTrace();
        }
    }

    @Override
    public void insertToRegistered(User user) throws UserExistenceException {
        for (User u : registered) {
            if (u.equals(user)) {
                throw new UserExistenceException("The username is already exists");
            }
        }
        try {
            userDataBase.insertUser(user);
            registered.add(user);
        } catch (SQLUniqueException e) {
            throw new UserExistenceException(e.getMessage());
        }
    }

    @Override
    public void deleteFromRegistered(User user) throws UserExistenceException {
        if (registered.contains(user)) {
            try {
                userDataBase.deleteUser(user);
                registered.remove(user);
            } catch (SQLNoDataException notExcepted) {
                notExcepted.printStackTrace();
            }
        } else
            throw new UserExistenceException("The user is not exists");
    }

    @Override
    public boolean isRegistered(User user) {
        if (user == null)
            return false;
        for (User u : registered) {
            if (user.getUserName().equals(u.getUserName()) && user.getPassword().equals(u.getPassword())) {
                return true;
            } else if (user.getUserName().equals(u.getUserName())) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isExist(User user) {
        if (user == null)
            return false;
        for (User u : registered) {
            if (u.getUserName().equals(user.getUserName()))
                return true;
        }
        return false;
    }
}
