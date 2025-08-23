package com.mycompany.mariopoorty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Cliente {
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private JFrame frame;
    private JPanel panelPersonajes, panelChat, panelTablero, panelBotones;
    private JTextArea areaChat;
    private JTextField campoMensaje;
    private JButton botonTirarDados;
    private String nombreJugador;
    private String personajeSeleccionado;
    private Map<String, Integer> posicionesJugadores;
    private JLabel[] casillas;
    private Map<String, JLabel> iconosJugadores;
    private boolean esMiTurno = false;

    public Cliente(String host) throws IOException {
        socket = new Socket(host, 12345);
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        salida = new PrintWriter(socket.getOutputStream(), true);

        posicionesJugadores = new HashMap<>();
        iconosJugadores = new HashMap<>();
        
        for (int i = 1; i <= 6; i++) {
            posicionesJugadores.put("Jugador" + i, 1);
        }
        construirGUI();
        new Thread(new EscuchaServidor()).start();
    }

    private void construirGUI() {
        frame = new JFrame("Juego Similar a Mario Party");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLayout(new BorderLayout());

        // Panel de Selección de Personajes
        panelPersonajes = new JPanel();
        panelPersonajes.setLayout(new GridLayout(2, 5));
        panelPersonajes.setBorder(BorderFactory.createTitledBorder("Selecciona tu Personaje"));

        String[] personajes = {"MarioChar", "LuigiChar", "ToadChar", "BowserChar", "MagikoopaChar",
                "YoshiChar", "PeachChar", "DaisyChar", "RosalinaChar", "GoombaChar"};

        for (String personaje : personajes) {
            JButton boton = new JButton(new ImageIcon("imagenes/" + personaje + ".png"));
            boton.setActionCommand(personaje);
            boton.addActionListener(e -> {
                if (personajeSeleccionado != null) {
                    JOptionPane.showMessageDialog(frame, "Ya has seleccionado tu personaje.");
                } else {
                    personajeSeleccionado = e.getActionCommand();
                    salida.println("SELECCIONAR:" + personajeSeleccionado);
                    boton.setEnabled(false);
                }
            });
            panelPersonajes.add(boton);
        }

        // Panel de Chat
        panelChat = new JPanel();
        panelChat.setLayout(new BorderLayout());
        panelChat.setBorder(BorderFactory.createTitledBorder("Chat"));

        areaChat = new JTextArea(10, 30);
        areaChat.setEditable(false);
        JScrollPane scrollChat = new JScrollPane(areaChat);
        campoMensaje = new JTextField();
        campoMensaje.addActionListener(e -> {
            String mensaje = campoMensaje.getText().trim();
            if (!mensaje.isEmpty()) {
                salida.println("MENSAJE:" + mensaje);
                campoMensaje.setText("");
            }
        });

        panelChat.add(scrollChat, BorderLayout.CENTER);
        panelChat.add(campoMensaje, BorderLayout.SOUTH);

        // Panel de Botones (Incluye el botón "Tirar Dados")
        panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout());
        botonTirarDados = new JButton("Tirar Dados");
        botonTirarDados.setEnabled(false); // Deshabilitado por defecto
        botonTirarDados.addActionListener(e -> {
            tirarDados();
        });
        panelBotones.add(botonTirarDados);

        // Añadir paneles al frame
        frame.getContentPane().add(panelPersonajes, BorderLayout.NORTH);
        frame.getContentPane().add(panelChat, BorderLayout.SOUTH);
        frame.getContentPane().add(panelBotones, BorderLayout.EAST);
        frame.setVisible(true);
    }

    private void mostrarTablero() {
        frame.getContentPane().removeAll();
        panelTablero = new JPanel();
        panelTablero.setLayout(new GridLayout(5, 6));
        panelTablero.setBorder(BorderFactory.createTitledBorder("Tablero de Juego"));

        casillas = new JLabel[26];
        for (int i = 0; i < 26; i++) {
            JLabel casilla = new JLabel(new ImageIcon("imagenes/casillaNeutral.png"));
            casilla.setHorizontalAlignment(SwingConstants.CENTER);
            casilla.setVerticalAlignment(SwingConstants.CENTER);
            casillas[i] = casilla;
            panelTablero.add(casilla);
        }

        // Añadir los iconos de los personajes en la posición inicial
        for (Map.Entry<String, JLabel> entry : iconosJugadores.entrySet()) {
            String jugador = entry.getKey();
            String personaje = entry.getValue().getToolTipText();
            JLabel icono = new JLabel(new ImageIcon("imagenes/" + personaje + ".png"));
            icono.setToolTipText(personaje);
            casillas[0].add(icono);
            iconosJugadores.put(jugador, icono);
        }

        // Panel de Chat
        panelChat.setBorder(BorderFactory.createTitledBorder("Chat"));

        // Panel de Botones
        panelBotones.setBorder(BorderFactory.createTitledBorder("Acciones"));
        panelBotones.add(botonTirarDados);

        // Añadir paneles al frame
        frame.getContentPane().add(panelTablero, BorderLayout.CENTER);
        frame.getContentPane().add(panelChat, BorderLayout.SOUTH);
        frame.getContentPane().add(panelBotones, BorderLayout.EAST);
        frame.revalidate();
        frame.repaint();
    }

    private void tirarDados() {
        if (!esMiTurno) {
            JOptionPane.showMessageDialog(frame, "No es tu turno.");
            return;
        }

        int dado1 = (int) (Math.random() * 6) + 1;
        int dado2 = (int) (Math.random() * 6) + 1;
        JOptionPane.showMessageDialog(frame, "Has sacado: " + dado1 + " y " + dado2);

        if (dado1 == 1 || dado2 == 1) {
            JOptionPane.showMessageDialog(frame, "¡Mala suerte de dado! Pierdes el turno.");
            salida.println("RESULTADO_DADOS:" + 0);
        } else {
            int movimiento = dado1 + dado2;
            salida.println("RESULTADO_DADOS:" + movimiento);
        }

        botonTirarDados.setEnabled(false);
        esMiTurno = false;
    }

    class EscuchaServidor implements Runnable {
        @Override
        public void run() {
            try {
                String mensaje;
                while ((mensaje = entrada.readLine()) != null) {
                    if (mensaje.startsWith("NOMBRE_JUGADOR:")) {
                        nombreJugador = mensaje.substring(15);
                        areaChat.append("Tu nombre de jugador es: " + nombreJugador + "\n");
                    } else if (mensaje.startsWith("DESHABILITAR:")) {
                        String personaje = mensaje.substring(13);
                        for (Component comp : panelPersonajes.getComponents()) {
                            JButton boton = (JButton) comp;
                            if (boton.getActionCommand().equals(personaje)) {
                                boton.setEnabled(false);
                            }
                        }
                    } else if (mensaje.startsWith("MENSAJE:")) {
                        areaChat.append(mensaje.substring(8) + "\n");
                    } else if (mensaje.startsWith("ELEGIR_NUMERO:")) {
                        String numeroElegido = JOptionPane.showInputDialog(frame, "Elige un número del 1 al 1000:");
                        while (!esNumeroValido(numeroElegido)) {
                            numeroElegido = JOptionPane.showInputDialog(frame, "Número inválido. Elige un número del 1 al 1000:");
                        }
                        salida.println("NUMERO_ELEGIDO:" + numeroElegido);
                    } else if (mensaje.startsWith("ORDEN_TURNO:")) {
                        int posicion = Integer.parseInt(mensaje.substring(12));
                        areaChat.append("Tu posición en el orden de turnos es: " + posicion + "\n");
                    } else if (mensaje.equals("INICIAR_JUEGO")) {
                        areaChat.append("El juego va a comenzar.\n");
                    } else if (mensaje.startsWith("TABLERO:")) {
                        String[] tiposCasillas = mensaje.substring(8).split(",");
                        mostrarTablero();
                        actualizarCasillas(tiposCasillas);
                    } else if (mensaje.startsWith("JUGADORES:")) {
                        String[] jugadores = mensaje.substring(10).split(",");
                        for (String jugador : jugadores) {
                            posicionesJugadores.put(jugador, 0);
                        }
                    } else if (mensaje.startsWith("PERSONAJES:")) {
                        String[] personajes = mensaje.substring(11).split(",");
                        int index = 0;
                        for (String jugador : posicionesJugadores.keySet()) {
                            String personaje = personajes[index];
                            JLabel icono = new JLabel(new ImageIcon("imagenes/" + personaje + ".png"));
                            icono.setToolTipText(personaje);
                            iconosJugadores.put(jugador, icono);
                            index++;
                        }
                    } else if (mensaje.startsWith("TU_TURNO:")) {
                        String modo = mensaje.substring(9);
                        if (modo.equals("GUESS_NUMBER")) {
                            JOptionPane.showMessageDialog(frame, "Es tu turno: Adivinar el Número.");
                        } else if (modo.equals("DICE_ROLL")) {
                            JOptionPane.showMessageDialog(frame, "Es tu turno: Lanzar Dados.");
                        }
                        esMiTurno = true;
                        botonTirarDados.setEnabled(true);
                    } else if (mensaje.startsWith("ACTUALIZAR_POSICION:")) {
                        int nuevaPosicion = Integer.parseInt(recortarSiEmpiezaConDosPuntos(mensaje.substring(19)));
                        actualizarPosicionJugador(nombreJugador, nuevaPosicion);
                    } else if (mensaje.startsWith("ACTUALIZAR_JUGADOR:")) {
                        String[] partes = mensaje.substring(18).split(":");
                        System.out.println(partes);
                        String jugador = partes[0];
                        System.out.println(jugador);
                        int posicion = posicionesJugadores.get(jugador) + 3;
                        actualizarPosicionJugador(jugador, posicion);
                    } else if (mensaje.startsWith("MINIJUEGO:")) {
                        String minijuego = mensaje.substring(9);
                        int opcion = JOptionPane.showConfirmDialog(frame, "¿Has ganado el minijuego " + minijuego + "?", "Minijuego", JOptionPane.YES_NO_OPTION);
                        if (opcion == JOptionPane.YES_OPTION) {
                            salida.println("MINIJUEGO_RESULTADO:GANADO");
                        } else {
                            salida.println("MINIJUEGO_RESULTADO:PERDIDO");
                        }
                    } else if (mensaje.equals("ESTRELLA")) {
                        int opcion = JOptionPane.showConfirmDialog(frame, "Has caído en una estrella. ¿Quieres volver a lanzar los dados?", "Estrella", JOptionPane.YES_NO_OPTION);
                        if (opcion == JOptionPane.YES_OPTION) {
                            botonTirarDados.setEnabled(true);
                            esMiTurno = true;
                        } else {
                            botonTirarDados.setEnabled(false);
                            esMiTurno = false;
                            salida.println("RESULTADO_DADOS:0");
                        }
                    } else if (mensaje.equals("COLA")) {
                        String opcion = JOptionPane.showInputDialog(frame, "Has caído en una cola. Ingresa un número entre -3 y 3 para avanzar o retroceder:");
                        while (!esOpcionValida(opcion)) {
                            opcion = JOptionPane.showInputDialog(frame, "Opción inválida. Ingresa un número entre -3 y 3 para avanzar o retroceder:");
                        }
                        salida.println("COLA_SELECCION:" + opcion);
                    } else if (mensaje.startsWith("VICTORIA:")) {
                        String ganador = mensaje.substring(8);
                        JOptionPane.showMessageDialog(frame, ganador + " ha ganado el juego. ¡Felicidades!");
                        System.exit(0);
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
    
        private boolean esNumeroValido(String numeroElegido) {
            try {
                int num = Integer.parseInt(numeroElegido);
                return num >= 1 && num <= 1000;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        private boolean esOpcionValida(String opcion) {
            try {
                int num = Integer.parseInt(opcion);
                return num >= -3 && num <= 3;
            } catch (NumberFormatException e) {
                return false;
            }
        }


        private void actualizarCasillas(String[] tiposCasillas) {
            for (int i = 0; i < tiposCasillas.length; i++) {
                casillas[i].setIcon(new ImageIcon("imagenes/" + tiposCasillas[i] + ".png"));
            }
        }

        private void actualizarPosicionJugador(String jugador, int nuevaPosicion) {
            if (!posicionesJugadores.containsKey(jugador)) {
                posicionesJugadores.put(jugador, nuevaPosicion);
                return;
            }

            int posicionAnterior = posicionesJugadores.get(jugador);
            JLabel iconoJugador = iconosJugadores.get(jugador);

            // Remover el icono del jugador de la posición anterior
            casillas[posicionAnterior].remove(iconoJugador);
            casillas[posicionAnterior].revalidate();
            casillas[posicionAnterior].repaint();

            // Añadir el icono del jugador a la nueva posición
            casillas[nuevaPosicion].add(iconoJugador);
            casillas[nuevaPosicion].revalidate();
            casillas[nuevaPosicion].repaint();

            posicionesJugadores.put(jugador, nuevaPosicion);
        }
    }
        public static void main(String[] args) {
            try {
                String host = JOptionPane.showInputDialog("Ingrese la dirección IP del servidor:");
                if (host == null || host.trim().isEmpty()) {
                    host = "localhost"; // Valor por defecto
                }
                new Cliente(host);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }