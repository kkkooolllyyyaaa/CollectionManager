package Server.server.runnable;

import Server.connection.request.RequestReader;
import general.IOImpl;
import general.Request;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

public class ReadThread implements Callable<Request>, IOImpl {
    private final SocketChannel socketChannel;
    private final RequestReader reader;

    public ReadThread(SocketChannel socketChannel, RequestReader reader) {
        this.socketChannel = socketChannel;
        this.reader = reader;
    }

    @Override
    public Request call() {
        try {
            return reader.readRequest(socketChannel);
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
            return null;
        }
    }
}
