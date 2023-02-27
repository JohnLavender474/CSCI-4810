package main.utils;

import java.util.function.IntFunction;

// https://introcs.cs.princeton.edu/java/95linear/Matrix.java.html
public class Matrix {

    public static final boolean TEST = false;

    private final int rows;
    private final int cols;
    private final int[][] data;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        data = new int[rows][cols];
    }

    public Matrix(int[][] data) {
        this(data.length, data[0].length);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.data[i][j] = data[i][j];
            }
        }
    }

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

    public int get(int x, int y) {
        return data[x][y];
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
