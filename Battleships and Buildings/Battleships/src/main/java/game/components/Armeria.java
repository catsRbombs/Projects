// game/components/Armeria.java

package game.components;

import java.awt.Point;

import javax.swing.JOptionPane;

import game.Player;
import game.weapons.Bomb;
import game.weapons.Cannon;
import game.weapons.DoubleCannon;
import game.weapons.RedBeardCannon;
import game.weapons.Weapon;


/**
 * La clase Armeria fabrica armas.
 */
public class Armeria extends Fabrica {

    private String weaponType;
    private Player owner;

    /**
     * Constructor de la clase Armeria.
     *
     * @param position  Posición de la armería
     * @param weaponType Tipo de arma a fabricar
     */
    public Armeria(Point position, Player owner, int tag) {
        super(position, 1500, "armeria"+tag); // Costo definido
        String weaponSelected = selectWeapon();
        this.owner = owner; //HOLA MUNDO <3
        while (weaponSelected== null) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una arma.");
            weaponSelected = selectWeapon();
        }
            this.weaponType = weaponSelected;
    }

    //constructor fake
    //
    //
    //
    //
    //

    public String getWeaponType() {
        return weaponType;
    }
    public static String selectWeapon() {
        // Opciones de armas
        String[] options = {"Cannon", "DoubleCannon", "RedBeardCannon", "Bomb"};

        
        // Mostrar el cuadro de diálogo
        String selectedOption = (String) JOptionPane.showInputDialog(
                null,
                "Selecciona un arma:",
                "Selector de Armas",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        // Crear y devolver el arma correspondiente
        if (selectedOption != null) {
            return selectedOption;
        }

        // Si el usuario cierra el cuadro de diálogo o no selecciona nada
        return null;
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

    public Weapon stringToWeapon(String weapon){
        // Implementar lógica para convertir un string en un tipo de arma
        return null;
    }
    @Override
    public void startProduction() {
        // Implementar lógica para producir armas
    }

    /**
     * Fabrica un arma.
     *
     * @return Arma fabricada
     */
    public void produceWeapon(int steel) {
        switch(weaponType) {
            case "Cannon":
                if(steel >= 500) {
                    owner.byebyeSteel(500);
                    owner.addWeapon(new Cannon());
                }
                else JOptionPane.showMessageDialog(null, "No tienes suficiente acero!", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case "DoubleCannon":
            if(steel >= 1000) {
                owner.byebyeSteel(1000);
                owner.addWeapon(new DoubleCannon());
                break;
            }
            else JOptionPane.showMessageDialog(null, "No tienes suficiente acero!", "Error", JOptionPane.ERROR_MESSAGE);
            break;
            case "RedBeardCannon":
            if(steel >= 5000) {
                owner.byebyeSteel(5000);
                owner.addWeapon(new RedBeardCannon());
                break;
            }
            else JOptionPane.showMessageDialog(null, "No tienes suficiente acero!", "Error", JOptionPane.ERROR_MESSAGE);
            break;
            case "Bomb":
            if(steel >= 2000) {
                owner.byebyeSteel(500);
                owner.addWeapon(new Cannon());
                break;
            }
            else JOptionPane.showMessageDialog(null, "No tienes suficiente acero!", "Error", JOptionPane.ERROR_MESSAGE);
            break;
            default:
                JOptionPane.showMessageDialog(null, "El arma seleccionada no es válida.");
                break;
        }
    }
}
