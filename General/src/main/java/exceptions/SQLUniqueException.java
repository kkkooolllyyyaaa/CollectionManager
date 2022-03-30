package exceptions;

/**
 * Класс исключения, которое создается, если элемент с уникальным значением уже существует
 */
public class SQLUniqueException extends Exception {
    public SQLUniqueException() {
        super("This element is already exist in DB, should be unique");
    }
}
