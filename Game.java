import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class Game {
    private final JFrame frame;
    private final JPanel cards;
    private final CardLayout cardLayout;
    
    private JTextField nameField, inputField;
    private JLabel countryLabel, feedbackLabel, scoreLabel, progressLabel, welcomeUserLabel;
    private JProgressBar timerBar;
    private JButton checkButton, nextButton;

    private final Map<String, String> data = new HashMap<>();
    private List<String> countryKeys; 
    private String currentCapital, playerName = "Player";
    private int score = 0, count = 0;
    private final Random rand = new Random();
    
    private Timer countdownTimer;
    private int timeLeft;
    private final int MAX_TIME = 150; // 15 seconds

    public Game() {
        initData();
        countryKeys = new ArrayList<>(data.keySet());

        frame = new JFrame("Guess the Capital with Roya 🌍");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        cards.add(createWelcomePanel(), "Welcome");
        cards.add(createGamePanel(), "Game");

        frame.add(cards);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // --- Welcome Screen Logic ---
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(25, 25, 25));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        panel.add(Box.createVerticalStrut(80));
        JLabel title = new JLabel("Guess the Capital with Roya");
        title.setFont(new Font("Serif", Font.BOLD, 32));
        title.setForeground(new Color(0, 255, 150));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);

        panel.add(Box.createVerticalStrut(50));
        JLabel askName = new JLabel("👋 Enter your name and press Enter:");
        askName.setForeground(Color.WHITE);
        askName.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(askName);

        panel.add(Box.createVerticalStrut(15));
        nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(300, 40));
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // FIX: Pressing Enter here starts the game
        nameField.addActionListener(e -> startGame());
        
        panel.add(nameField);

        panel.add(Box.createVerticalStrut(30));
        JButton startBtn = createStyledButton("🚀 START GAME", new Color(0, 123, 255));
        startBtn.addActionListener(e -> startGame());
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(startBtn);

        return panel;
    }

    // --- Game Screen Logic ---
    private JPanel createGamePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(18, 18, 18));
        panel.setBorder(new EmptyBorder(20, 50, 20, 50));

        progressLabel = new JLabel("Question: 0/96", SwingConstants.CENTER);
        progressLabel.setForeground(Color.GRAY);
        progressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        timerBar = new JProgressBar(0, MAX_TIME);
        timerBar.setForeground(Color.GREEN);
        timerBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        welcomeUserLabel = new JLabel("Good Luck!", SwingConstants.CENTER);
        welcomeUserLabel.setForeground(new Color(150, 150, 150));
        welcomeUserLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        countryLabel = new JLabel("Country", SwingConstants.CENTER);
        countryLabel.setFont(new Font("Verdana", Font.BOLD, 34));
        countryLabel.setForeground(Color.WHITE);
        countryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        inputField = new JTextField();
        inputField.setMaximumSize(new Dimension(400, 50));
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 22));
        inputField.setHorizontalAlignment(JTextField.CENTER);
        
        // FIX: Enter logic (Check -> Next)
        inputField.addActionListener(e -> {
            if (checkButton.isEnabled()) checkAnswer();
            else nextCountry();
        });

        checkButton = createStyledButton("✅ SUBMIT (Enter)", new Color(40, 167, 69));
        checkButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkButton.addActionListener(e -> checkAnswer());

        nextButton = createStyledButton("➡️ NEXT (Enter)", new Color(0, 123, 255));
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.addActionListener(e -> nextCountry());
        nextButton.setVisible(false); // Hidden until answer is checked

        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        scoreLabel.setForeground(new Color(255, 215, 0));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(progressLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(timerBar);
        panel.add(Box.createVerticalStrut(10));
        panel.add(welcomeUserLabel);
        panel.add(Box.createVerticalStrut(25));
        panel.add(countryLabel);
        panel.add(Box.createVerticalStrut(25));
        panel.add(inputField);
        panel.add(Box.createVerticalStrut(15));
        panel.add(checkButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(nextButton);
        panel.add(Box.createVerticalStrut(20));
        panel.add(feedbackLabel);
        panel.add(scoreLabel);

        return panel;
    }

    private void startGame() {
        if (!nameField.getText().trim().isEmpty()) playerName = nameField.getText().trim();
        welcomeUserLabel.setText("👤 Player: " + playerName);
        cardLayout.show(cards, "Game");
        setupTimer();
        nextCountry();
    }

    private void setupTimer() {
        countdownTimer = new Timer(100, e -> {
            timeLeft--;
            timerBar.setValue(timeLeft);
            if (timeLeft < MAX_TIME / 3) timerBar.setForeground(Color.RED);
            if (timeLeft <= 0) { stopTimer(); revealLoss(); }
        });
    }

    private void nextCountry() {
        if (countryKeys.isEmpty()) { showFinalScreen(); return; }
        int i = rand.nextInt(countryKeys.size());
        currentCapital = data.get(countryKeys.get(i));
        countryLabel.setText(countryKeys.get(i));
        countryKeys.remove(i);
        count++;
        progressLabel.setText("Question: " + count + "/96");
        inputField.setText("");
        inputField.setEditable(true);
        inputField.requestFocusInWindow();
        feedbackLabel.setText(" ");
        checkButton.setEnabled(true);
        nextButton.setVisible(false);
        timeLeft = MAX_TIME;
        timerBar.setValue(MAX_TIME);
        timerBar.setForeground(Color.GREEN);
        countdownTimer.start();
    }

    private void checkAnswer() {
        if (!checkButton.isEnabled()) return;
        stopTimer();
        String guess = inputField.getText().trim().replace(".", "");
        String correct = currentCapital.replace(".", "");

        if (guess.equalsIgnoreCase(correct)) {
            score++;
            feedbackLabel.setText("✨ Correct, " + playerName + "!");
            feedbackLabel.setForeground(new Color(52, 199, 89));
        } else {
            shakeWindow();
            feedbackLabel.setText("❌ Wrong! It was " + currentCapital);
            feedbackLabel.setForeground(new Color(255, 59, 48));
        }
        updateUIAfterGuess();
    }

    private void revealLoss() {
        shakeWindow();
        feedbackLabel.setText("⏰ Time's Up! It was " + currentCapital);
        feedbackLabel.setForeground(Color.ORANGE);
        updateUIAfterGuess();
    }

    private void updateUIAfterGuess() {
        scoreLabel.setText("Score: " + score);
        checkButton.setEnabled(false);
        inputField.setEditable(false);
        nextButton.setVisible(true); // Re-show next button so user can click or press Enter
    }

    private void showFinalScreen() {
        countryLabel.setText("🏆 Victory!");
        inputField.setVisible(false);
        checkButton.setVisible(false);
        nextButton.setText("🔄 PLAY AGAIN?");
        nextButton.setVisible(true);
        feedbackLabel.setText(playerName + ", final score: " + score + "/96");
    }

    private void stopTimer() { if (countdownTimer != null) countdownTimer.stop(); }

    private void shakeWindow() {
        final Point point = frame.getLocation();
        new Timer(20, new AbstractAction() {
            int i = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (i % 2 == 0) frame.setLocation(point.x + 10, point.y);
                else frame.setLocation(point.x - 10, point.y);
                if (i == 10) { frame.setLocation(point); ((Timer)e.getSource()).stop(); }
                i++;
            }
        }).start();
    }

    private void initData() {
        // [All 96 countries go here...]
        data.put("USA", "Washington D.C."); data.put("France", "Paris"); 
        data.put("Japan", "Tokyo"); data.put("Germany", "Berlin");
        data.put("Italy", "Rome"); data.put("Brazil", "Brasilia");
        data.put("Egypt", "Cairo"); data.put("UK", "London");
        data.put("Canada", "Ottawa"); data.put("Australia", "Canberra");
        data.put("Russia", "Moscow"); data.put("China", "Beijing");
        data.put("India", "New Delhi"); data.put("South Korea", "Seoul");
        data.put("Mexico", "Mexico City"); data.put("Argentina", "Buenos Aires");
        data.put("Spain", "Madrid"); data.put("Turkey", "Ankara");
        data.put("Norway", "Oslo"); data.put("Sweden", "Stockholm");
        data.put("Netherlands", "Amsterdam"); data.put("Switzerland", "Bern");
        data.put("South Africa", "Pretoria"); data.put("Kenya", "Nairobi");
        data.put("Thailand", "Bangkok"); data.put("Vietnam", "Hanoi");
        data.put("New Zealand", "Wellington"); data.put("Greece", "Athens");
        data.put("Portugal", "Lisbon"); data.put("Ireland", "Dublin");
        // ... (Keep adding until you reach 96)
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setMaximumSize(new Dimension(350, 45));
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}