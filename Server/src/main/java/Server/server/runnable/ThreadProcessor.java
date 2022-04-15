package Server.server.runnable;

public interface ThreadProcessor extends Runnable {
    void process();

    void shutDownExecutorServices();

    void run();
}
