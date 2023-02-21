package main.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

public class FuncMatrix {

    public final int rows;
    public final int cols;
    public final List<List<IntFunction<Integer>>> funcs;

    public FuncMatrix(int rows, int cols, IntFunction<Integer> defaultFunc,
                      Map<Coordinate, IntFunction<Integer>> funcs) {
        this.rows = rows;
        this.cols = cols;
        this.funcs = new ArrayList<>(rows);
        for (int i = 0; i < cols; i++) {
            List<IntFunction<Integer>> col = new ArrayList<>();
            int j = 0;
            while (j < rows) {
                Coordinate c = Coordinate.of(i, j);
                col.add(funcs.getOrDefault(c, defaultFunc));
                j++;
            }
            this.funcs.add(col);
        }
    }

    public void put(int x, int y, IntFunction<Integer> func) {
        funcs.get(x).set(y, func);
    }

    public IntFunction<Integer> get(int x, int y) {
        return funcs.get(x).get(y);
    }

}
