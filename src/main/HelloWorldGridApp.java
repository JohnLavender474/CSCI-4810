package main;

import javax.swing.*;
import java.awt.*;

class HelloWorldGrid extends JPanel {

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        int size = Math.min(getWidth() - 4, getHeight() - 4) / 10;
        int y = (getHeight() - (size * 10)) / 2;
        for (int i = 0; i < 10; i++) {
            int x = (getWidth() - (size * 10)) / 2;
            for (int vert = 0; vert < 10; vert++) {
                g.drawRect(x, y, size, size);
                x += size;
            }
            y += size;
        }
        g2d.dispose();
    }

}

public class HelloWorldGridApp {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Testing");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new HelloWorldGrid());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

}
