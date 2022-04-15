package Client.commands;

import Client.client.Client;
import exceptions.CommandIsNotExistException;
import exceptions.ScriptException;
import general.AbstractCommand;
import general.IO;
import general.Response;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class ExecuteScriptCommand extends AbstractCommand {
    private final ClientCommandReaderImpl commandReader;
    private final Client client;
    private final HashSet<String> scripts;

    public ExecuteScriptCommand(Client client, ClientCommandReaderImpl commandReader) {
        super("execute_script", "Execute script from file");
        this.client = client;
        this.commandReader = commandReader;
        scripts = new HashSet<>();
    }


    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            IO.errPrint("Invalid argument for command");
            return;
        }
        String fileName = args[1];

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            if (scripts.contains(fileName))
                throw new ScriptException();
            scripts.add(fileName);

            while (reader.ready()) {
                String command = reader.readLine();
                try {
                    commandReader.executeCommand(command.trim().toLowerCase(), null);
                } catch (CommandIsNotExistException e) {
                    try {
                        Response response = client.communicateWithServer(command);
                        IO.println(response.getMessage());
                    } catch (EOFException eof) {
                        IO.errPrint("too many bytes");
                    } catch (IOException | ClassNotFoundException ioe) {
                        IO.errPrint(ioe.getMessage());
                    }
                }
            }
            removeScript(fileName);
        } catch (IOException e) {
            IO.errPrint(e.getMessage());
        }
    }

    public void clearScripts() {
        scripts.clear();
    }

    private void removeScript(String fileName) {
        scripts.remove(fileName);
    }

    @Override
    public boolean isStudyGroupCommand() {
        return false;
    }
}