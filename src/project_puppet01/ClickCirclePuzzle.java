/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project_puppet01;

/**
 *
 * @author saran
 */
import java.awt.*;
import java.util.Random;

public class ClickCirclePuzzle extends Puzzle {

    private int x, y;
    private int radius = 40;

    public ClickCirclePuzzle() {
        Random rand = new Random();
        x = 100 + rand.nextInt(500);
        y = 100 + rand.nextInt(300);
    }

    
    @Override
    public void update() {        
    }

    
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 600);

        g.setColor(Color.RED);
        g.fillOval(x - radius, y - radius, radius * 2, radius * 2);

        g.setColor(Color.WHITE);
        g.drawString("Click the circle!", 350, 50);
    }

    
    @Override
    public void Click(int mx, int my) {
        double distance = Math.sqrt((mx - x) * (mx - x) + (my - y) * (my - y));

        if (distance <= radius) {
            success = true;
        } else {
            failed = true;
        }
    }
}
