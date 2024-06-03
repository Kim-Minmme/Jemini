package gui;

import db.DiaryEntryDao;
import db.UserDao;
import db.models.DiaryEntry;
import gemini.GeminiApiClient;
import gemini.StreamContentCallback;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainFrame extends JFrame {
    private String username;
    private JTextArea diaryTextArea;
    private JTextArea analysisTextArea;

    public MainFrame(String username) {
        this.username = username;
        setTitle("AI-Enhanced Personal Diary");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        getContentPane().add(mainPanel);

        // Left Panel for Writing Diary Entries
        JPanel diaryPanel = new JPanel(new BorderLayout());
        diaryPanel.setBorder(BorderFactory.createTitledBorder("Write Your Diary Entry"));
        diaryTextArea = new JTextArea();
        JScrollPane diaryScrollPane = new JScrollPane(diaryTextArea);
        diaryPanel.add(diaryScrollPane, BorderLayout.CENTER);

        // Right Panel for AI Analysis
        JPanel analysisPanel = new JPanel(new BorderLayout());
        analysisPanel.setBorder(BorderFactory.createTitledBorder("AI Analysis"));
        analysisTextArea = new JTextArea();
        analysisTextArea.setEditable(false);
        JScrollPane analysisScrollPane = new JScrollPane(analysisTextArea);
        analysisPanel.add(analysisScrollPane, BorderLayout.CENTER);

        // Use JSplitPane to split the space between diary and analysis panels
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, diaryPanel, analysisPanel);
        splitPane.setDividerLocation(400); // Set initial divider location
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save Entry");
        JButton analyzeButton = new JButton("Analyze Entry");
        buttonPanel.add(saveButton);
        buttonPanel.add(analyzeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners for buttons
        saveButton.addActionListener(e -> saveEntry());
        analyzeButton.addActionListener(e -> analyzeEntry());
    }

    private void saveEntry() {
        DiaryEntryDao diaryEntryDao = new DiaryEntryDao();
        try {
            DiaryEntry entry = new DiaryEntry(0, getUserId(), getCurrentDate(), diaryTextArea.getText(), "", "");
            diaryEntryDao.createEntry(entry);
            JOptionPane.showMessageDialog(this, "Entry saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to save entry: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void analyzeEntry() {
        analysisTextArea.setText(""); // Clear previous analysis
        GeminiApiClient.streamContent(diaryTextArea.getText(), new StreamContentCallback() {
		    @Override
		    public void onContentReceived(String content) {
		        analysisTextArea.append(content);
		    }

		    @Override
		    public void onError(Exception e) {
		        JOptionPane.showMessageDialog(MainFrame.this, "Failed to analyze entry: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		    }
		});
    }

    private int getUserId() {
        UserDao userDao = new UserDao();
        try {
            return userDao.getUserIdByUsername(username);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // or handle error appropriately
        }
    }

    private String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}