package Server.server.runnable;

import Server.connection.request.RequestHandler;
import Server.connection.request.RequestReader;
import Server.connection.response.ResponseSender;
import general.IOimpl;
import general.Request;
import general.Response;

import java.nio.channels.SocketChannel;
import java.util.concurrent.*;

public class ThreadProcessorImpl implements ThreadProcessor, IOimpl {
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
        try {
            Callable<Request> callable1 = new ReadThread(socketChannel, requestReader);
            FutureTask<Request> futureTask1 = new FutureTask<>(callable1);
            println("Reading request..." + Thread.currentThread().getName());
            Thread readThread = new Thread(futureTask1);
            readThread.start();
            Request request = futureTask1.get();
            readThread.join();

            Callable<Response> callable2 = new HandleThread(requestHandler, request);
            FutureTask<Response> futureTask2 = new FutureTask<>(callable2);
            println("Handling request..." + Thread.currentThread().getName());
            executorService.submit(futureTask2);
            Response response = futureTask2.get();

            println("Sending response..." + Thread.currentThread().getName() + "\n");
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
