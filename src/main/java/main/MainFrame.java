package main;

import db.DatabaseHelper;
import db.UserDao;
import gui.DiaryFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MainFrame extends JFrame {

    private int userId;
    private UserDao userDao;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public MainFrame() {
        userDao = new UserDao();

        setTitle("AI-Powered Diary");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new CardLayout());

        JPanel loginPanel = createLoginPanel(panel);
        JPanel registerPanel = createRegisterPanel(panel);

        panel.add(loginPanel, "Login");
        panel.add(registerPanel, "Register");

        add(panel);

        CardLayout cl = (CardLayout) (panel.getLayout());
        cl.show(panel, "Login");
    }

    private JPanel createLoginPanel(JPanel parentPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginButtonListener());

        JButton switchToRegisterButton = new JButton("Register");
        switchToRegisterButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) (parentPanel.getLayout());
            cl.show(parentPanel, "Register");
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(switchToRegisterButton);

        return panel;
    }

    private JPanel createRegisterPanel(JPanel parentPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(MainFrame.this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                if (userDao.register(username, password)) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    CardLayout cl = (CardLayout) (parentPanel.getLayout());
                    cl.show(parentPanel, "Login");
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this, "Registration failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(MainFrame.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton switchToLoginButton = new JButton("Login");
        switchToLoginButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) (parentPanel.getLayout());
            cl.show(parentPanel, "Login");
        });

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmPasswordField);
        panel.add(registerButton);
        panel.add(switchToLoginButton);

        return panel;
    }

    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                if (userDao.authenticate(username, password)) {
                    userId = userDao.getUserIdByUsername(username);
                    JOptionPane.showMessageDialog(MainFrame.this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new DiaryFrame(userId).setVisible(true);
                    MainFrame.this.dispose();
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(MainFrame.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
    	try {
			DatabaseHelper.createTables();
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}