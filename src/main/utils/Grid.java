package main.utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Grid extends JPanel {

    public final int ppm;
    public final int width;
    public final int height;
    public final List<Line> lines;

    public Grid(int ppm, int width, int height) {
        this.ppm = ppm;
        this.width = width;
        this.height = height;
        lines = new ArrayList<>();
    }

    @Override
    protected void paintComponent(Graphics g) {
        lines.forEach(line -> {
            float dx = line.x2 - line.x1;
            float dy = line.y2 - line.y1;
            float x = line.x1;
            float y = line.y1;
            float m;
            if (Math.abs(dx) > Math.abs(dy)) {
                m = dy / dx;
                for (int i = 0; i <= Math.abs(dx); i++) {
                    g.fillRect((int) x * ppm, (int) y * ppm, ppm, ppm);
                    x++;
                    y += m;
                }
            } else {
                m = dx / dy;
                for (int i = 0; i <= Math.abs(dy); i++) {
                    g.fillRect((int) x * ppm, (int) y * ppm, ppm, ppm);
                    if (dy < 0f) {
                        y--;
                        x -= m;
                    } else {
                        y++;
                        x += m;
                    }
                }
            }
        });
    }

}
