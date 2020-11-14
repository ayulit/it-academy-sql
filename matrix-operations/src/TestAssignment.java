import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiFunction;
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

    int getRowsNum() {
        return matrix.length;
    }

    int getColumnsNum() {
        return matrix[0].length;
    }

    Integer getCell(int row, int col) {
        return matrix[row][col];
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
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for (int row = 0; row < getRowsNum() ; row++) {
            for (int col = 0; col < getColumnsNum() ; col++) {
                if(col != 0) {
                    builder.append(' ');
                }
                builder.append(matrix[row][col]);
            }
            if(row != getRowsNum() - 1) {
                builder.append("; ");
            }
        }
        builder.append(']');
        return builder.toString();
    }
}


public class TestAssignment {
    /**
     * A map which maps matrixName to Matrix
     */
    private Map<Character, Matrix> matrices = new HashMap<>();

    TestAssignment() {
    }

    TestAssignment(Map<Character, Matrix> matrices) {
        this.matrices = matrices;
    }

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
                } else if (scanner.hasNextLine()) {
                    System.out.println(app.eval(scanner.nextLine()));
                    return;
                }
            }
            throw new IllegalArgumentException("read operations. Operations string doesn't exist.");
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
     * @param      matrixLine the {@code String} containing the representation of matrix to be parsed
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


    /**
     * Parses the string argument containing the representation of matrix.
     *
     * After parsing values stored into {@code Matrix} object.
     *
     *
     * @param      matrixExpression the {@code String} containing the representation of matrix to be parsed
     *                  in the following way x11 x12 ... x1n; x21 x22 ... x2n;...,xm1 xm2 ... xmm
     * @return     the {@code Matrix} represented by the string argument.
     *
     * @throws     ArrayStoreException if the matrix rows have different size.
     * @throws     IllegalArgumentException  if {@code matrixExpression} has wrong format.
     * @throws     IllegalArgumentException  if the matrix values are not integer.
     */
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

    /**
     * Parses the string argument containing the representation of matrix operations.
     * Works with three basic matrix operations (in priority): multiplication, addition and subtraction.
     *
     * During parsing method evaluates all operations with the relevant matrices and returns result as the {@code Matrix}.
     *
     * Parsing grammar:
     *  expression = term | expression `+` term | expression `-` term
     *  term = factor | term `*` factor
     *  factor = Matrix
     *
     *
     * @param      expression the {@code String} containing the representation of operators and operands to be parsed
     *                  in the following way D*K+M
     * @return     the {@code Matrix} evaluated.
     *
     * @throws     IllegalArgumentException  if {@code expression} is null or empty.
     * @throws     RuntimeException  if there is no specific matrix in the {@code matrices} Map.
     * @throws     RuntimeException  if the operand has invalid name.
     */
    Matrix eval(final String expression) {
        return new Object() {
            int pos = -1;
            int currentChar;

            void movePos() {
                currentChar = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (currentChar == ' ') {
                    movePos();
                }
                if (currentChar == charToEat) {
                    movePos();
                    return true;
                }
                return false;
            }

            Matrix parse() {
                if(expression == null || expression.isEmpty()) {
                    throw new IllegalArgumentException("read operations. Operations string doesn't exist.");
                }

                movePos();
                Matrix matrix = parseExpression();
                if (pos < expression.length()) {
                    throw new RuntimeException("parse expression: " + (char) currentChar + ". Unexpected stop.");
                }
                return matrix;
            }

            Matrix parseExpression() {
                Matrix matrix = parseTerm();
                for (;;) {
                    if(eat('+')) {
                        matrix = sumOrSubtractMatrices(matrix, parseTerm(), Integer::sum);
                    } else if(eat('-')) {
                        matrix = sumOrSubtractMatrices(matrix, parseTerm(), (a, b) -> a - b);
                    } else {
                        return matrix;
                    }
                }
            }

            Matrix parseTerm() {
                Matrix matrix = parseFactor();
                for (;;) {
                    if(eat('*')) {
                        matrix = multiplyMatrices(matrix, parseFactor());
                    } else {
                        return matrix;
                    }
                }
            }

            Matrix parseFactor() {
                Matrix matrix;
                if (currentChar >= 'A' && currentChar <= 'Z') { // matrix names
                    if(!matrices.containsKey((char) currentChar)) {
                        throw new RuntimeException("get matrix: " + (char) currentChar + ". Unknown matrix.");
                    }
                    matrix = matrices.get((char) currentChar);
                    movePos();
                } else {
                    throw new RuntimeException("parse operand: " + (char) currentChar + ". Unexpected operand.");
                }

                return matrix;
            }
        }.parse();
    }

    Matrix multiplyMatrices(Matrix firstMatrix, Matrix secondMatrix) {
        if(firstMatrix.getColumnsNum() != secondMatrix.getRowsNum()) {
            throw new IllegalArgumentException("perform multiplication");
        }

        Integer[][] result = new Integer[firstMatrix.getRowsNum()][secondMatrix.getColumnsNum()];
        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[row].length; col++) {
                result[row][col] = multiplyRow2Column(firstMatrix, secondMatrix, row, col);
            }
        }
        return new Matrix(result);
    }

    private int multiplyRow2Column(Matrix firstMatrix, Matrix secondMatrix, int row, int col) {
        int cell = 0;
        for (int i = 0; i < firstMatrix.getColumnsNum(); i++) {
            cell += firstMatrix.getCell(row, i) * secondMatrix.getCell(i, col);
        }
        return cell;
    }

    /**
     * Returns a sum or subtraction of two {@code Matrix} arguments according to func.
     *
     * @param      firstMatrix the first {@code Matrix}
     * @param      secondMatrix the second {@code Matrix}
     * @param      func the {@code BiFunction<Integer, Integer, Integer>} which can be
     *                 1. 'Integer::sum' for addition
     *                 2. lambda '(a,b) -> a - b' for subtraction
     *
     * @return     the {@code Matrix} evaluated.
     *
     * @throws     IllegalArgumentException  if matrices' dimension is different.
     */
    Matrix sumOrSubtractMatrices(Matrix firstMatrix, Matrix secondMatrix, BiFunction<Integer, Integer, Integer> func) {
        if(firstMatrix.getRowsNum() != secondMatrix.getRowsNum() ||
                firstMatrix.getColumnsNum() != secondMatrix.getColumnsNum()) {
            throw new IllegalArgumentException("perform addition or subtraction");
        }

        Integer[][] result = new Integer[firstMatrix.getRowsNum()][firstMatrix.getColumnsNum()];
        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[row].length; col++) {
                result[row][col] = func.apply(firstMatrix.getCell(row, col), secondMatrix.getCell(row, col));
            }
        }
        return new Matrix(result);
    }
}
