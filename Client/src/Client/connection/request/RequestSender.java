package Client.connection.request;


import general.Request;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface RequestSender extends RequsetCreator {

    void sendRequest(SocketChannel socketChannel, Request request) throws IOException;

}