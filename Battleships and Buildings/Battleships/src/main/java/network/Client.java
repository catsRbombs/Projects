// network/Client.java

package network;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import UI.PlayerGUI;
import game.MapJuego;
import game.Offer;
import game.Player;
import game.components.Component;
import utils.Logger;

/**
 * La clase Client establece conexión con el servidor.
 * Envía comandos y recibe actualizaciones.
 */
public class Client {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Logger logger;
    private String host;
    private int port;
    public Player player;
   public PlayerGUI gui;
    public Boolean myTurn = false;
	

    /**
     * Constructor de la clase Client.
     *
     * @param host Dirección del servidor
     * @param port Puerto del servidor
     */
    public Client( int port) {
        this.player = new Player();    
        this.host = "localhost";
        this.port = port;
        myTurn = false;
        logger = Logger.getInstance();
        gui = new PlayerGUI(this);
		gui.setVisible(true);
        player.setClientHost(this);
		player.actualizarDineroPlayer();
    }

    /**
     * Establece la conexión con el servidor.
     */
    public void connect() {
        try {
            socket = new Socket(host, port);
            logger.log("Conectado al servidor en " + host + ":" + port);

            // Inicializa ObjectOutputStream primero para evitar bloqueos
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush(); // Envía el encabezado del ObjectOutputStream

            // Inicializa ObjectInputStream
            in = new ObjectInputStream(socket.getInputStream());


            new Thread(this::escucharMensajes).start();

        } catch (IOException e) {
            logger.log("Error al conectar al servidor: " + e.getMessage());
        }
    }
	
	private void escucharMensajes() {
        try {
            Object mensaje;
            while ((mensaje = in.readObject()) != null) {
                // Procesar el mensaje recibido del servidor
                handleMessage(mensaje);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Desconectado del servidor");
            disconnect();
        }
    }

    /**
     * Envía un mensaje al servidor.
     *
     * @param message Mensaje a enviar
     */
    public void enviarMensaje(Message mensaje) {
        try {
            out.writeObject(mensaje);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    /**
     * Maneja mensajes recibidos del servidor.
     *
     * @param message Mensaje recibido
     */


	public void handleMessage(Object message) {
		Message message1 = (Message) message; 
		logger.log("Mensaje recibido del servidor: " + message1.getType());

		switch (message1.getType()) {
			case "chat":
				System.out.println("Mensage aqui");
				gui.getTxaChat().append(message1.getData().toString());
				break;
			case "getName":
				this.player.setName((String) message1.getData());
                                this.gui.startButtonEnabler();
				break;
			case "sendOffer":
				// Receive an offer from another player
				Offer receivedOffer = (Offer) message1.getData();
				receiveOffer(receivedOffer);
				player.actualizarDineroPlayer();
				break;
			case "acceptOffer":
				// The recipient accepted our offer
				Offer acceptedOffer = (Offer) message1.getData();
				JOptionPane.showMessageDialog(null, acceptedOffer.getRecipient() + " ha aceptado tu oferta.", "Oferta Aceptada", JOptionPane.INFORMATION_MESSAGE);
				player.receiveOffer(acceptedOffer);
				player.actualizarDineroPlayer();
				break;
			case "rejectOffer":
				// The recipient rejected our offer
				Offer rejectedOffer = (Offer) message1.getData();
				JOptionPane.showMessageDialog(null, rejectedOffer.getRecipient() + " ha rechazado tu oferta.", "Oferta Rechazada", JOptionPane.INFORMATION_MESSAGE);
				// Return the offered items to our player
				player.returnOffer(rejectedOffer);
				player.actualizarDineroPlayer();
				break;
			case "start":
					gui.escribirEnResultados("Comienza la partida! Es el turno de " + player.getName());
					if (player.getName().equals("player1")) {
						gui.attackTurn();
						gui.getBtnStart().setVisible(false);
						gui.startTurn();
						myTurn = true;
						gui.repaint();
						gui.revalidate();
						break;
					} else gui.notAttackTurn();
					break;
            case "startTurn":
                myTurn = true;
                gui.startTurn();
                break;
            case "updateTurnStatus":
			String whoTurn = (String) message1.getData();
			char lastChar = whoTurn.charAt(whoTurn.length() - 1); // Obtenemos el último carácter de la cadena
			if (lastChar != '!') { 
				// Si el último carácter no es un signo de exclamación
                            int nextPlayer = Character.getNumericValue(lastChar) + 1;
                            whoTurn = "Player " + nextPlayer; 
			}
              gui.updateTurn(whoTurn);
			  break;
			case "recibirKraken":
				showKrakenAttackMessage();
				handleRandomDestruction();
			case "tomarTablero":
				String remitente = (String) message1.getData();
				System.out.println("Enviando un mapa a " + remitente );
				enviarTablero(remitente);
				break;
			case "recibirTablero":
				System.out.println("Se recibio un mapa!");
				String mapismo = (String) message1.getData();
				MapJuego mapaGod = new MapJuego(mapismo);
				gui.loadEnemyGrid(mapaGod);
				break;
			case "recibirEscudo":
				gui.recibirMensajeChatString("Escudo recibido <3");
			default:
				break;
		}
	}
    public static void showKrakenAttackMessage() {
        // Crear un panel personalizado
        JPanel panel = new JPanel(new BorderLayout());
        
        // Crear el JLabel con el mensaje
        JLabel messageLabel = new JLabel("¡Has recibido un ataque de un kraken!");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Crear el JLabel con la imagen del Kraken
        JLabel imageLabel = new JLabel(new ImageIcon("Data/kraken.png"));

        // Agregar los elementos al panel
        panel.add(messageLabel, BorderLayout.NORTH);
        panel.add(imageLabel, BorderLayout.CENTER);

        // Mostrar el JOptionPane con el panel personalizado
        JOptionPane.showMessageDialog(
                null,
                panel,
                "Ataque del Kraken",
                JOptionPane.INFORMATION_MESSAGE
        );
	}
	public void enviarTablero(String remitente) {
	MapJuego mapita = this.player.getMap() ;
	String output = mapita.toStringRepresentation();
		enviarMensaje(new Message("receiveTablero", output, remitente));
	}

	public void handleRandomDestruction() {

		Component death = this.player.getRandomComponent();
		if (death!= null) { 
			gui.searchNdelete(death);
		System.out.println("nashe");
		}
		
	}
	
	public void receiveOffer(Offer offer) {
		// Display the offer in a popup
		JFrame offerFrame = new JFrame("Oferta Recibida de " + offer.getSender());
		offerFrame.setSize(400, 300);
		offerFrame.setLocationRelativeTo(null);
		offerFrame.setLayout(new BorderLayout());
		
		JPanel offerPanel = new JPanel(new GridLayout(7, 2));

		// Display what is being offered
		offerPanel.add(new JLabel("Te ofrecen:"));
		offerPanel.add(new JLabel(""));

		offerPanel.add(new JLabel("Dinero:"));
		offerPanel.add(new JLabel(String.valueOf(offer.getOfferMoney())));
		offerPanel.add(new JLabel("Steel:"));
		offerPanel.add(new JLabel(String.valueOf(offer.getOfferSteel())));
		offerPanel.add(new JLabel("Cannon:"));
		offerPanel.add(new JLabel(String.valueOf(offer.getOfferCannon())));
		offerPanel.add(new JLabel("DoubleCannon:"));
		offerPanel.add(new JLabel(String.valueOf(offer.getOfferDoubleCannon())));
		offerPanel.add(new JLabel("RedBeardCannon:"));
		offerPanel.add(new JLabel(String.valueOf(offer.getOfferRedBeardCannon())));
		offerPanel.add(new JLabel("Bomb:"));
		offerPanel.add(new JLabel(String.valueOf(offer.getOfferBomb())));

		// Display what is being requested
		JPanel requestPanel = new JPanel(new GridLayout(7, 2));
		requestPanel.add(new JLabel("A cambio de:"));
		requestPanel.add(new JLabel(""));

		requestPanel.add(new JLabel("Dinero:"));
		requestPanel.add(new JLabel(String.valueOf(offer.getRequestMoney())));
		requestPanel.add(new JLabel("Steel:"));
		requestPanel.add(new JLabel(String.valueOf(offer.getRequestSteel())));
		requestPanel.add(new JLabel("Cannon:"));
		requestPanel.add(new JLabel(String.valueOf(offer.getRequestCannon())));
		requestPanel.add(new JLabel("DoubleCannon:"));
		requestPanel.add(new JLabel(String.valueOf(offer.getRequestDoubleCannon())));
		requestPanel.add(new JLabel("RedBeardCannon:"));
		requestPanel.add(new JLabel(String.valueOf(offer.getRequestRedBeardCannon())));
		requestPanel.add(new JLabel("Bomb:"));
		requestPanel.add(new JLabel(String.valueOf(offer.getRequestBomb())));

		// Buttons to accept or reject
		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton acceptButton = new JButton("Aceptar");
		JButton rejectButton = new JButton("Rechazar");
		buttonPanel.add(acceptButton);
		buttonPanel.add(rejectButton);

		offerFrame.add(offerPanel, BorderLayout.NORTH);
		offerFrame.add(requestPanel, BorderLayout.CENTER);
		offerFrame.add(buttonPanel, BorderLayout.SOUTH);

		acceptButton.addActionListener(e -> {
			// Check if player has enough resources to accept the offer
			if (player.getMoney() < offer.getRequestMoney() ||
				player.getSteel() < offer.getRequestSteel() ||
				player.weaponCount("Cannon") < offer.getRequestCannon() ||
				player.weaponCount("DoubleCannon") < offer.getRequestDoubleCannon() ||
				player.weaponCount("RedBeardCannon") < offer.getRequestRedBeardCannon() ||
				player.weaponCount("Bomb") < offer.getRequestBomb()) {
				JOptionPane.showMessageDialog(offerFrame, "No tienes los recursos suficientes.", "Error", JOptionPane.ERROR_MESSAGE);
				player.actualizarDineroPlayer();
				return;
			}

			// Subtract requested resources from player
			player.usarDinero(offer.getRequestMoney());
			player.byebyeSteel(offer.getRequestSteel());
			player.removeWeapons("Cannon", offer.getRequestCannon());
			player.removeWeapons("DoubleCannon", offer.getRequestDoubleCannon());
			player.removeWeapons("RedBeardCannon", offer.getRequestRedBeardCannon());
			player.removeWeapons("Bomb", offer.getRequestBomb());

			// Add offered resources to player
			player.recibirDinero(offer.getOfferMoney());
			player.addSteel(offer.getOfferSteel());
			player.addWeapons("Cannon", offer.getOfferCannon());
			player.addWeapons("DoubleCannon", offer.getOfferDoubleCannon());
			player.addWeapons("RedBeardCannon", offer.getOfferRedBeardCannon());
			player.addWeapons("Bomb", offer.getOfferBomb());

			// Notify sender that offer is accepted
			enviarMensaje(new Message("acceptOffer", offer, offer.getSender()));
			player.actualizarDineroPlayer();
			offerFrame.dispose();
		});

		rejectButton.addActionListener(e -> {
			// Notify sender that offer is rejected
			enviarMensaje(new Message("rejectOffer", offer, offer.getSender()));

			offerFrame.dispose();
		});
		player.actualizarDineroPlayer();
		offerFrame.setVisible(true);
	}

    public void endTurn() {
        gui.endTurn();
        enviarMensaje(new Message("EndTurn", "."));
    }

    /**
     * Desconecta del servidor.
     */
    public void disconnect() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método principal para probar la conexión del cliente.
     * (Opcional: Puedes eliminarlo si no es necesario)
     */
    public static void main(String[] args) {
        Client client = new Client(12345);
        client.connect();

        // Enviar un mensaje de prueba
        Message testMessage = new Message("Chat", "¡Hola desde el cliente!");
        client.enviarMensaje(testMessage);
    }
}
