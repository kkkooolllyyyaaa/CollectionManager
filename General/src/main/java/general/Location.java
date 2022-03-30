package general;

import java.io.Serializable;

public class Location implements Serializable {
    private static final long serialVersionUID = 7404L;
    private long x;
    private Long y; //Поле не может быть null
    private Long z; //Поле не может быть null
    private String name; //Поле не может быть null

    public Location() {
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public long getZ() {
        return z;
    }

    public void setZ(Long z) {
        this.z = z;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", name='" + name + '\'' +
                '}';
    }
}
