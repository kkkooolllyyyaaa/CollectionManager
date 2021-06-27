package Server.connection.response;

import general.Response;
import general.ResponseType;

public interface ResponseCreator {
    void addToMsg(String message);

    Response getResponse(String message, ResponseType responseType);

    Response getResponse();
}
