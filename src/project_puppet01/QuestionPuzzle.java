/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author peeyathat
 */
package project_puppet01;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class QuestionPuzzle extends Puzzle {
    
    private Image questionImage;
    private int correctAnswer;  
    private Rectangle[] hitboxes;

    /**
     * @param questionImage
     * @param correctAnswer
     */
    public QuestionPuzzle(Image questionImage, int correctAnswer) {
        this.questionImage = questionImage;
        this.correctAnswer = correctAnswer;

        hitboxes = new Rectangle[4];
        
        hitboxes[0] = new Rectangle(150, 300, 240, 90);
        hitboxes[1] = new Rectangle(410, 300, 240, 90);
        hitboxes[2] = new Rectangle(150, 420, 240, 90);
        hitboxes[3] = new Rectangle(410, 420, 240, 90);
    }

    @Override
    public void draw(Graphics g) {
        if (questionImage != null) {
            g.drawImage(questionImage, 0, 0, 800, 600, null);
        }

        
        /*g.setColor(java.awt.Color.RED);
        for(int i = 0; i < 4; i++) {
            g.drawRect(hitboxes[i].x, hitboxes[i].y, hitboxes[i].width, hitboxes[i].height);
        }*/
        
    }

    @Override
    public void Click(int mx, int my) {
        if (success || failed) return; 

        
        for (int i = 0; i < 4; i++) {
            if (hitboxes[i].contains(mx, my)) {
                if ((i + 1) == correctAnswer) {
                    success = true;
                } else {
                    failed = true; 
                }
                break;
            }
        }
    }

    @Override
    public void update() {
    }
}
