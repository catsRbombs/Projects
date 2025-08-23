/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import java.awt.Point;

import game.components.Component;

/**
 *
 * @author gosor
 */
public class Remolino extends Component {

    public Remolino(Point position, int width, int height, int cost) {
        super(position, width, height, cost, "skibidiwhirlpool");
    }

    @Override
    public void place() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
	
}
