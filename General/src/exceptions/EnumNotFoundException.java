package exceptions;

/**
 * Класс исключения, которое создается, если enum не найден
 */
public class EnumNotFoundException extends Exception {
    /**
     * Конструктор
     *
     * @param message
     */
    public EnumNotFoundException(String message) {
        super(message);
    }
}
