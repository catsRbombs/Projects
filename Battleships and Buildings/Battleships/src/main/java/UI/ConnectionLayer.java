package UI;

import game.components.Component;
import game.components.Conector;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectionLayer extends JPanel {

    private List<Conector> connectors;
    private JLabel[][] gridLabels; // Referencia a la matriz de labels

    public ConnectionLayer(JLabel[][] gridLabels) {
        this.connectors = new ArrayList<>();
        this.gridLabels = gridLabels;
        setOpaque(false); // Hacemos el panel transparente
    }

    public void addConnector(Conector connector) {
        connectors.add(connector);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujamos una línea para cada conector
        for (Conector connector : connectors) {
            Component fromComp = connector.getFromComponent();
            Component toComp = connector.getToComponent();

            if (fromComp != null && toComp != null) {
                Point fromPoint = getComponentCenter(fromComp);
                Point toPoint = getComponentCenter(toComp);

                // Dibujamos la línea
                g.setColor(Color.RED);
                g.drawLine(fromPoint.x, fromPoint.y, toPoint.x, toPoint.y);
            }
        }
    }

    private Point getComponentCenter(Component comp) {
        // Obtener la posición del label en la GUI
        int x = comp.getPosition().x;
        int y = comp.getPosition().y;

        JLabel label = gridLabels[x][y];

        // Obtener la posición en píxeles del label
        Point labelPos = label.getLocation();

        // Calcular el centro del label
        int centerX = labelPos.x + label.getWidth() / 2;
        int centerY = labelPos.y + label.getHeight() / 2;

        return new Point(centerX, centerY);
    }
}
