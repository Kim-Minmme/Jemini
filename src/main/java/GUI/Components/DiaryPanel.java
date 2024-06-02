package GUI.Components;

import javax.swing.*;
import java.awt.*;

public class DiaryPanel extends JPanel {
    private JTextArea diaryTextArea;

    public DiaryPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Write Your Diary Entry"));
        diaryTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(diaryTextArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    public String getDiaryText() {
        return diaryTextArea.getText();
    }

    public void setDiaryText(String text) {
        diaryTextArea.setText(text);
    }
}