/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.clases.threads;

import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.clases.Curandero;
import main.clases.InterfazDeJuego;

/**
 *
 * @author Efraim Cuevas
 */
public class ThreadCurandero extends Thread{
    InterfazDeJuego juego;
    Curandero curandero;
    private boolean running = true;
    private boolean paused = false;
    
    public ThreadCurandero(InterfazDeJuego juego, Curandero curandero) {
        this.juego = juego;
        this.curandero = curandero;
    }
    
    public void run(){
        
        try {
            while(running){
                
                if (curandero.isMovingX() || curandero.isMovingY()) {
                    while (curandero.xDestino != -1 || curandero.yDestino != -1){
                        juego.moverPersonaje(curandero);
                        sleep(250);
                    }
                }
                sleep(1000);
            
            }
        } catch (InterruptedException e) {}   
        }
    
    }
    

