package main.utils;

import java.util.function.IntFunction;

/**
 * Represents a rows x cols matrix of integers. Based on implementation provided at
 * <link><a href="https://introcs.cs.princeton.edu/java/95linear/Matrix.java.html">Princeton Matrix class</a></link>
 */
public class Matrix {

    private final int rows;
    private final int cols;
    private final int[][] data;

    /**
     * Creates rows x cols matrix.
     *
     * @param rows the number of rows
     * @param cols the number of columns
     */
    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        data = new int[rows][cols];
    }

    /**
     * Creates a matrix that references the provided array.
     *
     * @param data the array that this instance references
     */
    public Matrix(int[][] data) {
        this(data.length, data[0].length);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.data[i][j] = data[i][j];
            }
        }
    }

    /**
     * Multiply this matrix by the provided matrix. Number of columns in this matrix must be equal to
     * number of rows in the provided matrix.
     *
     * @param B the other matrix
     * @return a matrix with one row and cols equals to {@link #cols} of the provided matrix
     */
    public Matrix times(Matrix B) {
        Matrix A = this;
        if (A.cols != B.rows) {
            throw new RuntimeException("Illegal matrix dimensions. A cols: " +
                    A.cols + ", B rows: " + B.rows);
        }
        Matrix C = new Matrix(A.rows, B.cols);
        for (int i = 0; i < C.rows; i++) {
            for (int j = 0; j < C.cols; j++) {
                for (int k = 0; k < A.cols; k++) {
                    int a = A.data[i][k];
                    int b = B.data[k][j];
                    int res = a * b;
                    C.data[i][j] += res;
                }
            }
        }
        return C;
    }

    /**
     * Apply the function matrix to this matrix. Number of columns in this matrix must be equal to
     * number of rows in the provided matrix.
     *
     * @param funcMatrix the matrix of functions
     * @return a matrix with one row and cols equals to {@link #cols} of the provided function matrix
     */
    public Matrix apply(FuncMatrix funcMatrix) {
        Matrix A = this;
        if (A.cols != funcMatrix.rows) {
            throw new RuntimeException("Illegal matrix dimensions. A cols: " +
                    A.cols + ", Func matrix rows: " + funcMatrix.rows);
        }
        Matrix C = new Matrix(A.rows, funcMatrix.cols);
        for (int i = 0; i < C.rows; i++) {
            for (int j = 0; j < C.cols; j++) {
                for (int k = 0; k < A.cols; k++) {
                    int a = A.data[i][k];
                    IntFunction<Integer> func = funcMatrix.get(k, j);
                    int res = func.apply(a);
                    C.data[i][j] += res;
                }
            }
        }
        return C;
    }

    /**
     * Get the value contained at the specified row and column.
     *
     * @param row the row
     * @param col the column
     * @return the value
     */
    public int get(int row, int col) {
        return data[row][col];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        for (int[] row : data) {
            sb.append("\t");
            for (int i = 0; i < row.length; i++) {
                sb.append(row[i]);
                if (i != row.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

}
