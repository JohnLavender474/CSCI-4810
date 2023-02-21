package main;

import main.utils.Line;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Assignment_1_and_2 implements Runnable {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;
    private static final int PPM = 1;

    private static final String TITLE = "John Lavender - CSCI 4810 Assignments 1 and 2";

    private enum DrawType {
        BASIC,
        BRESENHAM
    }

    private final boolean test;

    public Assignment_1_and_2(boolean test) {
        this.test = test;
    }

    @Override
    public void run() {
        if (test) {
            test();
            return;
        }
        final Map<Line, DrawType> lineMap = new HashMap<>();
        JFrame frame = new JFrame(TITLE);
        EventQueue.invokeLater(() -> {
            frame.add(new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    lineMap.forEach((line, drawType) -> {
                        switch (drawType) {
                            case BASIC -> drawBasic(g, line);
                            case BRESENHAM -> drawBresenham(g, line);
                        }
                    });
                }
            });
            frame.setVisible(true);
            frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
        Scanner scanner = new Scanner(System.in);
        System.out.println(TITLE + "\n");
        boolean running = true;
        while (running) {
            System.out.print("\nENTER THE FILE TO LOAD: ");
            String input = scanner.nextLine();
            if (input.equals("exit")) {
                running = false;
            }
            try {
                File file = new File(input);
                Scanner reader = new Scanner(file);
                while (reader.hasNextLine()) {
                    String[] nextLine = reader.nextLine().split(",");
                    int[] vals = new int[4];
                    for (int i = 1; i < 5; i++) {
                        vals[i - 1] = Integer.parseInt(nextLine[i]);
                    }
                    Line line = new Line(vals);
                    DrawType drawType = nextLine[0].equalsIgnoreCase("basic") ? DrawType.BASIC : DrawType.BRESENHAM;
                    lineMap.put(line, drawType);
                }
            } catch (Exception e) {
                System.out.println("ERROR! ----------\n");
                e.printStackTrace();
                System.out.println(e.getMessage());
                System.out.println("----------\n");
            }
            frame.repaint();
        }
    }

    private static void drawBasic(Graphics g, Line line) {
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
                g.fillRect((int) x, (int) (WINDOW_HEIGHT - y) * PPM, PPM, PPM);
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

    private static void drawBresenham(Graphics g, Line line) {
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

    private static void test() {
        Map<Line, DrawType> lineMap = new HashMap<>() {{
            put(new Line(50, 50, 300, 400), DrawType.BASIC);
            put(new Line(50, 500, 500, 500), DrawType.BRESENHAM);
        }};
        System.out.println("Drawing: " + lineMap);
        JFrame frame = new JFrame(TITLE);
        EventQueue.invokeLater(() -> {
            frame.add(new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    lineMap.forEach((line, drawType) -> {
                        switch (drawType) {
                            case BASIC -> drawBasic(g, line);
                            case BRESENHAM -> drawBresenham(g, line);
                        }
                    });
                }
            });
            frame.setVisible(true);
            frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }

}
