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
import java.awt.event.*;
import javax.sound.sampled.*;
import javax.swing.*;

public class GamePanel extends JPanel {

    private enum GameState {
        OPENGAME, MENU, TITLE, PREPARE, PUZZLE, BLACKSCREEN, BEFOREJUMPSCARE,
        WHITESCREEN, JUMPSCARE, WINSCREEN, LOSESCREEN, FINALSCREEN
    }
    private GameState gameState = GameState.OPENGAME;
    private PuppetBox puppetBox;
    private PuzzleController puzzleController;

    private float fade = 0f;
    private Timer openTimer;
    private Timer gameTimer;
    private Timer holdTimer;
    private Timer titleTimer;
    private Timer breakTimer;
    private boolean Break = false;
    private boolean playing = false;

    //private float puppetValue = 1;
    //private final float MAX = 100;
    //private boolean press = false;
    //private boolean bugclick = false;
    private int currentNight = 3;
    private final int MAX_NIGHT = 3;
    //private final int failHealth = 3;
    //private int failCount = 0;
    //private int successCount = 0;
    //private int requiredSuccess;

    private String MenuSound, PuppetboxSound, TitleSound, EndgameSound;
    private static Clip currentSound;

    private Image backgroundImage, MenuImage, TitleImage1, TitleImage2, TitleImage3,
            Boxopen, Jumpscare, WhiteImage, LoseImage, WinImage, FinalImage, OpenImage;
    private Image[] questionImages = new Image[15];
    private Rectangle newGameButton;
    private Rectangle continueButton;
    private Rectangle menuButton;
    private Rectangle nextButton;

    public GamePanel() {
        setFocusable(true);
        puppetBox = new PuppetBox();
        backgroundImage = new ImageIcon(
                getClass().getResource("/Image/display.png")).getImage();
        MenuImage = new ImageIcon(
                getClass().getResource("/Image/login3.png")).getImage();
        TitleImage1 = new ImageIcon(
                getClass().getResource("/Image/day1.jpg")).getImage();
        TitleImage2 = new ImageIcon(
                getClass().getResource("/Image/day2.jpg")).getImage();
        TitleImage3 = new ImageIcon(
                getClass().getResource("/Image/day3.jpg")).getImage();
        Boxopen = new ImageIcon(
                getClass().getResource("/Image/beforejumpscare.png")).getImage();
        Jumpscare = new ImageIcon(
                getClass().getResource("/Image/jumpscare.jpg")).getImage();
        LoseImage = new ImageIcon(
                getClass().getResource("/Image/youdied.png")).getImage();
        WinImage = new ImageIcon(
                getClass().getResource("/Image/live.png")).getImage();
        FinalImage = new ImageIcon(
                getClass().getResource("/Image/win.png")).getImage();
        WhiteImage = new ImageIcon(
                getClass().getResource("/Image/white.png")).getImage();
        OpenImage = new ImageIcon(
                getClass().getResource("/Image/opengame.png")).getImage();
        loadResources(); 
        MenuSound = "/Sound/menusound.wav";
        PuppetboxSound = "/Sound/puppetsound.wav";
        TitleSound = "/Sound/titlesound.wav";
        EndgameSound = "/Sound/endsound.wav";
        openTimer = new Timer(50, e -> {
            fade += 0.01f;
            if (fade >= 1f) {
                fade = 0f;
                gameState = GameState.MENU;
                playsound(MenuSound, true);
                openTimer.stop();
            }
            repaint();
        });
        openTimer.start();
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
                if (playing) {
                    puppetBox.decreaseAlltime();
                }
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
                        stopsound();
                        playing = false;
                        if (currentNight == MAX_NIGHT) {
                            Gamesuccess();
                        } else {
                            gameState = GameState.WINSCREEN;
                        }
                    } else if (puzzleController.isfail()) {
                        puppetBox.decrease(20);
                        if (puzzleController.isLose()) {
                            stopsound();
                            playing = false;
                            Gameover();
                        } else {
                            puzzleController.nextPuzzle();
                            startBreak();
                        }                        
                    } else {
                        puzzleController.nextPuzzle();
                        startBreak();
                    }
                }
            }
            repaint();
        });

        titleTimer = new Timer(4000, e -> {
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
                        stopsound();
                        puppetBox.setValue(50);
                        startNewGame();
                        playsound(TitleSound, false);
                    } else if (continueButton.contains(e.getPoint())) {
                        stopsound();
                        Difficulty();
                        gameState = GameState.TITLE;
                        playsound(TitleSound, false);
                        puppetBox.setValue(50);
                        titleTimer.start();
                    }
                } else if (gameState == GameState.PREPARE) {
                    if (isOnButton(e.getX(), e.getY())) {
                        if (!playing) {
                            playsound(PuppetboxSound, true);
                        }
                        playing = true;
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
                        playsound(MenuSound, true);
                    } else if (nextButton.contains(e.getPoint())) {
                        currentNight++;
                        Difficulty();
                        gameState = GameState.TITLE;
                        playsound(TitleSound, false);
                        puppetBox.setValue(50);
                        titleTimer.start();
                    }

                } else if (gameState == GameState.FINALSCREEN) {
                    if (menuButton.contains(e.getPoint())) {
                        gameState = GameState.MENU;
                        playsound(MenuSound, true);
                    }

                } else if (gameState == GameState.LOSESCREEN) {
                    if (menuButton.contains(e.getPoint())) {
                        gameState = GameState.MENU;
                        playsound(MenuSound, true);
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
        puzzleController = new PuzzleController(2 + currentNight, questionImages);
    }

    @Override

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        switch (gameState) {
            case OPENGAME:
                drawOpenGame(g);
                break;
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
            case WHITESCREEN:
                drawWhiteScreen(g);
                break;
            case BEFOREJUMPSCARE:
                drawBeforejumpscare(g);
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
        Rectangle button = new Rectangle(336, 192, 116, 116);
        return button.contains(x, y);
    }

    public void drawPrepare(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        //show puppetbutton
        /*int buttonWidth = 116;
        int buttonHeight = 116;
        int X = 336;
        int Y = 192;
        g.fillRect(X, Y, buttonWidth, buttonHeight);*/

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
        int buttonWidth = 120;
        int buttonHeight = 64;
        int X = 340;
        int newGameY = 374;
        int continueY = 454;
        newGameButton = new Rectangle(X, newGameY, buttonWidth, buttonHeight);
        continueButton = new Rectangle(X, continueY, buttonWidth, buttonHeight);
        /*g.setColor(Color.WHITE);
        g.fillRect(newGameButton.x, newGameButton.y, buttonWidth, buttonHeight);
        g.fillRect(continueButton.x, continueButton.y, buttonWidth, buttonHeight);*/
    }

    private void drawTitle(Graphics g) {
        if (currentNight == 1) {
            g.drawImage(TitleImage1, 0, 0, getWidth(), getHeight(), this);
        } else if (currentNight == 2) {
            g.drawImage(TitleImage2, 0, 0, getWidth(), getHeight(), this);
        } else if (currentNight == 3) {
            g.drawImage(TitleImage3, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void drawOpenGame(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(OpenImage, 0, 0, getWidth(), getHeight(), this);
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, fade));
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 1f));
    }

    private void drawBlackScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawWhiteScreen(Graphics g) {
        g.drawImage(WhiteImage, 0, 0, getWidth(), getHeight(), this);
        /*g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());*/
    }

    private void drawPuzzle(Graphics g) {
        if (puzzleController != null) {
            puzzleController.draw(g);
        }
    }

    private void drawBeforejumpscare(Graphics g) {
        g.drawImage(Boxopen, 0, 0, getWidth(), getHeight(), this);
    }

    private void drawJumpScare(Graphics g) {
        g.drawImage(Jumpscare, 0, 0, getWidth(), getHeight(), this);
    }

    private void drawWinScreen(Graphics g) {
        g.drawImage(WinImage, 0, 0, getWidth(), getHeight(), this);
        int buttonWidth = 168;
        int buttonHeight = 96;
        int nextX = 163;
        int menuX = 463;
        int Y = 423;
        nextButton = new Rectangle(nextX + 2, Y, buttonWidth, buttonHeight);
        menuButton = new Rectangle(menuX, Y, buttonWidth, buttonHeight);
        /*g.setColor(Color.WHITE);
        g.fillRect(nextButton.x, nextButton.y, buttonWidth, buttonHeight);
        g.fillRect(menuButton.x, menuButton.y, buttonWidth, buttonHeight); */
    }

    private void drawLoseScreen(Graphics g) {
        g.drawImage(LoseImage, 0, 0, getWidth(), getHeight(), this);
        int buttonWidth = 170;
        int buttonHeight = 94;
        int X = 322;
        int menuY = 408;
        menuButton = new Rectangle(X, menuY, buttonWidth, buttonHeight);
        /*g.setColor(Color.WHITE);
        g.fillRect(menuButton.x, menuButton.y, buttonWidth, buttonHeight);
        g.setColor(Color.BLACK);*/
    }

    private void drawFinalScreen(Graphics g) {
        g.drawImage(FinalImage, 0, 0, getWidth(), getHeight(), this);
        int buttonWidth = 170;
        int buttonHeight = 94;
        int X = 327;
        int menuY = 416;
        menuButton = new Rectangle(X, menuY, buttonWidth, buttonHeight);
        /* g.setColor(Color.WHITE);
        g.fillRect(menuButton.x, menuButton.y, buttonWidth, buttonHeight);*/
    }

    private void Gameover() {
        gameState = GameState.BLACKSCREEN;
        repaint();
        Timer blackTimer = new Timer(4000, null);
        blackTimer.setRepeats(false);
        blackTimer.addActionListener(B -> {
            gameState = GameState.BEFOREJUMPSCARE;
            repaint();
            Timer beforeTimer = new Timer(5000, null);
            beforeTimer.setRepeats(false);
            beforeTimer.addActionListener(bef -> {
                gameState = GameState.JUMPSCARE;
                repaint();
                Timer jumpTimer = new Timer(5000, null);
                jumpTimer.setRepeats(false);
                jumpTimer.addActionListener(J -> {
                    playsound(EndgameSound, false);
                    gameState = GameState.LOSESCREEN;
                    repaint();
                });
                jumpTimer.start();
            });
            beforeTimer.start();
        });
        blackTimer.start();
    }

    private void Gamesuccess() {
        gameState = GameState.WHITESCREEN;
        repaint();
        Timer WhiteTimer = new Timer(4000, null);
        WhiteTimer.setRepeats(false);
        WhiteTimer.addActionListener(W -> {
            playsound(EndgameSound, false);
            gameState = GameState.FINALSCREEN;
            repaint();
        });
        WhiteTimer.start();
    }

    public static void playsound(String sound, boolean loop) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(
                    GamePanel.class.getResource(sound));
            currentSound = AudioSystem.getClip();
            currentSound.open(audio);
            if (loop) {
                currentSound.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                currentSound.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopsound() {
        if (currentSound != null && currentSound.isRunning()) {
            currentSound.stop();
        }
    }
    

    private void loadResources() {
        try {
            for (int i = 0; i < 15; i++) {
                String path = "/Image/qna" + (i + 1) + ".png"; 
                java.net.URL imgURL = getClass().getResource(path);
                if (imgURL != null) {
                    questionImages[i] = new javax.swing.ImageIcon(imgURL).getImage();
                } else {
                    System.err.println("Could not find file: " + path);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
}
}
