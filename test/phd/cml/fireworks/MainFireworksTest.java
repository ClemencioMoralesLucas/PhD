package phd.cml.fireworks;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Created by Clemencio Morales Lucas.
 */

public class MainFireworksTest {

    private static final double ACCEPTANCE_THRESHOLD_PERCENTAGE = 35.0;
    private static final String IMPROVED_FIREWORKS_ALGORITHM_IS_PERFORMING_MESSAGE = "Improved Fireworks Algorithm is performing ";
    private static final String BELOW_THE_ACCEPTANCE_THRESHOLD_MESSAGE = "% below the acceptance threshold.";
    private static final String CURLY_BRACE_OPEN = "{";
    private static final String CURLY_BRACE_CLOSE = "}";
    private static final String THE_IMPROVED_ALGORITHM_IS_A_MESSAGE = "The improved algorithm is a";
    private static final int ARGS_LENGTH = 0;
    private static final String [] ARGS = new String[ARGS_LENGTH];

    private final ByteArrayOutputStream outputStreamContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errorStreamContent = new ByteArrayOutputStream();

    @BeforeMethod
    public void setUp() {
        initializeAndCaptureOutputStreams();
    }

    private void initializeAndCaptureOutputStreams() {
        System.setOut(new PrintStream(outputStreamContent));
        System.setErr(new PrintStream(errorStreamContent));
    }

    @Test
    public void testImprovedFireworksAlgorithmGetsAcceptableImprovement() throws FileNotFoundException {
        Main.main(ARGS);
        String consoleResult = assertThatThereIsImprovement();
        double improvedFireworksAlgorithmResult = parseImprovedFireworksAlgorithmResult(consoleResult);
        assertThatImprovedPertentageIsBiggerThanThreshold(improvedFireworksAlgorithmResult);
    }

    private void assertThatImprovedPertentageIsBiggerThanThreshold(final double improvedFireworksAlgorithmResult) {
        if (improvedFireworksAlgorithmResult < ACCEPTANCE_THRESHOLD_PERCENTAGE) {
            fail(IMPROVED_FIREWORKS_ALGORITHM_IS_PERFORMING_MESSAGE + (ACCEPTANCE_THRESHOLD_PERCENTAGE -
                    improvedFireworksAlgorithmResult) + BELOW_THE_ACCEPTANCE_THRESHOLD_MESSAGE);
        }
    }

    private double parseImprovedFireworksAlgorithmResult(String consoleResult) {
        consoleResult = consoleResult.substring(consoleResult.indexOf(CURLY_BRACE_OPEN) + 1);
        consoleResult = consoleResult.substring(0, consoleResult.indexOf(CURLY_BRACE_CLOSE));
        return Double.parseDouble(consoleResult);
    }

    private String assertThatThereIsImprovement() {
        String consoleResult = outputStreamContent.toString();
        assertTrue(consoleResult.contains(THE_IMPROVED_ALGORITHM_IS_A_MESSAGE));
        return consoleResult;
    }

    @AfterMethod
    public void cleanUp() {
        closeStreams();
    }

    private void closeStreams() {
        System.setOut(null);
        System.setErr(null);
    }
}
