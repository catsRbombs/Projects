/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.clases.threads;

import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.clases.Cazador;
import main.clases.InterfazDeJuego;

/**
 *
 * @author Efraim Cuevas
 */
public class ThreadCazador extends Thread{
    InterfazDeJuego juego;
    Cazador cazador;
    private boolean running = true;
    private boolean paused = false;
    
    public ThreadCazador(InterfazDeJuego juego, Cazador cazador) {
        this.juego = juego;
        this.cazador = cazador;
    }
    
    public void run(){
        
        try {
            while(running){
                
                if (cazador.isMovingX() || cazador.isMovingY()) {
                    while (cazador.xDestino != -1 || cazador.yDestino != -1){
                        juego.moverPersonaje(cazador);
                        sleep(250);
                    }
                }
                sleep(1000);
            
            }
        } catch (InterruptedException e) {}   
        }
    
    }
    

