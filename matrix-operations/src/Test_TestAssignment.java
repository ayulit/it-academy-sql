import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Test_TestAssignment {
    private TestAssignment app;

    @BeforeEach
    void setUp() {
        app = new TestAssignment();
    }

    @Test
    void parseMatrixTest() {
        String matrixLine = "A=[1 2 2; 0 3 1; -1 2 -4]";

        Integer[][] matrix = new Integer[][] {
                { 1, 2, 2},
                { 0, 3, 1},
                {-1, 2,-4},
        };
        Matrix expected = new Matrix(matrix);

        app.parseMatrix(matrixLine);

        assertTrue(app.getMatrices().containsKey('A'));
        assertEquals(expected, app.getMatrices().get('A'));
    }

    @Test
    void parseMatrixAlreadyExistsTest() {
        String matrixLine1 = "A=[1 2 2; 0 3 1; -1 2 -4]";
        String matrixLine2 = "A=[1 1 1; 1 1 1; 1 1 1]";

        app.parseMatrix(matrixLine1);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> app.parseMatrix(matrixLine2),
                "Expected parseAndValidateMatrixLine() to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Matrix name already exists"));
    }

    @Test
    void parseMatrixHasWrongFormatTest() {
        String matrixLine = "a=[1 2 2; 0 3 1; -1 2 -4]";
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> app.parseMatrix(matrixLine));
        assertTrue(thrown.getMessage().contains("Matrix has wrong format"));

        String matrixLine2 = "A=(1 2 2; 0 3 1; -1 2 -4)";
        thrown = assertThrows(
                IllegalArgumentException.class,
                () -> app.parseMatrix(matrixLine2));
        assertTrue(thrown.getMessage().contains("Matrix has wrong format"));

        String matrixLine3 = "AA=[1 2 2; 0 3 1; -1 2 -4]";
        thrown = assertThrows(
                IllegalArgumentException.class,
                () -> app.parseMatrix(matrixLine3));
        assertTrue(thrown.getMessage().contains("Matrix has wrong format"));

        String matrixLine4 = "A=[1 2 2; 0 3 1; -1 2 -4]`";
        thrown = assertThrows(
                IllegalArgumentException.class,
                () -> app.parseMatrix(matrixLine4));
        assertTrue(thrown.getMessage().contains("Matrix has wrong format"));
    }

    @Test
    void acquireMatrixTest() {
        Integer[][] matrix = new Integer[][] {
            { 1, 2, 2},
            { 0, 3, 1},
            {-1, 2,-4},
        };
        Matrix expected = new Matrix(matrix);

        Matrix actual = app.acquireMatrix("1 2 2; 0 3 1; -1 2 -4");

        assertEquals(expected, actual);
    }

    @Test
    void acquireMatrixVectorTest() {
        Integer[][] matrix = new Integer[][] {
                {1, 2, 2}
        };
        Matrix expected = new Matrix(matrix);

        Matrix actual = app.acquireMatrix("1 2 2");

        assertEquals(expected, actual);
    }

    @Test
    void acquireMatrixHasWrongFormatTest() {
        String matrix1 = "";
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> app.acquireMatrix(matrix1));
        assertTrue(thrown.getMessage().contains("Matrix expression has wrong format"));

        String matrix3 = ";;;";
        thrown = assertThrows(
                IllegalArgumentException.class,
                () -> app.acquireMatrix(matrix3));
        assertTrue(thrown.getMessage().contains("Matrix expression has wrong format"));
    }

    @Test
    void acquireMatrixHasWrongValuesTest() {
        String matrix1 = "1 2 3, 4 5 6";
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> app.acquireMatrix(matrix1));
        assertTrue(thrown.getMessage().contains("Values are not integer"));
    }

    @Test
    void acquireMatrixHasDifferentRowSizeTest() {
        String matrix1 = "1 2; 3 4 5; 6";
        ArrayStoreException thrown = assertThrows(
                ArrayStoreException.class,
                () -> app.acquireMatrix(matrix1));
        assertTrue(thrown.getMessage().contains("Rows have different size"));
    }
}
