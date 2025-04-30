import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class AH extends JPanel implements KeyListener {
    private int[][] board;
    private int pacmanX, pacmanY;
    private int score = 0;
    private int ghostX = 5, ghostY = 1;        // First ghost
    private int ghost2X = 10, ghost2Y = 5;     // Second ghost
    private Timer ghostTimer;
    private boolean gameOver = false;
    private boolean gameWon = false;

    private static final String SCORE_FILE = "score.txt";

    public AH() {
        // Initialize the board
        board = new int[][]{
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1},
                {1, 2, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 2, 1},
                {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
                {1, 2, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 2, 1},
                {1, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 1},
                {1, 1, 1, 1, 2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 1, 1, 1, 1},
                {1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1},
                {1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 0, 1, 2, 1, 1, 1, 1},
                {1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1},
                {1, 2, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 2, 1},
                {1, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 1},
                {1, 1, 2, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 2, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        pacmanX = 4; // Starting position
        pacmanY = 1;

        //loadScore();

        // Create a Timer to move the ghost every 500ms
        ghostTimer = new Timer(250, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveGhost();
                moveGhost2();
                repaint();
            }
        });

        ghostTimer.start();

        setFocusable(true);
        addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the board
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == 1) {
                    g.setColor(Color.BLUE);
                    g.fillRect(col * 20, row * 20, 20, 20);
                }
                if (board[row][col] == 2) {
                    g.setColor(Color.magenta);
                    g.fillOval(col * 20 + 7, row * 20 + 7, 3, 3); // Pellet
                }
            }
        }

        // Draw Pac-Man
        g.setColor(Color.PINK);
        g.fillArc(pacmanX * 20, pacmanY * 20, 20, 20, 30, 300);

        // Score
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.drawString("Score: " + score, 10, 10);

        // First ghost
        g.setColor(Color.RED);
        g.fillOval(ghostX * 20, ghostY * 20, 20, 20);

        // Second ghost
        g.setColor(Color.GREEN);
        g.fillOval(ghost2X * 20, ghost2Y * 20, 20, 20);

        // Game Over or Win message
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("GAME OVER!", getWidth() / 2 - 100, getHeight() / 2);
        } else if (gameWon) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("YOU WIN!", getWidth() / 2 - 80, getHeight() / 2);
        }
    }

    private void moveGhost() {
        int direction = (int) (Math.random() * 4);
        switch (direction) {
            case 0:
                if (ghostY > 0 && board[ghostY - 1][ghostX] != 1) ghostY--;
                break;
            case 1:
                if (ghostY < board.length - 1 && board[ghostY + 1][ghostX] != 1) ghostY++;
                break;
            case 2:
                if (ghostX > 0 && board[ghostY][ghostX - 1] != 1) ghostX--;
                break;
            case 3:
                if (ghostX < board[0].length - 1 && board[ghostY][ghostX + 1] != 1) ghostX++;
                break;
        }
    }

    private void moveGhost2() {
        int direction = (int) (Math.random() * 4);
        switch (direction) {
            case 0:
                if (ghost2Y > 0 && board[ghost2Y - 1][ghost2X] != 1) ghost2Y--;
                break;
            case 1:
                if (ghost2Y < board.length - 1 && board[ghost2Y + 1][ghost2X] != 1) ghost2Y++;
                break;
            case 2:
                if (ghost2X > 0 && board[ghost2Y][ghost2X - 1] != 1) ghost2X--;
                break;
            case 3:
                if (ghost2X < board[0].length - 1 && board[ghost2Y][ghost2X + 1] != 1) ghost2X++;
                break;
        }
    }

    private void checkGhostCollision() {
        if ((pacmanX == ghostX && pacmanY == ghostY) || (pacmanX == ghost2X && pacmanY == ghost2Y)) {
            gameOver = true;
            ghostTimer.stop();
        }
    }

    private boolean allPelletsCollected() {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == 2) return false;
            }
        }
        return true;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver || gameWon) return;

        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_UP:
                if (pacmanY > 0 && board[pacmanY - 1][pacmanX] != 1) pacmanY--;
                break;
            case KeyEvent.VK_DOWN:
                if (pacmanY < board.length - 1 && board[pacmanY + 1][pacmanX] != 1) pacmanY++;
                break;
            case KeyEvent.VK_LEFT:
                if (pacmanX > 0 && board[pacmanY][pacmanX - 1] != 1) pacmanX--;
                break;
            case KeyEvent.VK_RIGHT:
                if (pacmanX < board[0].length - 1 && board[pacmanY][pacmanX + 1] != 1) pacmanX++;
                break;
        }

        if (board[pacmanY][pacmanX] == 2) {
            board[pacmanY][pacmanX] = 0;
            score += 10;
            saveScore();
        }

        checkGhostCollision();

        if (allPelletsCollected()) {
            gameWon = true;
            ghostTimer.stop();
        }

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    private void saveScore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE))) {
            writer.write(String.valueOf(score));
        } catch (IOException ex) {
            System.err.println("Error saving score: " + ex.getMessage());
        }
    }

    private void loadScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                score = Integer.parseInt(line.trim());
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Score file not found. Starting with default score.");
        } catch (IOException | NumberFormatException ex) {
            System.err.println("Error loading score: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pac-Man");
        AH game = new AH();
        frame.add(game);
        frame.setSize(game.board[0].length * 20, game.board.length * 23);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
