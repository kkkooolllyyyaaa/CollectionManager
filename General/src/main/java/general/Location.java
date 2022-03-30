package general;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Location implements Serializable {
    @Serial
    private static final long serialVersionUID = 7404L;
    private long x;
    private Long y; //Поле не может быть null
    private Long z; //Поле не может быть null
    private String name; //Поле не может быть null
}
