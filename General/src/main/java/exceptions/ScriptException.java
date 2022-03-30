package exceptions;

/**
 * Класс исключения, которое создается, если произошла ошибка при выполнении скрипта
 */
public class ScriptException extends RuntimeException {
    public ScriptException() {
        super("script error");
    }
}
