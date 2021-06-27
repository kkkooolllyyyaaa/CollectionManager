package Client.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class ClientConnectionManagerImpl implements ClientConnectionManager {
    private SocketChannel socketChannel;

    public SocketChannel openConnection(int port) throws IOException {
        socketChannel = SocketChannel.open(new InetSocketAddress("localhost", port));
        return socketChannel;
    }

    public void closeConnection() throws IOException {
        socketChannel.close();
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }
}
