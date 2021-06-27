package Server.commands;

import Server.collection.CollectionManager;
import Server.collection.ServerStudyGroup;
import Server.collection.StudyGroupShower;
import Server.connection.response.ResponseCreator;
import general.AbstractCommand;

public class ShowCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final ResponseCreator responseCreator;

    public ShowCommand(CollectionManager collectionManager, ResponseCreator responseCreator) {
        super("show", " : вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.collectionManager = collectionManager;
        this.responseCreator = responseCreator;
    }

    @Override
    public void execute(String[] args) {
        StringBuilder sb = new StringBuilder("Элементов в коллекции: " + collectionManager.getStudyGroups().size()).append("\n");
        for (ServerStudyGroup sg : collectionManager.getStudyGroupsSortedByUsername()) {
            sb.append(StudyGroupShower.toStrView(sg));
        }
        responseCreator.addToMsg(sb.toString());
    }
}
