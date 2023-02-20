package main;

import javax.swing.*;
import java.awt.*;

class Surface extends JPanel {

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawString("Java 2D", 50, 50);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

}

public class HelloWorldGraphicsApp1 extends JFrame {

    public HelloWorldGraphicsApp1() {
        initUI();
    }

    private void initUI() {
        add(new Surface());
        setTitle("Simple Java 2D example");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            HelloWorldGraphicsApp1 helloWorldGraphicsApp = new HelloWorldGraphicsApp1();
            helloWorldGraphicsApp.setVisible(true);
        });
    }

}
