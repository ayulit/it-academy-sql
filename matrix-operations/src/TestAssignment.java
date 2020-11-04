import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code Matrix} class wraps a 2D array of type
 * {@code Integer} in an object.
 */
class Matrix {
    private Integer[][] matrix;

    Matrix(Integer[][] matrix) {
        this.matrix = matrix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Matrix m = (Matrix) o;
        return Arrays.deepEquals(matrix, m.matrix);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(matrix);
    }

    @Override
    public String toString() {
        return Arrays.deepToString(matrix);
    }
}


public class TestAssignment {
    /**
     * A map which maps matrixName to Matrix
     */
    private Map<Character, Matrix> matrices = new HashMap<>();

    Map<Character, Matrix> getMatrices() {
        return matrices;
    }

    public static void main(String[] args) {
        TestAssignment app = new TestAssignment();

        try(Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();

                if(!line.isEmpty()) {
                    app.parseMatrix(line);
                }
            }
        } catch (RuntimeException e) {
            System.err.println("Exception caught: " + e.getClass().getSimpleName() + ". Can't " + e.getMessage() + ".");
        }
    }

    /**
     * Parses the string argument containing the representation of matrix
     * extracting {@code matrixName} and {@code matrixExpression} using regexp.
     *
     * After extraction variable stored into {@code matrices} Map.
     *
     *
     * @param      matrixLine   the {@code String} containing the representation of matrix to be parsed
     *                  in the following way name=[x11 x12 ... x1n; x21 x22 ... x2n;...,xm1 xm2 ... xmm]
     * @throws     RuntimeException if the matrices Map
     *             already contains specific {@code matrixName}.
     * @throws     IllegalArgumentException  if {@code matrixLine} has wrong format.
     */
    void parseMatrix(String matrixLine) {
        Pattern pattern = Pattern.compile("^([A-Z])=\\[(.+)\\]$");
        Matcher matcher = pattern.matcher(matrixLine);
        if (matcher.find()) {
            Character matrixName = matcher.group(1).charAt(0);
            String matrixExpression = matcher.group(2);

            if(matrices.containsKey(matrixName)) {
                throw new RuntimeException("read matrix. Matrix name already exists");
            }

            Matrix matrix = acquireMatrix(matrixExpression);
            matrices.put(matrixName, matrix);
        } else {
            throw new IllegalArgumentException("read matrix. Matrix has wrong format");
        }
    }

    Matrix acquireMatrix(String matrixExpression) {
        String[] rows = matrixExpression.split(";");

        if(rows.length == 0 || rows[0].trim().isEmpty()) {
            throw new IllegalArgumentException("read matrix. Matrix expression has wrong format");
        }

        try {
            Integer[][] matrix = Arrays.stream(rows)
                           .map(row -> Arrays.stream(row.trim().split(" "))
                                             .map(Integer::valueOf)
                                             .toArray(Integer[]::new))
                           .toArray(Integer[][]::new);

            if(!Arrays.stream(matrix).map(row -> row.length).allMatch(size -> size == matrix[0].length)) {
                throw new ArrayStoreException("read matrix. Rows have different size");
            }

            return new Matrix(matrix);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("read matrix. Values are not integer");
        }

    }
}
