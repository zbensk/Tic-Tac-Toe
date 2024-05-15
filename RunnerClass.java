import javax.swing.*;

public class RunnerClass {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GameFrame frame = new GameFrame();
            }
        });
    }
}
