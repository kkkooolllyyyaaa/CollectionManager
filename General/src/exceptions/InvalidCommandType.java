package exceptions;

public class InvalidCommandType extends RuntimeException {
    public InvalidCommandType(String message) {
        super(message);
    }
}
