/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project_puppet01;

/**
 *
 * @author saran
 */

public class PuppetBox {

    private float puppetValue = 50;
    private final float MAX = 100;
    private boolean hold = false;

    public void decreaseAlltime() {
        if (!hold) {
            puppetValue -= 0.25f;
        }

        if (puppetValue < 0) {
            puppetValue = 0;
        }
    }

    public void increase() {
        if (hold) {
            puppetValue += 0.75f;
            if (puppetValue > MAX) {
                puppetValue = MAX;
            }
        }
    }

    public void decrease(float value) {
        puppetValue -= value;
    }

    public boolean isFull() {
        return puppetValue >= MAX;
    }

    public boolean isEmpty() {
        return puppetValue <= 0;
    }

    public void sethold(boolean press) {
        hold = press;
    }

    public float getValue() {
        return puppetValue;
    }

    public void setValue(float Value) {
        puppetValue = Value;
    }
}
