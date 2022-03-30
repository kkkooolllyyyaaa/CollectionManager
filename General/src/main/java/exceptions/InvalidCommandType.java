package exceptions;

/**
 * Класс исключения, которое создается, если тип команды не валиден
 */
public class InvalidCommandType extends RuntimeException {
    public InvalidCommandType(String message) {
        super(message);
    }
}
