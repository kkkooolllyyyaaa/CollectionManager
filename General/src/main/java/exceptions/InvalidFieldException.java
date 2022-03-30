package exceptions;

/**
 * Класс исключения, которое создается при некорректном вводе поля элемента
 */
public class InvalidFieldException extends Exception {

    /**
     * Конструктор
     *
     * @param message
     */
    public InvalidFieldException(String message) {
        super(message);
    }
}