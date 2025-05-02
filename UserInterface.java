import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class UserInterface {
    private JFrame frame;

    public UserInterface() {
        frame = new JFrame("Depression Prediction Program");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());
    }

    public void displayMenu() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton trainButton = new JButton("Train Model with Your Data");
        JButton userDataButton = new JButton("Enter Your Information");
        JButton exitButton = new JButton("Exit");

        trainButton.addActionListener((ActionEvent e) -> displayTrainingScreen());
        userDataButton.addActionListener((ActionEvent e) -> displayUserDataScreen());
        exitButton.addActionListener((ActionEvent e) -> System.exit(0));

        panel.add(trainButton);
        panel.add(userDataButton);
        panel.add(exitButton);

        frame.getContentPane().removeAll();
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void displayTrainingScreen() {
        JOptionPane.showMessageDialog(frame, "Training screen placeholder.\nUpload your data here.");
    }

    public void displayUserDataScreen() {
        JOptionPane.showMessageDialog(frame, "User data input placeholder.\nAsk questions here.");
    }

    public void displayWellnessScore(float score, List<String> factors) {
        StringBuilder message = new StringBuilder();
        message.append(String.format("Your Wellness Score: %.2f (scale 0–1)\n", score));
        message.append("Key contributing factors:\n");
        for (String factor : factors) {
            message.append("- ").append(factor).append("\n");
        }
        JOptionPane.showMessageDialog(frame, message.toString());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserInterface ui = new UserInterface();
            ui.displayMenu();
        });
    }
}
