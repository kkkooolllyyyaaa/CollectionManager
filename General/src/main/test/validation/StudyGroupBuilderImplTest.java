package validation;

import exceptions.EnumNotFoundException;
import general.FormOfEducation;
import general.IO;
import general.Semester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class StudyGroupBuilderImplTest {
    private StudyGroupBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new StudyGroupBuilderImpl(IO.getReader(), false, new StudyGroupValidatorImpl());
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "firs", "eleventh", "SECONDD"})
    @EmptySource
    void checkSemesterDoesNotExist(String value) {
        assertThrows(EnumNotFoundException.class,
                () -> builder.checkSemester(value));
    }

    @ParameterizedTest
    @EnumSource(Semester.class)
    void checkSemesterExists(Semester value) {
        Semester semester = assertDoesNotThrow(() -> builder.checkSemester(value.getStr()));
        assertNotNull(semester);
    }

    @ParameterizedTest
    @ValueSource(strings = {"first", "second", "FOurTH", "sIxTh", "SEVENTH"})
    void checkSemesterExistsAnyCase(String value) {
        Semester semester = assertDoesNotThrow(() -> builder.checkSemester(value));
        assertNotNull(semester);
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "distance_educationn", "bad_education", "EVENING__CLASSes"})
    @EmptySource
    void checkFormOfEducationDoesNotExist(String value) {
        assertThrows(EnumNotFoundException.class,
                () -> builder.checkFormOfEducation(value));
    }


    @ParameterizedTest
    @EnumSource(FormOfEducation.class)
    void checkFormOfEducationExists(FormOfEducation value) {
        FormOfEducation form = assertDoesNotThrow(() -> builder.checkFormOfEducation(value.getStr()));
        assertNotNull(form);
    }

    @ParameterizedTest
    @ValueSource(strings = {"distance_education", "fulL_TImE_EDUCATION", "eVENING_CLASSEs"})
    void checkFormOfEducationExistsAnyCase(String value) {
        FormOfEducation form = assertDoesNotThrow(() -> builder.checkFormOfEducation(value));
        assertNotNull(form);
    }
}
