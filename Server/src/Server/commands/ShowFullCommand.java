package Server.commands;

import Server.collection.CollectionManager;
import Server.collection.ServerStudyGroup;
import Server.collection.StudyGroupShower;
import Server.connection.response.ResponseCreator;
import general.AbstractCommand;

public class ShowFullCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final ResponseCreator responseCreator;

    public ShowFullCommand(CollectionManager collectionManager, ResponseCreator responseCreator) {
        super("show_full", " : вывести в стандартный поток вывода все элементы коллекции со всеми полями");
        this.collectionManager = collectionManager;
        this.responseCreator = responseCreator;
    }

    @Override
    public void execute(String[] args) {
        StringBuilder sb = new StringBuilder("Элементов в коллекции: " + collectionManager.getStudyGroups().size()).append("\n");
        for (ServerStudyGroup sg : collectionManager.getStudyGroupsSortedByUsername()) {
            sb.append(StudyGroupShower.toStrViewFull(sg));
        }
        responseCreator.addToMsg(sb.toString());
    }
}
