package phd.cml.fireworks;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class MainFireworksTest {

    private static final double ACCEPTANCE_THRESHOLD_PERCENTAGE = 35.0;
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
            fail("Improved Fireworks Algorithm is performing "+ (ACCEPTANCE_THRESHOLD_PERCENTAGE - improvedFireworksAlgorithmResult) +"% below the acceptance threshold.");
        }
    }

    private double parseImprovedFireworksAlgorithmResult(String consoleResult) {
        consoleResult = consoleResult.substring(consoleResult.indexOf("{") + 1);
        consoleResult = consoleResult.substring(0, consoleResult.indexOf("}"));
        return Double.parseDouble(consoleResult);
    }

    private String assertThatThereIsImprovement() {
        String consoleResult = outputStreamContent.toString();
        assertTrue(consoleResult.contains("The improved algorithm is a"));
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
