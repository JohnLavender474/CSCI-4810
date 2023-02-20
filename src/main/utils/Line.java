package main.utils;

import java.util.List;

public class Line {

    public int x1;
    public int y1;
    public int x2;
    public int y2;

    public Line(List<Integer> vals) {
        this(vals.get(0), vals.get(1), vals.get(2), vals.get(3));
    }

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
