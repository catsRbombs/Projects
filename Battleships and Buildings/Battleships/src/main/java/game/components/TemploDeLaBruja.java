// game/components/TemploDeLaBruja.java

package game.components;

import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;

import game.Player;
import java.util.Random;
import utils.Logger;

/**
 * La clase TemploDeLaBruja genera comodines cada 5 minutos.
 */
public class TemploDeLaBruja extends Fabrica {

    private Player owner;
    private Logger logger;

    /**
     * Constructor de la clase TemploDeLaBruja.
     *
     * @param position Posición del templo
     * @param owner    Propietario del templo
     */
    public TemploDeLaBruja(Point position, Player owner, int tag) {
        super(position, 2500, "TemploDeLaBruja"+tag); // Costo definido
        this.owner = owner;
        this.logger = Logger.getInstance();
        startProduction();
    }

    @Override
    public void place() {
        // Implementar lógica para colocar en el mapa
        startProduction();
    }

    @Override
    public void destroy() {
        // Implementar lógica para destruir
    }

	@Override
	public void startProduction() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Random random = new Random();
				int numeroAleatorio = random.nextInt(2) + 1; // Genera un número aleatorio entre 1 y 2 (inclusive)
				owner.usarComodin(numeroAleatorio);
                                System.out.println("COMODIN USADO");
			}
		}, 0, 300 * 1000); //faltan dos 0 // Cada 5 minutos
	}
}
