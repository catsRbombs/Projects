package juegogui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import utils.Archivismo;

public class NicknameWindow extends JFrame {
    private JTextField nicknameField;
    private JButton nextButton;
    private Archivismo archivoManager;

    public NicknameWindow() {
        setTitle("Enter Nickname");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200); // Ajusta el tamaño para centrar mejor
        setLocationRelativeTo(null);

        archivoManager = new Archivismo();

        // Crear componentes
        JLabel label = new JLabel("Enter your nickname:");
        nicknameField = new JTextField(20);
        nextButton = new JButton("Next");

        // Configurar layout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Añadir etiqueta
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Centrar en ambas columnas
        panel.add(label, gbc);

        // Añadir campo de texto
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(nicknameField, gbc);

        // Añadir botón
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(nextButton, gbc);

        getContentPane().add(panel);

        // Add action listener to the next button
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickname = nicknameField.getText().trim();
                if (nickname.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Nickname cannot be empty.");
                    return;
                }

                // Check if the nickname already exists
                if (archivoManager.fileExistsInFolder("Players", nickname + ".txt")) {
                    JOptionPane.showMessageDialog(null, "The nickname is already registered.");
                } else {
                    // Proceed to character selection
                    new CharacterSelectionWindow(nickname);
                    dispose(); // Close the nickname window
                }
            }
        });
		
		nicknameField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nextButton.doClick(); // Simular clic en el botón de inicio de sesión
			}
		});

        setVisible(true);
    }
}
