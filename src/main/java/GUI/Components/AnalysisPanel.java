package gui.components;

import javax.swing.*;
import java.awt.*;

public class AnalysisPanel extends JPanel {
    private JTextArea analysisTextArea;

    public AnalysisPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("AI Analysis"));
        analysisTextArea = new JTextArea();
        analysisTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(analysisTextArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setAnalysisText(String text) {
        analysisTextArea.setText(text);
    }
}