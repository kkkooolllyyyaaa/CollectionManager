package Server.commands;


import Server.collection.CollectionManager;
import Server.collection.StudyGroupShower;
import Server.connection.response.ResponseCreator;
import general.*;


public class PrintAscendingCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final ResponseCreator responseCreator;

    public PrintAscendingCommand(CollectionManager collectionManager, ResponseCreator responseCreator) {
        super("print_ascending", " : вывести элементы коллекции в порядке возрастания");
        this.collectionManager = collectionManager;
        this.responseCreator = responseCreator;
    }

    @Override
    public void execute(String[] args) {
        StringBuilder sb = new StringBuilder("Элементов в коллекции: " + collectionManager.getStudyGroups().size()).append("\n");
        collectionManager.getStudyGroups()
                .forEach(x -> sb.append(StudyGroupShower.toStrView(x)));
        responseCreator.addToMsg(sb.toString());
    }
}
