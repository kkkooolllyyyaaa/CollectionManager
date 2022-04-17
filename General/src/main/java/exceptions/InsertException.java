package exceptions;

/**
 * @author tsypk on 17.04.2022 18:28
 * @project Lab7
 * Класс исключение - если не удалось добавить элемент в БД или коллекцию
 */
public class InsertException extends Exception {
    public InsertException(String message) {
        super(message);
    }
}
