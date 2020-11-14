import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class E2eTest_TestAssignment {

    private static String E2E_TEST_DATA_FOLDER = "e2e_test_data";

    private final PrintStream standardOut = System.out;
    private final PrintStream standardErr = System.err;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errorStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setErr(new PrintStream(errorStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
        System.setErr(standardErr);
    }

    @Test
    void pub01Test() throws IOException {
        basicE2eTest("pub01.in", "pub01.out", "pub01.err");
    }

    @Test
    void pub02Test() throws IOException {
        basicE2eTest("pub02.in", "pub02.out", "pub02.err");
    }

    @Test
    void pub03Test() throws IOException {
        basicE2eTest("pub03.in", "pub03.out", "pub03.err");
    }

    @Test
    void example01Test() throws IOException {
        basicE2eTest("example01.in", "example01.out", "example01.err");
    }

    @Test
    void example06Test() throws IOException {
        basicE2eTest("example06.in", "example06.out", "example06.err");
    }

    @Test
    void example07Test() throws IOException {
        basicE2eTest("example07.in", "example07.out", "example07.err");
    }

    @Test
    void example08Test() throws IOException {
        basicE2eTest("example08.in", "example08.out", "example08.err");
    }

    @Test
    void example09Test() throws IOException {
        basicE2eTest("example09.in", "example09.out", "example09.err");
    }

    @Test
    void my01Test() throws IOException {
        basicE2eTest("my01.in", "my01.out", "my01.err");
    }

    @Test
    void my02Test() throws IOException {
        basicE2eTest("my02.in", "my02.out", "my02.err");
    }

    private void basicE2eTest(String in, String out, String err) throws IOException {
        final InputStream original = System.in;

        try(final FileInputStream inputStream = new FileInputStream(new File(E2E_TEST_DATA_FOLDER + File.separator + in));
                final BufferedReader outputExpectedReader = new BufferedReader(new FileReader(E2E_TEST_DATA_FOLDER + File.separator + out));
                final BufferedReader errorExpectedReader = new BufferedReader(new FileReader(E2E_TEST_DATA_FOLDER + File.separator + err))) {

            System.setIn(inputStream);
            TestAssignment.main(null);

            String outputExpected = outputExpectedReader.readLine();
            String errorExpected = errorExpectedReader.readLine();

            assertEquals(outputExpected != null ? outputExpected : "", outputStreamCaptor.toString().trim());
            assertTrue(errorStreamCaptor.toString().trim().contains(errorExpected != null ? errorExpected : ""));
        } finally {
            System.setIn(original);
        }
    }
}
