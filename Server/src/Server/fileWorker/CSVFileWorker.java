package Server.fileWorker;

public interface CSVFileWorker extends FileWorker {
    String getSep();

    void setSep(String separator);
}
