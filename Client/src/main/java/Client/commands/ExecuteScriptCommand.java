package Client.commands;

import Client.client.Client;
import exceptions.CommandIsNotExistException;
import exceptions.ScriptException;
import general.AbstractCommand;
import general.IOImpl;
import general.Response;

import java.io.*;
import java.util.HashSet;

public class ExecuteScriptCommand extends AbstractCommand implements IOImpl {
    ClientCommandReaderImpl commandReader;
    Client client;
    String fileName;
    HashSet<String> scripts;

    public ExecuteScriptCommand(Client client, ClientCommandReaderImpl commandReader) {
        super("execute_script", "Execute script from file");
        this.client = client;
        this.commandReader = commandReader;
        scripts = new HashSet<>();
    }


    @Override
    public void execute(String[] args) {
        if (args.length > 1) {
            fileName = args[1];
        } else {
            errPrint("Invalid argument for command");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            if (scripts.contains(fileName)) {
                throw new ScriptException();
            }
            scripts.add(fileName);
            while (reader.ready()) {
                String commands = reader.readLine();
                if (commands.split("\n")[0].trim().equalsIgnoreCase("execute_script"))
                    continue;
                try {
                    commandReader.executeCommand(commands.trim().toLowerCase(), null);
                } catch (CommandIsNotExistException e) {
                    try {
                        File file = new File(commands);
                        if (file.isFile())
                            continue;
                        Response response = client.communicateWithServer(commands);
                        println(response.getMessage());
                    } catch (EOFException e1) {
                        errPrint("too many bytes");
                    } catch (IOException | ClassNotFoundException ioe) {
                        errPrint(ioe.getMessage());
                    }
                }
            }
            removeScript(fileName);
        } catch (IOException e) {
            errPrint(e.getMessage());
        }
        scripts.clear();
    }

    private void removeScript(String fileName) {
        for (String st : scripts) {
            if (st.equals(fileName)) {
                scripts.remove(st);
                break;
            }
        }
    }

    @Override
    public boolean isStudyGroupCommand() {
        return false;
    }
}