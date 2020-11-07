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
        final InputStream original = System.in;

        try(final FileInputStream inputStream = new FileInputStream(new File("pub01.in"));
            final BufferedReader outputExpectedReader = new BufferedReader(new FileReader("pub01.out"));
            final BufferedReader errorExpectedReader = new BufferedReader(new FileReader("pub01.err"))) {

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

    @Test
    void example08Test() throws IOException {
        final InputStream original = System.in;

        try(final FileInputStream inputStream = new FileInputStream(new File("example08.in"));
                final BufferedReader outputExpectedReader = new BufferedReader(new FileReader("example08.out"));
                final BufferedReader errorExpectedReader = new BufferedReader(new FileReader("example08.err"))) {

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
