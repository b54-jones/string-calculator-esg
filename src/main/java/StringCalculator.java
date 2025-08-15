import exceptions.NegativeNumbersFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {

    private static final Pattern CUSTOM_DELIMITER_PATTERN = Pattern.compile("\\[(.*?)]");
    private static final int MAX_TO_INCLUDE = 1000;
    private static final String DEFAULT_DELIMITER = ",";
    private static final String SECONDARY_DELIMITER = "\n";

    public int add(String numbers) throws NegativeNumbersFoundException {
        String processedString = normalizeDelimiters(numbers);
        List<Integer> numbersList = parseNumbers(processedString);
        checkForNegatives(numbersList);
        return sumValidNumbers(numbersList);
    }

    public List<Integer> parseNumbers(String input) {
        return Arrays.stream(input.split(DEFAULT_DELIMITER)).filter(s -> !s.isEmpty()).map(Integer::parseInt).toList();
    }

    public void checkForNegatives(List<Integer> numbers) {
        List<Integer> negatives = numbers.stream().filter(n -> n < 0).toList();

        if (!negatives.isEmpty()) {
            throw new NegativeNumbersFoundException(negatives.toString());
        }
    }

    public int sumValidNumbers(List<Integer> numbers) {
        return numbers.stream().filter(n -> n <= MAX_TO_INCLUDE).mapToInt(Integer::intValue).sum();
    }

    private String normalizeDelimiters(String input) {
        if (!input.startsWith("//")) {
            return input.replace("\n", ",");
        }

        if (input.charAt(2) != '[') {
            return handleSingleCharDelimiter(input);
        }
        return handleMultiCharDelimiters(input);
    }

    public String handleSingleCharDelimiter(String input) {
        String customDelimiter = String.valueOf(input.charAt(2));
        String numbersPart = input.substring(4);
        return numbersPart.replace(customDelimiter, DEFAULT_DELIMITER)
                .replace(SECONDARY_DELIMITER, DEFAULT_DELIMITER);
    }

    public String handleMultiCharDelimiters(String input) {
        Matcher matcher = CUSTOM_DELIMITER_PATTERN.matcher(input);

        while (matcher.find()) {
            input = input.replace(matcher.group(1), DEFAULT_DELIMITER);
        }

        int lastBracketPos = input.lastIndexOf("]");
        input = input.substring(lastBracketPos + 1);
        return input.replace(SECONDARY_DELIMITER, DEFAULT_DELIMITER);
    }
}
