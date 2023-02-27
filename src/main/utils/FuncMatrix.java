package main.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

public class FuncMatrix {

    public final int rows;
    public final int cols;
    public final List<List<IntFunction<Integer>>> funcs;

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

    public IntFunction<Integer> get(int row, int col) {
        return funcs.get(row).get(col);
    }

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
