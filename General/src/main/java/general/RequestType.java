package general;

import java.io.Serializable;

public enum RequestType implements Serializable {
    COMMAND_REQUEST,

    EXECUTE_COMMAND,

    REGISTRATION_REQUEST,

    AUTHORIZATION_REQUEST,

    ERROR_TYPE_REQUEST;

    private final static long serialVersionUID = -243288L;
}
