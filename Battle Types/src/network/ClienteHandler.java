package network;

import factory.GuerreroFactoryImpl;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import model.Arma;
import model.Guerrero;
import model.Jugador;
import model.Tipo;
import utils.Archivismo;

public class ClienteHandler extends Thread {
    private Socket socket;
    private ServidorJuego servidor;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String idJugador; // Identificador único del jugador
    public Jugador jugador;
    public String estado; // disponible, peleando, esperandoRespuesta
    private ClienteHandler oponente;
	public Archivismo archivismo;

	public ClienteHandler(Socket socket, ServidorJuego servidor) {
		this.socket = socket;
		this.servidor = servidor;
		this.estado = "disponible";
		this.archivismo = new Archivismo();
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			// Leer el ID del jugador al conectarse
			idJugador = (String) in.readObject();
			System.out.println("Jugador conectado: " + idJugador);
			// Cargar objeto Jugador desde archivo
			this.jugador = cargarJugadorDesdeArchivo(idJugador);
			if (this.jugador != null) {
				servidor.agregarJugador(idJugador, this);
			} else {
				System.out.println("Error cargando jugador: " + idJugador);
				cerrarConexion();
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	private void procesarCombatCommands(Mensaje mensaje) {
		String content = mensaje.getContenido();
		switch(content.toLowerCase()){
			case "surrender":
				procesarSurrender();
				break;
			case "draw":
				procesarDrawRequest();
				break;
			case "acceptdraw":
				procesarAcceptDraw();
				break;
			case "rejectdraw":
				procesarRejectDraw();
				break;
                        case "passturn":
                                procesarPassTurn();
                                break;
                        case "reloadweapons":
                                procesarReloadWeapons();
                                break;
		}

	}

	private void procesarPassTurn(){
            this.enviarMensaje(new Mensaje("tuTurno", "false", "servidor"));
            oponente.enviarMensaje(new Mensaje("tuTurno", "true", "servidor"));
        }
        
        private void procesarReloadWeapons(){
            Boolean valid = true;
        
            for (Guerrero guerrero : this.jugador.getGuerreros()){
                if(guerrero.estaVivo()) { for (Arma arma : guerrero.getArmas()){
                    if(arma.isUsada() == false) valid = false;
                }
            }
        }
            
            if(valid) {
            for (Guerrero guerrero : this.jugador.getGuerreros()){
                if(guerrero.estaVivo()) { for (Arma arma : guerrero.getArmas()){
                    if(arma.isUsada() == false) arma.setUtilizada(false);
                }
            }
        }   this.enviarMensaje(new Mensaje("manejarreloadweapons", ".", "servidor")); }
            else this.enviarMensaje(new Mensaje("manejarrechazoreload", ".", "servidor"));
        }
        
	private Jugador cargarJugadorDesdeArchivo(String nickname) {
    try {
        Archivismo archivismo = new Archivismo();
        List<String> lineas = archivismo.leerArchivo("Players", nickname + ".txt");
        if (lineas == null || lineas.isEmpty()) {
            System.out.println("Archivo de jugador vacío o inexistente para: " + nickname);
            return null;
        }

        String nombre = lineas.get(0).trim();
        Jugador jugador = new Jugador(nombre);

        // Procesar guerreros
        GuerreroFactoryImpl creador = new GuerreroFactoryImpl();
        int index = 1;

        // Definir los tipos de guerreros válidos
        List<String> tiposStrings = Arrays.stream(Tipo.values())
                                         .map(Enum::name)
                                         .collect(Collectors.toList());

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
                        System.out.println("Formato de arma inválido: " + linea);
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
                        System.out.println("Formato de daño inválido en arma: " + nombreArma);
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
                System.out.println("Formato inválido en el archivo: " + linea);
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
                    System.out.println("Formato de 'Wins' inválido: " + linea);
                    return null;
                }
            } else if (lineaLower.startsWith("loses:") || lineaLower.startsWith("losses:")) {
                try {
                    jugador.setLoses(Integer.parseInt(linea.split(":")[1].trim()));
                } catch (NumberFormatException ex) {
                    System.out.println("Formato de 'Loses/Losses' inválido: " + linea);
                    return null;
                }
            } else if (lineaLower.startsWith("attacks:")) {
                try {
                    jugador.setAttacks(Integer.parseInt(linea.split(":")[1].trim()));
                } catch (NumberFormatException ex) {
                    System.out.println("Formato de 'Attacks' inválido: " + linea);
                    return null;
                }
            } else if (lineaLower.startsWith("success:")) {
                try {
                    jugador.setSuccess(Integer.parseInt(linea.split(":")[1].trim()));
                } catch (NumberFormatException ex) {
                    System.out.println("Formato de 'Success' inválido: " + linea);
                    return null;
                }
            } else if (lineaLower.startsWith("failed:")) {
                try {
                    jugador.setFailed(Integer.parseInt(linea.split(":")[1].trim()));
                } catch (NumberFormatException ex) {
                    System.out.println("Formato de 'Failed' inválido: " + linea);
                    return null;
                }
            } else if (lineaLower.startsWith("giveup:")) {
                try {
                    jugador.setGiveup(Integer.parseInt(linea.split(":")[1].trim()));
                } catch (NumberFormatException ex) {
                    System.out.println("Formato de 'Giveup' inválido: " + linea);
                    return null;
                }
            } else {
                // Si hay líneas adicionales no reconocidas
                System.out.println("Línea de estadísticas no reconocida: " + linea);
                return null;
            }
            index++;
        }

        return jugador;
    }	catch (IOException ex) {
			Logger.getLogger(ClienteHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;

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


	public Jugador getJugador() {
		return jugador;
	}

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
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
            System.out.println("Cliente desconectado: " + idJugador);
            servidor.removerCliente(this);
            
        } finally {
            cerrarConexion();
        }
    }

    private void procesarMensaje(Object mensajeObj) throws IOException {
        if (mensajeObj instanceof Mensaje) {
            Mensaje mensaje = (Mensaje) mensajeObj;
            // Procesar según el tipo de mensaje
            switch (mensaje.getTipo()) {
                case "comando":
                    procesarComando(mensaje);
                    break;
                case "chat":
                    servidor.broadcast(mensaje, this);
                    break;
                case "chatPrivado":
                    servidor.enviarMensajePrivado(mensaje.getDestinatario(), mensaje);
                    break;
                case "respuestaDesafio":
                    procesarRespuestaDesafio(mensaje);
                    break;
                case "combat":
                    procesarCombatCommands(mensaje);
                    break;
				case "attack":
					procesarAtaque(mensaje);
					break;
				case "useWildCard":
					procesarComodin(mensaje);
					break;
				case "fin":
					savePlayerData();
					break;
				case "syncStatsRequest": // Nuevo caso
					procesarSyncStatsRequest(mensaje);
					break;
            }
        }
    }
	
	private Jugador copiarJugador(Jugador original) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(original);
			out.flush();
			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
			ObjectInputStream in = new ObjectInputStream(bis);
			return (Jugador) in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void procesarSyncStatsRequest(Mensaje mensaje) {
		String nickname = (String) mensaje.getContenido();
		if (nickname.equalsIgnoreCase(this.idJugador)) {
			// Crear una copia profunda del objeto Jugador
			Jugador jugadorCopia = copiarJugador(this.getJugador());
			Mensaje respuesta = new Mensaje("syncJugadorResponse", jugadorCopia, "Servidor");
			enviarMensaje(respuesta);

		} else {
			// Enviar mensaje de error
			Mensaje error = new Mensaje("error", "Nickname no válido para sincronización.", "Servidor");
			enviarMensaje(error);
		}
	}



	private void procesarDrawRequest(){
            this.oponente.enviarMensaje(new Mensaje("ReceiveDrawRequest", ".", "Servidor"));
        }
        
        
        private void processDraw(ClienteHandler jugador1, ClienteHandler jugador2) {
            servidor.actualizarRanking();
            jugador1.enviarMensaje(new Mensaje("combateFinalizado", "empate", "Servidor"));
	jugador2.enviarMensaje(new Mensaje("combateFinalizado", "empate", "Servidor"));
                
            jugador1.estado = "disponible";
            jugador2.estado = "disponible";
            jugador1.oponente = null;
            jugador2.oponente = null;
        }
        
        private void procesarAcceptDraw(){
            servidor.actualizarRanking();
            processDraw(this.oponente, this);
        }
        
        private void procesarRejectDraw() {
            this.oponente.enviarMensaje(new Mensaje("ReceiveDrawRejection", ".", "Servidor"));
            this.enviarMensaje(new Mensaje("SendDrawRejection", ".", "Servidor"));
        }
        
	private void manejarUsoComodin() {
		Jugador jugador = this.getJugador();
		if (jugador.isTieneComodin()) {
			if (jugador.isComodinPositivo()) {
				jugador.setAtaquesRestantes(2);
				enviarMensaje(new Mensaje("chat", "Has usado un comodín positivo: puedes atacar dos veces este turno.", "Servidor"));
			}
			else {
				enviarMensaje(new Mensaje("chat", "Has usado un comodín negativo: pierdes tu turno.", "Servidor"));
				// Cambiar el turno al oponente
				if (this.oponente != null) {
					this.oponente.enviarMensaje(new Mensaje("tuTurno", "true", "Servidor"));
					this.enviarMensaje(new Mensaje("tuTurno", "false", "Servidor"));
				}
			}
			jugador.setTieneComodin(false);
		}
		else {
			enviarMensaje(new Mensaje("error", "No tienes un comodín para usar.", "Servidor"));
		}
	}
	
    private void procesarComando(Mensaje mensaje) {
        String contenido = mensaje.getContenido();
        String[] parts = contenido.split(" ", 2);
        String action = parts[0].toLowerCase();

        switch (action) {
            case "players":
                enviarListaJugadores();
                break;
			case "challenge":
				if (parts.length < 2) {
					enviarMensaje(new Mensaje("error", "Uso: challenge {player}", "Servidor"));
				} else {
					String oponenteNickname = parts[1];
					servidor.enviarDesafio(oponenteNickname, this);
				}
				break;

			case "stats":
				if (parts.length < 2) {
					enviarMensaje(new Mensaje("error", "Uso: stats {player}", "Servidor"));
				} else {
					String nickname = parts[1];
					servidor.enviarStatsJugador(nickname, this);
				}
				break;
			case "useWildCard":
				manejarUsoComodin();
				break;
        }}
	
	private void procesarComodin(Mensaje mensaje) {
		String tipoComodin = mensaje.getContenido(); // "positivo" o "negativo"
		if (tipoComodin.equalsIgnoreCase("positivo")) {
			// Permitir al jugador atacar dos veces
			getJugador().setAtaquesRestantes(2);
			enviarMensaje(new Mensaje("chat", "Has recibido un comodín positivo: puedes atacar dos veces este turno.", "Servidor"));
		} else {
			// Perder el turno
			enviarMensaje(new Mensaje("info", "Has recibido un comodín negativo: pierdes tu turno.", "Servidor"));
			// Cambiar el turno al oponente
			if (oponente != null) {
				oponente.enviarMensaje(new Mensaje("tuTurno", "true", "Servidor"));
				enviarMensaje(new Mensaje("tuTurno", "false", "Servidor"));
			}
		}
	}
	
	private void procesarRespuestaDesafio(Mensaje mensaje) {
		String respuesta = mensaje.getContenido(); // "A" o "R"
		ClienteHandler retador = servidor.getClienteHandler(mensaje.getDestinatario());
		ClienteHandler retado = this; // El que recibe el desafío

		if (retador != null) {
			if (respuesta.equalsIgnoreCase("A")) {
				// Actualizar estados
				retador.setEstado("peleando");
				retado.setEstado("peleando");
				retador.setOponente(retado);
				retado.setOponente(retador);

				// Iniciar combate en ambos jugadores
				Mensaje inicioCombate = new Mensaje("combateInicio", retado.getIdJugador(), "Servidor", retador.getIdJugador());
				retador.enviarMensaje(inicioCombate);

				Mensaje inicioCombateOponente = new Mensaje("combateInicio", retador.getIdJugador(), "Servidor", retado.getIdJugador());
				retado.enviarMensaje(inicioCombateOponente);
				
				// Decidir quién inicia el combate, por ejemplo, el retador
				Mensaje turnoRetador = new Mensaje("tuTurno", "true", "Servidor", retador.getIdJugador());
				retador.enviarMensaje(turnoRetador);

				Mensaje turnoRetado = new Mensaje("tuTurno", "false", "Servidor", retado.getIdJugador());
				retado.enviarMensaje(turnoRetado);
			} else {
				retador.enviarMensaje(new Mensaje("chat", retado.getIdJugador() + " ha rechazado tu desafío.", "Servidor"));
			}
		}
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setOponente(ClienteHandler oponente) {
		this.oponente = oponente;
	}
	
	private void procesarSurrender(){
            this.jugador.incrementarGiveup();
            oponente.enviarMensaje(new Mensaje("ReceiveSurrenderRequest", ".", "Servidor"));
            manejarVictoria(oponente, this);
        }
        
	private void procesarAtaque(Mensaje mensaje) {
		String contenido = mensaje.getContenido(); // Formato: guerreroNombre;armaNombre;estrategia
		String[] partes = contenido.split(";");
		String guerreroNombre = partes[0];
		String armaNombre = partes[1];
		String estrategia = partes.length > 2 ? partes[2] : "";

		// Obtener el jugador atacante y defensor
		ClienteHandler atacanteHandler = this;
		ClienteHandler defensorHandler = oponente;

		// Obtener guerrero y arma del atacante
		System.out.println("Procesando ataque:");
		System.out.println("Guerrero Atacante: " + guerreroNombre);
		System.out.println("Arma: " + armaNombre);
		System.out.println("Estrategia: " + estrategia);

		// Buscar el guerrero atacante
		Guerrero guerreroAtacante = jugador.getGuerreroPorNombre(guerreroNombre);
		System.out.println("Nombre guerrero: " + jugador.getGuerreroPorNombre(guerreroNombre));
		if (guerreroAtacante == null) {
			System.out.println("Error buscando guerrero: " + guerreroNombre);
			return;
		}

		// Buscar el arma
		Arma arma = guerreroAtacante.getArmaPorNombre(armaNombre);
		if (arma == null) {
			System.out.println("Error buscando arma: " + armaNombre);
			return;
		}

		// Validar que el arma no esté deshabilitada
		if (arma.isUsada()) {
			enviarMensaje(new Mensaje("error", "El arma está deshabilitada.", "Servidor"));
			return;
		}

		// Aplicar estrategia
		aplicarEstrategia(arma, estrategia);

		// Calcular daño a los guerreros del defensor
		Map<String, Integer> danioPorGuerrero = calcularDanio(defensorHandler.jugador.getGuerreros(), arma);

		// Actualizar vidas de los guerreros defensores
		for (Guerrero guerreroDefensor : defensorHandler.jugador.getGuerreros()) {
			int danio = danioPorGuerrero.getOrDefault(guerreroDefensor.getNombre(), 0);
			guerreroDefensor.recibirDanio(danio);
		}

		// Deshabilitar el arma usada
		arma.setUtilizada(true);

		// Actualizar estadísticas del atacante
		atacanteHandler.jugador.incrementarAttacks();
		int danioTotal = danioPorGuerrero.values().stream().mapToInt(Integer::intValue).sum();
		if (danioTotal > 100) {
			atacanteHandler.jugador.incrementarSuccess();
		} else {
			atacanteHandler.jugador.incrementarFailed();
		}

		// Enviar resultados a ambos jugadores
		String resultadoAtacante = "You attacked " + defensorHandler.jugador.getNickname() + " with " + guerreroNombre + ". Weapon: " + armaNombre + ".\nTotal damage dealt: " + danioTotal + "%\n";
		atacanteHandler.enviarMensaje(new Mensaje("attackResult", resultadoAtacante, "Servidor"));

		StringBuilder resultadoDefensor = new StringBuilder();
		resultadoDefensor.append("Attacked by " + atacanteHandler.jugador.getNickname() + " with " + guerreroNombre + ". Weapon: " + armaNombre + ".\n");
		for (Map.Entry<String, Integer> entry : danioPorGuerrero.entrySet()) {
			resultadoDefensor.append(entry.getKey()).append(": -").append(entry.getValue()).append("%\n");
		}
		defensorHandler.enviarMensaje(new Mensaje("defenseResult", resultadoDefensor.toString(), "Servidor"));

		// Verificar si el defensor ha sido derrotado
		if (defensorHandler.jugador.haSidoDerrotado()) {
			manejarVictoria(atacanteHandler, defensorHandler);
		} else {
			// Verificar si el atacante tiene ataques restantes
			if (atacanteHandler.jugador.getAtaquesRestantes() > 0) {
				atacanteHandler.jugador.decrementarAtaquesRestantes();
				atacanteHandler.enviarMensaje(new Mensaje("info", "Tienes " + atacanteHandler.jugador.getAtaquesRestantes() + " ataque(s) adicional(es).", "Servidor"));
			} else {
				// Cambiar turno
				atacanteHandler.enviarMensaje(new Mensaje("tuTurno", "false", "Servidor"));
				defensorHandler.enviarMensaje(new Mensaje("tuTurno", "true", "Servidor"));
			}
		}
	}

	
	private void aplicarEstrategia(Arma arma, String estrategia) {
		// Implementar las estrategias según tu lógica
		switch (estrategia.toLowerCase()) {
			case "randomduplex":
				// Duplica el valor de un tipo de daño aleatorio
				Tipo tipoRandom = Tipo.values()[new Random().nextInt(Tipo.values().length)];
				int danioOriginal = arma.getDanioPorTipo().getOrDefault(tipoRandom, 0);
				arma.getDanioPorTipo().put(tipoRandom, danioOriginal * 2);
				break;
			case "randomcombination":
				// Combina con un arma de otro guerrero
				// Implementar lógica
				break;
			case "bestcombination":
				// Obtener el mejor valor de cada tipo entre todas las armas del jugador
				// Implementar lógica
				break;
			case "average":
				// Promediar los valores de daño de todos los guerreros de todos los jugadores
				// Implementar lógica
				break;
			case "optimal":
				// Crear arreglo con la mejor combinación de todos los guerreros de todos los jugadores
				// Implementar lógica
				break;
			default:
				// Sin estrategia
				break;
		}
	}
	
	private Map<String, Integer> calcularDanio(List<Guerrero> guerrerosDefensores, Arma armaAtacante) {
		Map<String, Integer> danioPorGuerrero = new HashMap<>();
		for (Guerrero guerreroDefensor : guerrerosDefensores) {
			if (guerreroDefensor.getVida() <= 0) continue;
			Tipo tipoGuerrero = guerreroDefensor.getTipo();
			int danio = armaAtacante.getDanioPorTipo().getOrDefault(tipoGuerrero, 0);
			danioPorGuerrero.put(guerreroDefensor.getNombre(), danio);
		}
		return danioPorGuerrero;
	}
	
	private void manejarVictoria(ClienteHandler ganadorHandler, ClienteHandler perdedorHandler) {
		// Actualizar estadísticas
		ganadorHandler.jugador.incrementarWins();
		perdedorHandler.jugador.incrementarLoses();

		// Actualizar ranking
		servidor.actualizarRanking();

		// Notificar a los jugadores
		ganadorHandler.enviarMensaje(new Mensaje("combateFinalizado", "victoria", "Servidor"));
		perdedorHandler.enviarMensaje(new Mensaje("combateFinalizado", "derrota", "Servidor"));

		// Resetear estados
		ganadorHandler.estado = "disponible";
		perdedorHandler.estado = "disponible";
		ganadorHandler.oponente = null;
		perdedorHandler.oponente = null;
	}

	private void iniciarCombate(ClienteHandler jugador1, ClienteHandler jugador2) {
		// Ambos jugadores entran en estado de combate
		jugador1.estado = "peleando";
		jugador2.estado = "peleando";
		jugador1.oponente = jugador2;
		jugador2.oponente = jugador1;

		// Reiniciar vida de los guerreros a 100 y resetear armas
		resetearGuerreros(jugador1.getJugador());
		resetearGuerreros(jugador2.getJugador());

		// Notificar a ambos jugadores que el combate ha iniciado
		Mensaje mensajeInicio1 = new Mensaje("combateInicio", jugador2.getIdJugador(), "Servidor");
		Mensaje mensajeInicio2 = new Mensaje("combateInicio", jugador1.getIdJugador(), "Servidor");
		jugador1.enviarMensaje(mensajeInicio1);
		jugador2.enviarMensaje(mensajeInicio2);

		// Decidir quién inicia el turno (puedes implementar una lógica más compleja)
		jugador1.enviarMensaje(new Mensaje("tuTurno", "true", "Servidor"));
		jugador2.enviarMensaje(new Mensaje("tuTurno", "false", "Servidor"));
	}
	
	private void resetearGuerreros(Jugador jugador) {
		for (Guerrero guerrero : jugador.getGuerreros()) {
			guerrero.setVida(100);
			for (Arma arma : guerrero.getArmas()) {
				arma.setUtilizada(false);
			}
		}
	}

    public void enviarMensaje(Object mensaje) {
        try {
            out.writeObject(mensaje);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    private void enviarListaJugadores() {
        StringBuilder lista = new StringBuilder();
        List<ClienteHandler> clientes = servidor.getClientes();
        List<ClienteHandler> resultado = new ArrayList<>();
        List<Integer> puntajes = new ArrayList<>();
        for (ClienteHandler ch : clientes) {
            puntajes.add(ch.jugador.getPuntuacion().calcularPuntuacionTotal());
        }
        Collections.sort(puntajes, Collections.reverseOrder());
        List<ClienteHandler> usados = new ArrayList<>();
        for(Integer puntaje : puntajes){
            for(ClienteHandler ch : clientes){
                if(puntaje == ch.jugador.getPuntuacion().calcularPuntuacionTotal()) { 
                    if(!usados.contains(ch)){
                    resultado.add(ch);
                    usados.add(ch);
                    break;
                    }}
}
}
        int rankManager = 1;
        for(ClienteHandler ch : resultado){
            lista.append(rankManager);
            lista.append(": ");
            lista.append(ch.jugador.getNickname());
            lista.append("\n");
            rankManager++;
        }
        Mensaje mensaje = new Mensaje("listaJugadores", lista.toString(), "Servidor");
        enviarMensaje(mensaje);
    }

    public String getIdJugador() {
        return idJugador;
    }

    public String getEstado() {
        return estado;
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
	
	private void savePlayerData() throws IOException {
		System.out.println("Iniciando guardado de datos para el jugador: " + jugador.getNickname());

		StringBuilder content = new StringBuilder();

		// 1. Añadir el nickname del jugador
		content.append(jugador.getNickname()).append("\n");

		// 2. Definir el orden de los tipos para mantener la consistencia en los daños
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

		// 3. Iterar sobre cada guerrero del jugador
		for (Guerrero guerrero : jugador.getGuerreros()) {
			System.out.println("Guardando guerrero: " + guerrero.getTipo());
			content.append(guerrero.getTipo().toString()).append("\n");

			// Iterar sobre cada arma del guerrero
			for (Arma arma : guerrero.getArmas()) {
				System.out.println("Guardando arma: " + arma.getNombre());
				content.append(arma.getNombre()).append(":");

				// Obtener los daños por tipo en el orden definido
				List<Integer> danios = orderedTipos.stream()
					.map(tipo -> arma.getDanioPorTipo().getOrDefault(tipo, 0))
					.collect(Collectors.toList());

				// Convertir la lista de daños a una cadena separada por comas
				String daniosStr = danios.stream()
					.map(String::valueOf)
					.collect(Collectors.joining(","));

				content.append(daniosStr).append("\n");
			}
		}

		// 4. Añadir las estadísticas del jugador una sola vez
		System.out.println("Guardando estadisticas del jugador.");
		content.append("Wins: ").append(jugador.getWins()).append("\n");
		content.append("Loses: ").append(jugador.getLoses()).append("\n");
		content.append("Attacks: ").append(jugador.getAttacks()).append("\n");
		content.append("Success: ").append(jugador.getSuccess()).append("\n");
		content.append("Failed: ").append(jugador.getFailed()).append("\n");
		content.append("Giveup: ").append(jugador.getGiveup()).append("\n");

		// 5. Guardar el contenido en el archivo correspondiente usando Archivismo
		archivismo.createFile("Players", jugador.getNickname() + ".txt", content.toString());

		System.out.println("Guardado completado para el jugador: " + jugador.getNickname());
	}
}
