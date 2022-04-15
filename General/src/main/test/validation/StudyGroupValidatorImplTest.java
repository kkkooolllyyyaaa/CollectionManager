package validation;

import exceptions.InvalidFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class StudyGroupValidatorImplTest {
    private StudyGroupValidator validator;

    @BeforeEach
    void setUp() {
        validator = new StudyGroupValidatorImpl();
    }

    @Test
    void validateVoidName() {
        InvalidFieldException exception =
                assertThrows(InvalidFieldException.class, () -> validator.validateName(""));

        assertEquals(exception.getMessage(), "Invalid value for StudyGroup name");
    }

    @Test
    void validateNullName() {
        InvalidFieldException exception =
                assertThrows(InvalidFieldException.class, () -> validator.validateName(null));

        assertEquals(exception.getMessage(), "Invalid value for StudyGroup name");
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "kolya", "@!&#*!@HIAJIu1ehqwejh12u3y129y7129837129jasju182y32981"})
    void validateGoodNames(String value) {
        assertDoesNotThrow(() -> validator.validateName(value));
    }

    @ParameterizedTest
    @ValueSource(ints = {-393, -4000, Integer.MIN_VALUE})
    void validateBadCoordinatesX(int value) {
        assertThrows(InvalidFieldException.class,
                () -> validator.validateCoordinateX(value));
    }

    @ParameterizedTest
    @ValueSource(ints = {-392, -391, 0, 393, Integer.MAX_VALUE})
    void validateGoodCoordinatesX(int value) {
        assertDoesNotThrow(() -> validator.validateCoordinateX(value));
    }

    @ParameterizedTest
    @ValueSource(longs = {-741L, -4000L, Long.MIN_VALUE, -745})
    void validateBadCoordinatesY(Long value) {
        assertThrows(InvalidFieldException.class,
                () -> validator.validateCoordinateY(value));
    }

    @ParameterizedTest
    @ValueSource(longs = {-740L, -739L, 741L, 123, Long.MAX_VALUE})
    void validateGoodCoordinatesY(Long value) {
        assertDoesNotThrow(() -> validator.validateCoordinateY(value));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100})
    void validateBadStudentsCounts(int value) {
        assertThrows(InvalidFieldException.class,
                () -> validator.validateStudentsCount(value));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, Integer.MAX_VALUE})
    void validateGoodStudentsCounts(int value) {
        assertDoesNotThrow(() -> validator.validateStudentsCount(value));
    }

    @Test
    void validateVoidGAName() {
        InvalidFieldException exception =
                assertThrows(InvalidFieldException.class, () -> validator.validateGAName(""));

        assertEquals(exception.getMessage(), "Invalid value for StudyGroup GroupAdmin name");
    }

    @Test
    void validateNullGAName() {
        InvalidFieldException exception =
                assertThrows(InvalidFieldException.class, () -> validator.validateGAName(null));

        assertEquals(exception.getMessage(), "Invalid value for StudyGroup GroupAdmin name");
    }

}
