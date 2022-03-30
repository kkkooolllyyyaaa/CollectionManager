package exceptions;

/**
 * Класс исключения, которое создается, если клиент собирается манипулировать над не своими объектами
 */
public class NotOwnerException extends Exception {
    public NotOwnerException() {
        super("You are not owner of this element");
    }
}
