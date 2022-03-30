package general;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
@Getter
@Setter
public class Request implements Serializable {
    @Serial
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
}
