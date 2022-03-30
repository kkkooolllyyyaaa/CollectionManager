package Server.connection.response;

import general.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ResponseSenderImpl implements ResponseSender {
    private SocketChannel socketChannel;

    public void sendResponse(SocketChannel socketChannel, Response response) throws IOException {
        this.socketChannel = socketChannel;
        sendBytes(serializeResponse(response));
    }

    private void sendBytes(byte[] bytes) throws IOException {
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        socketChannel.write(buf);
    }


    private byte[] serializeResponse(Response response) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(byteStream);
        stream.writeObject(response);
        return byteStream.toByteArray();
    }
}
