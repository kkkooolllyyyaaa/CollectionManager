package general;

import java.io.Serializable;

public enum FormOfEducation implements Serializable {
    DISTANCE_EDUCATION("DISTANCE_EDUCATION"),
    FULL_TIME_EDUCATION("FULL_TIME_EDUCATION"),
    EVENING_CLASSES("EVENING_CLASSES");
    private static final long serialVersionUID = 25236L;

    private final String url;

    FormOfEducation(String url) {
        this.url = url;
    }

    public static void printValues() {
        System.out.println("List of FormOfEducation enum values:");
        for (FormOfEducation fOE : FormOfEducation.values()) {
            System.out.println(fOE.getUrl());
        }
    }

    public String getUrl() {
        return url;
    }
}
