package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import game.MapJuego;
import game.Offer;
import utils.Archivismo;

public class ClienteHandler extends Thread {
    private ServidorJuego server;
    private Socket socket;
    private ServidorJuego servidor;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String nombre; 
    public boolean ready;
	public Archivismo archivismo;
    public Boolean myTurn = false;

	public ClienteHandler(Socket socket, ServidorJuego servidor, int playerCount) {
        this.server = servidor;
		this.socket = socket;
		this.servidor = servidor;
		this.archivismo = new Archivismo();
                this.ready  = false;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			// Leer el ID del jugador al conectarse
			// You might need to read the player's name from the client
			this.nombre = "player" + playerCount;
			enviarMensaje(new Message("getName", nombre));
			System.out.println("Enviando nombre! " + nombre);

			// Add the player to the server's map
			servidor.agregarPlayer(nombre, this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	


    @Override
    public void run() {
        try {
            Object mensaje;
            while ((mensaje = in.readObject()) != null) {
                // Procesar el mensaje recibido del cliente
                procesarMensaje(mensaje);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Cliente desconectado: " + nombre);
            servidor.removerCliente(this);
            
        } finally {
            cerrarConexion();
        }
    }

// In network/ClienteHandler.java

	private void procesarMensaje(Object mensajeObj) throws IOException {
		if (mensajeObj instanceof Message) {
			Message mensaje = (Message) mensajeObj;
			// Procesar según el tipo de mensaje
			switch (mensaje.getType()) {
				case "comando":
					// Handle command
					break;
				case "chat":
					servidor.broadcast(mensaje, this);
					break;
				case "sendOffer":
					// Forward the offer to the recipient
					String recipientName = mensaje.getRecipient();
					ClienteHandler recipientHandler = servidor.getClienteHandler(recipientName);
					if (recipientHandler != null) {
						recipientHandler.enviarMensaje(mensaje);
					}
					break;
				case "acceptOffer":
				case "rejectOffer":
					// Notify the sender
					String senderName = ((Offer) mensaje.getData()).getSender();
					ClienteHandler senderHandler = servidor.getClienteHandler(senderName);
					if (senderHandler != null) {
						senderHandler.enviarMensaje(mensaje);
					}
					break;
                                case "ready":
                                        ready = true;
                                        System.out.println(getName() + " esta listo");
                                        break;
                                case "start":
                                        if (servidor.allReady()) {
                                            servidor.broadcast(mensaje, this);
                                            server.nextPlayerTurn();
                                    }
                                        break;
														
								case "updateTurns":
									String fakeMyTurn = (String) mensaje.getData();
									if(fakeMyTurn.equals("true")) myTurn = true;
									else myTurn = false;
									break;
								case "EndTurn":
									server.nextPlayerTurn();
									break;
                                case "pedirTableroP1":
                                    String enviador = (String) mensaje.getData();
									System.out.println(enviador + " le esta solicitando a p1 su tablero");
                                    if (!enviador.equals("player1") && servidor.getClientes().size() >= 1) {
                                        server.getClienteHandler("player1").enviarMensaje(new Message("tomarTablero", enviador));
                                    }
                                    break;
                                case "pedirTableroP2":
                                    String enviador2 = (String) mensaje.getData();
									System.out.println(enviador2 + " le esta solicitando a p2 su tablero");
                                    if (!enviador2.equals("player2") && servidor.getClientes().size() >= 2) {
                                        server.getClienteHandler("player2").enviarMensaje(new Message("tomarTablero", enviador2));
                                    }
                                    break;
                                case "pedirTableroP3":
                                    String enviador3 = (String) mensaje.getData();
                                    if (!enviador3.equals("player3")&& servidor.getClientes().size() >= 3) {
                                        server.getClienteHandler("player3").enviarMensaje(new Message("tomarTablero", enviador3));
                                    }
                                    break;
                                case "pedirTableroP4":
                                    String enviador4 = (String) mensaje.getData();
                                    if (!enviador4.equals("player4") && servidor.getClientes().size() >= 4) {
                                        server.getClienteHandler("player4").enviarMensaje(new Message("tomarTablero", enviador4));
                                    }
                                    break;
								case "receiveTablero":
								 String input = (String) mensaje.getData();
								 String destino = (String) mensaje.getDestinatario();
								 System.out.println("input " +input);
								 System.out.println("Destino " +destino);
								 System.out.println(destino + " <- se le esta enviando un mapa");
								 server.getClienteHandler(destino).enviarMensaje(new Message("recibirTablero", input));
								 break;
								case "mataKrakens":
									String target = (String) mensaje.getData();
									server.getClienteHandler(target).enviarMensaje(new Message("recibirKraken", ":("));
								case "Broadcast":
								    servidor.broadcast(new Message("chat", "Se ha hecho un ataque"), null);
								default:
									// Handle other message types
									break;
			}
		}
	}
	
	// In network/ClienteHandler.java


	
	public void enviarMensaje(Object mensaje) {
		try {
			out.writeObject(mensaje);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public void startTurn() {
        myTurn = true;
        enviarMensaje(new Message("startTurn", myTurn));
    }

    public void updateTurn(String whoTurn) {
        enviarMensaje(new Message("updateTurn", whoTurn));
    }

    private void cerrarConexion() {
        try {
            servidor.removerCliente(this);
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}