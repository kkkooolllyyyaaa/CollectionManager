package Server.connection.request;


import general.Request;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface RequestReader {
    void setSocket(SocketChannel socketChannel);

    Request readRequest() throws IOException, ClassNotFoundException;

}
