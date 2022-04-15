package Client.commands;

import exceptions.CommandIsNotExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientCommandReaderImplTest {
    private static ClientCommandReader commandReader;

    @BeforeEach
    void setUp() {
        commandReader = new ClientCommandReaderImpl();
        commandReader.addCommand("current_user", new CurrentUserCommand());
        commandReader.addCommand("client_help", new ClientHelpCommand(null));
        commandReader.addCommand("exit", new ClientExitCommand(null));
        commandReader.addCommand("execute_script", new ExecuteScriptCommand(null, null));
        commandReader.addCommand("auth", new ClientAuthCommand(null));
        commandReader.addCommand("register", new ClientRegisterCommand(null));
        commandReader.addCommand("current_user", new CurrentUserCommand());
    }


    @Test()
    void commandIsNotExist() {
        String command = "UNKNOWN_COMMAND";

        CommandIsNotExistException exception = assertThrows(CommandIsNotExistException.class,
                () -> commandReader.executeCommand(command, null));

        assertEquals("command is not exist", exception.getMessage());
    }

    @Test
    void commandExists() {
        String command = "current_user";

        assertDoesNotThrow(() -> commandReader.executeCommand(command, null));
    }

    @Test
    void commandExistsAnyCase() {
        String command = "cUrRenT_USeR";

        assertDoesNotThrow(() -> commandReader.executeCommand(command, null));
    }

}
