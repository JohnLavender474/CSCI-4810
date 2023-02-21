package main;

import main.utils.Coordinate;
import main.utils.FuncMatrix;
import main.utils.Line;
import main.utils.Matrix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
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
            Assignment3::test5,
            Assignment3::test6,
            Assignment3::test7
    );

    private static final List<Integer> TESTS_TO_RUN = List.of(
            7
    );

    private static final String TITLE = "John Lavender - CSCI 4810 Assignment 3";

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;
    private static final int PPM = 1;

    private enum Option {

        INPUT_LINES("Input lines: \"1 <path to file of lines> <enter \"y\" to clear current lines>\""),
        OUTPUT_LINES("Output lines: \"2 <name of file>\""),
        TRANSFORMATIONS("Transformations: \"3 <matrix file>\""),
        TRANSLATION("Translation: \"4 <dx> <dy>\""),
        BASIC_SCALE("Basic scale: \"5 <sx> <sy>\""),
        BASIC_ROTATE("Basic rotate: \"6 <angle>\""),
        SCALE("Scale: \"7 <sx> <sy> <cx> <cy>\""),
        ROTATE("Rotate: \"8 <angle> <cx> <cy>\""),
        HELP("Help: \"9\""),
        EXIT("Exit: \"10\"");

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
        JFrame frame = new JFrame(TITLE);
        EventQueue.invokeLater(() -> {
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
            System.out.print("\nENTER YOUR INPUT: ");
            String[] input = scanner.nextLine().split("\\s+");
            for (int i = 0; i < input.length; i++) {
                input[i] = input[i].replace("\\s+", "");
            }
            try {
                int ordinal = Integer.parseInt(input[0]);
                Option option = Option.values()[ordinal - 1];
                switch (option) {
                    case INPUT_LINES -> {
                        String file = input[1];
                        boolean clear = input.length < 3 || input[2].equalsIgnoreCase("y");
                        inputLines(lines, file, clear);
                    }
                    case TRANSFORMATIONS -> {
                        String file = input[1];
                        transformations(lines, file);
                    }
                    case TRANSLATION -> {
                        int dx = Integer.parseInt(input[1]);
                        int dy = Integer.parseInt(input[2]);
                        translate(lines, dx, dy);
                    }
                    case BASIC_ROTATE -> {
                        int rotation = Integer.parseInt(input[1]);
                        basicRotate(lines, rotation);
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
                EventQueue.invokeLater(frame::repaint);
            } catch (Exception e) {
                printErr(e);
            }
        }
        System.out.println("\n----------\nTHANK YOU FOR TRYING OUT MY PROJECT :)");
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
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

    private static void help() {
        System.out.println("OPTIONS:");
        Arrays.stream(Option.values()).forEach(option -> System.out.println("\t: " + option.getPrompt()));
        System.out.println('\n');
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
        System.out.println("Lines:");
        lines.forEach(line -> System.out.println("\t" + line));
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

    private static void transformations(List<Line> lines, String filename) {
        // TODO: apply trans
    }

    private static void translate(List<Line> lines, int dx, int dy) {
        lines.forEach(line -> {
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
        });
    }

    private static void basicScale(List<Line> lines, int sx, int sy) {
        scale(lines, sx, sy, 0, 0);
    }

    private static void scale(List<Line> lines, int sx, int sy, int cx, int cy) {
        lines.forEach(line -> {
            line.x1 -= cx;
            line.x2 -= cx;
            line.y1 -= cy;
            line.y2 -= cy;
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
            FuncMatrix funcMatrix = new FuncMatrix(3, 3, i -> 0, new HashMap<>() {{
                put(Coordinate.of(0, 0), i -> i * sx);
                put(Coordinate.of(1, 1), i -> i * sy);
                put(Coordinate.of(2, 2), i -> i);
            }});
            Matrix appliedMatrix1 = lineMatrix1.apply(funcMatrix);
            Matrix appliedMatrix2 = lineMatrix2.apply(funcMatrix);
            line.x1 = appliedMatrix1.get(0, 0) + cx;
            line.y1 = appliedMatrix1.get(0, 1) + cy;
            line.x2 = appliedMatrix2.get(0, 0) + cx;
            line.y2 = appliedMatrix2.get(0, 1) + cy;
        });
    }

    private static void basicRotate(List<Line> lines, int angle) {
        // TODO: basic rotation
    }

    private static void rotate(List<Line> lines, int angle, int cx, int cy) {
        // TODO: rotate all lines
    }

    private static void displayPixels(String filename) {

        // TODO: update pixels on screen
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
        Assignment3.translate(List.of(line), 30, 10);
        System.out.println(line);
        Assignment3.translate(List.of(line), -50, 10);
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

    private static void test6() {
        FuncMatrix funcMatrix = new FuncMatrix(3, 3, i -> 0, new HashMap<>() {{
            put(Coordinate.of(0, 0), i -> i * 3);
            put(Coordinate.of(1, 1), i -> i * 2);
            put(Coordinate.of(2, 2), i -> i);
        }});
        Matrix a = new Matrix(new int[][]{
                {
                        20, 50, 1
                }
        });
        Matrix b = a.apply(funcMatrix);
        System.out.println(b);
    }

    private static void test7() {
        List<Line> lines = new ArrayList<>() {{
            add(new Line(50, 200, 400, 100));
            add(new Line(100, 100, 150, 150));
        }};
        JFrame frame = new JFrame(TITLE);
        EventQueue.invokeLater(() -> {
            System.out.println("Lines before: " + lines);
            frame.add(new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    lines.forEach(line -> draw(g, line));
                }
            });
            frame.setVisible(true);
            frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
        Scanner scanner = new Scanner(System.in);
        System.out.print("Apply scale x: ");
        int sx = Integer.parseInt(scanner.nextLine());
        System.out.print("Apply scale y: ");
        int sy = Integer.parseInt(scanner.nextLine());
        System.out.print("Press enter to apply...");
        scanner.nextLine();
        basicScale(lines, sx, sy);
        System.out.println("Lines after: " + lines);
        System.out.print("Press enter to display changes...");
        scanner.nextLine();
        frame.repaint();
    }

}
