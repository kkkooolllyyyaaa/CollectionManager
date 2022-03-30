package general;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Person implements Serializable {
    @Serial
    private static final long serialVersionUID = 6885L;
    private String name; //Поле не может быть null, Строка не может быть пустой
    private String passportID; //Поле не может быть null
    private Location location; //Поле может быть null
}
