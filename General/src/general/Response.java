package general;

import java.io.Serial;
import java.io.Serializable;

public class Response implements Serializable {

    @Serial
    private static final long serialVersionUID = -19764L;
    private final ResponseType responseType;
    private String message;

    public Response(ResponseType responseType, String message) {
        this.responseType = responseType;
        this.message = message;
    }

    public Response() {
        message = "";
        responseType = ResponseType.BASIC_RESPONSE;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseType getResponseType() {
        return responseType;
    }
}
