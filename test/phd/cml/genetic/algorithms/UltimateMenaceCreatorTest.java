package phd.cml.genetic.algorithms;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class UltimateMenaceCreatorTest {

    private static final int TEST_GENOTYPE_LENGTH = 10;
    private static final int TEST_CHROMOSOME_LENGTH = 7;
    private static final int TEST_NUMBER_OF_ITERATIONS = 100;
    private static final String[] TEST_SPECIES = new String[] {"BUTTERFLY", "PREDATOR", "ALIEN", "CAT", "TERMINATOR",
            "KOALA", "HAKE"};
    private static final String PHENOTYPE_FOUND_MESSAGE = "PHENOTYPE FOUND";
    private static final String BEST_FITNESS_MESSAGE = "Best fitness: 3";
    private static final String ULTIMATE_MENACE_EXPECTED_MIXTURE = "Ultimate menace formed by the mixture of:\n[PREDATOR,ALIEN,TERMINATOR]";
    private static final String ULTIMATE_MENACE_DISCARDED_SPECIMENS = "Discarded specimens:\n[BUTTERFLY,CAT,KOALA,HAKE]";

    private UltimateMenaceCreator ultimateMenaceCreator;
    private final ByteArrayOutputStream outputStreamContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errorStreamContent = new ByteArrayOutputStream();

    @BeforeMethod
    public void setUp() throws Exception {
        this.ultimateMenaceCreator = new UltimateMenaceCreator(TEST_GENOTYPE_LENGTH, TEST_CHROMOSOME_LENGTH, TEST_NUMBER_OF_ITERATIONS);
        System.setOut(new PrintStream(outputStreamContent));
        System.setErr(new PrintStream(errorStreamContent));
    }

    @Test
    public void testValidSpeciesExist() {
        for (int i = 0; i < TEST_SPECIES.length; i++) {
            assertTrue(contains(TEST_SPECIES[i]));
        }
    }
    public static boolean contains(String test) {
        for (UltimateMenaceCreator.Species species : UltimateMenaceCreator.Species.values()) {
            if(species.name().equals(test)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testEvolve() throws Exception {
        try {
            this.ultimateMenaceCreator.evolve(true);
        } catch (final Exception exception) {
            fail();
        }
    }

    @Test
    public void testPrintPhenotype() throws Exception {
        try {
            this.ultimateMenaceCreator.printPhenotype();
        } catch (final Exception exception) {
            fail();
        }
    }

    @Test
    public void testMain() throws Exception {
        UltimateMenaceCreator.main(null);
        final String consoleResult = outputStreamContent.toString();
        assertTrue(consoleResult.contains(PHENOTYPE_FOUND_MESSAGE));
        assertTrue(consoleResult.contains(BEST_FITNESS_MESSAGE));
        assertTrue(consoleResult.contains(ULTIMATE_MENACE_EXPECTED_MIXTURE));
        assertTrue(consoleResult.contains(ULTIMATE_MENACE_DISCARDED_SPECIMENS));
    }
}
