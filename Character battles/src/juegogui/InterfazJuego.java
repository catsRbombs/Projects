package juegogui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import network.Mensaje;
import network.ClienteJuego;
import model.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.TitledBorder;
import utils.Archivismo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Interfaz principal del juego.
 */
public class InterfazJuego extends JFrame {

    // Componentes de la interfaz
    private JTextArea rankingTextArea;
	private JLabel turnoLabel;
    private JPanel infoPanel1;
    private JPanel infoPanel2;
    private JTextArea centralTextArea1;
    private JTextArea centralTextArea2;
    private JLabel yourCardsLabel;
    private JLabel[] cardLabels;
    private JTextArea warriorInfoTextArea; // Nuevo JTextArea para información del guerrero
    private JTextField commandTextField;
    private JTextArea chatTextArea;
    private JButton sendButton;
    private ClienteJuego clienteJuego;
    private Jugador jugador;
    private Guerrero guerreroSeleccionado;
    private Archivismo archivismo;
    private Boolean waiting_draw = false;

    // Estado de combate
    private boolean enCombate = false;
	private boolean enTurno = false;
	private String oponenteActual;



    public InterfazJuego(Jugador jugador) {
        this.jugador = jugador;
        this.enTurno = false;
        this.archivismo = new Archivismo(); // Inicializar Archivismo
        initComponents();
        conectarAlServidor();

        // Agregar WindowListener para guardar datos al cerrar la ventana
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
				System.out.println("Se ha cerrado la ventana. Guardando datos del jugador...");
				System.out.println("Datos del jugador guardados exitosamente.");
				clienteJuego.enviarMensaje(new Mensaje("fin", "fin", jugador.getNickname()));
				guardarLog();
            }
        });
    }

    private void initComponents() {
        // Configuración básica del JFrame
        setTitle("SkibidiMon - " + jugador.getNickname());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1800, 1000); // Aumentado para más espacio
        setLocationRelativeTo(null);

        // Establecer layout del JFrame
        getContentPane().setLayout(new BorderLayout());

        // Panel Izquierdo (West)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Ranking
        JLabel rankingLabel = new JLabel("RANKING");
        rankingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rankingLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Aumentar tamaño de fuente
        leftPanel.add(rankingLabel);

        rankingTextArea = new JTextArea(15, 20); // Aumentado número de filas y ancho
        rankingTextArea.setEditable(false);
        rankingTextArea.setFont(new Font("Arial", Font.PLAIN, 14)); // Mejor legibilidad
        rankingTextArea.setBorder(new EmptyBorder(5, 5, 5, 5)); // Añadir margen interno
        JScrollPane rankingScrollPane = new JScrollPane(rankingTextArea);
        rankingScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(rankingScrollPane);

        // Ventana de información del rival o jugador consultado
        infoPanel1 = new JPanel();
        infoPanel1.setLayout(new GridLayout(7, 1, 10, 10)); // Aumentar hgap y vgap
        infoPanel1.setBorder(BorderFactory.createTitledBorder("Jugador Consultado"));
        infoPanel1.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(infoPanel1);

        // Ventana de información del jugador actual
        infoPanel2 = new JPanel();
        infoPanel2.setLayout(new GridLayout(7, 1, 10, 10)); // Aumentar hgap y vgap
        infoPanel2.setBorder(BorderFactory.createTitledBorder("Tus Estadísticas"));
        infoPanel2.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(infoPanel2);

        getContentPane().add(leftPanel, BorderLayout.WEST);

        // Panel Central (Center)
        JPanel centerPanel = new JPanel();
        // Cambiar GridLayout a BoxLayout para mejor control de tamaños
        centerPanel.setLayout(new GridLayout(2, 1, 10, 10)); // Usar GridLayout para dividir en dos filas con espacio
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        centralTextArea1 = new JTextArea();
        centralTextArea1.setEditable(false);
        centralTextArea1.setFont(new Font("Arial", Font.PLAIN, 14)); // Mejor legibilidad
        centralTextArea1.setLineWrap(true);
        centralTextArea1.setWrapStyleWord(true);
        centralTextArea1.setBorder(new EmptyBorder(10, 10, 10, 10)); // Añadir margen interno
        JScrollPane centralScrollPane1 = new JScrollPane(centralTextArea1);
        // Eliminar setMaximumSize para permitir expansión
        centralScrollPane1.setPreferredSize(new Dimension(800, 400)); // Ajustar tamaño inicial
        centerPanel.add(centralScrollPane1);

        centralTextArea2 = new JTextArea();
        centralTextArea2.setEditable(false);
        centralTextArea2.setFont(new Font("Arial", Font.PLAIN, 14)); // Mejor legibilidad
        centralTextArea2.setLineWrap(true);
        centralTextArea2.setWrapStyleWord(true);
        centralTextArea2.setBorder(new EmptyBorder(10, 10, 10, 10)); // Añadir margen interno
        JScrollPane centralScrollPane2 = new JScrollPane(centralTextArea2);
        centralScrollPane2.setPreferredSize(new Dimension(800, 400)); // Ajustar tamaño inicial
        centerPanel.add(centralScrollPane2);

        getContentPane().add(centerPanel, BorderLayout.CENTER);

        // Panel Derecho (East)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout(10, 10)); // Usar BorderLayout para mayor flexibilidad
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        rightPanel.setPreferredSize(new Dimension(900, getHeight())); // Aumentar ancho a 900

        // Título "TUS GUERREROS"
        yourCardsLabel = new JLabel("TUS GUERREROS");
        yourCardsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        yourCardsLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Aumentar tamaño de fuente
        rightPanel.add(yourCardsLabel, BorderLayout.NORTH);

        // Panel de etiquetas de cartas
        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new GridLayout(1, 4, 20, 40)); // Aumentado hgap y vgap a 20 y 40
        cardsPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Añadir margen interno

        // Crear etiquetas de cartas
        cardLabels = new JLabel[jugador.getGuerreros().size()];
        int index = 0;
        for (Guerrero guerrero : jugador.getGuerreros()) {
            JLabel label = new JLabel();
            Image img = resizeImage(guerrero.getImagen(), 200, 270); // Aumentar tamaño de imagen
            if (img != null) {
                label.setIcon(new ImageIcon(img));
            } else {
                label.setText(guerrero.getTipo().toString()); // Mostrar texto si la imagen no carga
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
            }
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalTextPosition(SwingConstants.BOTTOM);
            label.setHorizontalTextPosition(SwingConstants.CENTER);
            label.setText(guerrero.getTipo().toString());
            label.setName(guerrero.getTipo().toString());
            label.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cambiar cursor a mano para indicar que es clickeable
            label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Añadir margen interno

            cardLabels[index++] = label;
            cardsPanel.add(label);
        }

        rightPanel.add(cardsPanel, BorderLayout.CENTER);

        // Panel para información del guerrero seleccionado (usando JTextArea)
        JPanel warriorInfoPanel = new JPanel();
        warriorInfoPanel.setLayout(new BorderLayout());
        warriorInfoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), "Información del Guerrero Seleccionado", 
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16), Color.BLACK));
        // Permitir que warriorInfoPanel ocupe todo el espacio restante
        warriorInfoPanel.setPreferredSize(new Dimension(900, 250));

        warriorInfoTextArea = new JTextArea();
        warriorInfoTextArea.setEditable(false);
        warriorInfoTextArea.setLineWrap(true);
        warriorInfoTextArea.setWrapStyleWord(true);
        warriorInfoTextArea.setFont(new Font("Arial", Font.PLAIN, 11)); // Reducir tamaño de fuente a 12
        warriorInfoTextArea.setText("Selecciona un guerrero para ver su información.");
        JScrollPane warriorInfoScrollPane = new JScrollPane(warriorInfoTextArea);
        warriorInfoScrollPane.setBorder(new EmptyBorder(10, 10, 10, 10)); // Añadir margen interno
        warriorInfoPanel.add(warriorInfoScrollPane, BorderLayout.CENTER);

        rightPanel.add(warriorInfoPanel, BorderLayout.SOUTH);

        getContentPane().add(rightPanel, BorderLayout.EAST);

        // Panel Inferior (South) - Terminal y Chat
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout(10, 10)); // Añadir espacio entre componentes
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Área de visualización del chat (Console)
        chatTextArea = new JTextArea(10, 50); // Reducido el número de filas para hacerlo menos alto
        chatTextArea.setEditable(false);
        chatTextArea.setLineWrap(true);
        chatTextArea.setWrapStyleWord(true);
        chatTextArea.setFont(new Font("Arial", Font.PLAIN, 15)); // Reducir tamaño de fuente a 14
        chatTextArea.setBorder(new EmptyBorder(10, 10, 10, 10)); // Añadir margen interno
        JScrollPane chatScrollPane = new JScrollPane(chatTextArea);
        chatScrollPane.setPreferredSize(new Dimension(1200, 250)); // Aumentar tamaño para mayor visibilidad
        bottomPanel.add(chatScrollPane, BorderLayout.CENTER);

        // Panel para el campo de entrada y botón de enviar
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout(10, 10));

        commandTextField = new JTextField();
        sendButton = new JButton("Enviar");
        sendButton.setFont(new Font("Arial", Font.BOLD, 17)); // Mejor apariencia

        inputPanel.add(commandTextField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        bottomPanel.add(inputPanel, BorderLayout.SOUTH);

        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        // Añadir evento al botón de enviar
        addSendButtonListener();

        // Actualizar tus estadísticas
        actualizarMisEstadisticas();

        // Añadir etiqueta para mostrar el turno
        turnoLabel = new JLabel("Turno: Esperando...");
        turnoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        turnoLabel.setForeground(Color.BLACK);
        turnoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(turnoLabel, BorderLayout.NORTH);

        // Hacer visible la interfaz
        setVisible(true);
    }
	
	private void guardarLog() {
		String nickname = jugador.getNickname().replaceAll("[<>:\"/\\\\|?*]", "_");
		String directory = "SkibidiLogs";
		String fileName = nickname + "Log.txt";
		String message = chatTextArea.getText();

		// Verificar si el directorio existe, si no crearlo
		File logDirectory = new File(directory);
		if (!logDirectory.exists()) {
			if (logDirectory.mkdirs()) {
				System.out.println("Directorio creado: " + logDirectory.getAbsolutePath());
			} else {
				System.err.println("No se pudo crear el directorio: " + logDirectory.getAbsolutePath());
				return; // Si no se puede crear el directorio, no se puede continuar
			}
		}

		System.out.println("Intentando agregar al archivo: " + directory + File.separator + fileName);

		if (archivismo.fileExistsInFolder(directory, fileName)) {
			archivismo.appendToFile(directory, fileName, message);
		} else {
			try {
				archivismo.createFile(directory, fileName, message);
			} catch (IOException ex) {
				Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}




    private Image resizeImage(String imagePath, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage();
            Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return resizedImg;
        } catch (Exception e) {
            System.out.println("Error al cargar la imagen: " + imagePath);
            e.printStackTrace();
            return null;
        }
    }

    private void seleccionarGuerrero(String nombreGuerrero) {
        for (Guerrero guerrero : jugador.getGuerreros()) {
            if (guerrero.getTipo().toString().equalsIgnoreCase(nombreGuerrero)) {
                guerreroSeleccionado = guerrero;
                actualizarInfoGuerrero();
                break;
            }
        }
    }

    private void addSendButtonListener() {
        sendButton.addActionListener(e -> processCommand());
        commandTextField.addActionListener(e -> processCommand());
    }

    private void processCommand() {
        String command = commandTextField.getText();
        if (!command.isEmpty()) {
            // Obtener la fecha y hora actual
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = ahora.format(formateador);
    
            // Agregar el comando con fecha y hora al chatTextArea
            chatTextArea.append("> [" + timestamp + "] " + command + "\n");
            chatTextArea.setCaretPosition(chatTextArea.getDocument().getLength()); // Scroll automático
    
            // Procesar el comando
            procesarComando(command.trim());
            commandTextField.setText("");
        }
    }
    

    private void procesarComando(String command) {
        // Dividir el comando y obtener la acción
        String[] parts = command.split(" ", 2);
        String action = parts[0].toLowerCase();

        // Validar si el jugador está en combate para restringir comandos
        boolean enCombate = this.enCombate;

        // Validar si es el turno del jugador
        boolean esTuTurno = enTurno;

        switch (action) {
            case "players":
                if (enCombate) {
                    chatTextArea.append("No puedes usar este comando durante una partida.\n");
                } else {
                    clienteJuego.enviarMensaje(new Mensaje("comando", "players", jugador.getNickname()));
                }
                break;
			case "challenge":
				if (enCombate) {
					chatTextArea.append("No puedes desafiar a otros jugadores durante una partida.\n");
				} else {
					if (parts.length < 2) {
						chatTextArea.append("Uso: challenge {player}\n");
					} else {
						String oponente = parts[1];
						Mensaje mensajeDesafio = new Mensaje("comando", "challenge " + oponente, jugador.getNickname());
						clienteJuego.enviarMensaje(mensajeDesafio);
					}
				}
				break;
            case "send":
                if (enCombate && !esComandoPermitidoEnCombate(action)) {
                    chatTextArea.append("No puedes usar este comando durante una partida.\n");
                } else {
                    if (parts.length < 2) {
                        chatTextArea.append("Uso: send {message}\n");
                    } else {
                        clienteJuego.enviarMensaje(new Mensaje("chat", parts[1], jugador.getNickname()));
                    }
                }
                break;
            case "sendprivate":
                if (enCombate && !esComandoPermitidoEnCombate(action)) {
                    chatTextArea.append("No puedes usar este comando durante una partida.\n");
                } else {
                    if (parts.length < 2) {
                        chatTextArea.append("Uso: sendprivate {player} {message}\n");
                    } else {
                        String[] msgParts = parts[1].split(" ", 2);
                        if (msgParts.length < 2) {
                            chatTextArea.append("Uso: sendprivate {player} {message}\n");
                        } else {
                            String destinatario = msgParts[0];
                            String mensaje = msgParts[1];
                            Mensaje mensajePrivado = new Mensaje("chatPrivado", mensaje, jugador.getNickname(), destinatario);
                            clienteJuego.enviarMensaje(mensajePrivado);
                        }
                    }
                }
                break;
            case "stats":
                if (enCombate && !esComandoPermitidoEnCombate(action)) {
                    chatTextArea.append("No puedes usar este comando durante una partida.\n");
                } else {
                    if (parts.length < 2) {
                        chatTextArea.append("Uso: stats {player}\n");
                    } else {
                        clienteJuego.enviarMensaje(new Mensaje("comando", "stats " + parts[1], jugador.getNickname()));
                    }
                }
                break;
            case "help":
                mostrarAyuda();
                break;
			case "select":
				if (enCombate) {
					if (parts.length < 2) {
						chatTextArea.append("Uso: select {guerrero}\n");
					} else {
						if(parts[1] != ""){
							seleccionarGuerrero(parts[1]);
							// Opcional: Notificar al servidor sobre la selección
							Mensaje seleccion = new Mensaje("select", parts[1], jugador.getNickname(), oponenteActual);
							clienteJuego.enviarMensaje(seleccion);
						}
					}
				} else {
					seleccionarGuerrero(parts[1]);
				}
				break;
            // Comandos de Combate
			case "attack":
				if (!enCombate) {
					chatTextArea.append("No estas en una partida para realizar ataques.\n");
				} else if (!enTurno) {
					chatTextArea.append("No es tu turno para atacar.\n");
				} else {
					String[] attackParts = command.split(" ", 4);
					if (attackParts.length < 3) {
						chatTextArea.append("Uso: attack {guerrero} {arma} [estrategia opcional]\n");
					} else {
						String guerreroNombre = attackParts[1];
						String armaNombre = attackParts[2];
						String estrategia = (attackParts.length == 4) ? attackParts[3] : "";
						// Validar guerrero y arma
						Guerrero guerrero = jugador.getGuerreroPorNombre(guerreroNombre);
						if (guerrero == null || guerrero.getVida() <= 0) {
							chatTextArea.append("Guerrero inválido o fuera de combate.\n");
							break;
						}
						
						Arma arma = guerrero.getArmaPorNombre(armaNombre);
						if (arma == null || arma.isUsada()) {
							chatTextArea.append("Arma inválida o deshabilitada.\n");
							break;
						}
						
						// Enviar ataque al servidor
						String contenidoAtaque = guerreroNombre + ";" + armaNombre + ";" + estrategia;
						Mensaje mensajeAtaque = new Mensaje("attack", contenidoAtaque, jugador.getNickname(), oponenteActual);
						clienteJuego.enviarMensaje(mensajeAtaque);
                                                autoReject();
						enTurno = false;	
					}
				}
				break;

            case "selectweapon":
                if (!enCombate) {
                    chatTextArea.append("No estás en una partida para seleccionar armas.\n");
                } else if (!esTuTurno) {
                    chatTextArea.append("No es tu turno para seleccionar armas.\n");
                } else {
                    if (parts.length < 2) {
                        chatTextArea.append("Uso: selectweapon {arma}\n");
                    } else {
                        clienteJuego.enviarMensaje(new Mensaje("combat", "selectweapon " + parts[1], jugador.getNickname()));
                    }
                }
                break;
            case "selectenemy":
                if (!enCombate) {
                    chatTextArea.append("No estás en una partida para seleccionar enemigos.\n");
                } else if (!esTuTurno) {
                    chatTextArea.append("No es tu turno para seleccionar enemigos.\n");
                } else {
                    if (parts.length < 2) {
                        chatTextArea.append("Uso: selectenemy {player}\n");
                    } else {
                        clienteJuego.enviarMensaje(new Mensaje("combat", "selectenemy " + parts[1], jugador.getNickname()));
                    }
                }
                break;
            case "surrender":
                if (enCombate) {
                    autoReject();
                    clienteJuego.enviarMensaje(new Mensaje("combat", "surrender", jugador.getNickname()));
                    chatTextArea.append("Te has rendido.. ");
                } else {
                    chatTextArea.append("No estás en una partida para rendirte.\n");
                }
                break;
            case "draw":
                if (enCombate) {
                    clienteJuego.enviarMensaje(new Mensaje("combat", "draw", jugador.getNickname()));
                    chatTextArea.append("Se ha enviado una solicitud de empate al rival... \n");
                } else {
                    chatTextArea.append("No estás en una partida para proponer una empate.\n");
                }
                break;
            case "reloadweapons":
                if (enCombate) {
                    clienteJuego.enviarMensaje(new Mensaje("combat", "reloadweapons", jugador.getNickname()));
                } else {
                    chatTextArea.append("No estás en una partida para recargar armas.\n");
                }
                break;
			case "usewildcard":
				if (!enCombate) {
					chatTextArea.append("No estás en una partida para usar un comodín.\n");
				} else if (!enTurno) {
					chatTextArea.append("No es tu turno para usar un comodín.\n");
				} else if (!jugador.isTieneComodin()) {
					chatTextArea.append("No tienes un comodín disponible.\n");
				} else {
					Mensaje mensajeComodin = new Mensaje("useWildCard", "", jugador.getNickname());
					clienteJuego.enviarMensaje(mensajeComodin);
					// El comodín será manejado en la respuesta del servidor
				}
				break;


            case "selectplayer":
                if (enCombate) {
                    if (parts.length < 2) {
                        chatTextArea.append("Uso: selectplayer {player}\n");
                    } else {
                        clienteJuego.enviarMensaje(new Mensaje("combat", "selectplayer " + parts[1], jugador.getNickname()));
                    }
                } else {
                    chatTextArea.append("No estás en una partida para seleccionar jugadores.\n");
                }
                break;
				
            case "passturn":
                if (enCombate) {
                    if(this.enTurno){
                    clienteJuego.enviarMensaje(new Mensaje("combat", "passturn", jugador.getNickname()));}
                } else {
                    chatTextArea.append("No puedes pasar de turno ahora mismo.\n");
                }
                break;
            // Agregar más comandos aquí

            case "Y":
                if(waiting_draw){
                    System.out.println("Se ha detectado una aceptacion de empate");
                    clienteJuego.enviarMensaje(new Mensaje("combat", "acceptdraw", jugador.getNickname()));
                    setWaitingDraw(false);
                    break;
                }
            case "y":
                if(waiting_draw){
                    clienteJuego.enviarMensaje(new Mensaje("combat", "acceptdraw", jugador.getNickname()));
                                        setWaitingDraw(false);
                    break;
                }
            case "N":
                if(waiting_draw){
                    clienteJuego.enviarMensaje(new Mensaje("combat", "rejectdraw", jugador.getNickname()));
                                        setWaitingDraw(false);
                    break;
                } 
            case "n":
                if(waiting_draw){
                    clienteJuego.enviarMensaje(new Mensaje("combat", "rejectdraw", jugador.getNickname()));
                                        setWaitingDraw(false);
                    break;
                }                 
            default:
                chatTextArea.append("Comando no reconocido. Escribe 'help' para ver los comandos disponibles.\n");
        }
    }
	private void setWaitingDraw(Boolean option){
            this.waiting_draw = option;
        }
        
	
	private void actualizarMisEstadisticas() {
    infoPanel2.removeAll();
    infoPanel2.setLayout(new GridLayout(7, 1, 10, 10));
    infoPanel2.add(new JLabel("Nickname: " + jugador.getNickname()));
    infoPanel2.add(new JLabel("Wins: " + jugador.getWins()));
    infoPanel2.add(new JLabel("Loses: " + jugador.getLoses()));
    infoPanel2.add(new JLabel("Attacks: " + jugador.getAttacks()));
    infoPanel2.add(new JLabel("Success: " + jugador.getSuccess()));
    infoPanel2.add(new JLabel("Failed: " + jugador.getFailed()));
    infoPanel2.add(new JLabel("Giveup: " + jugador.getGiveup()));
    for (Component comp : infoPanel2.getComponents()) {
        if (comp instanceof JLabel) {
            ((JLabel) comp).setFont(new Font("Arial", Font.PLAIN, 12));
        }
    }
    
    infoPanel2.revalidate();
    infoPanel2.repaint();
}

	private void actualizarInfoGuerrero() {
		if (guerreroSeleccionado != null) {
			StringBuilder info = new StringBuilder();
			info.append("Nombre: ").append(guerreroSeleccionado.getNombre()).append("\n");
			info.append("Vida: ").append(guerreroSeleccionado.getVida()).append("%\n\n");
			info.append("Armas:\n");

			// Obtener los tipos ordenados para mantener la consistencia
			List<Tipo> orderedTipos = Arrays.asList(
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

			for (Arma arma : guerreroSeleccionado.getArmas()) {
				info.append(arma.getNombre()).append(" [");
				List<String> tipoDanio = new ArrayList<>();
				for (Tipo tipo : orderedTipos) {
					int danio = arma.getDanioPorTipo().getOrDefault(tipo, 0);
					if (danio > 0) {
						tipoDanio.add(tipo.toString() + " " + danio);
					}
				}
				info.append(String.join(", ", tipoDanio));
				info.append("] Estado: ").append(arma.isUsada() ? "Usada" : "Disponible").append("\n");
			}

			warriorInfoTextArea.setText(info.toString());
		} else {
			warriorInfoTextArea.setText("Selecciona un guerrero para ver su información.");
		}
	}

	
	private void iniciarCombateUI(String oponenteNickname) {
		enCombate = true;
		oponenteActual = oponenteNickname;
		chatTextArea.append("¡El combate contra " + oponenteNickname + " ha comenzado!\n");
		// Actualizar el panel de infchatTextArea.append("¡El combate contra " + oponenteNickname + " ha comenzado!\n");ormación
		Mensaje mensajeStats = new Mensaje("comando", "stats " + oponenteNickname, jugador.getNickname());
		clienteJuego.enviarMensaje(mensajeStats);
	}

	private void procesarRespuestaDesafio(Mensaje mensaje) {
		String respuesta = mensaje.getContenido();
		if (respuesta.equalsIgnoreCase("A")) {
			chatTextArea.append(mensaje.getEmisor() + " ha aceptado tu desafío. ¡Comienza el combate!\n");
			enCombate = true; // Usar solo la variable local
			enTurno = true; // Puedes implementar un sistema de turnos más complejo
			iniciarCombateUI(mensaje.getEmisor());
		} else {
			chatTextArea.append(mensaje.getEmisor() + " ha rechazado tu desafío.\n");
		}
	}

    private boolean esComandoPermitidoEnCombate(String comando) {
        // Define qué comandos están permitidos durante el combate
        // Por ejemplo, enviar mensajes, rendirse, etc.
        List<String> comandosPermitidos = Arrays.asList("send", "sendprivate", "surrender", "draw", "usewildcard", "select");
        return comandosPermitidos.contains(comando.toLowerCase());
    }

	private void mostrarAyuda() {
		String ayuda = "Comandos disponibles:\n"
				+ "- players: Lista los jugadores conectados.\n"
				+ "- challenge {player}: Reta a otro jugador a una batalla.\n"
				+ "- send {message}: Envía un mensaje a todos.\n"
				+ "- sendprivate {player} {message}: Envía un mensaje privado a un jugador.\n"
				+ "- stats {player}: Muestra estadísticas de un jugador.\n"
				+ "- help: Muestra esta ayuda.\n"
				+ "- select {guerrero}: Selecciona un guerrero.\n"
				// Nuevos comandos de combate
				+ "- attack {guerrero} {arma} [estrategia opcional]: Ataca al oponente con un guerrero y arma específicos.\n"
				+ "- usewildcard: Usa un comodín disponible.\n"
				+ "- surrender: Te rindes en la partida.\n"
				+ "- draw: Propon una salida mutua para declarar empate.\n"
				+ "- reloadweapons: Recarga las armas usadas.\n"
				+ "- passturn: Cede tu turno.\n";
		chatTextArea.append(ayuda);
	}
	
	public void actualizarCompleto(){
		sincronizarEstadisticas();
		clienteJuego.enviarMensaje(new Mensaje("comando", "stats " + oponenteActual, jugador.getNickname()));
		actualizarMisEstadisticas();
		guerreroSeleccionado = jugador.getGuerreros().get(0);
		actualizarInfoGuerrero();
	}

    public void procesarMensajeServidor(Mensaje mensaje) {
        switch (mensaje.getTipo()) {
            case "chat":
                chatTextArea.append(mensaje.getEmisor() + ": " + mensaje.getContenido() + "\n");
                break;
            case "chatPrivado":
                chatTextArea.append("(Privado) " + mensaje.getEmisor() + ": " + mensaje.getContenido() + "\n");
                break;
            case "listaJugadores":
                actualizarRanking(mensaje.getContenido());
                break;
            case "statsJugador":
                mostrarStatsJugador(mensaje.getContenido());
                break;
			case "desafio":
				procesarDesafio(mensaje);
				break;
            case "respuestaDesafio":
                procesarRespuestaDesafio(mensaje);
                break;
			case "combateInicio":
				iniciarCombateUI(mensaje.getContenido());
				break;
            case "combateActualizacion":
                actualizarCompleto();
                actualizarEstadoCombate(mensaje);
                break;
            case "error":
                chatTextArea.append("(Error) " + mensaje.getContenido() + "\n");
                break;
			case "attackResult":
				actualizarCompleto();
				centralTextArea1.append(mensaje.getContenido());
				break;
			case "defenseResult":
				actualizarCompleto();
				centralTextArea2.append(mensaje.getContenido());
				break;
			case "tuTurno":
				enTurno = mensaje.getContenido().equals("true");
				System.out.println("Mensaje 'tuTurno' recibido: " + mensaje.getContenido());
				if (enTurno) {
					chatTextArea.append("Es tu turno.\n");
					turnoLabel.setText("Turno: Tú");
				} else {
					chatTextArea.append("Es el turno de tu oponente.\n");
					turnoLabel.setText("Turno: Oponente");
				}
				break;
			case "combateFinalizado":
				actualizarCompleto();
				enCombate = false;
				if (mensaje.getContenido().equals("victoria")) {
					chatTextArea.setForeground(Color.GREEN);
					chatTextArea.append("¡Has ganado!\n");
				} else if (mensaje.getContenido().equals("derrota")){
					chatTextArea.setForeground(Color.RED);
					chatTextArea.append("Has perdido.\n");
				}
                                if(mensaje.getContenido().equals("empate")) {
                                      chatTextArea.append("La partida ha terminado en un empate...! \n");
                                }
				chatTextArea.setForeground(Color.BLACK);
				break;
			case "info":
				// Mostrar mensajes de información (como uso de comodines)
				chatTextArea.append("(Info) " + mensaje.getContenido() + "\n");
				break;
            case "syncJugadorResponse": // Nuevo caso
                Jugador updatedJugador = (Jugador) mensaje.getContenidoObjeto();
                actualizarJugador(updatedJugador);
                break;
            case "ReceiveSurrenderRequest":
                handleSurrender();
                break;
        
            case "ReceiveDrawRequest":
                handleDrawRequest();
                break;
            
            case "ReceiveDrawRejection":
                handleDrawRejection();
                break;
                
            case "SendDrawRejection":
                handleSendingRejection();
                break;
            
            case "manejarreloadweapons":
                handleSuccessfulReload();
                break;
            case "manejarrechazoreload":
                handleFailedReload();
                break;
                        
    }}
    
    private void handleFailedReload(){
        this.chatTextArea.append("No puedes recargar tus armas ahora mismo.\n");
    }
    private void handleSuccessfulReload(){
        chatTextArea.append("Recarga de armas exitosa\n");
    }
    private void handleSendingRejection(){
        setWaitingDraw(false);
        chatTextArea.append("Has rechazado la propuesto de empate. \n");
    }
    private void handleDrawRejection(){
        setWaitingDraw(false);
        chatTextArea.append("Tu rival ha rechazado tu propuesta de empate! \n");
    }
    
    
    private void handleDrawRequest(){
        setWaitingDraw(true);
        chatTextArea.append("Tu oponente propone un empate. Aceptar? <Y> <N>\n");
    }
    private void handleSurrender() {
        chatTextArea.append("Tu oponente se ha rendido. ");
    }
	
	private void actualizarJugador(Jugador updatedJugador) {
		System.out.println("Recibido Jugador actualizado: " + updatedJugador.getNickname());
		System.out.println(updatedJugador.toString());
		
		// Actualizar las estadísticas básicas
		this.jugador.setWins(updatedJugador.getWins());
		this.jugador.setLoses(updatedJugador.getLoses());
		this.jugador.setAttacks(updatedJugador.getAttacks());
		this.jugador.setSuccess(updatedJugador.getSuccess());
		this.jugador.setFailed(updatedJugador.getFailed());
		this.jugador.setGiveup(updatedJugador.getGiveup());
		this.jugador.setTieneComodin(updatedJugador.isTieneComodin());
		this.jugador.setComodinPositivo(updatedJugador.isComodinPositivo());
		this.jugador.setAtaquesRestantes(updatedJugador.getAtaquesRestantes());

		// Actualizar Guerreros y sus Armas
		List<Guerrero> updatedGuerreros = updatedJugador.getGuerreros();
		List<Guerrero> localGuerreros = this.jugador.getGuerreros();

		for (int i = 0; i < localGuerreros.size(); i++) {
			Guerrero localGuerrero = localGuerreros.get(i);
			Guerrero updatedGuerrero = updatedGuerreros.get(i);

			localGuerrero.setVida(updatedGuerrero.getVida());

			// Actualizar Armas
			List<Arma> localArmas = localGuerrero.getArmas();
			List<Arma> updatedArmas = updatedGuerrero.getArmas();

			for (int j = 0; j < localArmas.size(); j++) {
				Arma localArma = localArmas.get(j);
				Arma updatedArma = updatedArmas.get(j);

				localArma.setUtilizada(updatedArma.isUsada());
				// Actualizar otros atributos si es necesario
			}
		}

		// Actualizar la interfaz gráfica con los nuevos datos
		actualizarMisEstadisticas();
		actualizarInfoGuerrero();
		// Si tienes otros componentes de la interfaz que dependen de los datos, actualízalos aquí
	}
	
	
	private void procesarDesafio(Mensaje mensaje) {
		int respuesta = JOptionPane.showOptionDialog(this,
				"Has recibido un desafío de " + mensaje.getEmisor() + ". ¿Aceptas?",
				"Desafío recibido",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				new Object[]{"Aceptar", "Rechazar"},
				"Aceptar");

		String respuestaStr = (respuesta == JOptionPane.YES_OPTION) ? "A" : "R";
		Mensaje respuestaMensaje = new Mensaje("respuestaDesafio", respuestaStr, jugador.getNickname(), mensaje.getEmisor());
		clienteJuego.enviarMensaje(respuestaMensaje);
	}


    private void actualizarRanking(String listaJugadores) {
        rankingTextArea.setText(listaJugadores);
    } 

    private void autoReject(){
       if(waiting_draw){
            clienteJuego.enviarMensaje(new Mensaje("combat", "rejectdraw", jugador.getNickname()));
            setWaitingDraw(false);
       }
    }
    private void mostrarStatsJugador(String stats) {
        infoPanel1.removeAll();
        String[] lines = stats.split("\n");
        for (String line : lines) {
            JLabel label = new JLabel(line);
            label.setFont(new Font("Arial", Font.PLAIN, 12)); // Mejor legibilidad
            infoPanel1.add(label);
        }
        infoPanel1.revalidate();
        infoPanel1.repaint();
    }   

	
    private void actualizarEstadoCombate(Mensaje mensaje) {
        // Actualizar el estado del combate en la interfaz
        // Supongamos que el contenido del mensaje es un resumen del combate
        chatTextArea.append("(Combate) " + mensaje.getEmisor() + ": " + mensaje.getContenido() + "\n");
    }

    private void conectarAlServidor() {
        clienteJuego = new ClienteJuego("localhost", 12345, jugador, this);
    }
	
	public void sincronizarEstadisticas() {
		String nickname = jugador.getNickname();
		Mensaje solicitud = new Mensaje("syncStatsRequest", nickname, nickname);
		clienteJuego.enviarMensaje(solicitud);
	}
}
 