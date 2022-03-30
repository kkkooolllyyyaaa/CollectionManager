package Server.server.runnable;

public interface ThreadProcessor {
    void process();

    void shutDownExecutorServices();
}
