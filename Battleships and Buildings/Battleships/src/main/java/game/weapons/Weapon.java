/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.weapons;

/**
 *
 * @author gosor
 */
public abstract class Weapon {
    String name;
	
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

    public Object getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
