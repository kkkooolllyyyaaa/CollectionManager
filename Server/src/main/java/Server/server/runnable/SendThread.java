package Server.server.runnable;

import Server.connection.response.ResponseSender;
import general.Response;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class SendThread implements Runnable {
    private final ResponseSender responseSender;
    private final Response response;

    public SendThread(ResponseSender responseSender, Response response) {
        this.responseSender = responseSender;
        this.response = response;
    }


    @Override
    public void run() {
        try {
            responseSender.sendResponse(response);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
