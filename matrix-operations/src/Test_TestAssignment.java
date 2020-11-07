import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.Map;

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

    @Test
    void multiplyMatricesTest() {
        Matrix firstMatrix = new Matrix(new Integer[][] {
                {1, 5},
                {2, 3},
                {1, 7},
        });

        Matrix secondMatrix = new Matrix(new Integer[][] {
                {1, 2, 3, 7},
                {5, 2, 8, 1}
        });

        Matrix expected = new Matrix(new Integer[][] {
                {26, 12, 43, 12},
                {17, 10, 30, 17},
                {36, 16, 59, 14}
        });

        Matrix actual = app.multiplyMatrices(firstMatrix, secondMatrix);

        assertEquals(expected, actual);
    }

    @Test
    void multiplyMatricesInvalidSizeTest() {
        Matrix firstMatrix = new Matrix(new Integer[][] {
                {1, 5},
                {2, 3},
                {1, 7},
        });

        Matrix secondMatrix = new Matrix(new Integer[][] {
                {1, 2, 3, 7},
                {5, 2, 8, 1},
                {4, 9, 5, 6},
        });

        ArithmeticException thrown = assertThrows(
                ArithmeticException.class,
                () -> app.multiplyMatrices(firstMatrix, secondMatrix));
        assertTrue(thrown.getMessage().contains("Invalid matrix size"));
    }

    @Test
    void sumOrSubtractMatricesTest() {
        Matrix firstMatrix = new Matrix(new Integer[][] {
                {1, 2, 3},
                {7, 8, 9},
        });

        Matrix secondMatrix = new Matrix(new Integer[][] {
                {5, 6, 7},
                {3, 4, 5}
        });

        Matrix expectedSum = new Matrix(new Integer[][] {
                {6, 8, 10},
                {10, 12, 14}
        });
        Matrix expectedDiff = new Matrix(new Integer[][] {
                {-4, -4, -4},
                { 4,  4,  4}
        });

        Matrix actualSum = app.sumOrSubtractMatrices(firstMatrix, secondMatrix, Integer::sum);
        assertEquals(expectedSum, actualSum);

        Matrix actualDiff = app.sumOrSubtractMatrices(firstMatrix, secondMatrix, (a, b) -> a - b);
        assertEquals(expectedDiff, actualDiff);
    }

    @Test
    void sumOrSubtractMatricesInvalidSizeTest() {
        Matrix firstMatrix = new Matrix(new Integer[][] {
                {1, 2},
                {2, 3},
                {1, 7},
        });

        Matrix secondMatrix = new Matrix(new Integer[][] {
                {1, 2, 3, 7},
                {5, 2, 8, 1},
                {4, 9, 5, 6},
        });

        ArithmeticException thrown = assertThrows(
                ArithmeticException.class,
                () -> app.sumOrSubtractMatrices(firstMatrix, secondMatrix, Integer::sum));
        assertTrue(thrown.getMessage().contains("Invalid matrix size"));
    }

    @Test
    void evalTest() {
        Matrix K = new Matrix(new Integer[][] {
                {-10,  0,  2},
                { -6, 10, -6},
                { -9,  2,  0},
        });

        Matrix D = new Matrix(new Integer[][] {
                {0,  6,  7}
        });

        Matrix M = new Matrix(new Integer[][] {
                {10, -5, -4}
        });

        Map<Character, Matrix> matrices = Map.of('K', K, 'D', D, 'M', M);

        app = new TestAssignment(matrices);

        String operationsString = "M+D*K";

        Matrix expected = new Matrix(new Integer[][] {
                {-89, 69, -40}
        });

        Matrix actual = app.eval(operationsString);
        assertEquals(expected, actual);
    }

    @Test
    void evalUnknownMatrixTest() {
        String operationsString = "M+D*K";

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> app.eval(operationsString));
        assertTrue(thrown.getMessage().contains("Unknown matrix"));
    }

    @Test
    void evalUnexpectedOperandTest() {
        String operationsString = "m+D*K";

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> app.eval(operationsString));
        assertTrue(thrown.getMessage().contains("Unexpected operand"));
    }

    @Test
    void example03Test() {
        app.parseMatrix("S=[1]");
        app.parseMatrix("P=[1]");

        String operationsString = "S+P";
        String expected = "[2]";

        assertEquals(expected, app.eval(operationsString).toString());
    }

    @Test
    void example04Test() {
        app.parseMatrix("B=[5 2 4; 0 2 -1; 3 -5 -4]");
        app.parseMatrix("E=[-6 -5 -8; -1 -1 -10; 10 0 -7]");
        app.parseMatrix("R=[-1 -7 6; -2 9 -4; 6 -10 2]");

        String operationsString = "R+E+B";
        String expected = "[-2 -10 2; -3 10 -15; 19 -15 -9]";

        assertEquals(expected, app.eval(operationsString).toString());
    }

    @Test
    void example05Test() {
        app.parseMatrix("K=[-10 0 2; -6 10 -6; -9 2 0]");
        app.parseMatrix("D=[0 6 7]");
        app.parseMatrix("M=[10 -5 -4]");

        String operationsString = "D*K+M";
        String expected = "[-89 69 -40]";

        assertEquals(expected, app.eval(operationsString).toString());
    }

}
