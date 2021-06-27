package Client.connection;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface ClientConnectionManager {

    SocketChannel openConnection(int port) throws IOException;

    void closeConnection() throws IOException;

    SocketChannel getSocketChannel();

}
