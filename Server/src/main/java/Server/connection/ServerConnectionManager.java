package Server.connection;

import java.nio.channels.ServerSocketChannel;

public interface ServerConnectionManager {
    ServerSocketChannel openConnection(int port);
}
