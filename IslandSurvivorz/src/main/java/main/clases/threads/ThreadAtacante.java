/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.clases.threads;

import static java.lang.Thread.sleep;
import main.clases.Atacante;
import main.clases.Cazador;
import main.clases.InterfazDeJuego;
import main.clases.Lesion;
import main.clases.Personaje;
/**
 *
 * @author fluff
 */
public class ThreadAtacante extends Thread{
    InterfazDeJuego juego;
    Atacante atacante;
    private boolean running = true;
    private boolean paused = false;
    
    public ThreadAtacante(InterfazDeJuego juego, Atacante atacante) {
        this.juego = juego;
        this.atacante = atacante;
    }
    
    @Override
    public void run() {
        
        try {
            while(running){
                
                if (atacante.isMovingX() || atacante.isMovingY()) {
                    while (atacante.xDestino != -1 || atacante.yDestino != -1){
                        juego.moverPersonaje(atacante);
                        sleep(250);
                    }
                }
            
            sleep(10000);
            if(atacante.isVivo()){
                Personaje pj = juego.conseguirChampRandom();
                int posX = pj.getLabel().getX();
                int posY = pj.getLabel().getY();

                juego.setXY(atacante, posX, posY);
                juego.moverPersonaje(atacante);
                var cazador = (Cazador) juego.personajes.get(0);
                if(juego.estanCerca(atacante.getLabel(), pj.getLabel(), 20)){
                    if(juego.estanCerca(atacante.getLabel(), cazador.getLabel(),20)){
                        cazador.defender(pj, atacante);
                    }
                    else{
                        atacante.atacarPersonaje(pj);
                        pj.nuevaLesion(new Lesion(1));
                    }
                }
            }
            
            else{
                running = false;
                atacante.getLabel().setVisible(false);
            }
            
            
            }
        } catch (InterruptedException e) {}   
        }
    
    }
