import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.CancellationException;

public class UserInterface {
    private JFrame frame;
    private Forest forest;
    private WellnessFeedback feedback;
    private boolean trainingCompleted = false;  

    public UserInterface() {
        frame = new JFrame("Depression Prediction Program");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        forest = new Forest();
        feedback = new WellnessFeedback();
    }

    public void displayMenu() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton trainButton    = new JButton("Train Model with Your Data");
        JButton userDataButton = new JButton("Enter Your Information");
        JButton exitButton     = new JButton("Exit");

        trainButton.addActionListener(e -> displayTrainingScreen());
        userDataButton.addActionListener(e -> displayUserDataScreen());
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(trainButton);
        panel.add(userDataButton);
        panel.add(exitButton);

        frame.getContentPane().removeAll();
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void displayTrainingScreen() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select CSV Data File");
        if (chooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) return;
        File file = chooser.getSelectedFile();

        RecordCollection data;
        try {
            data = RecordCollection.loadFromCSV(file.getAbsolutePath());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame,
                "Error loading CSV: " + ex.getMessage(),
                "Load Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog progress = new JDialog(frame, "Training Forest…", true);
        JLabel label = new JLabel("Starting…");
        final int TOTAL = 10;
        JProgressBar bar = new JProgressBar(0, TOTAL);
        bar.setStringPainted(true);
        bar.setIndeterminate(true);
        JButton cancel = new JButton("Cancel");

        progress.setLayout(new BorderLayout(8,8));
        progress.add(label, BorderLayout.NORTH);
        progress.add(bar, BorderLayout.CENTER);
        progress.add(cancel, BorderLayout.SOUTH);
        progress.pack();
        progress.setLocationRelativeTo(frame);

        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                forest.trainForest(data, (done, total) -> publish(done));
                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                if (bar.isIndeterminate()) {
                    bar.setIndeterminate(false);
                    label.setText("Built tree 0 of " + TOTAL + "…");
                }
                int done = chunks.get(chunks.size() - 1);
                bar.setValue(done);
                label.setText("Built tree " + done + " of " + TOTAL);
            }

            @Override
            protected void done() {
                progress.dispose();
                try {
                    get();  
                    trainingCompleted = true;
                    System.out.println("DEBUG: forest has " + forest.getTrees().size() + " trees after training.");
                    JOptionPane.showMessageDialog(frame,
                        "Training complete on " + data.size() + " records.",
                        "Training Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (CancellationException e) {
                    JOptionPane.showMessageDialog(frame,
                        "Training cancelled.",
                        "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame,
                        "Error during training: " + ex.getCause().getMessage(),
                        "Training Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        cancel.addActionListener(e -> {
            worker.cancel(true);
            forest.cancelTraining();
        });

        worker.execute();
        progress.setVisible(true);
    }

    public void displayUserDataScreen() {
        if (!trainingCompleted) {
            JOptionPane.showMessageDialog(frame,
                "Please train the model with your data before entering your information.",
                "Model Not Trained", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        // Gender
        panel.add(new JLabel("Gender:"));
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        panel.add(genderBox);

        // Age
        panel.add(new JLabel("Age:"));
        JTextField ageField = new JTextField();
        panel.add(ageField);

        // Academic Pressure (0-5)
        panel.add(new JLabel("Academic Pressure (0-5):"));
        JSpinner pressureSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));
        panel.add(pressureSpinner);

        // Study Satisfaction (0-5)
        panel.add(new JLabel("Study Satisfaction (0-5):"));
        JSpinner satisfactionSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));
        panel.add(satisfactionSpinner);

        // Sleep Duration
        panel.add(new JLabel("Sleep Duration:"));
        JComboBox<String> sleepBox = new JComboBox<>(new String[]{
            "< 5 hours", "5-6 hours", "6-7 hours", "7-8 hours", "> 8 hours"
        });
        panel.add(sleepBox);

        // Dietary Habits
        panel.add(new JLabel("Dietary Habits:"));
        JComboBox<String> dietBox = new JComboBox<>(new String[]{
            "Unhealthy", "Moderate", "Healthy", "Very Healthy"
        });
        panel.add(dietBox);

        // Suicidal Thoughts
        panel.add(new JLabel("Suicidal Thoughts:"));
        JCheckBox suicidalBox = new JCheckBox("Yes");
        panel.add(suicidalBox);

        // Study Hours
        panel.add(new JLabel("Study Hours:"));
        JTextField hoursField = new JTextField();
        panel.add(hoursField);

        // Financial Stress (1-5)
        panel.add(new JLabel("Financial Stress (1-5):"));
        JSpinner financeSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        panel.add(financeSpinner);

        // Family History
        panel.add(new JLabel("Family History of Depression:"));
        JCheckBox familyBox = new JCheckBox("Yes");
        panel.add(familyBox);

        int option = JOptionPane.showConfirmDialog(
            frame, panel, "Enter Your Information",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (option == JOptionPane.OK_OPTION) {
            try {
                Record record = new Record();
                record.setGender((String) genderBox.getSelectedItem());
                record.setAge(Integer.parseInt(ageField.getText()));
                record.setAcademicPressure((int) pressureSpinner.getValue());
                record.setStudySatisfaction((int) satisfactionSpinner.getValue());
                record.setSleepDuration((String) sleepBox.getSelectedItem());
                record.setDietaryHabits((String) dietBox.getSelectedItem());
                record.setSuicidalThoughts(suicidalBox.isSelected());
                record.setStudyHours(Integer.parseInt(hoursField.getText()));
                record.setFinancialStress((int) financeSpinner.getValue());
                record.setFamilyHistory(familyBox.isSelected());

                float score = forest.predictScore(record);
                List<String> factors = feedback.obtainFactors(record, forest);
                displayWellnessScore(score, factors);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame,
                    "Invalid input: " + ex.getMessage(),
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void displayWellnessScore(float score, List<String> factors) {
        StringBuilder message = new StringBuilder();
        message.append(String.format("Your Wellness Score: %.2f (scale 0–1)%n", score));
        message.append("Key contributing factors:\n");
        for (String factor : factors) {
            message.append("- ").append(factor).append("\n");
        }
        JOptionPane.showMessageDialog(
            frame,
            message.toString(),
            "Wellness Results",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserInterface ui = new UserInterface();
            ui.displayMenu();
        });
    }
}
