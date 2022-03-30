package exceptions;
/**
 * Класс исключения, которое создается, если пароль не валиден или уже существует
 */
public class BadPasswordException extends Exception{

    public BadPasswordException(String message) {
        super(message);
    }
}
