package Server.server.runnable;

import Server.connection.request.RequestHandler;
import exceptions.BadPasswordException;
import exceptions.CommandIsNotExistException;
import exceptions.NotAuthorizeException;
import exceptions.UserExistenceException;
import general.Request;
import general.Response;
import general.ResponseType;

import java.util.concurrent.Callable;

public class HandleThread implements Callable<Response> {
    private final RequestHandler requestHandler;
    private final Request request;

    public HandleThread(RequestHandler requestHandler, Request request) {
        this.requestHandler = requestHandler;
        this.request = request;
    }

    @Override
    public Response call() {
        try {
            return requestHandler.handleRequest(request);
        } catch (CommandIsNotExistException | BadPasswordException | NotAuthorizeException | UserExistenceException e) {
            Response response = new Response(ResponseType.ERROR_RESPONSE, e.getMessage());
            response.setMessage(response.getMessage());
            return response;
        }
    }
}
