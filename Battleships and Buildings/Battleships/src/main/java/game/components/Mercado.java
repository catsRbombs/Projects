// game/components/Mercado.java

package game.components;



import java.awt.Point;

import game.Player;

/**
 * La clase Mercado permite a los jugadores intercambiar bienes por dinero.
 */
public class Mercado extends Component {

    /**
     * Constructor de la clase Mercado.
     *
     * @param position Posición del mercado
     */
    public Mercado(Point position, int tag) {
        super(position, 1, 2, 2000, "Mercado" + tag); // Tamaño 1x2 o 2x1 y costo definido
    }

    @Override
    public void place() {
        // Implementar lógica para colocar en el mapa
    }

    @Override
    public void destroy() {
        // Implementar lógica para destruir
    }

    /**
     * Realiza una transacción en el mercado.
     *
     * @param item   Artículo a intercambiar
     * @param amount Cantidad
     * @param price  Precio
     */
    public void transactBuy(Player player, String item, int amount, int price) {
        // Implementar lógica de transacción
    }

    public int getPriceWeapon(String weapon) {
        switch (weapon.toLowerCase()) {
            case "metal":
                return 100;
            case "cannon":
                return 500;
            case "doublecannon":
                return 1000;
            case "redbeardcannon":
                return 3000;
            case "bomb":
                return 1500;   
        }      
        System.out.println("Invalid weapon type");
        return -1;   
    }
    
    public void transactSell(Player player1, String item, int amount, int price) {
        // Implementar lógica de transacción
    }

    public void BuyMarket(Player player){

    }

    public void SellMarket(Player player){

    }

}
