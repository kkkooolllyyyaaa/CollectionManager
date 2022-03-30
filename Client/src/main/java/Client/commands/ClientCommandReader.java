package Client.commands;


import exceptions.CommandIsNotExistException;
import general.AbstractCommand;
import general.StudyGroup;

import java.util.TreeMap;

public interface ClientCommandReader {

    void executeCommand(String userCommand, StudyGroup studyGroup) throws CommandIsNotExistException;

    void addCommand(String commandName, AbstractCommand command);

    TreeMap<String, AbstractCommand> getCommandMap();
}
