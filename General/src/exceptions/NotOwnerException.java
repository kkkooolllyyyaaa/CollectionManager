package exceptions;

public class NotOwnerException extends Exception {
    public NotOwnerException() {
        super("You are not owner of this element");
    }
}
