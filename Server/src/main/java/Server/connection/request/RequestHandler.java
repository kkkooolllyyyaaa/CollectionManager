package Server.connection.request;

import exceptions.BadPasswordException;
import exceptions.CommandIsNotExistException;
import exceptions.NotAuthorizeException;
import exceptions.UserExistenceException;
import general.Request;
import general.Response;

public interface RequestHandler {
    Response handleRequest(Request request) throws
            CommandIsNotExistException,
            BadPasswordException,
            NotAuthorizeException,
            UserExistenceException;
}
