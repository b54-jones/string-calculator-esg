package exceptions;

public class NegativeNumbersFoundException extends RuntimeException {
    public NegativeNumbersFoundException(String message) {
        super("Negative numbers not allowed: " + message);
    }
}
