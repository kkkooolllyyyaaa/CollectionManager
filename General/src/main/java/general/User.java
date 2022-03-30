package general;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class User implements Serializable {
    @Serial
    private final static long serialVersionUID = 567897L;
    private final String userName;
    private String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = SHA384Coder.encryptPassword(password);
    }

    public User(String userName) {
        this.userName = userName;
    }
}
