package general;

import lombok.Getter;

import java.io.Serializable;
@Getter
public enum Semester implements Serializable {
    FIRST("FIRST", 1),
    SECOND("SECOND", 2),
    FOURTH("FOURTH", 4),
    SIXTH("SIXTH", 6),
    SEVENTH("SEVENTH", 7);
    private static final long serialVersionUID = -6358L;

    private final int value;
    private final String str;

    Semester(String str, int value) {
        this.value = value;
        this.str = str;
    }

    public static void printValues() {
        System.out.println("List of SEMESTER enum values:");
        for (Semester semester : Semester.values()) {
            System.out.println(semester.getStr());
        }
    }
}
