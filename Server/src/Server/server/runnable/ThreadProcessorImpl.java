package Server.server.runnable;

import Server.connection.request.RequestHandler;
import Server.connection.request.RequestReader;
import Server.connection.response.ResponseSender;
import general.Request;
import general.Response;

import java.nio.channels.SocketChannel;
import java.util.concurrent.*;

public class ThreadProcessorImpl implements ThreadProcessor {
    private final RequestReader requestReader;
    private final RequestHandler requestHandler;
    private final ResponseSender responseSender;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public ThreadProcessorImpl(RequestReader requestReader, RequestHandler requestHandler, ResponseSender responseSender) {
        this.requestReader = requestReader;
        this.requestHandler = requestHandler;
        this.responseSender = responseSender;

    }

    @Override
    public void run(SocketChannel socketChannel) {
        Callable<Request> callable1 = new ReadThread(socketChannel, requestReader);
        FutureTask<Request> futureTask1 = new FutureTask<>(callable1);
        System.out.println("Reading request...");
        new Thread(futureTask1).start();
        Request request = null;
        try {
            request = futureTask1.get();
        } catch (InterruptedException | ExecutionException e) {
            shutDownExecutorServices();
            e.printStackTrace();
        }

        Callable<Response> callable2 = new HandleThread(requestHandler, request);
        FutureTask<Response> futureTask2 = new FutureTask<>(callable2);
        System.out.println("Handling request...");
        executorService.submit(futureTask2);
        Response response = null;
        try {
            response = futureTask2.get();
        } catch (InterruptedException | ExecutionException e) {
            shutDownExecutorServices();
            e.printStackTrace();
        }
        System.out.println("Sending response...");
        new Thread(new SendThread(responseSender, socketChannel, response)).start();
    }

    @Override
    public void shutDownExecutorServices() {
        executorService.shutdownNow();
    }
}
