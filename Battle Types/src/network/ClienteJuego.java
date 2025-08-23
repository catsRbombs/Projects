package network;

import java.io.*;
import java.net.Socket;
import juegogui.InterfazJuego;
import model.Jugador;

public class ClienteJuego {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Jugador jugador;
    private InterfazJuego interfazJuego;

    public ClienteJuego(String host, int puerto, Jugador jugador, InterfazJuego interfazJuego) {
        this.jugador = jugador;
        this.interfazJuego = interfazJuego;
        try {
            socket = new Socket(host, puerto);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // Enviar el ID del jugador al servidor
            out.writeObject(jugador.getNickname());
            out.flush();

            // Iniciar un hilo para escuchar mensajes del servidor
            new Thread(this::escucharMensajes).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void escucharMensajes() {
        try {
            Object mensaje;
            while ((mensaje = in.readObject()) != null) {
                // Procesar el mensaje recibido del servidor
                procesarMensaje(mensaje);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Desconectado del servidor");
            cerrarConexion();
        }
    }

    private void procesarMensaje(Object mensajeObj) {
        if (mensajeObj instanceof Mensaje) {
            Mensaje mensaje = (Mensaje) mensajeObj;
            // Actualizar la interfaz según el tipo de mensaje
            interfazJuego.procesarMensajeServidor(mensaje);
        }
    }

    
    public void enviarMensaje(Mensaje mensaje) {
        try {
            out.writeObject(mensaje);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cerrarConexion() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public Jugador getJugador() {
		return jugador;
	}

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}
	
	
}
