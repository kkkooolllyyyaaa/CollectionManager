package exceptions;

/**
 * Класс исключения, которое создается, если команда не найдена
 */
public class CommandIsNotExistException extends Exception {
    public CommandIsNotExistException(String message) {
        super(message);
    }
}
