package Server.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

public class ServerConnectionManagerImpl implements ServerConnectionManager {
    private final String host = "localhost";

    @Override
    public ServerSocketChannel openConnection(int port) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(host, port));
            serverSocketChannel.configureBlocking(false);
            return serverSocketChannel;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }
}
