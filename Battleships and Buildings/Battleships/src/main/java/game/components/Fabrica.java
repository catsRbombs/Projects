// game/components/Fabrica.java

package game.components;

import java.awt.Point;

/**
 * Clase abstracta Fabrica para las diferentes fábricas.
 */
public abstract class Fabrica extends Component {

    /**
     * Constructor de la clase Fabrica.
     *
     * @param position Posición de la fábrica
     * @param cost     Costo de la fábrica
     */
    public Fabrica(Point position, int cost, String tag) {
        super(position, 2, 1, cost, tag); // Tamaño 1x2 o 2x1
    }

    /**
     * Método para iniciar la producción.
     */
    public abstract void startProduction();
}
