package juegogui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main window with options to Register or Login.
 */
public class MainWindow extends JFrame {
    private JButton registerButton;
    private JButton loginButton;

    public MainWindow() {
        setTitle("Welcome to the Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        // Create buttons
        registerButton = new JButton("Register");
        loginButton = new JButton("Login");

        // Set up layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Use GridBagLayout for centering
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 10, 10, 10); // Add some spacing between buttons
        gbc.anchor = GridBagConstraints.CENTER;

        panel.add(registerButton, gbc);
        panel.add(loginButton, gbc);

        getContentPane().add(panel);

        // Add action listeners
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the nickname input window for registration
                new NicknameWindow();
                dispose(); // Close the main window
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the login window
                LoginWindow login = new LoginWindow(); // Asegúrate de que esta clase existe
				login.setVisible(true);
                dispose(); // Close the main window
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        // Run the main window in the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new MainWindow());
    }
}
