package com.mycompany.mariopoorty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ManejadorCliente extends Thread {
    private Socket socket;
    private Servidor servidor;
    private BufferedReader entrada;
    private PrintWriter salida;
    private String nombreJugador;
    private String personajeSeleccionado;
    private int numeroElegido = -1;
    private int turnosPerdidos = 0;
    private boolean turnoProcesado = false;

    public ManejadorCliente(Socket socket, Servidor servidor) {
        this.socket = socket;
        this.servidor = servidor;
        try {
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);
            servidor.addPlayerCount();
            nombreJugador = "Jugador" + servidor.getPlayerCount();
            enviarMensaje("NOMBRE_JUGADOR:" + nombreJugador);
            servidor.notificarATodos("MENSAJE:" + nombreJugador + " se ha unido al juego.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarMensaje(String mensaje) {
        salida.println(mensaje);
    }

    public void run() {
        try {
            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                if (mensaje.startsWith("SELECCIONAR:")) {
                    String personaje = mensaje.substring(12);
                    if (servidor.personajeDisponible(personaje)) {
                        personajeSeleccionado = personaje;
                        servidor.registrarSeleccionPersonaje(this, personaje);
                    } else {
                        enviarMensaje("MENSAJE:El personaje " + personaje + " ya ha sido seleccionado.");
                    }
                } else if (mensaje.startsWith("MENSAJE:")) {
                    String textoMensaje = mensaje.substring(8);
                    servidor.notificarATodos("MENSAJE:" + nombreJugador + ": " + textoMensaje);
                } else if (mensaje.startsWith("NUMERO_ELEGIDO:")) {
                    int numero = Integer.parseInt(mensaje.substring(15));
                    servidor.registrarNumeroElegido(this, numero);
                } else if (mensaje.startsWith("RESULTADO_DADOS:")) {
                    int movimiento = Integer.parseInt(recortarSiEmpiezaConDosPuntos(mensaje.substring(16)));
                    servidor.procesarMovimiento(this, movimiento);
                    turnoProcesado = true;
                } else if (mensaje.startsWith("MINIJUEGO_RESULTADO:")) {
                    String resultado = mensaje.substring(20);
                    if (resultado.equals("GANADO")) {
                        enviarMensaje("MENSAJE:¡Felicidades! Has ganado el minijuego.");
                    } else {
                        turnosPerdidos = 1;
                        enviarMensaje("MENSAJE:Perdiste el minijuego y perderás tu próximo turno.");
                    }
                } else if (mensaje.startsWith("COLA_SELECCION:")) {
                    int opcion = Integer.parseInt(mensaje.substring(15));
                    servidor.procesarColaSeleccion(this, opcion);
                    turnoProcesado = true;
                } else if (mensaje.startsWith("MODO:")) {
                    String modo = mensaje.substring(5);
                    servidor.manejarModoSeleccionado(this, modo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String recortarSiEmpiezaConDosPuntos(String cadena) {
        if (cadena == null || cadena.isEmpty()) {
            return cadena; // Retorna la cadena original si es nula o vacía
        }
        // Verifica si empieza con ':'
        if (cadena.startsWith(":")) {
            return cadena.substring(1); // Recorta el primer carácter
        }
        return cadena; // Retorna la cadena sin modificaciones
    }
    
    public String getNombreJugador() {
        return nombreJugador;
    }

    public int getNumeroElegido() {
        return numeroElegido;
    }

    public void setNumeroElegido(int numeroElegido) {
        this.numeroElegido = numeroElegido;
    }

    public int getTurnosPerdidos() {
        return turnosPerdidos;
    }

    public void setTurnosPerdidos(int turnosPerdidos) {
        this.turnosPerdidos = turnosPerdidos;
    }

    public void decrementarTurnosPerdidos() {
        if (turnosPerdidos > 0) {
            turnosPerdidos--;
        }
    }

    public boolean isTurnoProcesado() {
        return turnoProcesado;
    }

    public void setTurnoProcesado(boolean turnoProcesado) {
        this.turnoProcesado = turnoProcesado;
    }
}