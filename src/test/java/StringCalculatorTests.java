import exceptions.NegativeNumbersFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StringCalculatorTests {

    private static StringCalculator stringCalculator;

    @BeforeAll
    static void setUp() {
        stringCalculator = new StringCalculator();
    }

    @Test
    public void parsesNumbers() {
        assertEquals(List.of(1,2,3), stringCalculator.parseNumbers("1,2,3"));
    }

    @Test
    public void findsNegatives() {
        NegativeNumbersFoundException exception = assertThrows(
                NegativeNumbersFoundException.class,
                () -> stringCalculator.checkForNegatives(List.of(-1, 2, -3))
        );

        assertEquals("Negative numbers not allowed: [-1, -3]", exception.getMessage());
    }

    @Test
    public void replacesDelimitersSimple() {
        assertEquals("1,2,3", stringCalculator.handleSingleCharDelimiter("//;\n1;2;3"));
    }

    @Test
    public void replacesDelimitersComplex() {
        assertEquals("1,2,3", stringCalculator.handleMultiCharDelimiters("//[|][%%]1|2%%3"));
    }

    @Test
    public void sumsNumbersAndDoesntCountOver1000() {
        assertEquals(1004, stringCalculator.sumValidNumbers(List.of(1001, 2, 3, 999)));
    }

    @Test
    public void emptyStringReturnsZero() {
        assertEquals(0, stringCalculator.add(""));
    }

    @Test
    public void singleNumberReturnsNumber() {
        assertEquals(1, stringCalculator.add("1"));
    }

    @Test
    public void twoNumbersReturnsSum() {
        assertEquals(3, stringCalculator.add("1,2"));
    }

    @Test
    public void fiveNumbersReturnsSum() {
        assertEquals(15, stringCalculator.add("1,2,3,4,5"));
    }

    @Test
    public void handlesNewLinesAsDelimiter() {
        assertEquals(6, stringCalculator.add("1\n2,3"));
    }

    @Test
    public void allowsCustomDelimiter() {
        assertEquals(3, stringCalculator.add("//;\n1;2"));
    }

    @Test
    public void throwsExceptionIfInputContainsNegativeNumbers() {
        NegativeNumbersFoundException exception = assertThrows(
                NegativeNumbersFoundException.class,
                () -> stringCalculator.add("-1,2,-3")
        );

        assertEquals("Negative numbers not allowed: [-1, -3]", exception.getMessage());
    }

    @Test
    public void doesNotCountNumbersOver1000() {
        assertEquals(2, stringCalculator.add("1001,2"));
    }

    @Test
    public void allowsAnyLengthDelimiter() {
        assertEquals(6, stringCalculator.add("//[|||]\n1|||2|||3"));
    }

    @Test
    public void allowsMultipleCustomDelimiters() {
        assertEquals(9, stringCalculator.add("//[|||][@]1@3|||5"));
    }

    @Test
    public void allowsMultipleCustomDelimitersOfAnyLength() {
        assertEquals(6, stringCalculator.add("//[|][%%]\n1|2%%3"));
    }
}
