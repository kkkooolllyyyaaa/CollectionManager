package Server.commands;


import Server.collection.ServerStudyGroup;
import general.Command;

public interface StudyGroupCommand extends Command {
    void execute(String[] args, ServerStudyGroup studyGroup);

    default boolean isStudyGroupCommand() {
        return true;
    }
}
