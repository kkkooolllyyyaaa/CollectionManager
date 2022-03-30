package exceptions;

/**
 * Класс исключения, которое создается, если пользователь с таким username уже существует
 */
public class UserExistenceException extends Exception {
    public UserExistenceException(String msg) {
        super(msg);
    }
}
