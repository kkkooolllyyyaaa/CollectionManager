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
        if (registered.contains(user))
            throw new UserExistenceException("The username is already exists");

        try {
            userDataBase.insertUser(user);
            registered.add(user);
        } catch (SQLUniqueException e) {
            throw new UserExistenceException(e.getMessage());
        }
    }

    @Override
    public void deleteFromRegistered(User user) throws UserExistenceException {
        if (!registered.contains(user))
            throw new UserExistenceException("The user is not exists");

        try {
            userDataBase.deleteUser(user);
            registered.remove(user);
        } catch (SQLNoDataException notExcepted) {
            notExcepted.printStackTrace();
        }
    }

    @Override
    public boolean isRegistered(User user) {
        if (user == null)
            return false;

        long found = registered.stream().filter(x ->
                x.getUserName().equals(user.getUserName()) &&
                        x.getPassword().equals(user.getPassword())).count();

        return found > 0;
    }

    @Override
    public boolean isExist(User user) {
        long found = registered.stream().filter(x -> x.getUserName().equals(user.getUserName())).count();

        return found > 0;
    }
}
