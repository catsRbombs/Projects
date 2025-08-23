/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.clases;

import java.util.ArrayList;
import javax.swing.JLabel;

/**
 *
 * @author Efraim Cuevas
 */
public class Refugio {
    /*
    La estabilidad del refugio es de 0 a 100
    Utilicemos un solo tipo de recurso como material para hacer los refugios
    "Madera"
    Para las reparaciones, cada 1 de madera le repara 1 de estabilidad
    */
    
    private int estabilidad;
    private int capacidad;
    private ArrayList<Personaje> personajesAdentro;
    public JLabel label;

    public Refugio(JLabel label) {
        this.estabilidad = 100;
        this.capacidad = 3;
        this.label = label;
        personajesAdentro = new ArrayList<Personaje>();
    }
    
    public boolean addPersonaje(Personaje personaje){
        if (personajesAdentro.size() < capacidad) {
            personaje.setRefugioActual(this);
            personajesAdentro.add(personaje);
            return  true;
        }
        return false;
    }
    
    public void eliminarPersonaje(Personaje personaje){
        if (personajesAdentro.remove(personaje)) {
            personaje.setRefugioActual(null);
        }
    }
    
    public void reparar(){
        this.estabilidad += 5;
        if (this.estabilidad > 100){
            this.estabilidad = 100;
        }
    }

    public int evaluarEstabilidad(){
        return estabilidad;
    }

    public void reducirEstabilidad(int reduccion){
        estabilidad -= reduccion;
        if(estabilidad >= 0) serDestruido();
    }

    public void reducirEnergiaTormenta(){
        int damage = 0;
        if(estabilidad>=70) damage = 1;
        else if(estabilidad>=30) damage = 2;
        else damage = 3;
        for (Personaje personaje : personajesAdentro) {
            personaje.reducirEnergia(damage*10 - 5);
        }
    }

    public void descansarPersonajes(){
        for (Personaje personaje : personajesAdentro) {
            personaje.recuperarEnergia(15);
        }
    }
    
    public void serDestruido(){
        
    }

    public int getCapacidad() {
        return capacidad;
    }

    public ArrayList<Personaje> getPersonajesAdentro() {
        return personajesAdentro;
    }
    
    
}
