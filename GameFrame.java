import javax.swing.*;
import java.awt.*;

public class GameFrame {
    
    private JFrame frame;

    public GameFrame() {

        // Create frame and game panel
        frame = new JFrame();
        GamePanel panel = new GamePanel();
        frame.add(panel);

        // Configure settings
        // Initialize frame with default settings
        frame.setTitle("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
