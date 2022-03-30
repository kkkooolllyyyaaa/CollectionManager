package general;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = -19764L;
    @Getter
    private final ResponseType responseType;
    @Getter
    @Setter
    private String message;

    public Response(ResponseType responseType, String message) {
        this.responseType = responseType;
        this.message = message;
    }

    public Response() {
        message = "";
        responseType = ResponseType.BASIC_RESPONSE;
    }
}
