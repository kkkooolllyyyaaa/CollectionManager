package validation;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InputCheckerTest {

    @ParameterizedTest
    @ValueSource(strings = {"123A", "A321", "504.21", "", "10.0", "1e9", "9223372036854775807"})
    @NullSource
    void checkBadInts(String badInteger) {
        assertFalse(InputChecker.checkInt(badInteger));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "0", "0321", "1033010301", "9"})
    void checkGoodInts(String goodInteger) {
        assertTrue(InputChecker.checkInt(goodInteger));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123L", "9223372036854775808", "008127A", "505$"})
    @NullSource
    void checkBadLong(String badLong) {
        assertFalse(InputChecker.checkLong(badLong));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "0", "0321", "10330103010000", "9", "9223372036854775807"})
    void checkGoodLong(String goodLong) {
        assertTrue(InputChecker.checkLong(goodLong));
    }
}