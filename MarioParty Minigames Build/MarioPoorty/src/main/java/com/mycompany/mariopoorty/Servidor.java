package com.mycompany.mariopoorty;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Servidor {
    public int joinedCount = 0;
    private int numJugadores;
    List<ManejadorCliente> clientes;
    private ServerSocket servidor;
    private int puerto = 12345;
    private List<String> personajesDisponibles = new ArrayList<>(Arrays.asList(
            "MarioChar", "LuigiChar", "ToadChar", "BowserChar", "MagikoopaChar",
            "YoshiChar", "PeachChar", "DaisyChar", "RosalinaChar", "GoombaChar"));
    private Map<ManejadorCliente, String> seleccionPersonajes;
    private List<String> ordenJugadores;
    Map<String, Integer> posicionesJugadores;
    String[] tablero;
    private Random random;

    // Modo de juego: "GUESS_NUMBER" o "DICE_ROLL"
    private String modoSeleccionado = null;

    public void addPlayerCount() {
        joinedCount++;
    }

    public int getPlayerCount() {
        return joinedCount;
    }

    public Servidor() {
        clientes = new ArrayList<>();
        seleccionPersonajes = new ConcurrentHashMap<>();
        ordenJugadores = new ArrayList<>();
        posicionesJugadores = new HashMap<>();
        random = new Random();
    }

    public void iniciar() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la cantidad de jugadores (2-6): ");
        numJugadores = scanner.nextInt();
        while (numJugadores < 2 || numJugadores > 6) {
            System.out.print("Número inválido. Ingrese un número entre 2 y 6: ");
            numJugadores = scanner.nextInt();
        }

        servidor = new ServerSocket(puerto);
        System.out.println("Servidor iniciado en el puerto " + puerto);
        System.out.println("Esperando a " + numJugadores + " jugadores...");

        // Aceptar conexiones de clientes
        while (clientes.size() < numJugadores) {
            Socket socket = servidor.accept();
            ManejadorCliente manejador = new ManejadorCliente(socket, this);
            clientes.add(manejador);
            manejador.start();
            System.out.println("Jugador conectado: " + socket.getInetAddress());
        }

        // Esperar a que todos seleccionen su personaje
        esperarSeleccionPersonajes();

    }

    public void notificarATodos(String mensaje) {
        for (ManejadorCliente cliente : clientes) {
            cliente.enviarMensaje(mensaje);
        }
    }

    public void esperarSeleccionPersonajes() {
        System.out.println("Esperando a que todos los jugadores seleccionen su personaje...");
        while (seleccionPersonajes.size() < numJugadores) {
            try {
                Thread.sleep(100); // Esperar 100ms antes de verificar nuevamente
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Todos los jugadores han seleccionado su personaje.");
                // Decidir el orden de los turnos
        decidirOrdenTurnos();

        // Iniciar el juego
        iniciarJuego();
        
    }

    private void decidirOrdenTurnos() {


            decidirOrdenPorDiceRoll();
    }

    private void decidirOrdenPorGuessNumber() {
        Map<ManejadorCliente, Integer> elecciones = new HashMap<>();
        int numeroGenerado = random.nextInt(1000) + 1;
        System.out.println("Número generado para decidir el orden: " + numeroGenerado);

        // Solicitar a cada jugador que elija un número
        for (ManejadorCliente cliente : clientes) {
            cliente.enviarMensaje("ELEGIR_NUMERO:" + numeroGenerado);
        }

        // Esperar a que todos elijan su número
        while (elecciones.size() + 1 < numJugadores) {
            for (ManejadorCliente cliente : clientes) {
                if (cliente.getNumeroElegido() != -1 && !elecciones.containsKey(cliente)) {
                    elecciones.put(cliente, cliente.getNumeroElegido());
                    System.out.println(cliente.getNombreJugador() + " ha elegido el número " + cliente.getNumeroElegido());
                }
            }
            try {
                Thread.sleep(100); // Evitar espera activa excesiva
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Calcular la distancia de cada elección al número generado
        Map<ManejadorCliente, Integer> distancias = new HashMap<>();
        for (Map.Entry<ManejadorCliente, Integer> entry : elecciones.entrySet()) {
            int distancia = Math.abs(entry.getValue() - numeroGenerado);
            distancias.put(entry.getKey(), distancia);
        }

        // Ordenar los jugadores según la distancia
        distancias.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(entry -> ordenJugadores.add(entry.getKey().getNombreJugador()));

        System.out.println("Orden de turnos (Guess Number):");
        for (String nombre : ordenJugadores) {
            System.out.println(nombre);
        }

        // Notificar a los jugadores su posición en el orden
        for (ManejadorCliente cliente : clientes) {
            int posicion = ordenJugadores.indexOf(cliente.getNombreJugador()) + 1;
            cliente.enviarMensaje("ORDEN_TURNO:" + posicion);
        }
    }

    private void decidirOrdenPorDiceRoll() {
        Map<ManejadorCliente, Integer> resultados = new HashMap<>();

        // Generar dos dados para cada jugador
        for (ManejadorCliente cliente : clientes) {
            int dado1 = random.nextInt(6) + 1;
            int dado2 = random.nextInt(6) + 1;
            int suma = dado1 + dado2;
            resultados.put(cliente, suma);
            cliente.enviarMensaje("RESULTADO_DADOS:" + dado1 + "," + dado2);
            System.out.println(cliente.getNombreJugador() + " ha sacado: " + dado1 + " y " + dado2 + " (Total: " + suma + ")");
        }

        // Esperar un momento para que los clientes muestren los dados
        try {
            Thread.sleep(2000); // Esperar 2 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Ordenar los jugadores según la suma de los dados (de mayor a menor)
        resultados.entrySet().stream()
                .sorted((e1, e2) -> {
                    int cmp = e2.getValue().compareTo(e1.getValue()); // Descendente
                    if (cmp == 0) {
                        // Si hay empate, usar el orden de llegada
                        return Integer.compare(clientes.indexOf(e1.getKey()), clientes.indexOf(e2.getKey()));
                    }
                    return cmp;
                })
                .forEachOrdered(entry -> ordenJugadores.add(entry.getKey().getNombreJugador()));

        System.out.println("Orden de turnos (Dice Roll):");
        for (String nombre : ordenJugadores) {
            System.out.println(nombre);
        }

        // Notificar a los jugadores su posición en el orden
        for (ManejadorCliente cliente : clientes) {
            int posicion = ordenJugadores.indexOf(cliente.getNombreJugador()) + 1;
            cliente.enviarMensaje("ORDEN_TURNO:" + posicion);
        }
    }

    private void iniciarJuego() {
        // Generar el tablero
        generarTablero();

        // Enviar el tablero a los clientes
        for (ManejadorCliente cliente : clientes) {
            cliente.enviarMensaje("INICIAR_JUEGO");
            cliente.enviarMensaje("TABLERO:" + String.join(",", tablero));
            cliente.enviarMensaje("JUGADORES:" + String.join(",", ordenJugadores));
            cliente.enviarMensaje("PERSONAJES:" + obtenerPersonajesSeleccionados());
        }

        // Inicializar posiciones de jugadores
        for (String jugador : ordenJugadores) {
            posicionesJugadores.put(jugador, 0); // Todos empiezan en la casilla 0
        }

        // Iniciar los turnos
        iniciarTurnos();
    }

    private void generarTablero() {
        tablero = new String[26];
        // Inicializar todas las casillas como "casillaNeutral"
        Arrays.fill(tablero, "casillaNeutral");

        // Distribuir las casillas especiales aleatoriamente
        List<String> casillasEspeciales = new ArrayList<>(Arrays.asList(
                "carcel", "tubo1", "tubo2", "tubo3", "estrella", "florDeFuego",
                "hielo", "cola", "casillaWin));"));

        // Añadir 9 casillas de juego
        casillasEspeciales.addAll(Arrays.asList("gato", "sopaDeLetras", "memoryPath",
                "superBrosMemory", "catchTheCat", "bomberMario",
                "guessWho", "collectTheCoins", "marioCards"));

        // Colocar la casilla de victoria al final
        tablero[25] = "casillaWin";

        // Distribuir el resto de casillas especiales
        for (String casillaEspecial : casillasEspeciales) {
            int posicion;
            do {
                posicion = random.nextInt(25); // No sobreescribir la casilla de victoria
            } while (!tablero[posicion].equals("casillaNeutral"));
            tablero[posicion] = casillaEspecial;
        }

        System.out.println("Tablero generado:");
        for (int i = 0; i < tablero.length; i++) {
            System.out.println("Casilla " + i + ": " + tablero[i]);
        }
    }

    private String obtenerPersonajesSeleccionados() {
        List<String> personajes = new ArrayList<>();
        for (String personaje : seleccionPersonajes.values()) {
            personajes.add(personaje);
        }
        return String.join(",", personajes);
    }

    private void iniciarTurnos() {
        int indiceTurno = 0;
        boolean juegoTerminado = false;

        while (!juegoTerminado) {
            String jugadorActual = ordenJugadores.get(indiceTurno);
            ManejadorCliente clienteActual = obtenerClientePorNombre(jugadorActual);

            // Verificar si el jugador tiene turnos perdidos
            if (clienteActual.getTurnosPerdidos() > 0) {
                clienteActual.decrementarTurnosPerdidos();
                clienteActual.enviarMensaje("MENSAJE:Has perdido un turno. Turnos restantes: " + clienteActual.getTurnosPerdidos());
            } else {
                // Enviar mensaje para lanzar dados o elegir números según el modo
                clienteActual.enviarMensaje("TU_TURNO:" + modoSeleccionado);
                esperarRespuestaTurno(clienteActual);

                // Verificar si ha llegado a la casilla de victoria
                if (posicionesJugadores.get(jugadorActual) >= 25) {
                    notificarVictoria(jugadorActual);
                    juegoTerminado = true;
                    break;
                }
            }

            // Pasar al siguiente jugador
            indiceTurno = (indiceTurno + 1) % numJugadores;
        }
    }

    private ManejadorCliente obtenerClientePorNombre(String nombre) {
        for (ManejadorCliente cliente : clientes) {
            if (cliente.getNombreJugador().equals(nombre)) {
                return cliente;
            }
        }
        return null;
    }

    private void esperarRespuestaTurno(ManejadorCliente cliente) {
        while (!cliente.isTurnoProcesado()) {
            try {
                Thread.sleep(100); // Esperar 100ms antes de verificar nuevamente
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        cliente.setTurnoProcesado(false);
    }

    private void notificarVictoria(String jugadorGanador) {
        for (ManejadorCliente cliente : clientes) {
            cliente.enviarMensaje("VICTORIA:" + jugadorGanador);
        }
        System.out.println("El jugador " + jugadorGanador + " ha ganado el juego.");
    }

    public synchronized void registrarSeleccionPersonaje(ManejadorCliente cliente, String personaje) {
        seleccionPersonajes.put(cliente, personaje);
        personajesDisponibles.remove(personaje);
        // Notificar a todos los clientes para deshabilitar el botón y actualizar el chat
        for (ManejadorCliente c : clientes) {
            c.enviarMensaje("DESHABILITAR:" + personaje);
            c.enviarMensaje("MENSAJE:" + cliente.getNombreJugador() + " ha seleccionado a " + personaje);
        }
    }

    public synchronized boolean personajeDisponible(String personaje) {
        return personajesDisponibles.contains(personaje);
    }

    public synchronized void registrarNumeroElegido(ManejadorCliente cliente, int numero) {
        cliente.setNumeroElegido(numero);
    }

    // Manejar la selección del modo desde el Jugador1
    public synchronized void manejarModoSeleccionado(ManejadorCliente cliente, String modo) {
        if (cliente.equals(clientes.get(0)) && (modo.equals("GUESS_NUMBER") || modo.equals("DICE_ROLL"))) {
            this.modoSeleccionado = modo;
            registrarModoSeleccionado(modo);
        } else {
            cliente.enviarMensaje("MENSAJE:Modo inválido o no tienes permiso para seleccionar el modo.");
        }
    }

    private void registrarModoSeleccionado(String modo) {
        notificarATodos("MENSAJE:El modo de juego ha sido seleccionado: " + (modo.equals("GUESS_NUMBER") ? "Adivinar el Número" : "Lanzamiento de Dados"));
    }

    // Manejar la selección de opciones en la casilla de cola
    public synchronized void procesarColaSeleccion(ManejadorCliente cliente, int opcion) {
        String nombreJugador = cliente.getNombreJugador();
        int posicionActual = posicionesJugadores.get(nombreJugador);
        int nuevaPosicion = posicionActual + opcion;

        if (nuevaPosicion < 0) {
            nuevaPosicion = 0;
        } else if (nuevaPosicion >= 25) {
            nuevaPosicion = 25;
        }

        posicionesJugadores.put(nombreJugador, nuevaPosicion);
        cliente.enviarMensaje("ACTUALIZAR_POSICION:" + nuevaPosicion);
        manejarCasilla(cliente, tablero[nuevaPosicion]);

        // Notificar a todos los clientes la nueva posición
        for (ManejadorCliente c : clientes) {
            c.enviarMensaje("ACTUALIZAR_JUGADOR:" + nombreJugador + ":" + posicionesJugadores.get(nombreJugador));
        }
    }

    private void manejarCasilla(ManejadorCliente cliente, String tipoCasilla) {
    switch (tipoCasilla) {
        case "casillaNeutral":
            enviarMensajeCliente(cliente, "MENSAJE", "Has caído en una casilla neutral.");
            break;
        case "carcel":
            cliente.setTurnosPerdidos(2);
            enviarMensajeCliente(cliente, "MENSAJE", "Has caído en la cárcel y perderás dos turnos.");
            break;
        case "tubo1":
            moverATubo(cliente, "tubo2");
            break;
        case "tubo2":
            moverATubo(cliente, "tubo3");
            break;
        case "tubo3":
            moverATubo(cliente, "tubo1");
            break;
        case "estrella":
            enviarMensajeCliente(cliente, "ESTRELLA");
            break;
        case "florDeFuego":
            enviarJugadorAlInicio(cliente);
            break;
        case "hielo":
            cliente.setTurnosPerdidos(2);
            enviarMensajeCliente(cliente, "MENSAJE", "Has caído en hielo y perderás dos turnos.");
            break;
        case "cola":
            enviarMensajeCliente(cliente, "COLA");
            break;
        case "casillaWin":
            // Ya manejado en procesarMovimiento
            break;
        default:
            enviarMensajeCliente(cliente, "MENSAJE", "Has caído en una casilla desconocida.");
            break;
    }
}

    private void moverATubo(ManejadorCliente cliente, String tuboDestino) {
        int posicionDestino = -1;
        for (int i = 0; i < tablero.length; i++) {
            if (tablero[i].equals(tuboDestino)) {
                posicionDestino = i;
                break;
            }
        }
        if (posicionDestino != -1) {
            posicionesJugadores.put(cliente.getNombreJugador(), posicionDestino);
            cliente.enviarMensaje("MENSAJE:Has sido transportado al " + tuboDestino + ".");
            cliente.enviarMensaje("ACTUALIZAR_POSICION:" + posicionDestino);
        }
    }

    private void enviarJugadorAlInicio(ManejadorCliente cliente) {
        // Enviar al primer jugador (que no sea el actual) al inicio
        for (String jugador : ordenJugadores) {
            if (!jugador.equals(cliente.getNombreJugador())) {
                posicionesJugadores.put(jugador, 0);
                ManejadorCliente c = obtenerClientePorNombre(jugador);
                c.enviarMensaje("MENSAJE:Has sido enviado al inicio por la Flor de Fuego.");
                c.enviarMensaje("ACTUALIZAR_POSICION:0");
                break;
            }
        }
    }
public synchronized void procesarMovimiento(ManejadorCliente cliente, int movimiento) {
    String nombreJugador = cliente.getNombreJugador();
    int posicionActual = posicionesJugadores.getOrDefault(nombreJugador, 0);
    int nuevaPosicion = posicionActual + movimiento;

    // Validar nueva posición
    if (nuevaPosicion < 0) {
        nuevaPosicion = 0;
    } else if (nuevaPosicion >= tablero.length) {
        nuevaPosicion = tablero.length - 1;
    }

    posicionesJugadores.put(nombreJugador, nuevaPosicion);
    String tipoCasilla = tablero[nuevaPosicion];

    // Notificar al jugador sobre su nueva posición
    enviarMensajeCliente(cliente, "ACTUALIZAR_POSICION", String.valueOf(nuevaPosicion));

    // Manejar la casilla donde ha caído el jugador
    manejarCasilla(cliente, tipoCasilla);

    // Verificar si el jugador ha llegado a la casilla de victoria
    if (nuevaPosicion >= tablero.length - 1) {
        notificarVictoria(nombreJugador);
        return; // Finalizar el juego
    }

    // Notificar a todos los jugadores sobre la nueva posición del jugador
    notificarTodosJugadores("ACTUALIZAR_JUGADOR", nombreJugador, String.valueOf(nuevaPosicion));
}

    /**
     * Método auxiliar para enviar mensajes con un comando y múltiples parámetros sin incluir ':' en los parámetros.
     */
    private void enviarMensajeCliente(ManejadorCliente cliente, String comando, String... parametros) {
        String mensaje = comando;
        for (String parametro : parametros) {
            mensaje += ":" + parametro;
        }
        cliente.enviarMensaje(mensaje);
    }

    /**
     * Método auxiliar para notificar a todos los jugadores con un comando y múltiples parámetros.
     */
    private void notificarTodosJugadores(String comando, String... parametros) {
        String mensaje = comando;
        for (String parametro : parametros) {
            mensaje += ":" + parametro;
        }
        notificarATodos(mensaje);
    }
    public static void main(String[] args) {
        try {
            Servidor servidor = new Servidor();
            servidor.iniciar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

