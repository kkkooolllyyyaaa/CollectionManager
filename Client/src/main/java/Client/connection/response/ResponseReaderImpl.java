package Client.connection.response;


import general.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.BufferOverflowException;
import java.nio.channels.SocketChannel;

public class ResponseReaderImpl implements ResponseReader {

    @Override
    public Response readResponse(SocketChannel socketChannel) throws IOException, ClassNotFoundException, BufferOverflowException {
        int capacity = 16384;
        byte[] bytes = new byte[capacity];
        InputStream stream = socketChannel.socket().getInputStream();
        if (capacity == stream.read(bytes)) {
            throw new BufferOverflowException();
        }
        return deserializeRequest(bytes);
    }

    private Response deserializeRequest(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream stream = new ObjectInputStream(byteStream);
        return (Response) stream.readObject();
    }
}
