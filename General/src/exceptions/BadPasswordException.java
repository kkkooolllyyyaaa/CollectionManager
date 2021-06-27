package exceptions;

public class BadPasswordException extends Exception{

    public BadPasswordException(String message) {
        super(message);
    }
}
