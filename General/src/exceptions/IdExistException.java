package exceptions;

public class IdExistException extends RuntimeException {
    public IdExistException() {
        super("Id is already exist");
    }
}
