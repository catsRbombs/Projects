// game/Game.java

package game;

import java.util.ArrayList;
import java.util.List;

import utils.Logger;

/**
 * La clase Game mantiene el estado global del juego.
 * Gestiona turnos, reglas y condiciones de victoria.
 */
public class Game {

    private List<Player> players;
    private int currentPlayerIndex;
    private boolean isGameOver;
    private Logger logger;

    /**
     * Constructor de la clase Game.
     */
    public Game() {
        players = new ArrayList<>();
        currentPlayerIndex = 0;
        isGameOver = false;
        logger = Logger.getInstance();
    }

    /**
     * Agrega un jugador al juego.
     *
     * @param player Jugador a agregar
     */
    public void addPlayer(Player player) {
        players.add(player);
        logger.log("Jugador agregado: " + player.getName());
    }

    /**
     * Inicia el juego.
     */
    public void startGame() {
        logger.log("El juego ha comenzado.");
        // Implementar lógica para iniciar el juego
    }

    /**
     * Avanza al siguiente turno.
     */
    public void nextTurn() {
        if (!isGameOver) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            logger.log("Es el turno de: " + getCurrentPlayer().getName());
        }
    }

    /**
     * Obtiene el jugador actual.
     *
     * @return Jugador actual
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Verifica las condiciones de victoria.
     */
    public void checkVictoryConditions() {
        // Implementar lógica para verificar si hay un ganador
    }
}
