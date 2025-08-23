/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.clases.threads;

import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.clases.Cientifico;
import main.clases.InterfazDeJuego;

/**
 *
 * @author Efraim Cuevas
 */
public class ThreadCientifico extends Thread{
    InterfazDeJuego juego;
    Cientifico cientifico;
    private boolean running = true;
    private boolean paused = false;
    
    public ThreadCientifico(InterfazDeJuego juego, Cientifico cientifico) {
        this.juego = juego;
        this.cientifico = cientifico;
    }
    
    public void run(){
        
        try {
            while(running){
                
                if (cientifico.isMovingX() || cientifico.isMovingY()) {
                    while (cientifico.xDestino != -1 || cientifico.yDestino != -1){
                        juego.moverPersonaje(cientifico);
                        sleep(250);
                    }
                }
                sleep(1000);
            
            }
        } catch (InterruptedException e) {}   
        }
    
    }
    

