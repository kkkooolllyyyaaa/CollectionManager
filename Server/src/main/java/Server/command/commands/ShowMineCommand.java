package Server.command.commands;

import Server.collection.CollectionManager;
import Server.collection.StudyGroupShower;
import Server.connection.response.ResponseCreator;
import general.AbstractCommand;

/**
 * @author tsypk on 14.04.2022 21:24
 * @project Lab7
 */
public class ShowMineCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final ResponseCreator responseCreator;

    public ShowMineCommand(CollectionManager collectionManager, ResponseCreator responseCreator) {
        super("show_mine", " : вывести в стандартный поток вывода элементы коллекции, принадлежищие текущему пользователю, в строковом представлении");
        this.collectionManager = collectionManager;
        this.responseCreator = responseCreator;
    }

    @Override
    public void execute(String[] args) {
        String username = args[2];
        long cnt = collectionManager.getStudyGroups().stream().filter(x -> x.getUsername().equals(username)).count();

        StringBuilder sb = new StringBuilder("Ваших элементов в коллекции: " + cnt).append("\n");

        collectionManager.getStudyGroups().stream().filter(x -> x.getUsername().equals(username)).
                forEach(x -> sb.append(StudyGroupShower.toStrView(x)));

        responseCreator.addToMsg(sb.toString());
    }
}
