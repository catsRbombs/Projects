// game/components/Component.java

package game.components;

import java.awt.Point;
import java.io.Serializable;

/**
 * Clase abstracta Component que representa los componentes del juego.
 */
public abstract class Component implements Serializable {

    Point position;
    int width;
    int height;
    int cost;
    String name;
    boolean visibleToEnemies = false;

    public boolean isVisibleToEnemies() {
        return visibleToEnemies;
    }

    public void setVisibleToEnemies(boolean visible) {
        this.visibleToEnemies = visible;
    }
	
	
    public Component(Point position, int width, int height, int cost, String name) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.cost = cost;
        this.name = name;
    }
    
    public boolean isChocando(Point coord){
        int x = coord.x;
        int y = coord.y;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (x+i == position.x && y+j == position.y) return true;
            }
        }        
        return false;
    }
    
    public boolean isSalidoDelLimite(Point coord){
        int x = coord.x;
        int y = coord.y;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (x+i > 19 || y+j > 19) return true;
            }
        }  
        return false;
    }

    // Getters y Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Point getPosition() {
        return position;
    }

    public int getCost() {
        return cost;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    

    /**
     * Método para colocar el componente en el mapa.
     */
    public abstract void place();

    /**
     * Método para destruir el componente.
     */
    public abstract void destroy();
}
