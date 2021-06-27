package Server.fileWorker;

import Server.collection.ServerStudyGroup;
import general.*;
import exceptions.*;

public interface StudyGroupReader extends CSVFileWorker {
    ServerStudyGroup read(String[] values);

    void loadInput();

    void setFilePath(String fileName) throws UnsupportedFileException;

}
