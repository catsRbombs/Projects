/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.clases.threads;

import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.clases.Recolector;
import main.clases.InterfazDeJuego;

/**
 *
 * @author Efraim Cuevas
 */
public class ThreadRecolector extends Thread{
    InterfazDeJuego juego;
    Recolector recolector;
    private boolean running = true;
    private boolean paused = false;
    
    public ThreadRecolector(InterfazDeJuego juego, Recolector recolector) {
        this.juego = juego;
        this.recolector = recolector;
    }
    
    public void run(){
        
        try {
            while(running){
                
                if (recolector.isMovingX() || recolector.isMovingY()) {
                    while (recolector.xDestino != -1 || recolector.yDestino != -1){
                        juego.moverPersonaje(recolector);
                        sleep(250);
                    }
                }
                sleep(1000);
            
            }
        } catch (InterruptedException e) {}   
        }
    
    }
    

