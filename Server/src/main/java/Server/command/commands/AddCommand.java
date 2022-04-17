package Server.command.commands;


import Server.collection.CollectionManager;
import Server.collection.ServerStudyGroup;
import Server.command.StudyGroupCommand;
import Server.connection.response.ResponseCreator;
import exceptions.InsertException;
import exceptions.InvalidCommandType;
import general.AbstractCommand;

public class AddCommand extends AbstractCommand implements StudyGroupCommand {
    private final CollectionManager collectionManagerImpl;
    private final ResponseCreator creator;

    public AddCommand(CollectionManager collectionManager, ResponseCreator creator) {
        super("add {element}", " : добавить новый элемент в коллекцию", true);
        this.collectionManagerImpl = collectionManager;
        this.creator = creator;
    }


    @Override
    public void execute(String[] args) {
        throw new InvalidCommandType("add command required Study Group instance");
    }

    @Override
    public void execute(String[] args, ServerStudyGroup studyGroup) {
        try {
            collectionManagerImpl.addElement(studyGroup);
            creator.addToMsg("Element is successfully added");
        } catch (InsertException e) {
            creator.addToMsg(e.getMessage());
        }
    }

    @Override
    public boolean isStudyGroupCommand() {
        return true;
    }
}