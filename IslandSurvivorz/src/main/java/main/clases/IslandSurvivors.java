/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package main.clases;

/**
 *
 * @author Efraim Cuevas & Fabián Mata
 */
public class IslandSurvivors {

    public static void main(String[] args) {
        System.out.println("Hola mundo");
        System.out.println("Autores: Efraim Cuevas y Fabián Mata");
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterfazDeJuego().setVisible(true);
            }
        });
        
    }
}
