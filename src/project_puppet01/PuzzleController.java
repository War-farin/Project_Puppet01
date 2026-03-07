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

    private int puzzleIndex = 0;
    private int successCount = 0;
    private int failCount = 0;
    private int requiredSuccess;
    private int maxFail = 3;
    private boolean finish = false;

    public PuzzleController(int requiredSuccess, Image[] loadedImages) {
        this.requiredSuccess = requiredSuccess;
        addPuzzles(loadedImages);
        RandomPuzzle();
    }

    private void addPuzzles(Image[] imgs) {
        puzzles.clear(); 

        int[] answers = {3, 2, 1, 3, 2, 2, 4, 3, 1, 1, 3, 4, 4, 4, 1}; 

        for (int i = 0; i < 15; i++) {
            if (imgs[i] != null) {
                puzzles.add(new QuestionPuzzle(imgs[i], answers[i]));
            }
        }
        
        java.util.Collections.shuffle(puzzles);
    
        puzzleIndex = 0;
        currentPuzzle = (Puzzle)puzzles.get(puzzleIndex);
    }

    private void RandomPuzzle() {
    if (puzzleIndex >= puzzles.size()) {
        Collections.shuffle(puzzles);
        puzzleIndex = 0;
    }

    currentPuzzle = puzzles.get(puzzleIndex);
    puzzleIndex++;
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
    public boolean isfail() {
        if (currentPuzzle == null) return false;
        return currentPuzzle.isFailed();
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
