/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.clases;

import java.util.Random;
import javax.swing.JLabel;

/**
 *
 * @author fluff
 */
public class Atacante extends Personaje{
    
    private int fuerza;
    private boolean vivo;
    
    public Atacante(JLabel Label, int Fuerza){
    super("Atacante", Label);
    this.fuerza = Fuerza;
    vivo = true;
    }
    
    public boolean isVivo() {
        return vivo;
    }
    public void morir(){
        System.out.println("mori");
        vivo = false;
    }
    
    public void atacarPersonaje(Personaje personaje) {
        Random rand = new Random();
        int damage = rand.nextInt(5, fuerza);
        personaje.reducirSalud(damage);
    }
    
    public int getFuerza() {return fuerza;}

    @Override
    public void comer(Recurso comida) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void accionar(int accion) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
