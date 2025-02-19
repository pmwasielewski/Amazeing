import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Knight {
    private Coordinates prevPosition;
    Coordinates currPosition;
    private BufferedImage[] knightFrames;
    int mazeWidth;
    int mazeHeight;
    private int frameIndex = 0;
    public int knightMoves = 0;
    
    public Knight(Coordinates position, int width, int height) {
        this.prevPosition = new Coordinates(position.x, position.y);
        this.currPosition = new Coordinates(position.x, position.y);
        this.mazeWidth = width;
        this.mazeHeight = height;

        knightFrames = new BufferedImage[2];
        try {
            knightFrames[0] = ImageIO.read(new File("./data/images/KnightFrontExhale.png"));
            knightFrames[1] = ImageIO.read(new File("./data/images/KnightFrontInhale.png"));
        } catch (IOException e) {
            e.printStackTrace();
    }
    }

    public boolean move(Direction direction, Maze maze) {
        prevPosition.x = currPosition.x;
        prevPosition.y = currPosition.y;
        int i = (int) currPosition.y;
        int j = (int) currPosition.x;

        switch (direction) {
            case UP:
                if (i == 0) break;
                if (maze.maze[i][j] == Direction.UP && maze.maze[i-1][j]!=null) {
                    currPosition.y--;
                }
                else if (maze.maze[i-1][j] == Direction.DOWN) {
                    currPosition.y--;
                }
                break;
            case DOWN:
                if (i == mazeHeight - 1) {
                    System.out.println("loool" + i + " " + j);
                    break;
                }
                if (maze.maze[i][j] == Direction.DOWN && maze.maze[i+1][j]!=null) {
                    currPosition.y++;
                }
                else if (maze.maze[i+1][j] == Direction.UP) {
                    currPosition.y++;
                }
                break;
            case LEFT:
                if (j == 0) break;
                if (maze.maze[i][j] == Direction.LEFT && maze.maze[i][j-1]!=null) {
                    currPosition.x--;
                }
                else if (maze.maze[i][j-1] == Direction.RIGHT) {
                    currPosition.x--;
                }
                break;
            case RIGHT:
                if (j == mazeWidth - 1) {
                    System.out.println("loool");
                    break;
                }
                if (maze.maze[i][j] == Direction.RIGHT && maze.maze[i][j+1]!=null) {
                    currPosition.x++;
                }
                else if (maze.maze[i][j+1] == Direction.LEFT) {
                    currPosition.x++;
                }
                break;
            case START:
                break;
        }
        if (prevPosition.x == currPosition.x && prevPosition.y == currPosition.y) {
            return false;
        }
        knightMoves ++;
        return true;

    }

    public BufferedImage[] getFrames() {
        return knightFrames;
    }

    public BufferedImage getCurrentFrame() {
        return knightFrames[frameIndex];
    }

    public void nextFrame() {
        frameIndex = (frameIndex + 1) % knightFrames.length;
    }
}
