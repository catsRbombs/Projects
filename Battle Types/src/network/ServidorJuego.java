package network;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import model.Jugador;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.Random;


public class ServidorJuego {
    private ScheduledExecutorService scheduler;
    private ServerSocket serverSocket;
    private List<ClienteHandler> clientes;
    private Map<String, ClienteHandler> mapaClientes;

    public ServidorJuego(int puerto) {
        try {
            serverSocket = new ServerSocket(puerto);
            clientes = new ArrayList<>();
            mapaClientes = new HashMap<>();
            
            // Inicializar el scheduler antes de usarlo
            scheduler = Executors.newScheduledThreadPool(1);
            
            System.out.println("Servidor iniciado en el puerto " + puerto);
            
            // Iniciar la generación de comodines después de inicializar el scheduler
            iniciarGeneradorComodines();
            
		while (true) {
			Socket socket = serverSocket.accept();
			ClienteHandler clienteHandler = new ClienteHandler(socket, this); // Sin 'jugador' compartido
			clientes.add(clienteHandler);
			clienteHandler.start();
		}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private void iniciarGeneradorComodines() {
		scheduler.scheduleAtFixedRate(() -> generarComodin(), 5, 5, TimeUnit.MINUTES);
	}

	private void generarComodin() {
		synchronized (this) {
			List<ClienteHandler> jugadoresEnCombate = clientes.stream().filter(c -> c.estado.equals("peleando")).collect(Collectors.toList());
			if (jugadoresEnCombate.isEmpty()) return;
			ClienteHandler jugadorConComodin = jugadoresEnCombate.get(new Random().nextInt(jugadoresEnCombate.size()));
			boolean esPositivo = new Random().nextBoolean();
			jugadorConComodin.jugador.setTieneComodin(true);
			jugadorConComodin.jugador.setComodinPositivo(esPositivo);

			// Notificar al jugador
			Mensaje mensajeComodin = new Mensaje("comodin", esPositivo ? "positivo" : "negativo", "Servidor");
			jugadorConComodin.enviarMensaje(mensajeComodin);
		}
	}

    public synchronized void agregarJugador(String idJugador, ClienteHandler handler) {
        mapaClientes.put(idJugador, handler);
    }

    public synchronized void removerCliente(ClienteHandler cliente) {
        clientes.remove(cliente);
        mapaClientes.remove(cliente.getIdJugador());
    }

    public synchronized void broadcast(Mensaje mensaje, ClienteHandler emisor) {
        for (ClienteHandler cliente : clientes) {
            if (cliente != emisor) {
                cliente.enviarMensaje(mensaje);
            }
        }
    }

    public synchronized void enviarMensajePrivado(String destinatario, Mensaje mensaje) {
        ClienteHandler cliente = mapaClientes.get(destinatario);
        if (cliente != null) {
            cliente.enviarMensaje(mensaje);
        } else {
            ClienteHandler emisor = mapaClientes.get(mensaje.getEmisor());
            if (emisor != null) {
                emisor.enviarMensaje(new Mensaje("error", "El jugador no está conectado.", "Servidor"));
            }
        }
    }


    public synchronized void enviarStatsJugador(String nickname, ClienteHandler solicitante) {
        ClienteHandler cliente = mapaClientes.get(nickname);
        if (cliente != null) {
            Jugador jugador = cliente.getJugador();
            String stats = "Nickname: " + jugador.getNickname() + "\n"
                    + "Wins: " + jugador.getWins() + "\n"
                    + "Loses: " + jugador.getLoses() + "\n"
                    + "Attacks: " + jugador.getAttacks() + "\n"
                    + "Success: " + jugador.getSuccess() + "\n"
                    + "Failed: " + jugador.getFailed() + "\n"
                    + "Giveup: " + jugador.getGiveup();
            Mensaje mensaje = new Mensaje("statsJugador", stats, "Servidor");
            solicitante.enviarMensaje(mensaje);
        } else {
            solicitante.enviarMensaje(new Mensaje("error", "El jugador no está conectado.", "Servidor"));
        }
    }

	public synchronized void enviarDesafio(String destinatario, ClienteHandler retador) {
		ClienteHandler retado = mapaClientes.get(destinatario);
		if (retado != null && retado.getEstado().equals("disponible")) {
			retador.estado = "esperandoRespuesta";
			Mensaje desafio = new Mensaje("desafio", "", retador.getIdJugador());
			retado.enviarMensaje(desafio);
		} else {
			retador.enviarMensaje(new Mensaje("error", "El jugador no está disponible.", "Servidor"));
		}
	}
	
	public synchronized void actualizarRanking() {
		List<Jugador> jugadores = new ArrayList<>();
		for (ClienteHandler handler : clientes) {
			jugadores.add(handler.getJugador());
		}
		jugadores.sort((j1, j2) -> Integer.compare(j2.getPuntuacion().calcularPuntuacionTotal(), j1.getPuntuacion().calcularPuntuacionTotal()));
		// Puedes enviar el ranking actualizado a todos los clientes si lo deseas
	}

	

    public List<ClienteHandler> getClientes() {
        return clientes;
    }

    public ClienteHandler getClienteHandler(String idJugador) {
        return mapaClientes.get(idJugador);
    }
}
