package Client.connection.request;


import general.Request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.channels.SocketChannel;

public class RequestSenderImpl implements RequestSender {
    @Override
    public void sendRequest(SocketChannel socketChannel, Request request) throws IOException {
        byte[] bytes = serializeRequest(request);
        OutputStream stream = socketChannel.socket().getOutputStream();
        stream.write(bytes);
        stream.flush();
    }

    private byte[] serializeRequest(Request request) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream);
        objectOutputStream.writeObject(request);
        return byteStream.toByteArray();
    }
}
