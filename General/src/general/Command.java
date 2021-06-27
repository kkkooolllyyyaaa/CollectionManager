package general;

public interface Command {
    void execute(String[] args);

    default boolean isStudyGroupCommand() {
        return false;
    }
}
