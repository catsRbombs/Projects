package game.components;

import java.awt.Point;

import UI.ConnectionLayer;

/**
 * La clase Conector representa los conectores entre componentes.
 */
public class Conector extends Component {

    public Component fromComponent;
    public Component toComponent;
    public double peso; 

    private static ConnectionLayer connectionLayer; // Referencia estática

    public Conector(Point position, int tag) {
        super(position, 1, 1, 100, "Conector" + tag); // Tamaño 1x1 y costo definido
    }

    public Conector(Point position, int width, int height, int cost, String name) {
        super(position, width, height, cost, name); 
    }

    @Override
    public void place() {
        // Implementar lógica para colocar en el mapa
    }

    @Override
    public void destroy() {
        // Implementar lógica para destruir y actualizar conexiones
    }

    public static double calculateDistance(Point p1, Point p2) {
        if (p1 == null || p2 == null) {
            throw new IllegalArgumentException("Los puntos no pueden ser nulos.");
        }

        int deltaX = p2.x - p1.x;
        int deltaY = p2.y - p1.y;

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public void setConnectedComponents(Component from, Component to) {
        this.fromComponent = from;
        this.toComponent = to;
        peso = calculateDistance(fromComponent.position, toComponent.position);

        // Agregar el conector al ConnectionLayer si está configurado
        if (connectionLayer != null) {
            connectionLayer.addConnector(this);
        }
    }

    public Component getFromComponent() {
        return fromComponent;
    }

    public Component getToComponent() {
        return toComponent;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Point getFromPosition() {
        return fromComponent.position;
    }

    public Point getToPosition() {
        return toComponent.position;    
    }

    // Método estático para establecer la referencia a ConnectionLayer
    public static void setConnectionLayer(ConnectionLayer cl) {
        connectionLayer = cl;
    }
}
