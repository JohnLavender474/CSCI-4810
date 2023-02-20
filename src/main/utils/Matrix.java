package main.utils;

// https://introcs.cs.princeton.edu/java/95linear/Matrix.java.html
public class Matrix {

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
                    C.data[i][j] += A.data[i][k] * B.data[k][j];
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
