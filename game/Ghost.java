import java.util.ArrayList;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.sqrt;

import javax.imageio.ImageIO;

public class Ghost {
    protected Coordinates startPosition;
    protected Coordinates currPosition;
    private ArrayList<Coordinates> possibleMoves;
    private Coordinates targetPostion;
    private Random random = new Random();
    private double time;
    private double speed = 0;
    private double distance = 0;


    public Ghost(Coordinates position, int width, int height, double time) {
        this.startPosition = position;
        this.currPosition = new Coordinates(position.x, position.y);
        this.possibleMoves = new ArrayList<Coordinates>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (!(i == 0 && j == 0) && !(i == height - 1 && j == width - 1))
                    this.possibleMoves.add(new Coordinates(i, j));
            }
        }        
        this.time = time;
    }

    public void move() {
        if (!possibleMoves.isEmpty()) {
            int index = random.nextInt(possibleMoves.size());
            this.targetPostion = possibleMoves.get(index);
            possibleMoves.remove(index);
            startPosition.x = currPosition.x;
            startPosition.y = currPosition.y;
            distance = sqrt((targetPostion.x - startPosition.x) * (targetPostion.x - startPosition.x) + (targetPostion.y - startPosition.y) * (targetPostion.y - startPosition.y));
            speed = distance / time;
            //System.out.println("speed: " + speed + " distance: " + distance);
        }
    }

    public boolean update(double dt, Maze maze) {
        if (speed > 0) {
            //System.out.println("speed: " + speed);
            double s = speed * dt;
            double relativeDist = s / distance;
            double dx = (double)(targetPostion.x - startPosition.x) * relativeDist;
            double dy = (double)(targetPostion.y - startPosition.y) * relativeDist;
            
            currPosition.x += dx;
            currPosition.y += dy;

            if ((targetPostion.x - currPosition.x) * dx < 0 || (targetPostion.y - currPosition.y) * dy < 0) {
                currPosition.x = targetPostion.x;
                currPosition.y = targetPostion.y;
                speed = 0;

                maze.block((int)targetPostion.y, (int)targetPostion.x);
                maze.blocks.add(new Coordinates(targetPostion.y, targetPostion.x));
                System.out.println(maze);
                return true;
            }
            
            
        }
        return false;
    }

    BufferedImage getImage() {
        try {
            return ImageIO.read(new File("./data/images/ghost.png"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
    }


    
}
