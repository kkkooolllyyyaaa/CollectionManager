package Client.commands;

import exceptions.CommandIsNotExistException;
import general.AbstractCommand;
import general.IOImpl;
import general.StudyGroup;

import java.util.HashSet;
import java.util.TreeMap;


/**
 * Класс, отвечающий за общение Client и CollectionManagerImpl
 * Является пунктом упрравления для всех команд
 */
public class ClientCommandReaderImpl implements ClientCommandReader, IOImpl {
    private final TreeMap<String, AbstractCommand> commandMap;
    private final HashSet<String> scriptSet;

    public ClientCommandReaderImpl() {
        commandMap = new TreeMap<>();
        scriptSet = new HashSet<>();
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
        if (command != null) {
            if (!command.isStudyGroupCommand())
                command.execute(updatedUserCommand);
            else
                throw new RuntimeException();
        } else if (!updatedUserCommand[0].equals(""))
            throw new CommandIsNotExistException("Данной команды не существует");
    }


    @Override
    public void addCommand(String commandName, AbstractCommand command) {
        commandMap.put(commandName, command);
    }

    @Override
    public TreeMap<String, AbstractCommand> getCommandMap() {
        return commandMap;
    }

    public void addScript(String name) {
        scriptSet.add(name);
    }

    public void removeScript(String name) {
        scriptSet.remove(name);
    }

}