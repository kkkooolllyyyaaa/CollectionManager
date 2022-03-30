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
public class Coordinates implements Serializable {
    @Serial
    private static final long serialVersionUID = -23406L;
    private int x; //Значение поля должно быть больше -393
    private Long y; //Значение поля должно быть больше -741, Поле не может быть null
}
