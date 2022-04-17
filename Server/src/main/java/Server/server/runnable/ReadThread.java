package Server.server.runnable;

import Server.connection.request.RequestReader;
import general.Request;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

public class ReadThread implements Callable<Request> {
    private final RequestReader reader;

    public ReadThread(RequestReader reader) {
        this.reader = reader;
    }

    @Override
    public Request call() {
        try {
            return reader.readRequest();
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
            return null;
        }
    }
}
