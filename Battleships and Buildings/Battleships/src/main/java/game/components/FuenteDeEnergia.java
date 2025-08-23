// game/components/FuenteDeEnergia.java

package game.components;

import java.awt.Point;

/**
 * La clase FuenteDeEnergia representa la fuente de energía del jugador.
 */
public class FuenteDeEnergia extends Component {

    private boolean isVisible;

    /**
     * Constructor de la clase FuenteDeEnergia.
     *
     * @param position Posición de la fuente
     */
    public FuenteDeEnergia(Point position, int tag) {
        super(position, 2, 2, 12000, "FuenteDeEnergia"+tag); // Tamaño 2x2 y costo definido
        this.isVisible = false;
    }
	
	public FuenteDeEnergia(Point position,String nombre) {
        super(position, 2, 2, 12000, nombre); // Tamaño 2x2 y costo definido
        this.isVisible = false;
    }
	

    /**
     * Coloca la fuente de energía en el mapa.
     */
    @Override
    public void place() {
        // Implementar lógica para colocar en el mapa
    }

    /**
     * Destruye la fuente de energía.
     */
    @Override
    public void destroy() {
        // Implementar lógica para destruir y actualizar visibilidad de subgrafos
    }

    /**
     * Establece la visibilidad de la fuente y subgrafos.
     *
     * @param visible Visibilidad
     */
    public void setVisibility(boolean visible) {
        this.isVisible = visible;
        // Actualizar visibilidad de subgrafos si es necesario
    }
}
