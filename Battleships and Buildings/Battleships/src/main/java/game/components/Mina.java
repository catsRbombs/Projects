// game/components/Mina.java

package game.components;

import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;

import game.Player;

/**
 * La clase Mina produce acero cada cierto tiempo.
 */
public class Mina extends Fabrica {

    private int productionRate; // Cantidad de acero producida
    private int productionTime; // Tiempo entre producciones en segundos
    private Player owner;

    /**
     * Constructor de la clase Mina.
     *
     * @param position       Posición de la mina
     * @param productionRate Cantidad de acero producida
     * @param productionTime Tiempo entre producciones
     * @param owner          Propietario de la mina
     */
    public Mina(Point position, int productionRate, int productionTime, Player owner, int tag) {
        super(position, 1000, "Mina" + tag); // Costo definido
        this.productionRate = productionRate;
        this.productionTime = productionTime;
        this.owner = owner;
        place();
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
                owner.setSteel(owner.getSteel() + productionRate);
                owner.getClientHost().gui.actualizarDinero();
//                System.out.println("Hierrooooooo");
                // Continuar produciendo hasta que la mina sea destruida
            }
        }, 0, productionTime * 1000);
    }
}
