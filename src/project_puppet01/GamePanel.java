/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project_puppet01;

/**
 *
 * @author saran
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Image;

public class GamePanel extends JPanel {

    private enum GameState {
        MENU, TITLE, PREPARE, PUZZLE, BLACKSCREEN, JUMPSCARE, WINSCREEN, LOSESCREEN, FINALSCREEN
    }
    private GameState gameState = GameState.MENU;
    private PuppetBox puppetBox;
    private PuzzleController puzzleController;

    private Timer gameTimer;
    private Timer holdTimer;
    private Timer titleTimer;
    private Timer breakTimer;
    private boolean Break = false;

    //private float puppetValue = 1;
    //private final float MAX = 100;
    //private boolean press = false;
    //private boolean bugclick = false;
    private int currentNight = 1;
    private final int MAX_NIGHT = 3;
    //private final int failHealth = 3;
    //private int failCount = 0;
    //private int successCount = 0;
    //private int requiredSuccess;

    private Image backgroundImage, MenuImage, TitleImage;

    private Rectangle newGameButton;
    private Rectangle continueButton;
    private Rectangle menuButton;
    private Rectangle nextButton;

    public GamePanel() {
        setFocusable(true);
        puppetBox = new PuppetBox();
        backgroundImage = new ImageIcon(
                getClass().getResource("/image/background.jpg")).getImage();
        MenuImage = new ImageIcon(
                getClass().getResource("/image/login.png")).getImage();
        TitleImage = new ImageIcon(
                getClass().getResource("/image/loading.png")).getImage();
        breakTimer = new Timer(5000, e -> {
            Break = false;
            puppetBox.sethold(false);
            gameState = GameState.PUZZLE;
            ((Timer) e.getSource()).stop();
        });
        breakTimer.setRepeats(false);
        gameTimer = new Timer(100, e -> {
            if (gameState == GameState.PREPARE
                    || gameState == GameState.PUZZLE) {
                puppetBox.decreaseAlltime();

                if (puppetBox.isEmpty()) {
                    Gameover();
                }
                if (gameState == GameState.PREPARE && puppetBox.isFull() && !Break) {
                    gameState = GameState.PUZZLE;
                    puppetBox.sethold(false);
                }
            }
            if (gameState == GameState.PUZZLE && puzzleController != null) {
                if (puzzleController.isfinish()) {
                    puzzleController.update();
                    puppetBox.sethold(false);
                    if (puzzleController.isWin()) {
                        if (currentNight == MAX_NIGHT) {
                            gameState = GameState.FINALSCREEN;
                        } else {
                            gameState = GameState.WINSCREEN;
                        }
                    } else if (puzzleController.isLose()) {
                        gameState = GameState.LOSESCREEN;
                    } else {
                        puzzleController.nextPuzzle();
                        startBreak();
                    }
                }
            }
            repaint();
        });

        titleTimer = new Timer(2000, e -> {
            gameState = GameState.PREPARE;
            ((Timer) e.getSource()).stop();
        });
        titleTimer.setRepeats(false);

        gameTimer.start();
        holdTimer = new Timer(100, e -> {
            puppetBox.increase();
            repaint();
        });
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (gameState == GameState.MENU) {
                    if (newGameButton.contains(e.getPoint())) {
                        puppetBox.setValue(50);
                        startNewGame();
                    } else if (continueButton.contains(e.getPoint())) {
                        gameState = GameState.TITLE;
                        puppetBox.setValue(50);
                        titleTimer.start();
                    }
                } else if (gameState == GameState.PREPARE) {
                    if (isOnButton(e.getX(), e.getY())) {
                        puppetBox.sethold(true);
                        holdTimer.start();
                    }
                } else if (gameState == GameState.PUZZLE) {
                    if (puzzleController != null) {
                        puzzleController.Click(e.getX(), e.getY());
                    }
                } else if (gameState == GameState.WINSCREEN) {
                    if (menuButton.contains(e.getPoint())) {
                        gameState = GameState.MENU;
                    } else if (nextButton.contains(e.getPoint())) {
                        currentNight++;
                        Difficulty();
                        gameState = GameState.TITLE;
                        puppetBox.setValue(50);
                        titleTimer.start();
                    }

                } else if (gameState == GameState.FINALSCREEN) {
                    if (menuButton.contains(e.getPoint())) {
                        gameState = GameState.MENU;
                    }

                } else if (gameState == GameState.LOSESCREEN) {
                    if (menuButton.contains(e.getPoint())) {
                        gameState = GameState.MENU;
                    }

                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (holdTimer != null && gameState == GameState.PREPARE) {
                    puppetBox.sethold(false);
                    holdTimer.stop();
                }
            }
        });
    }

    private void startBreak() {
        Break = true;
        gameState = GameState.PREPARE;
        breakTimer.restart();
    }

    private void startNewGame() {
        currentNight = 1;
        Difficulty();
        gameState = GameState.TITLE;
        titleTimer.start();
    }

    private void Difficulty() {
        puzzleController = new PuzzleController(2 + currentNight);
    }

    @Override

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        switch (gameState) {
            case MENU:
                drawMenu(g);
                break;
            case TITLE:
                drawTitle(g);
                break;
            case PREPARE:
                drawPrepare(g);
                break;
            case PUZZLE:
                drawPuzzle(g);
                break;
            case BLACKSCREEN:
                drawBlackScreen(g);
                break;
            case JUMPSCARE:
                drawJumpScare(g);
                break;
            case LOSESCREEN:
                drawLoseScreen(g);
                break;
            case WINSCREEN:
                drawWinScreen(g);
                break;
            case FINALSCREEN:
                drawFinalScreen(g);
                break;
        }
    }

    private boolean isOnButton(int x, int y) {
        Rectangle button = new Rectangle(330, 300, 140, 50);
        return button.contains(x, y);
    }

    public void drawPrepare(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        int barWidth = 550;
        int barHeight = 20;
        int x = (getWidth() - barWidth) / 2;
        int y = 40;

        g.setColor(Color.DARK_GRAY);
        g.fillRoundRect(x, y, barWidth, barHeight, 30, 30);

        int currentWidth = (int) (barWidth * (puppetBox.getValue() / (double) 100));
        g.setColor(new Color(227, 228, 234));
        g.fillRoundRect(x, y, currentWidth, barHeight, 30, 30);

        g.setColor(Color.BLACK);
        g.drawString((int) puppetBox.getValue() + "%", x + 10, y + 15);
    }

    public void drawMenu(Graphics g) {
        g.drawImage(MenuImage, 0, 0, getWidth(), getHeight(), this);
        int buttonWidth = 200;
        int buttonHeight = 50;
        int X = getWidth() / 2 - buttonWidth / 2;
        int newGameY = 420;
        int continueY = 490;
        newGameButton = new Rectangle(X, newGameY, buttonWidth, buttonHeight);
        continueButton = new Rectangle(X, continueY, buttonWidth, buttonHeight);
        g.setColor(Color.WHITE);
        g.fillRect(newGameButton.x, newGameButton.y, buttonWidth, buttonHeight);
        g.fillRect(continueButton.x, continueButton.y, buttonWidth, buttonHeight);
        g.setColor(Color.BLACK);
        g.drawString("NEW GAME", X + 50, newGameY + 30);
        g.drawString("CONTINUE", X + 50, continueY + 30);
    }

    private void drawTitle(Graphics g) {
        g.drawImage(TitleImage, 0, 0, getWidth(), getHeight(), this);
        Graphics2D g2 = (Graphics2D) g;

        String text = "NIGHT " + currentNight;

        Font font = new Font("Arial", Font.BOLD, 80);
        g2.setFont(font);

        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() - textHeight) / 2 + fm.getAscent();

        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);
    }

    private void drawBlackScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawPuzzle(Graphics g) {
        if (puzzleController != null) {
            puzzleController.draw(g);
        }
    }

    private void drawJumpScare(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);
        g.drawString("JUMPSCARE!", getWidth() / 2 - 40, getHeight() / 2);
    }

    private void drawWinScreen(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        g.drawString("YOU LIVE", getWidth() / 2 - 30, getHeight() / 2);
        int buttonWidth = 200;
        int buttonHeight = 50;
        int X = getWidth() / 2 - buttonWidth / 2;
        int nextY = 420;
        int menuY = 490;
        nextButton = new Rectangle(X, nextY, buttonWidth, buttonHeight);
        menuButton = new Rectangle(X, menuY, buttonWidth, buttonHeight);
        g.setColor(Color.WHITE);
        g.fillRect(nextButton.x, nextButton.y, buttonWidth, buttonHeight);
        g.fillRect(menuButton.x, menuButton.y, buttonWidth, buttonHeight);
        g.setColor(Color.BLACK);
        g.drawString("NEXT", X + 50, nextY + 30);
        g.drawString("MENU", X + 50, menuY + 30);
    }

    private void drawLoseScreen(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);
        g.drawString("YOU DIED", getWidth() / 2 - 40, getHeight() / 2);
        int buttonWidth = 200;
        int buttonHeight = 50;
        int X = getWidth() / 2 - buttonWidth / 2;
        int menuY = 490;
        menuButton = new Rectangle(X, menuY, buttonWidth, buttonHeight);
        g.setColor(Color.WHITE);
        g.fillRect(menuButton.x, menuButton.y, buttonWidth, buttonHeight);
        g.setColor(Color.BLACK);
        g.drawString("MENU", X + 50, menuY + 30);
    }

    private void drawFinalScreen(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.BLACK);
        g.drawString("YOU SURVIVE", getWidth() / 2 - 80, getHeight() / 2);
        int buttonWidth = 200;
        int buttonHeight = 50;
        int X = getWidth() / 2 - buttonWidth / 2;
        int menuY = 490;
        menuButton = new Rectangle(X, menuY, buttonWidth, buttonHeight);
        g.setColor(Color.WHITE);
        g.fillRect(menuButton.x, menuButton.y, buttonWidth, buttonHeight);
        g.setColor(Color.BLACK);
        g.drawString("MENU", X + 50, menuY + 30);

    }

    private void Gameover() {
        gameState = GameState.BLACKSCREEN;
        repaint();
        Timer blackTimer = new Timer(5000, null);
        blackTimer.setRepeats(false);
        blackTimer.addActionListener(e -> {
            gameState = GameState.JUMPSCARE;
            repaint();
            Timer jumpTimer = new Timer(3000, null);
            jumpTimer.setRepeats(false);
            jumpTimer.addActionListener(ev -> {
                gameState = GameState.LOSESCREEN;
                repaint();
            });
            jumpTimer.start();
        });
        blackTimer.start();
    }
}
