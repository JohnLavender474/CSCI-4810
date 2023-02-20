package main;

import main.utils.Line;
import main.utils.Matrix;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class Assignment3 implements Runnable {

    private static final List<Runnable> TESTS = List.of(
            Assignment3::test1,
            Assignment3::test2,
            Assignment3::test3,
            Assignment3::test4,
            Assignment3::test5
    );

    private static final List<Integer> TESTS_TO_RUN = List.of(
            4
    );

    private static final String TITLE = "John Lavender - CSCI 4810 Assignment 3";

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;
    private static final int PPM = 1;

    private enum Option {

        INPUT_LINES("Input lines: \"1 <path to file of lines> <enter \"y\" to clear current lines>\""),
        APPLY_TRANS("Apply transformations: \"2 <matrix file>\""),
        DISPLAY_PIXELS("Display pixels: \"3: <path to file of lines>\""),
        OUTPUT_LINES("Output lines: \"4 <name of file>\""),
        BASIC_TRANS("Basic transformation: \"5 <dx> <dy>\""),
        BASIC_SCALE("Basic scale: \"6 <sx> <sy>\""),
        BASIC_ROTATE("Basic rotate: \"7 <angle>\""),
        SCALE("Scale: \"8 <sx> <sy> <cx> <cy>\""),
        ROTATE("Rotate: \"9 <angle> <cx> <cy>\""),
        HELP("Help: \"10\""),
        EXIT("Exit: \"11\"");

        private final String prompt;

        Option(String prompt) {
            this.prompt = prompt;
        }

        String getPrompt() {
            return prompt;
        }

    }

    private final boolean test;

    public Assignment3(boolean test) {
        this.test = test;
    }

    @Override
    public void run() {
        if (test) {
            TESTS_TO_RUN.forEach(testNum -> {
                System.out.println("TEST " + testNum + " ----------");
                TESTS.get(testNum - 1).run();
                System.out.println("----------\n");
            });
            return;
        }
        final List<Line> lines = new ArrayList<>();
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame(TITLE);
            frame.add(new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    draw(g, lines);
                }
            });
            frame.setVisible(true);
            frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
        Scanner scanner = new Scanner(System.in);
        System.out.println(TITLE + "\n");
        help();
        boolean running = true;
        while (running) {
            String[] input = scanner.nextLine().split("\\s+");
            for (int i = 0; i < input.length; i++) {
                input[i] = input[i].replace("\\s+", "");
            }
            try {
                int ordinal = Integer.parseInt(input[0]);
                Option option = Option.values()[ordinal];
                switch (option) {
                    case INPUT_LINES -> {
                        String file = input[1];
                        boolean clear = input.length < 3 || input[2].equalsIgnoreCase("y");
                        inputLines(lines, file, clear);
                    }
                    case APPLY_TRANS -> {
                        String file = input[1];
                        applyTrans(lines, file);
                    }
                    case DISPLAY_PIXELS -> {
                        String file = input[1];
                        displayPixels(file);
                    }
                    case BASIC_ROTATE -> {
                        int rotation = Integer.parseInt(input[1]);
                        basicRotate(lines, rotation);
                    }
                    case BASIC_TRANS -> {
                        int dx = Integer.parseInt(input[1]);
                        int dy = Integer.parseInt(input[2]);
                        basicTrans(lines, dx, dy);
                    }
                    case BASIC_SCALE -> {
                        int sx = Integer.parseInt(input[1]);
                        int sy = Integer.parseInt(input[2]);
                        basicScale(lines, sx, sy);
                    }
                    case SCALE -> {
                        int sx = Integer.parseInt(input[1]);
                        int sy = Integer.parseInt(input[2]);
                        int cx = Integer.parseInt(input[3]);
                        int cy = Integer.parseInt(input[4]);
                        scale(lines, sx, sy, cx, cy);
                    }
                    case ROTATE -> {
                        int angle = Integer.parseInt(input[1]);
                        int cx = Integer.parseInt(input[2]);
                        int cy = Integer.parseInt(input[3]);
                        rotate(lines, angle, cx, cy);
                    }
                    case OUTPUT_LINES -> {
                        String filename = input[1];
                        outputLines(filename, lines);
                    }
                    case HELP -> help();
                    case EXIT -> running = false;
                }
            } catch (Exception e) {
                printErr(e);
            }
        }
        System.out.println("\n----------\nTHANK YOU FOR TRYING OUT MY PROJECT :)");
    }

    private static void draw(Graphics g, Line line) {
        float dx = line.x2 - line.x1;
        float dy = line.y2 - line.y1;
        float x = line.x1;
        float y = line.y1;
        float m;
        if (Math.abs(dx) > Math.abs(dy)) {
            m = dy / dx;
            for (int i = 0; i <= Math.abs(dx); i++) {
                g.fillRect((int) x, (int) (WINDOW_HEIGHT - y) * PPM, PPM, PPM);
                x++;
                y += m;
            }
        } else {
            m = dx / dy;
            for (int i = 0; i <= Math.abs(dy); i++) {
                g.fillRect((int) x * PPM, (int) (WINDOW_HEIGHT - y) * PPM, PPM, PPM);
                if (dy < 0f) {
                    y--;
                    x -= m;
                } else {
                    y++;
                    x += m;
                }
            }
        }
    }

    private static void draw(Graphics g, List<Line> lines) {
        lines.forEach(line -> draw(g, line));
    }

    private static void printErr(Exception e) {
        System.out.println("ERROR! ----------\n");
        e.printStackTrace();
        System.out.println(e.getMessage());
        System.out.println("----------\n");
    }

    private static void translate(Line line, int dx, int dy) {
        Matrix lineMatrix1 = new Matrix(new int[][]{
                {
                        line.x1, line.y1, 1
                }
        });
        Matrix lineMatrix2 = new Matrix(new int[][]{
                {
                        line.x2, line.y2, 1
                }
        });
        Matrix transMatrix = new Matrix(new int[][]{
                {
                        1, 0, 0
                },
                {
                        0, 1, 0
                },
                {
                        dx, dy, 1
                }
        });
        Matrix trans1 = lineMatrix1.times(transMatrix);
        Matrix trans2 = lineMatrix2.times(transMatrix);
        line.x1 = trans1.get(0, 0);
        line.y1 = trans1.get(0, 1);
        line.x2 = trans2.get(0, 0);
        line.y2 = trans2.get(0, 1);
    }

    private static void help() {
        System.out.println("OPTIONS:");
        Arrays.stream(Option.values()).forEach(option -> System.out.println("\t: " + option.getPrompt()));
        System.out.println('\n');
    }

    private static void outputLines(String filename, Collection<Line> lines) throws Exception {
        File file = new File(filename);
        FileWriter writer = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        for (Line line : lines) {
            bufferedWriter.write(line.toString());
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
        System.out.println("Successfully wrote to " + filename);
    }

    private static void rotate(List<Line> lines, int angle, int cx, int cy) {
        // TODO: rotate all lines
    }

    private static void scale(List<Line> lines, int sx, int sy, int cx, int cy) {
        // TODO: scale lines
    }

    private static void basicScale(List<Line> lines, int sx, int sy) {
        // TODO: basic scale
    }

    private static void inputLines(List<Line> lines, String filename, boolean clear) throws Exception {
        File file = new File(filename);
        Scanner reader = new Scanner(file);
        List<Line> newLines = new ArrayList<>();
        while (reader.hasNextLine()) {
            List<Integer> vals = Arrays.stream(reader.nextLine().split(",")).map(Integer::parseInt).toList();
            Line line = new Line(vals);
            newLines.add(line);
        }
        if (clear) {
            lines.clear();
        }
        lines.addAll(newLines);
    }

    private static void applyTrans(List<Line> lines, String filename) {
        // TODO: apply trans
    }

    private static void displayPixels(String filename) {
        // TODO: update pixels on screen
    }

    private static void basicRotate(List<Line> lines, int rotation) {
        // TODO: basic rotation
    }

    private static void basicTrans(List<Line> lines, int dx, int dy) {
        // TODO: basic trans
    }

    private static void test1() {
        Matrix a = new Matrix(new int[][]{
                {
                        100, 110, 1
                }
        });
        Matrix b = new Matrix(new int[][]{
                {
                        1, 0, 0
                },
                {
                        0, 1, 0
                },
                {
                        20, 40, 1
                }
        });
        System.out.println(a.times(b));
    }

    private static void test2() {
        Matrix a = new Matrix(new int[][]{
                {
                        100, 110, 1
                },
                {
                        50, 48, 2
                }
        });
        Matrix b = new Matrix(new int[][]{
                {
                        3, 1, 2
                },
                {
                        2, 3, 1
                },
                {
                        20, 40, 1
                }
        });
        System.out.println(a.times(b));
    }

    private static void test3() {
        Line line = new Line(0, 0, 10, 10);
        Assignment3.translate(line, 30, 10);
        System.out.println(line);
        Assignment3.translate(line, -50, 10);
        System.out.println(line);
    }

    private static void test4() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter file name: ");
        String[] input = scanner.nextLine().split("\\s+");
        String filename = input[0];
        try {
            File file = new File(filename);
            FileWriter writer = new FileWriter(file, input.length > 1 && input[1].equalsIgnoreCase("y"));
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            List<Line> lines = new ArrayList<>() {{
                add(new Line(4, 5, 8, 1));
                add(new Line(3, 4, 8, 1));
            }};
            for (Line line : lines) {
                bufferedWriter.write(line.toString());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Successfully wrote to " + filename);
    }

    private static void test5() {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame(TITLE);
            frame.add(new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    draw(g, new Line(50, 50, 500, 500));
                    draw(g, new Line(0, 700, 500, 100));
                }
            });
            frame.setVisible(true);
            frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }

}
