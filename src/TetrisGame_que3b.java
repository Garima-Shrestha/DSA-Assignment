/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Lenovo
 */
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class TetrisGame_que3b extends JPanel implements ActionListener, KeyListener, MouseListener {
    private final int GRID_WIDTH = 10; // Board width set to 10
    private final int GRID_HEIGHT = 18; // Reduced board height
    private final int BLOCK_SIZE = 25; // Smaller block size for more detail
    private Timer gameTimer; // Timer object to control game speed
    private boolean[][] grid; // 2D grid representing the game area
    private Shape activeShape, upcomingShape; // Active shape and next shape preview
    private int shapePosX, shapePosY; // Position of the current shape on the grid
    private boolean isGameOver; // Flag to check if the game is over
    private Random rand; // Random generator for generating shapes

    public TetrisGame_que3b() {
        setPreferredSize(new Dimension(GRID_WIDTH * BLOCK_SIZE + 100, GRID_HEIGHT * BLOCK_SIZE));
        setBackground(Color.DARK_GRAY); // Dark background for better contrast
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);

        grid = new boolean[GRID_HEIGHT][GRID_WIDTH];
        gameTimer = new Timer(400, this); // 400 ms timer interval for better speed
        gameTimer.start();
        rand = new Random();

        upcomingShape = new Shape(rand.nextInt(4));
        spawnNewShape();
    }

    // Reset the game to the initial state
    private void restartGame() {
        grid = new boolean[GRID_HEIGHT][GRID_WIDTH];
        isGameOver = false;
        gameTimer.start();
        upcomingShape = new Shape(rand.nextInt(4));
        spawnNewShape();
        repaint();
    }

    // Initialize a new shape and check for collision
    private void spawnNewShape() {
        activeShape = upcomingShape;
        upcomingShape = new Shape(rand.nextInt(4));
        shapePosX = GRID_WIDTH / 2 - 1;
        shapePosY = 0;

        if (checkCollision()) { // If collision happens at spawn, end the game
            isGameOver = true;
            gameTimer.stop();
        }
    }

    // Check if the shape collides with the grid or edges
    private boolean checkCollision() {
        for (int i = 0; i < activeShape.getHeight(); i++) {
            for (int j = 0; j < activeShape.getWidth(); j++) {
                if (activeShape.isFilled(i, j)) {
                    int x = shapePosX + j;
                    int y = shapePosY + i;

                    if (x < 0 || x >= GRID_WIDTH || y >= GRID_HEIGHT || (y >= 0 && grid[y][x])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Place the shape on the grid
    private void placeShape() {
        for (int i = 0; i < activeShape.getHeight(); i++) {
            for (int j = 0; j < activeShape.getWidth(); j++) {
                if (activeShape.isFilled(i, j)) {
                    grid[shapePosY + i][shapePosX + j] = true;
                }
            }
        }
        clearCompleteLines(); // Clear any full lines
        spawnNewShape(); // Spawn a new shape after placing the current one
    }

    // Clear any full lines from the grid
    private void clearCompleteLines() {
        for (int i = GRID_HEIGHT - 1; i >= 0; i--) {
            boolean lineFull = true;
            for (int j = 0; j < GRID_WIDTH; j++) {
                if (!grid[i][j]) {
                    lineFull = false;
                    break;
                }
            }
            if (lineFull) {
                for (int k = i; k > 0; k--) {
                    grid[k] = grid[k - 1].clone(); // Shift lines down
                }
                grid[0] = new boolean[GRID_WIDTH]; // Clear the top row
                i++;
            }
        }
    }

    // Action to be performed every game tick (timer event)
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver) {
            shapePosY++; // Move the shape down
            if (checkCollision()) { // If collision detected, place the shape
                shapePosY--;
                placeShape();
            }
            repaint(); // Redraw the screen
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Render the game grid (filled tiles)
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                if (grid[i][j]) {
                    g.setColor(Color.CYAN); // Color for filled tiles
                    g.fillRect(j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }

        // Render the active shape
        if (activeShape != null) {
            for (int i = 0; i < activeShape.getHeight(); i++) {
                for (int j = 0; j < activeShape.getWidth(); j++) {
                    if (activeShape.isFilled(i, j)) {
                        g.setColor(activeShape.getColor());
                        g.fillRect((shapePosX + j) * BLOCK_SIZE, (shapePosY + i) * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    }
                }
            }
        }

        // Render the "Next Shape" preview box
        int previewX = GRID_WIDTH * BLOCK_SIZE + 20;
        int previewY = 100;
        g.setColor(Color.WHITE);
        g.drawRect(previewX - 10, previewY - 10, 4 * BLOCK_SIZE, 4 * BLOCK_SIZE);
        g.drawString("Next Shape:", previewX, previewY - 20);

        // Display the next shape in the preview box
        if (upcomingShape != null) {
            for (int i = 0; i < upcomingShape.getHeight(); i++) {
                for (int j = 0; j < upcomingShape.getWidth(); j++) {
                    if (upcomingShape.isFilled(i, j)) {
                        g.setColor(upcomingShape.getColor());
                        g.fillRect(previewX + j * BLOCK_SIZE, previewY + i * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    }
                }
            }
        }

        // Show the "Game Over" message when the game ends
        if (isGameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            String message = "GAME OVER - Press Space to Restart";
            FontMetrics fm = g.getFontMetrics();
            int x = (GRID_WIDTH * BLOCK_SIZE - fm.stringWidth(message)) / 2;
            int y = GRID_HEIGHT * BLOCK_SIZE / 2;
            g.drawString(message, x, y);
        }
    }

    // Handle key presses for movement and actions
    @Override
    public void keyPressed(KeyEvent e) {
        if (isGameOver && e.getKeyCode() == KeyEvent.VK_SPACE) {
            restartGame();
            return;
        }

        if (isGameOver) return;

        int key = e.getKeyCode();

        // Move left
        if (key == KeyEvent.VK_LEFT) {
            shapePosX--;
            if (checkCollision()) shapePosX++;
        } 
        // Move right
        else if (key == KeyEvent.VK_RIGHT) {
            shapePosX++;
            if (checkCollision()) shapePosX--;
        } 
        // Move down
        else if (key == KeyEvent.VK_DOWN) {
            shapePosY++;
            if (checkCollision()) {
                shapePosY--;
                placeShape();
            }
        } 
        // Rotate the shape
        else if (key == KeyEvent.VK_UP) {
            activeShape.rotate();
            if (checkCollision()) activeShape.rotateBack();
        } 
        // Rotate the shape back
        else if (key == KeyEvent.VK_PAGE_UP) {
            activeShape.rotateBack();
            if (checkCollision()) activeShape.rotate();
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) { if (isGameOver) restartGame(); }
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris");
        TetrisGame_que3b gamePanel = new TetrisGame_que3b();
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class Shape {
    private int[][] shapePattern;
    private Color shapeColor;

    public Shape(int type) {
        switch (type) {
            case 0: // L shape
                shapePattern = new int[][]{{1, 0}, {1, 0}, {1, 1}};
                shapeColor = Color.ORANGE;
                break;
            case 1: // T shape
                shapePattern = new int[][]{{1, 1, 1}, {0, 1, 0}};
                shapeColor = Color.MAGENTA;
                break;
            case 2: // I shape
                shapePattern = new int[][]{{1, 1, 1, 1}};
                shapeColor = Color.CYAN;
                break;
            case 3: // Square shape
                shapePattern = new int[][]{{1, 1}, {1, 1}};
                shapeColor = Color.YELLOW;
                break;
            default:
                shapePattern = new int[][]{{1}};
                shapeColor = Color.WHITE;
                break;
        }
    }

    public int getWidth() { return shapePattern[0].length; }
    public int getHeight() { return shapePattern.length; }
    public boolean isFilled(int row, int col) { return shapePattern[row][col] == 1; }
    public Color getColor() { return shapeColor; }

    // Rotate the shape clockwise
    public void rotate() {
        int[][] rotated = new int[shapePattern[0].length][shapePattern.length];
        for (int i = 0; i < shapePattern.length; i++) {
            for (int j = 0; j < shapePattern[0].length; j++) {
                rotated[j][shapePattern.length - 1 - i] = shapePattern[i][j];
            }
        }
        shapePattern = rotated;
    }

    // Rotate the shape counter-clockwise
    public void rotateBack() {
        int[][] rotated = new int[shapePattern[0].length][shapePattern.length];
        for (int i = 0; i < shapePattern.length; i++) {
            for (int j = 0; j < shapePattern[0].length; j++) {
                rotated[shapePattern[0].length - 1 - j][i] = shapePattern[i][j];
            }
        }
        shapePattern = rotated;
    }
}
