package general;

import java.io.Serializable;

public class Request implements Serializable {

    private final static long serialVersionUID = -14567L;

    private final RequestType requestType;

    private String commandName;

    private String arg;

    private StudyGroup studyGroup;

    private User user;

    public Request(RequestType requestType, String commandName, String arg) {
        this.requestType = requestType;
        this.commandName = commandName;
        this.arg = arg;
    }

    public Request(RequestType requestType, User user) {
        this.requestType = requestType;
        this.user = user;
    }

    public RequestType getRequestType() {
        return requestType;
    }


    public String getCommandName() {
        return commandName;
    }

    public String getArg() {
        return arg;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }

    public void setStudyGroup(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
