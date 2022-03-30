package Server.server.runnable;

import Server.connection.response.ResponseSender;
import general.Response;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class SendThread implements Runnable {
    private final ResponseSender responseSender;
    private final SocketChannel socketChannel;
    private final Response response;

    public SendThread(ResponseSender responseSender, SocketChannel socketChannel, Response response) {
        this.responseSender = responseSender;
        this.socketChannel = socketChannel;
        this.response = response;
    }


    @Override
    public void run() {
        try {
            responseSender.sendResponse(socketChannel, response);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
