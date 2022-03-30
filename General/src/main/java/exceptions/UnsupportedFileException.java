package exceptions;

/**
 * Класс исключения, которое создается, если читаемый файл не поддерживается
 */
public class UnsupportedFileException extends Exception {
    public UnsupportedFileException(String message) {
        super(message);
    }
}
