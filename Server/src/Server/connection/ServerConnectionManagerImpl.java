package Server.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

public class ServerConnectionManagerImpl implements ServerConnectionManager {
    private ServerSocketChannel serverSocketChannel;


    @Override
    public ServerSocketChannel openConnection(int port) {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress("localhost", port));
            serverSocketChannel.configureBlocking(false);
            return serverSocketChannel;

        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }

    @Override
    public void closeConnection() {
        try {
            serverSocketChannel.socket().close();
            serverSocketChannel.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}