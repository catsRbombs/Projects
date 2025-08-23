package juegogui;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import factory.GuerreroFactoryImpl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import model.Arma;
import model.Guerrero;
import model.Jugador;
import model.Tipo;
import utils.Archivismo;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ventana que muestra las estadísticas de los guerreros seleccionados.
 */
public class StatsWindow extends JFrame {
    private GuerreroFactoryImpl creador;
    private String nickname;
    private List<Tipo> selectedWarriorTypes;
    private List<Guerrero> guerreros;
    private Archivismo archivoManager;
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
    private Jugador jugador;

    /**
     * Constructor de la ventana de estadísticas.
     * @param nickname El nickname del jugador.
     * @param selectedWarriorTypes Lista de tipos de guerreros seleccionados.
     */
    public StatsWindow(String nickname, List<Tipo> selectedWarriorTypes) {
        this.creador = new GuerreroFactoryImpl();
        this.nickname = nickname;
        this.selectedWarriorTypes = selectedWarriorTypes;
        guerreros = new ArrayList<>();
        archivoManager = new Archivismo();
        jugador = new Jugador(nickname); // Instanciar el jugador

        setTitle("Estadísticas de tus Guerreros");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cambiado para no cerrar la aplicación
        setSize(1765, 915);
        setLocationRelativeTo(null);

        // Panel principal con BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Etiqueta superior
        JLabel topLabel = new JLabel("ESTADÍSTICAS DE TUS GUERREROS");
        topLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(topLabel, BorderLayout.NORTH);

        // Panel central que contiene los guerreros
        JPanel warriorsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        warriorsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
        for (Tipo tipo : selectedWarriorTypes) {
            Guerrero guerrero = createGuerrero(tipo);
            guerreros.add(guerrero);
            JPanel warriorPanel = createWarriorStatsPanel(guerrero);
            warriorsPanel.add(warriorPanel);
        }

        mainPanel.add(warriorsPanel, BorderLayout.CENTER);

        // Botón de confirmación
        JButton confirmButton = new JButton("Confirmar");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 16));
        confirmButton.setPreferredSize(new Dimension(150, 50));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Acción al presionar el botón de confirmar
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Guardar los datos del jugador en un archivo
                    savePlayerData();
                } catch (IOException ex) {
                    Logger.getLogger(StatsWindow.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, "Error al guardar los datos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(null, "Datos guardados exitosamente.");
                // Abrir la ventana principal del juego
                SwingUtilities.invokeLater(() -> new InterfazJuego(jugador));
                // Cerrar la ventana de estadísticas
                dispose();
            }
        });

        getContentPane().add(mainPanel);
        setVisible(true);
    }

    /**
     * Crea un guerrero con el tipo especificado.
     * @param tipo El tipo del guerrero.
     * @return Un objeto Guerrero.
     */
    private Guerrero createGuerrero(Tipo tipo) {
        Guerrero guerrero = creador.crearGuerrero(tipo, jugador);
        // Las armas se asignan automáticamente en el constructor de Guerrero
        for (Arma arma : guerrero.getArmas()) {
            System.out.print(arma.getNombre());
        }
        jugador.addGuerrero(guerrero);
        return guerrero;
    }
   
    /**
     * Crea un panel que muestra las estadísticas de un guerrero.
     * @param guerrero El guerrero cuyas estadísticas se mostrarán.
     * @return Un JPanel con la información del guerrero.
     */
    private JPanel createWarriorStatsPanel(Guerrero guerrero) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // Etiqueta con la imagen y el tipo del guerrero
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        imageLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        imageLabel.setText(guerrero.getTipo().toString());

        // Cargar y redimensionar la imagen del guerrero
        try {
            String imagePath = guerrero.getImagen();
            ImageIcon icon = new ImageIcon(resizeImage(imagePath, 415, 553));
            imageLabel.setIcon(icon);
        } catch (Exception e) {
            imageLabel.setText("<html>" + guerrero.getTipo().toString() + "<br>[Imagen no encontrada]</html>");
        }

        panel.add(imageLabel, BorderLayout.NORTH);

        // Panel para listar las armas y sus estadísticas
        JPanel armasPanel = new JPanel();
        armasPanel.setLayout(new BoxLayout(armasPanel, BoxLayout.Y_AXIS));
        armasPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Arma arma : guerrero.getArmas()) {
            System.out.println("Arma encontrada");
            JPanel armaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            armaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Nombre del arma
            JLabel armaNameLabel = new JLabel(arma.getNombre() + ": ");
            armaNameLabel.setFont(new Font("Arial", Font.BOLD, 13));
            armaPanel.add(armaNameLabel);

            // Estadísticas de daño
            Map<Tipo, Integer> danios = arma.getDanioPorTipo();
            List<Integer> valoresDanio = new ArrayList<>(danios.values());

            // Convertir las estadísticas a una cadena separada por comas
            StringBuilder danioStr = new StringBuilder();
            for (int i = 0; i < valoresDanio.size(); i++) {
                danioStr.append(valoresDanio.get(i));
                if (i < valoresDanio.size() - 1) {
                    danioStr.append(", ");
                }
            }

            JLabel danioLabel = new JLabel("[" + danioStr.toString() + "]");
            armaPanel.add(danioLabel);

            armasPanel.add(armaPanel);
        }

        JScrollPane armasScrollPane = new JScrollPane(armasPanel);
        armasScrollPane.setPreferredSize(new Dimension(300, 200));
        panel.add(armasScrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Guarda los datos del jugador y sus guerreros en un archivo de texto.
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    private void savePlayerData() throws IOException {
        StringBuilder content = new StringBuilder();
        content.append(nickname).append("\n");
        for (Guerrero guerrero : guerreros) {
            content.append(guerrero.getTipo().toString()).append("\n");
            for (Arma arma : guerrero.getArmas()) {
                content.append(arma.getNombre()).append(":");
                Map<Tipo, Integer> danios = arma.getDanioPorTipo();
                List<Integer> valoresDanio = new ArrayList<>(danios.values());
                for (int i = 0; i < valoresDanio.size(); i++) {
                    content.append(valoresDanio.get(i));
                    if (i < valoresDanio.size() - 1) {
                        content.append(",");
                    }
                }
                content.append("\n");
            }
        }
        // Guardar el contenido en un archivo usando la función createFile
        archivoManager.createFile("Players", nickname + ".txt", content.toString() + "Wins: 0\nLoses: 0\nAttacks: 0\nSuccess: 0\nFailed: 0\nGiveup: 0");
    }

    /**
     * Redimensiona una imagen.
     * @param imagePath La ruta de la imagen.
     * @param width El ancho deseado.
     * @param height El alto deseado.
     * @return Un objeto Image redimensionado.
     */
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
}
