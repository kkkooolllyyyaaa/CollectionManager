package exceptions;

public class NotAuthorizeException extends Exception {
    public NotAuthorizeException() {
        super("You are not authorize");
    }
}
