package juegogui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import utils.Archivismo;
import model.Jugador;
import model.Tipo;
import model.Guerrero;
import model.Arma;
import factory.GuerreroFactoryImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Ventana para iniciar sesión con un nickname existente.
 */
public class LoginWindow extends JFrame {
    private JTextField nicknameField;
    private JButton loginButton;
    private Archivismo archivoManager;

    public LoginWindow() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200); // Ajusta el tamaño para centrar mejor
        setLocationRelativeTo(null);

        archivoManager = new Archivismo();

        // Crear componentes
        JLabel label = new JLabel("Enter your nickname:");
        nicknameField = new JTextField(20);
        loginButton = new JButton("Login");

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
        panel.add(loginButton, gbc);

        getContentPane().add(panel);

        // Add action listener to the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickname = nicknameField.getText().trim();
                if (nickname.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Nickname cannot be empty.");
                    return;
                }

                // Check if the nickname exists
                if (!archivoManager.fileExistsInFolder("Players", nickname + ".txt")) {
                    JOptionPane.showMessageDialog(null, "The nickname is not registered.");
                } else {
                    try {
                        // Load player data from file
                        Jugador jugador = cargarJugadorDesdeArchivo(nickname);
                        if (jugador != null) {
                            // Proceed to the main game interface
                            new InterfazJuego(jugador);
                            dispose(); // Close the login window
                        } else {
                            JOptionPane.showMessageDialog(null, "Error loading player data.");
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error reading player data.");
                    }
                }
            }
        });
		
		nicknameField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginButton.doClick(); // Simular clic en el botón de inicio de sesión
			}
		});

        setVisible(true);
    }

    /**
     * Carga los datos del jugador desde el archivo de texto.
     * @param nickname El nickname del jugador.
     * @return Un objeto Jugador con los datos cargados.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    private Jugador cargarJugadorDesdeArchivo(String nickname) throws IOException {
        List<String> lineas = archivoManager.leerArchivo("Players", nickname + ".txt");
        if (lineas == null || lineas.isEmpty()) {
            return null;
        }

        String nombre = lineas.get(0).trim();
        Jugador jugador = new Jugador(nombre);

        // Procesar guerreros
        GuerreroFactoryImpl creador = new GuerreroFactoryImpl();
        int index = 1;

        // Definir los tipos de guerreros válidos
        List<String> tiposStrings = new ArrayList<>();
        for (Tipo t : Tipo.values()) {
            tiposStrings.add(t.name());
        }

        // Iterar a través de las líneas para identificar tipos y armas
        while (index < lineas.size()) {
            String linea = lineas.get(index).trim();

            // Verificar si hemos llegado a las estadísticas
            if (linea.startsWith("Wins:")) {
                break; // Salir del bucle de guerreros
            }

            // Verificar si la línea es un tipo de guerrero
            if (tiposStrings.contains(linea.toUpperCase())) {
                Tipo tipo = Tipo.valueOf(linea.toUpperCase());
                index++;

                // Armas del guerrero
                List<Arma> armas = new ArrayList<>();
                while (index < lineas.size()) {
                    linea = lineas.get(index).trim();
					
					if (linea.startsWith("Wins:")) {
						break; // Salir del bucle de guerreros
					}

                    // Si la línea está vacía o es otro tipo de guerrero, salir del bucle de armas
                    if (linea.isEmpty() || tiposStrings.contains(linea.toUpperCase())) {
                        break;
                    }

                    // Procesar línea de arma: NombreArma:daño1,daño2,...,daño10
                    String[] parts = linea.split(":");
                    if (parts.length != 2) {
                        JOptionPane.showMessageDialog(null, "Formato de arma inválido: " + linea);
                        return null;
                    }

                    String nombreArma = parts[0].trim();
                    String[] daniosStr = parts[1].split(",");
                    List<Integer> danios = new ArrayList<>();
                    try {
                        for (String d : daniosStr) {
                            danios.add(Integer.parseInt(d.trim()));
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Formato de daño inválido en arma: " + nombreArma);
                        return null;
                    }

                    // Crear el arma con los daños
                    Arma arma = new Arma(nombreArma, crearMapaDanios(danios));
                    armas.add(arma);
                    index++;
                }

                // Crear el guerrero con sus armas
                Guerrero guerrero = creador.crearGuerrero(tipo, jugador);
                guerrero.setArmas(armas);
                jugador.addGuerrero(guerrero);
            } else {
                // Si la línea no es un tipo válido y no es una línea de arma, podría ser una línea inválida
                JOptionPane.showMessageDialog(null, "Formato inválido en el archivo: " + linea);
                return null;
            }
        }

        // Procesar estadísticas
        while (index < lineas.size()) {
            String linea = lineas.get(index).trim();
            String lineaLower = linea.toLowerCase();

            if (lineaLower.startsWith("wins:")) {
                try {
                    jugador.setWins(Integer.parseInt(linea.split(":")[1].trim()));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Formato de 'Wins' inválido: " + linea);
                    return null;
                }
            } else if (lineaLower.startsWith("loses:") || lineaLower.startsWith("losses:")) {
                try {
                    jugador.setLoses(Integer.parseInt(linea.split(":")[1].trim()));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Formato de 'Loses/Losses' inválido: " + linea);
                    return null;
                }
            } else if (lineaLower.startsWith("attacks:")) {
                try {
                    jugador.setAttacks(Integer.parseInt(linea.split(":")[1].trim()));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Formato de 'Attacks' inválido: " + linea);
                    return null;
                }
            } else if (lineaLower.startsWith("success:")) {
                try {
                    jugador.setSuccess(Integer.parseInt(linea.split(":")[1].trim()));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Formato de 'Success' inválido: " + linea);
                    return null;
                }
            } else if (lineaLower.startsWith("failed:")) {
                try {
                    jugador.setFailed(Integer.parseInt(linea.split(":")[1].trim()));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Formato de 'Failed' inválido: " + linea);
                    return null;
                }
            } else if (lineaLower.startsWith("giveup:")) {
                try {
                    jugador.setGiveup(Integer.parseInt(linea.split(":")[1].trim()));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Formato de 'Giveup' inválido: " + linea);
                    return null;
                }
            } else {
                // Si hay líneas adicionales no reconocidas
                JOptionPane.showMessageDialog(null, "Línea de estadísticas no reconocida: " + linea);
                return null;
            }
            index++;
        }

        return jugador;
    }

    private Map<Tipo, Integer> crearMapaDanios(List<Integer> danios) {
        Map<Tipo, Integer> mapaDanios = new HashMap<>();
        List<Tipo> ordenTipos = Arrays.asList(
            Tipo.FUEGO,
            Tipo.AIRE,
            Tipo.AGUA,
            Tipo.MAGIA_BLANCA,
            Tipo.MAGIA_OSCURA,
            Tipo.ELECTRICIDAD,
            Tipo.HIELO,
            Tipo.ACIDO,
            Tipo.ESPIRITUALIDAD,
            Tipo.HIERRO
        );

        for (int i = 0; i < ordenTipos.size() && i < danios.size(); i++) {
            mapaDanios.put(ordenTipos.get(i), danios.get(i));
        }

        return mapaDanios;
    }
}
