package validation;

public interface InputChecker {
    static boolean checkInt(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    static boolean checkLong(String string) {
        try {
            Long.parseLong(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
