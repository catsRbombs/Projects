package game.components;

import java.awt.Point;

import game.Player;

public class Token {
    
    private String holder;
    private Player owner;

    public Token(String holder, Player owner) {
        this.holder = holder;
        this.owner = owner;
    }

    public String getHolder() {
        return holder;
    }

    public Component extractComponent (Point position, int tag) {
        
        switch (holder) {
            case "Mina":
                return new Mina(position, 50, 60, owner, owner.countComponentsOfType (Mina.class)+ 1);
            case "Armeria":
                return new Armeria(position, owner, owner.countComponentsOfType (Armeria.class)+ 1);
            case "TemploDeLaBruja":
                return new TemploDeLaBruja(position, owner, owner.countComponentsOfType (TemploDeLaBruja.class)+ 1);
            case "Mercado":
                return new Mercado(position, owner.countComponentsOfType (Mercado.class) + 1);
            case "FuenteDeEnergia":
                return new FuenteDeEnergia(position, owner.countComponentsOfType (FuenteDeEnergia.class)+ 1);  
            case "Conector":
                return new Conector(position, owner.countComponentsOfType (Conector.class)+ 1);
            default:
                return null;
        }
        

    }
}
