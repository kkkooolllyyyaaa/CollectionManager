package Server.connection.request;

import Server.collection.CollectionManager;
import Server.commands.ServerCommandReader;
import Server.connection.response.ResponseCreator;
import Server.user_manager.UserManager;
import exceptions.BadPasswordException;
import exceptions.CommandIsNotExistException;
import exceptions.NotAuthorizeException;
import exceptions.UserExistenceException;
import general.*;

public class RequestHandlerImpl implements RequestHandler {
    private final UserManager userManager;
    private final ServerCommandReader commandReader;
    private final CollectionManager collectionManager;
    private final ResponseCreator responseCreator;
    private Response response;

    public RequestHandlerImpl(UserManager userManager,
                              ServerCommandReader commandReader,
                              CollectionManager collectionManager,
                              ResponseCreator responseCreator) {
        this.userManager = userManager;
        this.commandReader = commandReader;
        this.collectionManager = collectionManager;
        this.responseCreator = responseCreator;
    }


    @Override
    public Response handleRequest(Request request) throws
            CommandIsNotExistException,
            BadPasswordException,
            NotAuthorizeException,
            UserExistenceException {

        if (userManager.isRegistered(request.getUser())) {
            if (request.getRequestType().equals(RequestType.EXECUTE_COMMAND)) {
                response = handleExecuteRequest(request);
            } else if (request.getRequestType().equals(RequestType.ERROR_TYPE_REQUEST)) {
                response = handleErrorTypeRequest();
            } else if (request.getRequestType() == RequestType.REGISTRATION_REQUEST) {
                response = handleRegisterRequest(request);
            } else if (request.getRequestType() == RequestType.AUTHORIZATION_REQUEST) {
                response = handleAuthRequest(request);
            } else {
                response = handleCommandRequest(request);
            }
        } else if (request.getRequestType() == RequestType.REGISTRATION_REQUEST) {
            response = handleRegisterRequest(request);
        } else if (request.getRequestType() == RequestType.AUTHORIZATION_REQUEST) {
            response = handleAuthRequest(request);
        } else {
            throw new NotAuthorizeException();
        }
        return response;
    }


    private Response handleAuthRequest(Request request) throws BadPasswordException, UserExistenceException {
        if (userManager.isRegistered(request.getUser())) {
            response = responseCreator.getResponse();
            response.setMessage(response.getMessage() + "Authorization is successful");
        } else {
            if (userManager.isExist(request.getUser())) {
                throw new BadPasswordException("Incorrect password");
            } else {
                throw new UserExistenceException("User is not exist");
            }
        }
        return response;
    }

    private Response handleRegisterRequest(Request request) throws UserExistenceException, BadPasswordException {
        if (userManager.isRegistered(request.getUser())) {
            throw new UserExistenceException("This user is already registered");
        } else {
            if (!userManager.isExist(request.getUser())) {
                userManager.insertToRegistered(request.getUser());
                response = responseCreator.getResponse();
                response.setMessage(response.getMessage() + "Registration is successful");
            } else {
                throw new UserExistenceException("This username is already used");
            }
        }
        return response;
    }

    private Response handleExecuteRequest(Request request) throws CommandIsNotExistException {
        String addingString = request.getCommandName().trim();
        if (request.getArg() != null)
            addingString = addingString + " " + request.getArg().trim();
        else
            addingString = addingString + " " + "null";

        addingString = addingString + " " + request.getUser().getUserName();
        commandReader.executeCommand(addingString, collectionManager.getServerStudyGroup(request.getStudyGroup()));
        response = responseCreator.getResponse();
        return response;
    }

    private Response handleCommandRequest(Request request) throws CommandIsNotExistException {
        Command command = commandReader.getCommandByName(request.getCommandName().trim().toLowerCase());
        if (request.getCommandName().trim().equalsIgnoreCase("execute_script")) {
            return new Response(ResponseType.BASIC_RESPONSE, "");
        }
        if (command == null) {
            response = responseCreator.getResponse();
            response.setMessage(response.getMessage() + "Command is not exist");
        } else if (command.isStudyGroupCommand()) {
            response = responseCreator.getResponse("", ResponseType.STUDY_GROUP_RESPONSE);
        } else {
            String addingString = request.getCommandName();
            if (request.getArg() != null)
                addingString += " " + request.getArg();
            else
                addingString += " " + "null";
            addingString = addingString + " " + request.getUser().getUserName();
            commandReader.executeCommand(addingString, null);
            response = responseCreator.getResponse();
        }
        return response;
    }

    private Response handleErrorTypeRequest() {
        return new Response(ResponseType.ERROR_RESPONSE, "Your message is too big");
    }
}
