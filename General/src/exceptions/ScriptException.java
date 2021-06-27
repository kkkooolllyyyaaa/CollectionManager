package exceptions;

public class ScriptException extends RuntimeException {
    public ScriptException() {
        super("script error");
    }
}
