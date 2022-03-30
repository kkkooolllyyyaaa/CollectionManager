package exceptions;

/**
 * Класс исключения, которое создается, если в базе данных нет соответствующих элементов
 */
public class SQLNoDataException extends Exception {
    public SQLNoDataException() {
        super("There is no elements in database");
    }
}
