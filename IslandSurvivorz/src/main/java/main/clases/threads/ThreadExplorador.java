/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.clases.threads;

import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.clases.Explorador;
import main.clases.InterfazDeJuego;

/**
 *
 * @author Efraim Cuevas
 */
public class ThreadExplorador extends Thread{
    InterfazDeJuego juego;
    Explorador explorador;
    private boolean running = true;
    private boolean paused = false;
    
    public ThreadExplorador(InterfazDeJuego juego, Explorador explorador) {
        this.juego = juego;
        this.explorador = explorador;
    }
    
    public void run(){
        
        try {
            while(running){
                
                if (explorador.isMovingX() || explorador.isMovingY()) {
                    while (explorador.xDestino != -1 || explorador.yDestino != -1){
                        juego.moverPersonaje(explorador);
                        sleep(250);
                    }
                }
                sleep(1000);
            
            }
        } catch (InterruptedException e) {}   
        }
    
    }
    

