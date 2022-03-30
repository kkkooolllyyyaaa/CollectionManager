package exceptions;

public class SQLNoDataException extends Exception {
    public SQLNoDataException() {
        super("There is no elements in database");
    }
}
