package exceptions;

public class CommandIsNotExistException extends Exception {
    public CommandIsNotExistException(String message) {
        super(message);
    }
}
