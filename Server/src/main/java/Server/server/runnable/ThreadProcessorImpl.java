package Server.server.runnable;

import Server.connection.request.RequestHandler;
import Server.connection.request.RequestReader;
import Server.connection.response.ResponseSender;
import general.IO;
import general.Request;
import general.Response;

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
    public void run() {
        process();
    }

    @Override
    public void process() {
        try {
            Callable<Request> callable1 = new ReadThread(requestReader);
            FutureTask<Request> futureTask1 = new FutureTask<>(callable1);
            IO.println("Reading request...");
            Thread readThread = new Thread(futureTask1);
            readThread.start();
            Request request = futureTask1.get();
            String username = (request.getUser() != null) ? request.getUser().getUserName() : "UNKNOWN";
            IO.println("Request received from " + username);
            readThread.join();

            Callable<Response> callable2 = new HandleThread(requestHandler, request);
            FutureTask<Response> futureTask2 = new FutureTask<>(callable2);
            IO.println("Handling request...");
            executorService.submit(futureTask2);
            Response response = futureTask2.get();
            IO.println(request.getCommandName() + " request from " + username + " handled");

            IO.println("Sending response...");
            new Thread(new SendThread(responseSender, response)).start();
            IO.println("Response send\n");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            shutDownExecutorServices();
        }
    }

    @Override
    public void shutDownExecutorServices() {
        executorService.shutdownNow();
    }
}
