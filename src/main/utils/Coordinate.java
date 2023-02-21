package main.utils;

public record Coordinate(int x, int y) {

    public static Coordinate of(int x, int y) {
        return new Coordinate(x, y);
    }

}