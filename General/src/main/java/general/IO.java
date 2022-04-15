package general;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

public class IO {
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static String readLine() throws IOException {
        String line = reader.readLine();
        line = line != null ? line.trim() : null;
        return line;
    }

    public static String readPassword() throws IOException {
        Console console = System.console();
        if (console == null)
            return readLine();
        char[] chars = console.readPassword();
        return new String(chars);
    }

    public static void println(String str) {
        System.out.println(str);
    }

    public static void errPrint(String str) {
        System.err.println(str);
    }

    public static BufferedReader getReader() {
        return reader;
    }
}
