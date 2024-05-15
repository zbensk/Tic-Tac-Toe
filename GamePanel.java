import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel {
    
    // screen settings
    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;
    private static final int squareSize = 400/3;

    // Board: 1 represented by O, 2 represented by X, 0 for empty square
    // private int[][] gameBoard = {{1, 1, 1}, {2, 2, 2}, {1, 2, 0}}; 
    private int[][] gameBoard = new int[3][3];

    private int turn = 1;
    private String winner;

    private boolean running = true;

    public GamePanel() {

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setFocusable(true);

        // Mouse listener
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                if (running) {
                    int[] boardCoord = getBoardCoord(e.getX(), e.getY());
                    if (!(boardCoord[0] == -1)) {
                        makeMove(boardCoord);
                    }
                }
                
            }
        });

    }

    /**
     * Allows a move to be made based on the user's click
     */
    public void makeMove(int[] boardCoord) {
        int r = boardCoord[0];
        int c = boardCoord[1];

        // make sure square is unoccupied, then can move
        if (gameBoard[r][c] != 0) {
            return;
        }
        
        gameBoard[r][c] = turn;
        if (turn == 1) {
            turn = 2;
        } else {
            turn = 1;
        }

        // Check for winner 
        int result = checkWin();
        if (result != 0) {
            // determine winner as a string
            if (result == 1) {
                winner = "O";
            } else if (result == 2) {
                winner = "X";
            } else {
                winner = "No one";
            }
            running = false;
        }
        repaint();
    }

    /**
     * From an x and y value, return the index of the array that it is referring to, or [-1, -1] if not present
     */
    public int[] getBoardCoord(int x, int y) {
        for (int r = 0; r < 3; r++) {
            int yTopBound = 100 + (squareSize * r);
            int yBottomBound = 100 + (squareSize * (r + 1));
            for (int c = 0; c < 3; c++) {
                int xLeftBound = 100 + (squareSize * c);
                int xRightBound = 100 + (squareSize * (c + 1));
                if ((x >= xLeftBound && x <= xRightBound) && (y >= yTopBound && y <= yBottomBound)) {
                    return new int[] {r, c};
                }

            }
        }
        return new int[] {-1, -1};
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
    }

    /**
     * Draws the game board in the middle of the screen
     */
    public void drawBoard(Graphics g) {
        // background
        g.setColor(new Color(8, 153, 141));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // outer rectangle
        g.setColor(Color.BLACK);
        g.drawRect(100, 100, 400, 400);

        // inner rectangle
        g.setColor(Color.GRAY);
        g.fillRect(100, 100, 400, 400);

        // dividers between squares
        g.setColor(Color.BLACK);
        
        for (int i = 1; i <= 3; i++) {
            // columns
            g.drawLine(100 + (squareSize * i), 100, 100 + (squareSize * i), 500);
            // rows
            g.drawLine(100, 100 + (squareSize * i), 500, 100 + (squareSize * i));
        }

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (gameBoard[r][c] == 1) {
                    drawSquare(r, c, "O", g);
                } else if (gameBoard[r][c] == 2) {
                    drawSquare(r, c, "X", g);
                }
            }
        }

        // see if game is over
        if (!running) {
            g.setColor(new Color(189, 73, 6));
            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.drawString(winner + " wins!", 200, 75);
        }
    }

    /**
     * Draws just a square of the game board
     */
    public void drawSquare(int r, int c, String s, Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        g.drawString(s, 100 + (c * squareSize) + squareSize/3, 100 + (r * squareSize) + 2 * squareSize/3);
    }

    /**
     * @return 0 if game still going (board not full), 1 if O wins, 2 if X wins, and -1 if game is a tie
     */
    public int checkWin() {
        // Check rows
        for (int r = 0; r < gameBoard.length; r++) {
            int res = getWinner(gameBoard[r]);
            if (res != 0) { return res; }
        }
        // Check cols
        for (int c = 0; c < gameBoard.length; c++) {
            int res = getWinner(getCol(c));
            if (res != 0) {return res; }
        }
        // Check diagonals
        int winner = getForwardDiagWinner();
        if (winner != 0) { return winner; }
        winner = getBackwardsDiagWinner();
        if (winner != 0) { return winner; }
        // Check if board full (cats)
        if (boardFull()) { return -1; }
        return 0;
    }

    /**
     * Return 1 or 2 if all values are 1 or 2, and 0 otherwise
     */
    private int getWinner(int[] vals) {
        if (vals[0] == 0) { return 0; }
        for (int v : vals) {
            if (v != vals[0]) { return 0; }
        }
        return vals[0];
    }

    /**
     * Returns given column as an array
     */
    private int[] getCol(int c) {
        int[] column = new int[gameBoard.length];
        for (int r = 0; r < gameBoard.length; r++) { 
            column[r] = gameBoard[r][c];
        }
        return column;
    }

    private int getForwardDiagWinner() {
        int first = gameBoard[0][0];
        if (first == 0) { return 0; }
        for (int r = 1; r < gameBoard.length; r++) {
            if (gameBoard[r][r] != first) { return 0; }
        } 
        return first;
    }

    private int getBackwardsDiagWinner() {
        int width = gameBoard[0].length;
        int first = gameBoard[0][width - 1];
        if (first == 0) { return 0; }
        for (int r = 1; r < gameBoard.length; r++) {
            if (gameBoard[r][width - 1 - r] != first) { return 0; }
        } 
        return first;
    }

    private boolean boardFull() {
        for (int[] row : gameBoard) {
            for (int val : row) {
                if (val == 0) { return false; }
            }
        }
        return true;
    }

 
}

