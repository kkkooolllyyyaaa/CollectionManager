package Server.server.runnable;

import Server.connection.request.RequestHandler;
import Server.connection.request.RequestReader;
import Server.connection.response.ResponseSender;
import general.IOImpl;
import general.Request;
import general.Response;

import java.nio.channels.SocketChannel;
import java.util.concurrent.*;

public class ThreadProcessorImpl implements ThreadProcessor, IOImpl, Runnable {
    private final RequestReader requestReader;
    private final RequestHandler requestHandler;
    private final ResponseSender responseSender;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final SocketChannel socketChannel;

    public ThreadProcessorImpl(RequestReader requestReader, RequestHandler requestHandler, ResponseSender responseSender, SocketChannel socketChannel) {
        this.requestReader = requestReader;
        this.requestHandler = requestHandler;
        this.responseSender = responseSender;
        this.socketChannel = socketChannel;

    }

    @Override
    public void run() {
        process();
    }

    @Override
    public void process() {
        try {
            Callable<Request> callable1 = new ReadThread(socketChannel, requestReader);
            FutureTask<Request> futureTask1 = new FutureTask<>(callable1);
            println("Reading request...");
            Thread readThread = new Thread(futureTask1);
            readThread.start();
            Request request = futureTask1.get();
            readThread.join();

            Callable<Response> callable2 = new HandleThread(requestHandler, request);
            FutureTask<Response> futureTask2 = new FutureTask<>(callable2);
            println("Handling request...");
            executorService.submit(futureTask2);
            Response response = futureTask2.get();

            println("Sending response...\n");
            new Thread(new SendThread(responseSender, socketChannel, response)).start();

        } catch (InterruptedException | ExecutionException e) {
            shutDownExecutorServices();
            e.printStackTrace();
        }
    }

    @Override
    public void shutDownExecutorServices() {
        executorService.shutdownNow();
    }
}
