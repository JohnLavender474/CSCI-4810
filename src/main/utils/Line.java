package main.utils;

import java.util.List;

/**
 * Represents a line. The value of x1 will be less than x2 when initialized in the constructor.
 */
public class Line {

    public int x1;
    public int y1;
    public int x2;
    public int y2;

    /**
     * Create a new line using the list of values. There should be exactly 4 values.
     *
     * @param vals the list of values
     */
    public Line(List<Integer> vals) {
        this(vals.get(0), vals.get(1), vals.get(2), vals.get(3));
    }

    /**
     * Create a new line using the array of values. There should be exactly 4 values.
     *
     * @param vals the array of values
     */
    public Line(int[] vals) {
        this(vals[0], vals[1], vals[2], vals[3]);
    }

    /**
     * Create a new line using the values.
     *
     * @param x1 the first x
     * @param y1 the first y
     * @param x2 the second x
     * @param y2 the second y
     */
    public Line(int x1, int y1, int x2, int y2) {
        if (x1 < x2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        } else {
            this.x1 = x2;
            this.y1 = y2;
            this.x2 = x1;
            this.y2 = y1;
        }
    }

    @Override
    public String toString() {
        return x1 + "," + y1 + "," + x2 + "," + y2;
    }

}
