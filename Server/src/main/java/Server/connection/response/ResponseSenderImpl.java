package Server.connection.response;

import general.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ResponseSenderImpl implements ResponseSender {
    private SocketChannel socketChannel;

    @Override
    public void sendResponse(Response response) throws IOException {
        sendBytes(serializeResponse(response));
    }

    @Override
    public void setSocket(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
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
