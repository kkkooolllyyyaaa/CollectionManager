package Server.connection.request;


import general.Request;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface RequestReader {
    Request readRequest(SocketChannel socketChannel) throws IOException, ClassNotFoundException;
}
