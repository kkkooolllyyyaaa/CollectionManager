package Server.connection.request;

import general.Request;
import general.RequestType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class RequestReaderImpl implements RequestReader {
    private SocketChannel socketChannel;

    @Override
    public void setSocket(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public Request readRequest() throws IOException, ClassNotFoundException {
        byte[] bytes;
        try {
            bytes = readBytes();
        } catch (BufferOverflowException e) {
            return new Request(RequestType.ERROR_TYPE_REQUEST, "no command", "");
        }
        return deserializeRequest(bytes);
    }

    private byte[] readBytes() throws IOException {
        int capacity = 16384;
        ByteBuffer buf = ByteBuffer.allocate(capacity);
        socketChannel.read(buf);
        return buf.array();
    }

    private Request deserializeRequest(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream stream;
        stream = new ObjectInputStream(inputStream);
        Request request = (Request) stream.readObject();
        stream.close();
        return request;
    }
}
