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

public abstract class Puzzle {

    protected boolean success = false;
    protected boolean failed = false;

    public abstract void update();

    public abstract void draw(Graphics g);

    public abstract void Click(int x, int y);

    public boolean issuccess() {
        return success;
    }

    public boolean isFailed() {
        return failed;
    }
}

