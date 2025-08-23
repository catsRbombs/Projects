/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.clases.threads;

import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.clases.Constructor;
import main.clases.InterfazDeJuego;

/**
 *
 * @author Efraim Cuevas
 */
public class ThreadConstructor extends Thread{
    InterfazDeJuego juego;
    Constructor constructor;
    private boolean running = true;
    private boolean paused = false;
    
    public ThreadConstructor(InterfazDeJuego juego, Constructor constructor) {
        this.juego = juego;
        this.constructor = constructor;
    }
    
    public void run(){
        
        try {
            while(running){
                
                if (constructor.isMovingX() || constructor.isMovingY()) {
                    while (constructor.xDestino != -1 || constructor.yDestino != -1){
                        juego.moverPersonaje(constructor);
                        sleep(250);
                    }
                }
                sleep(1000);
            
            }
        } catch (InterruptedException e) {}   
        }
    
    }
    

