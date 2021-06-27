package Server.commands;


import Server.collection.CollectionManager;
import Server.fileWorker.StudyGroupWriter;

public class SaveCommand implements ServerCommand {
    private final CollectionManager collectionManager;
    private final StudyGroupWriter writer;

    public SaveCommand(CollectionManager collectionManager, StudyGroupWriter writer) {
        this.collectionManager = collectionManager;
        this.writer = writer;
    }

    @Override
    public void execute() {
        writer.write();
        System.out.println("The collection is saved successfully");
    }
}
