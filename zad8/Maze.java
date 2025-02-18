import java.util.Random;

import javax.imageio.ImageIO;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;



public class Maze {
    Direction[][] maze;
    int width;
    int height;
    private BufferedImage mazeImage;
    private BufferedImage blockImage;
    BufferedImage floor;
    BufferedImage leftWall;
    BufferedImage rightWall;
    BufferedImage upWall;
    BufferedImage downWall;
    BufferedImage metaGrey;
    BufferedImage floorDark;
    BufferedImage leftWallDark;
    BufferedImage rightWallDark;
    BufferedImage upWallDark;
    BufferedImage downWallDark;
    BufferedImage floorLight;
    BufferedImage leftWallLight;
    BufferedImage rightWallLight;
    BufferedImage upWallLight;
    BufferedImage downWallLight;
    boolean darkWalls = false;
    boolean darkFloor = false;
    Direction[][] mazeOriginal;
    ArrayList<Coordinates> blocks = new ArrayList<>();


    public Maze(int width, int height, boolean darkFloor, boolean darkWalls) {
        this.width = width;
        this.height = height;
        this.darkFloor = darkFloor;
        this.darkWalls = darkWalls;
        maze = new Direction[height][width];
        maze[0][0] = Direction.START;
        Random random = new Random();
        randomizedDfs(0, 0, maze, random);
        mazeOriginal = new Direction[height][width];
        for (int i = 0; i < height; i++) {
            mazeOriginal[i] = maze[i].clone();
        }

        try {
            floorLight = ImageIO.read(new File("./data/images/BackgroundLight.png"));
            floorDark = ImageIO.read(new File("./data/images/kafalekBezKrawedzi.png"));
            leftWallLight = ImageIO.read(new File("./data/images/MazeLLight.png"));
            rightWallLight = ImageIO.read(new File("./data/images/MazeRLight.png"));
            upWallLight = ImageIO.read(new File("./data/images/MazeULight.png"));
            downWallLight = ImageIO.read(new File("./data/images/MazeDLight.png"));
            blockImage = ImageIO.read(new File("./data/images/floorGhosted.png"));
            metaGrey = ImageIO.read(new File("./data/images/MetaGrey.png"));
            leftWallDark = ImageIO.read(new File("./data/images/MazeL.png"));
            rightWallDark = ImageIO.read(new File("./data/images/MazeR.png"));
            upWallDark = ImageIO.read(new File("./data/images/MazeU.png"));
            downWallDark = ImageIO.read(new File("./data/images/MazeD.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        if (darkFloor) {
            floor = floorDark;
        } else {
            floor = floorLight;
        }

        if (darkWalls) {
            leftWall = leftWallDark;
            rightWall = rightWallDark;
            upWall = upWallDark;
            downWall = downWallDark;
        } else {
            leftWall = leftWallLight;
            rightWall = rightWallLight;
            upWall = upWallLight;
            downWall = downWallLight;
        }

    }

    private void randomizedDfs(int i, int j, Direction[][] maze, Random random) {
        ArrayList<Direction> directions = new ArrayList<>();
        directions.add(Direction.RIGHT);
        directions.add(Direction.DOWN);
        directions.add(Direction.LEFT);
        directions.add(Direction.UP);

        if (i == 0 || maze[i-1][j] != null) directions.remove(Direction.UP);    
        if (i == maze.length - 1 || maze[i+1][j] != null) directions.remove(Direction.DOWN);
        if (j == 0 || maze[i][j-1] != null) directions.remove(Direction.LEFT);
        if (j == maze[0].length - 1 || maze[i][j+1] != null) directions.remove(Direction.RIGHT);

        //int possibleDirections = directions.size();

        while (!directions.isEmpty()) {
            //System.out.println(directions);
           // System.out.println(this);
            int index = random.nextInt(directions.size());
            Direction direction = directions.get(index);
            directions.remove(index);

            switch (direction) {
                case RIGHT:
                    maze[i][j+1] = Direction.LEFT;
                    randomizedDfs(i, j+1, maze, random);
                    break;
                case DOWN:
                    maze[i+1][j] = Direction.UP;
                    randomizedDfs(i+1, j, maze, random);
                    break;
                case LEFT:
                    maze[i][j-1] = Direction.RIGHT;
                    randomizedDfs(i, j-1, maze, random);
                    break;
                case UP:
                    maze[i-1][j] = Direction.DOWN;
                    randomizedDfs(i-1, j, maze, random);
                    break;
                case START:
                    break;
            }
            if (i == 0 || maze[i-1][j] != null) directions.remove(Direction.UP);    
            if (i == maze.length - 1 || maze[i+1][j] != null) directions.remove(Direction.DOWN);
            if (j == 0 || maze[i][j-1] != null) directions.remove(Direction.LEFT);
            if (j == maze[0].length - 1 || maze[i][j+1] != null) directions.remove(Direction.RIGHT);
            //possibleDirections = directions.size();
        }
        
    }

    private void walling(int i, int j, Graphics g) {
        boolean left = false;
        boolean right = false;
        boolean up = false;
        boolean down = false;

        if ((i == 0 || mazeOriginal[i-1][j] != Direction.DOWN) && maze[i][j]!=Direction.UP) up = true;
        if ((i == maze.length - 1 || mazeOriginal[i+1][j] != Direction.UP) && maze[i][j]!=Direction.DOWN) down = true;
        if ((j == 0 || mazeOriginal[i][j-1] != Direction.RIGHT) && maze[i][j]!=Direction.LEFT) left = true;
        if ((j == maze[0].length - 1 || mazeOriginal[i][j+1] != Direction.LEFT) && maze[i][j]!=Direction.RIGHT) right = true;

        if (left) g.drawImage(leftWall, j * floor.getWidth(), i * floor.getHeight(), null);
        if (right) g.drawImage(rightWall, j * floor.getWidth(), i * floor.getHeight(), null);
        if (up) g.drawImage(upWall, j * floor.getWidth(), i * floor.getHeight(), null);
        if (down) g.drawImage(downWall, j * floor.getWidth(), i * floor.getHeight(), null);
    }

    public BufferedImage mazeToImage() {
        mazeImage = new BufferedImage(floor.getWidth() * width, floor.getHeight() * height, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                Graphics g = mazeImage.getGraphics();
                g.drawImage(floor, j * floor.getWidth(), i * floor.getHeight(), null);
                if (i == maze.length - 1 && j == maze[0].length - 1) 
                    g.drawImage(metaGrey, j * floor.getWidth(), i * floor.getHeight(), null);

                    
                walling(i, j, g);
            }
        }
        System.out.println(this);

        return mazeImage;
    }

    public void block(int i, int j) {
        // for (int k = 0; k < mazeOriginal.length; k++) {
        //     for (int l = 0; l < mazeOriginal[0].length; l++) {
        //         System.out.print(mazeOriginal[k][l] + " ");
        //     }
        //     System.out.println();
        // }
        Graphics g = mazeImage.getGraphics();
        g.drawImage(blockImage, j * floor.getWidth(), i * floor.getHeight(), null);
        walling(i, j, g);
        maze[i][j] = null;
    }

    public BufferedImage getMazeImage() {
        return mazeImage;
    }

    public BufferedImage changeBackground() {
        if (darkFloor) {
            floor = floorLight;
            darkFloor = false;
        } else {
            floor = floorDark;
            darkFloor = true;
        }

        for (int i = 0; i < maze.length; i++) {
            maze[i] = mazeOriginal[i].clone();
        }
        mazeImage = mazeToImage();
        for (Coordinates c : blocks) {
            block((int)c.x, (int)c.y);
        }
        return mazeImage;
    }

    public BufferedImage changeWalls() {
        if (darkWalls) {
            leftWall = leftWallLight;
            rightWall = rightWallLight;
            upWall = upWallLight;
            downWall = downWallLight;
            darkWalls = false;
        } else {
            leftWall = leftWallDark;
            rightWall = rightWallDark;
            upWall = upWallDark;
            downWall = downWallDark;
            darkWalls = true;
        }

        for (int i = 0; i < maze.length; i++) {
            maze[i] = mazeOriginal[i].clone();
        }
        mazeImage = mazeToImage();
        for (Coordinates c : blocks) {
            block((int)c.x, (int)c.y);
        }
        return mazeImage;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                sb.append(maze[i][j] + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
