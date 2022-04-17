package general;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum FormOfEducation implements Serializable {
    DISTANCE_EDUCATION("DISTANCE_EDUCATION"),
    FULL_TIME_EDUCATION("FULL_TIME_EDUCATION"),
    EVENING_CLASSES("EVENING_CLASSES");
    private static final long serialVersionUID = 25236L;

    private final String str;

    FormOfEducation(String str) {
        this.str = str;
    }

    public static void printValues() {
        System.out.println("List of FormOfEducation enum values:");
        for (FormOfEducation fOE : FormOfEducation.values()) {
            System.out.println(fOE.getStr());
        }
    }
}
