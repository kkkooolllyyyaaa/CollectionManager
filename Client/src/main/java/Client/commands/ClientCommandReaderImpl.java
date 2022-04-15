package Client.commands;

import exceptions.CommandIsNotExistException;
import general.AbstractCommand;
import general.StudyGroup;

import java.util.Collection;
import java.util.HashMap;


/**
 * Класс, отвечающий за общение Client и CollectionManagerImpl
 * Является пунктом упрравления для всех команд
 */
public class ClientCommandReaderImpl implements ClientCommandReader {
    private final HashMap<String, AbstractCommand> commandMap;

    public ClientCommandReaderImpl() {
        commandMap = new HashMap<>();
    }


    /**
     * Метод, обеспечивающий чтение команды в строковом формате
     */
    @Override
    public void executeCommand(String userCommand, StudyGroup studyGroup) throws CommandIsNotExistException {
        if (userCommand == null)
            return;
        String[] updatedUserCommand = userCommand.trim().toLowerCase().split("\\s+", 2);
        AbstractCommand command = commandMap.get(updatedUserCommand[0]);

        if (command != null)
            command.execute(updatedUserCommand);
        else if (!updatedUserCommand[0].equals(""))
            throw new CommandIsNotExistException("command is not exist");
    }


    @Override
    public void addCommand(String commandName, AbstractCommand command) {
        commandMap.put(commandName, command);
    }

    @Override
    public Collection<AbstractCommand> getCommands() {
        return commandMap.values();
    }

}