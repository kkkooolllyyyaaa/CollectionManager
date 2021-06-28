package general;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

public interface IOImpl {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    default String readLine() throws IOException {
        return reader.readLine();
    }

    default String readPassword() throws IOException {
        Console console = System.console();
        if (console == null)
            return readLine();
        char[] chars = console.readPassword();
        return new String(chars);
    }

    default void println(String str) {
        System.out.println(str);
    }

    default void errPrint(String str) {
        System.err.println(str);
    }

    default BufferedReader getReader() {
        return reader;
    }

}
