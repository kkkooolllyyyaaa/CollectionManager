package exceptions;

/**
 * Класс исключения, которое создается, если клиент не авторизован
 */
public class NotAuthorizeException extends Exception {
    public NotAuthorizeException() {
        super("You are not authorize");
    }
}
