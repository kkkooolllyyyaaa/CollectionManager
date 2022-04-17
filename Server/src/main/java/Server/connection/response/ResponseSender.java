package Server.connection.response;

import general.Response;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface ResponseSender {

    void setSocket(SocketChannel socketChannel);

    void sendResponse(Response response) throws IOException;
}
