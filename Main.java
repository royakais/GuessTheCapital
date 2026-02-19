import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Launch the game GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // This creates the window and starts the game
                new Game(); 
            }
        });
    }
}