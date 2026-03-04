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
import java.util.*;

public class PuzzleController {

    private ArrayList<Puzzle> puzzles = new ArrayList<>();
    private Puzzle currentPuzzle;

    private int successCount = 0;
    private int failCount = 0;
    private int requiredSuccess;
    private int maxFail = 3;
    private boolean finish = false;

    public PuzzleController(int requiredSuccess) {
        this.requiredSuccess = requiredSuccess;
        addPuzzles();
        RandomPuzzle();
    }

    private void addPuzzles() {
        puzzles.add(new ClickCirclePuzzle());
        puzzles.add(new ClickCirclePuzzle());
        puzzles.add(new ClickCirclePuzzle());
        puzzles.add(new ClickCirclePuzzle());
        puzzles.add(new ClickCirclePuzzle());

    }

    private void RandomPuzzle() {
        Random r = new Random();
        currentPuzzle = puzzles.get(r.nextInt(puzzles.size()));
    }

    public void update() {
        if (currentPuzzle == null) {
            return;
        }
        currentPuzzle.update();
    }

    public void draw(Graphics g) {
        if (currentPuzzle != null) {
            currentPuzzle.draw(g);
        }
    }

    public void Click(int x, int y) {
        if (currentPuzzle != null) {
            currentPuzzle.Click(x, y);
        }
        if (currentPuzzle.issuccess()) {
            successCount++;
            finish = true;
            

        }

        if (currentPuzzle.isFailed()) {
            failCount++;
            finish = true;
            

        }
    }

    public boolean isfinish() {
        if (finish) {
            finish = false;
            return true;
        }
        return false;

    }
    public void nextPuzzle() {
        RandomPuzzle();
    }

    public boolean isWin() {
        return successCount == requiredSuccess;
    }

    public boolean isLose() {
        return failCount == maxFail;
    }
}
