package juegogui;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Tipo;
import utils.Archivismo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class CharacterSelectionWindow extends JFrame {
    private String nickname;
    private List<Tipo> tiposGuerreros = Arrays.asList(
        Tipo.ACIDO,
        Tipo.AGUA,
        Tipo.AIRE,
        Tipo.ELECTRICIDAD,
        Tipo.ESPIRITUALIDAD,
        Tipo.FUEGO,
        Tipo.HIELO,
        Tipo.HIERRO,
        Tipo.MAGIA_BLANCA,
        Tipo.MAGIA_OSCURA
    );
    private Map<Tipo, JLabel> warriorLabels;
    private Set<Tipo> selectedWarriors;
    private JTextArea chatArea;
    private JTextField commandField;
    private Archivismo archivismo; // Corregir el nombre de la variable

    public CharacterSelectionWindow(String nickname) {
        this.archivismo = new Archivismo(); // Inicializar correctamente
        this.nickname = nickname;
        selectedWarriors = new HashSet<>();
        warriorLabels = new HashMap<>();

        setTitle("Selecciona Tus Guerreros");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 750);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Etiqueta superior
        JLabel topLabel = new JLabel("SELECCIONA TUS GUERREROS!");
        topLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(topLabel, BorderLayout.NORTH);

        // Panel de guerreros
        JPanel warriorsPanel = new JPanel(new GridLayout(2, 5, 10, 10));
        for (Tipo tipo : tiposGuerreros) {
            JLabel label = createWarriorLabel(tipo);
            warriorsPanel.add(label);
            warriorLabels.put(tipo, label);
        }
        mainPanel.add(warriorsPanel, BorderLayout.CENTER);

        // Panel de chat
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatArea = new JTextArea(5, 100);
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 12)); // Mejor legibilidad
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);

        commandField = new JTextField();
        chatPanel.add(commandField, BorderLayout.SOUTH);

        mainPanel.add(chatPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);

        // Mensaje de ayuda inicial
        chatArea.append("Ingresa 'help' para ayuda.\n");

        // Agregar action listener al campo de comando
        commandField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String comando = commandField.getText().trim();
                if (!comando.isEmpty()) {
                    processCommand(comando);
                    commandField.setText("");
                }
            }
        });

        setVisible(true);
    }

    private JLabel createWarriorLabel(Tipo tipo) {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalTextPosition(SwingConstants.BOTTOM);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setText(tipo.toString());

        // Cargar y redimensionar la imagen
        try {
            String imagePath = tipo.getImagen();
            ImageIcon icon = new ImageIcon(resizeImage(imagePath, 173, 230));
            label.setIcon(icon);
        } catch (Exception e) {
            label.setText(tipo.toString() + "\n[Imagen no encontrada]");
        }

        return label;
    }

    private void processCommand(String command) {
        // Obtener la fecha y hora actual
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = ahora.format(formateador);
    
        // Agregar el comando con fecha y hora al chatArea
        chatArea.append("> [" + timestamp + "] " + command + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength()); // Scrolling automático
    
        String[] parts = command.trim().split("\\s+");
        if (parts.length == 0) {
            chatArea.append("Comando no válido.\n");
            return;
        }
        String action = parts[0].toLowerCase();
    
        switch (action) {
            case "help":
                chatArea.append("Comandos disponibles:\n");
                chatArea.append("- select tipo : Selecciona un guerrero.\n");
                chatArea.append("- discard tipo : Deselecciona un guerrero.\n");
                chatArea.append("- confirm : Confirma la selección.\n");
                break;
            case "select":
                if (parts.length < 2) {
                    chatArea.append("Uso: select tipo\n");
                    break;
                }
                selectWarrior(parts[1]);
                break;
            case "discard":
                if (parts.length < 2) {
                    chatArea.append("Uso: discard tipo\n");
                    break;
                }
                discardWarrior(parts[1]);
                break;
            case "confirm":
                confirmSelection();
                break;
            default:
                chatArea.append("Comando no reconocido.\n");
        }
    }
    

    private void selectWarrior(String typeName) {
        Tipo tipo = buscarTipoPorNombre(typeName);
        if (tipo == null) {
            chatArea.append("Tipo de guerrero no valido.\n");
            return;
        }
        if (selectedWarriors.contains(tipo)) {
            chatArea.append("Ya has seleccionado este guerrero.\n");
            return;
        }
        if (selectedWarriors.size() >= 4) {
            chatArea.append("No puedes seleccionar mas de 4 guerreros.\n");
            return;
        }
        selectedWarriors.add(tipo);
        warriorLabels.get(tipo).setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
        chatArea.append("Has seleccionado: " + tipo.toString() + ".\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength()); // Scrolling automatico
    }

    private void discardWarrior(String typeName) {
        Tipo tipo = buscarTipoPorNombre(typeName);
        if (tipo == null) {
            chatArea.append("Tipo de guerrero no valido.\n");
            return;
        }
        if (!selectedWarriors.contains(tipo)) {
            chatArea.append("No has seleccionado este guerrero.\n");
            return;
        }
        selectedWarriors.remove(tipo);
        warriorLabels.get(tipo).setBorder(null);
        chatArea.append("Has deseleccionado: " + tipo.toString() + ".\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength()); // Scrolling automatico
    }

    private void confirmSelection() {
        if (selectedWarriors.size() < 4) {
            chatArea.append("Aun no se puede continuar. Selecciona 4 guerreros.\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength()); // Scrolling automatico
            return;
        }
        // Obtener el texto completo del JTextArea
        String textoCompleto = chatArea.getText();
		
		try {
			archivismo.createFile("SkibidiLogs", nickname + "Log.txt", textoCompleto);
		} catch (IOException ex) {
			Logger.getLogger(CharacterSelectionWindow.class.getName()).log(Level.SEVERE, null, ex);
		}

        List<Tipo> selectedTypes = new ArrayList<>(selectedWarriors);
        new StatsWindow(nickname, selectedTypes);
        dispose(); // Cerrar la ventana de seleccion de personajes
    }

    private Image resizeImage(String imagePath, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage();
            Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return resizedImg;
        } catch (Exception e) {
            return null;
        }
    }

    private Tipo buscarTipoPorNombre(String nombre) {
        for (Tipo tipo : tiposGuerreros) {
            if (tipo.toString().equalsIgnoreCase(nombre)) {
                return tipo;
            }
        }
        return null;
    }
}
