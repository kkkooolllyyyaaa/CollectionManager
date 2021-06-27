package Server.server.runnable;

import java.nio.channels.SocketChannel;

public interface ThreadProcessor {
    void run(SocketChannel socketChannel);

    void shutDownExecutorServices();
}
