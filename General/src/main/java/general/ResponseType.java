package general;


import java.io.Serial;
import java.io.Serializable;

public enum ResponseType implements Serializable {
    BASIC_RESPONSE,
    STUDY_GROUP_RESPONSE,
    ERROR_RESPONSE;
    @Serial
    private final static long serialVersionUID = 8260L;
}
