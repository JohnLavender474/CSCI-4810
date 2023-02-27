package main.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

/**
 * Represents a rows x cols matrix of functions. See {@link Matrix}.
 */
public class FuncMatrix {

    public final int rows;
    public final int cols;
    public final List<List<IntFunction<Integer>>> funcs;

    /**
     * Creates rows x cols function matrix.
     *
     * @param rows the number of rows
     * @param cols the number of cols
     * @param defaultFunc the default function to be put in each cell
     */
    public FuncMatrix(int rows, int cols, IntFunction<Integer> defaultFunc) {
        this.rows = rows;
        this.cols = cols;
        funcs = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            List<IntFunction<Integer>> col = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                col.add(defaultFunc);
            }
            funcs.add(col);
        }
    }

    /**
     * Get number of rows
     *
     * @return number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Get number of columns
     *
     * @return number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Get the function contained at the specified row and column.
     *
     * @param row the row
     * @param col the column
     * @return the function
     */
    public IntFunction<Integer> get(int row, int col) {
        return funcs.get(row).get(col);
    }

    /**
     * Put the provided function at the specified row and column.
     *
     * @param row the row
     * @param col the column
     * @param func the function
     */
    public void put(int row, int col, IntFunction<Integer> func) {
        funcs.get(row).set(col, func);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        for (List<IntFunction<Integer>> row : funcs) {
            sb.append("\t");
            for (int i = 0; i < row.size(); i++) {
                sb.append(row.get(i));
                if (i != row.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

}
