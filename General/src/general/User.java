package general;

import java.io.Serializable;

public class User implements Serializable {
    private final String userName;
    private String password;
    private final static long serialVersionUID = 567897L;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = SHA384Coder.encryptPassword(password);
    }

    public User(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User))
            return false;
        User user = (User) obj;
        return user.getUserName().equals(this.userName);
    }

    @Override
    public String toString() {
        return "username : " + getUserName() +
                "\n" +
                "password: " + getPassword() +
                "\n";
    }
}
