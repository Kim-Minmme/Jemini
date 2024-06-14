package gui;

import db.DiaryEntryDao;
import db.models.DiaryEntry;
import gemini.GeminiApiClient;
import gemini.StreamContentCallback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class DiaryFrame extends JFrame {
    private int userId;
    private JTextField titleField;
    private JTextArea contentArea;
    private JList<DiaryEntry> entriesList;
    private DefaultListModel<DiaryEntry> listModel;
    private JTextArea aiAnalysisArea;

    public DiaryFrame(int userId) {
        this.userId = userId;

        setTitle("Diary");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        entriesList = new JList<>(listModel);
        entriesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        entriesList.addListSelectionListener(e -> loadSelectedEntry());

        JScrollPane listScrollPane = new JScrollPane(entriesList);
        listScrollPane.setPreferredSize(new Dimension(200, 600));

        JPanel entryPanel = new JPanel();
        entryPanel.setLayout(new BorderLayout());

        JPanel entryHeaderPanel = new JPanel();
        entryHeaderPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField();

        entryHeaderPanel.add(titleLabel, BorderLayout.WEST);
        entryHeaderPanel.add(titleField, BorderLayout.CENTER);

        contentArea = new JTextArea();
        JScrollPane contentScrollPane = new JScrollPane(contentArea);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new SaveButtonListener());

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new DeleteButtonListener());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);

        entryPanel.add(entryHeaderPanel, BorderLayout.NORTH);
        entryPanel.add(contentScrollPane, BorderLayout.CENTER);
        entryPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(listScrollPane, BorderLayout.WEST);
        panel.add(entryPanel, BorderLayout.CENTER);

        JPanel aiPanel = new JPanel();
        aiPanel.setLayout(new BorderLayout());

        aiAnalysisArea = new JTextArea();
        aiAnalysisArea.setEditable(false);
        aiAnalysisArea.setLineWrap(true);
        aiAnalysisArea.setWrapStyleWord(true);
        JScrollPane aiScrollPane = new JScrollPane(aiAnalysisArea);

        aiScrollPane.setPreferredSize(new Dimension(300, 0));

        JButton analyzeButton = new JButton("Analyze with AI");
        analyzeButton.addActionListener(new AnalyzeButtonListener());

        aiPanel.add(new JLabel("AI Analysis"), BorderLayout.NORTH);
        aiPanel.add(aiScrollPane, BorderLayout.CENTER);
        aiPanel.add(analyzeButton, BorderLayout.SOUTH);

        panel.add(aiPanel, BorderLayout.EAST);

        add(panel);

        loadDiaryEntries();
    }

    private void loadDiaryEntries() {
    	aiAnalysisArea.setText("");
    	
        DiaryEntryDao diaryEntryDao = new DiaryEntryDao();
        try {
            List<DiaryEntry> entries = diaryEntryDao.listDiaryEntriesByUser(userId);
            listModel.clear();
            for (DiaryEntry entry : entries) {
                listModel.addElement(entry);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading diary entries: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSelectedEntry() {
        DiaryEntry selectedEntry = entriesList.getSelectedValue();
        if (selectedEntry != null) {
            titleField.setText(selectedEntry.getTitle());
            contentArea.setText(selectedEntry.getContent());
        } else {
            titleField.setText("");
            contentArea.setText("");
        }
    }

    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = titleField.getText();
            String content = contentArea.getText();

            DiaryEntryDao diaryEntryDao = new DiaryEntryDao();
            DiaryEntry selectedEntry = entriesList.getSelectedValue();

            if (selectedEntry != null) {
                selectedEntry.setTitle(title);
                selectedEntry.setContent(content);
                try {
                    diaryEntryDao.updateDiaryEntry(selectedEntry);
                    loadDiaryEntries();
                    JOptionPane.showMessageDialog(DiaryFrame.this, "Entry updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(DiaryFrame.this, "Error updating entry: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                DiaryEntry newEntry = new DiaryEntry(0, title, java.time.LocalDate.now().toString(), content, userId);
                try {
                    diaryEntryDao.createDiaryEntry(newEntry);
                    loadDiaryEntries();
                    JOptionPane.showMessageDialog(DiaryFrame.this, "Entry created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(DiaryFrame.this, "Error creating entry: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            DiaryEntry selectedEntry = entriesList.getSelectedValue();
            if (selectedEntry != null) {
                int confirmation = JOptionPane.showConfirmDialog(DiaryFrame.this, "Are you sure you want to delete this entry?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    try {
                        DiaryEntryDao diaryEntryDao = new DiaryEntryDao();
                        diaryEntryDao.deleteDiaryEntry(selectedEntry.getId());
                        loadDiaryEntries();
                        titleField.setText("");
                        contentArea.setText("");
                        JOptionPane.showMessageDialog(DiaryFrame.this, "Entry deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(DiaryFrame.this, "Error deleting entry: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(DiaryFrame.this, "No entry selected to delete", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class AnalyzeButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String content = contentArea.getText();
            if (content.isEmpty()) {
                JOptionPane.showMessageDialog(DiaryFrame.this, "Content is empty. Please write something to analyze.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            aiAnalysisArea.setText("");
            GeminiApiClient.streamContent(content, new StreamContentCallback() {
                @Override
                public void onContentReceived(String text) {
                    SwingUtilities.invokeLater(() -> aiAnalysisArea.setText(aiAnalysisArea.getText() + text));
                }

                @Override
                public void onError(Exception e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(DiaryFrame.this, "Error during AI analysis: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
                }
            });
        }
    }
}