import javax.swing.*;

import static java.lang.Math.floor;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class GamePanel extends JPanel{
    JMenuBar menuBar;
    JMenu gra;
    JMenu ustawienia;
    JMenu pomoc;
    JMenu ruchy;
    Okno okno;
    int MENU_BAR_HEIGHT = 23;
    Maze maze;
    BufferedImage mazeImage;
    double[] scale = new double[2];
    BufferedImage ghostImage;
    BufferedImage ghostedFloorImage;
    BufferedImage ghostResized;
    BufferedImage mazeResized;
    Ghost ghost;
    double[] ghostParams;
    int mazeWidth;
    int mazeHeight;
    Timer timer;
    Timer animationTimer;
    long lastTime;
    int lastWidth;
    int lastHeight;
    boolean topPanelVisible = false;
    int topOfPanel = 0;
    boolean mazeChanged = false;
    Knight knight;
    boolean knightMoved = false;
    BufferedImage knightImage;
    BufferedImage knightResized;
    BufferedImage[] knightFrames = new BufferedImage[2];
    Timer knightTimer;
    boolean knightChanged = false;
    int nextWidth;
    int nextHeight;
    long startTime;
    long gameTime;

    {
        MouseListener ml = new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                System.out.println("Mouse clicked");
            }
            public void mousePressed(MouseEvent e) {
                System.out.println("Mouse pressed");
            }
            public void mouseReleased(MouseEvent e) {
                System.out.println("Mouse released");
            }
            public void mouseEntered(MouseEvent e) {
                System.out.println("Mouse entered");
            }
            public void mouseExited(MouseEvent e) {
                System.out.println("Mouse exited");
            }
        };
        addMouseListener(ml);

        MouseMotionListener mml = new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {
                if (e.getY() < MENU_BAR_HEIGHT) {
                    menuBar.setVisible(true);
                } else {
                    menuBar.setVisible(false);
                }
            }
            public void mouseDragged(MouseEvent e) {
                // Implement the method as required
            }
        };
        addMouseMotionListener(mml);

        KeyListener kl = new KeyListener() {
            public void keyTyped(KeyEvent e) {
                System.out.println("Key typed");
                // Implement the method as required
            }
            public void keyPressed(KeyEvent e) {
                System.out.println("Key pressed");
                
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        moveUp();
                        break;

                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        moveDown();
                        break;

                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        moveLeft();
                        break;

                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        moveRight();
                        break;
                }
                if (knightMoved) {
                    if (knight.currPosition.x == mazeWidth - 1 && knight.currPosition.y == mazeHeight - 1) {
                        win();
                    }
                }
            }
            public void keyReleased(KeyEvent e) {
                // Implement the method as required
            }
        };
        addKeyListener(kl);
        //setFocusable(true);
    }

    public void createRound(int width, int height, boolean darkFloor, boolean darkWalls) {
        okno.cardLayout.show(okno.statesPanels, "game");
        System.out.println("Creating round");
        mazeWidth = nextWidth;
        mazeHeight = nextHeight;
        okno.setJMenuBar(menuBar);
        menuBar.setVisible(false);
        maze = new Maze(mazeWidth, mazeHeight, darkFloor, darkWalls);
        mazeImage = maze.mazeToImage();
        requestFocusInWindow();

        ghost = new Ghost(new Coordinates(mazeWidth-1, mazeHeight-1), mazeWidth, mazeHeight, 4000);    
        timer = new Timer(5000, e -> {
            ghost.move();
        });
        timer.start();
        animationTimer = new Timer(16, e -> {
            repaint();
        });
        animationTimer.start();

        ghostImage = ghost.getImage();

        lastTime = System.nanoTime();

        knight = new Knight(new Coordinates(0, 0), mazeWidth, mazeHeight);
        //knightImage = knight.
        knightFrames = knight.getFrames();
        
        System.out.println("Knight frames: " + knightFrames[0] + " " + knightFrames[1]);
        knightImage = knight.getCurrentFrame();

        knightTimer = new Timer(400, e -> {
            knightImage = knight.getCurrentFrame();
            knight.nextFrame();
            knightChanged = true;
        });
        knightTimer.start();
        

        setVisible(true);

        startTime = System.currentTimeMillis();
    }

    public void endRound() {
        setVisible(false);
        timer.stop();
        animationTimer.stop();
        knightTimer.stop();

        gameTime = System.currentTimeMillis() - startTime;
    }

    public void restart() {
        endRound();
        createRound(mazeWidth, mazeHeight, maze.darkFloor, maze.darkWalls);
    }

    public void lose() {
        endRound();
        okno.losePanel.getStats(knight.knightMoves, gameTime);
        okno.cardLayout.show(okno.statesPanels, "lose");
    }
    
    public void win() {
        endRound();
        okno.winPanel.getStats(knight.knightMoves, gameTime);
        okno.cardLayout.show(okno.statesPanels, "win");
        menuBar.setVisible(true);
    }

    public void moveRight() {
        knightMoved = knight.move(Direction.RIGHT, maze);
    }
    public void moveLeft() {
        knightMoved = knight.move(Direction.LEFT, maze);
    }
    public void moveUp() {
        knightMoved = knight.move(Direction.UP, maze);
    }
    public void moveDown() {
        knightMoved = knight.move(Direction.DOWN, maze);
    }

    public void showRules() {
        JOptionPane.showMessageDialog(this, "Zasady gry:\n" +
        "1. Ruchy rycerza wykonuje się za pomocą strzałek na klawiaturze.\n" +
        "2. Celem gry jest dotarcie rycerzem do dolnego prawego rogu labiryntu.\n" +
        "3. Rycerz nie może przechodzić przez ściany labiryntu.\n" +
        "4. Rycerz nie może przechodzić przez duchy.\n" +
        "5. Duchy poruszają się losowo po labiryncie.\n" +
        "6. W przypadku dotknięcia ducha, gracz przegrywa.\n" +
        "7. W przypadku dotarcia do dolnego prawego rogu labiryntu, gracz wygrywa.\n" +
        "8. Gra kończy się po wygranej lub przegranej.");

    }

    public void showAuthor() {
        JOptionPane.showMessageDialog(this, "Autor: Petrus1013");
    }

    public void changeBackground() {
        mazeImage = maze.changeBackground();
        mazeChanged = true;
    }

    public void changeWalls() {
        mazeImage = maze.changeWalls();
        mazeChanged = true;
    }

    public void changeHeight(int height) {
        nextHeight = height;
    }

    public void changeWidth(int width) {
        nextWidth = width;
    }

    public GamePanel(Okno okno) {
        super();
        //setLayout(new GridLayout(10, 10));
        //setBackground(Color.DARK_GRAY);
        this.okno = okno;
        setFocusable(true);
        requestFocusInWindow();
        setVisible(false);

        nextHeight = 10;
        nextWidth = 10;

        menuBar = new Bar(this);

        //okno.setJMenuBar(menuBar);   
    }

    private double[] scaledParams (BufferedImage img) {
        double[] params = new double[2];
        params[0] = (double)img.getWidth() * scale[0];
        params[1] = (double)img.getHeight() * scale[1];
        return params;
    }

    private BufferedImage resizeImage(BufferedImage img, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resized.createGraphics();
        g.drawImage(img, 0, 0, width, height, null);
        g.dispose();
        return resized;
    }



    public void paintComponent(Graphics g) {
        long dt = System.nanoTime() - lastTime;
        double dtMillis = (double)dt / 1000000;


        if (this.getWidth() != lastWidth || this.getHeight() != lastHeight || menuBar.isVisible() != topPanelVisible || mazeChanged || knightChanged) {
            if (menuBar.isVisible()) {
                topOfPanel = MENU_BAR_HEIGHT;
                topPanelVisible = true;
            } else {
                topOfPanel = 0;
                topPanelVisible = false;
            }
            if (mazeChanged) {
                mazeImage = maze.getMazeImage();
                mazeChanged = false;
            }
            scale[0] = (double)this.getWidth() / (mazeImage.getWidth());
            scale[1] = (double)(this.getHeight() + topOfPanel) / (mazeImage.getHeight());
            lastWidth = this.getWidth();
            lastHeight = this.getHeight();

            mazeResized = resizeImage(mazeImage, this.getWidth(), this.getHeight() + topOfPanel);

            ghostParams = scaledParams(ghostImage);
            
            ghostResized = resizeImage(ghostImage, (int)scaledParams(ghostImage)[0], (int)scaledParams(ghostImage)[1]);

            knightResized = resizeImage(knightImage, (int)scaledParams(knightImage)[0], (int)scaledParams(knightImage)[1]);


            knightChanged = false;
            //System.out.println("Resized");

        }

        double tileWidth = (double)this.getWidth() / (double)mazeWidth;
        double tileHeight = (double)(this.getHeight() + topOfPanel) / (double)mazeHeight;
        mazeChanged = ghost.update(dtMillis, maze);
        int ghostX = (int)floor(tileWidth * ghost.currPosition.x + (double)(tileWidth - ghostParams[0])/2);
        int ghostY = (int)floor(tileHeight * ghost.currPosition.y - topOfPanel + (double)(tileHeight - ghostParams[1])/2);
        int knightX = (int)floor(tileWidth * knight.currPosition.x + (double)(tileWidth - ghostParams[0])/2);
        int knightY = (int)floor(tileHeight * knight.currPosition.y - topOfPanel + (double)(tileHeight - ghostParams[1])/2);


        lastTime = System.nanoTime();

        g.drawImage(mazeResized, 0, 0 - topOfPanel, null);
        g.drawImage(ghostResized, ghostX, ghostY, null);
        g.drawImage(knightResized, knightX, knightY, null);

        //System.out.println(dtMillis);
        Toolkit.getDefaultToolkit().sync();
        //System.out.println(this.hasFocus());
    }
    
}
