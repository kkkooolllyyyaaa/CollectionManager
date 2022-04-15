package Server.command.commands;

import Server.collection.CollectionManager;
import Server.collection.StudyGroupShower;
import Server.connection.response.ResponseCreator;
import general.AbstractCommand;

import java.util.stream.Collectors;


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

        String str = "Ваших элементов в коллекции: " + cnt + "\n" +
                collectionManager.getStudyGroups().stream().
                        filter(x -> x.getUsername().equals(username)).
                        map(StudyGroupShower::toStrView).collect(Collectors.joining());

        responseCreator.addToMsg(str);
    }
}
