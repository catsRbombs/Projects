package main.clases;

import java.util.Random;
import javax.swing.JLabel;

public class Animal {
    private String tipoAnimal;
    private String nombre;
    private int fuerza;
    private int dificultadCaza;
    private int comidaProporcionada;
    private boolean agresivo;
    private JLabel label;
    
    public Animal(String tipoAnimal, String nombre, int fuerza, int dificultadCaza, int comidaProporcionada, boolean agresivo) {
        this.tipoAnimal = tipoAnimal;
        this.nombre = nombre;
        this.fuerza = fuerza;
        this.dificultadCaza = dificultadCaza;
        this.comidaProporcionada = comidaProporcionada;
        this.agresivo = agresivo;
    }

    public Animal(String tipoAnimal, JLabel label){
        if(tipoAnimal == "Cerdo"){
            this.tipoAnimal = "Placeholder1";
            this.nombre = "Placeholder1";
            this.fuerza = 10;
            this.dificultadCaza = 5;
            this.comidaProporcionada = 5;
            this.agresivo=false;
            this.label = label;
        }

        if(tipoAnimal == "Lobo"){
            this.tipoAnimal = "Placeholder2";
            this.nombre = "Placeholder2";
            this.fuerza = 15;
            this.dificultadCaza = 4;
            this.comidaProporcionada = 7;
            this.agresivo=true;
            this.label = label;
        }

        if(tipoAnimal == "Oso"){
            this.tipoAnimal = "Placeholder3";
            this.nombre = "Placeholder3";
            this.fuerza = 20;
            this.dificultadCaza = 3;
            this.comidaProporcionada = 9;
            this.agresivo=true;
            this.label = label;
        }
    }

    public void atacarPersonaje(Personaje personaje) {
        Random rand = new Random();
        int damage = rand.nextInt(5, fuerza);
        personaje.reducirSalud(damage);
    }

    public void morir() {
        System.out.println(nombre + " ha sido cazado.");
    }

    public void serCazado(Cazador cazador) {
        if (agresivo) {
            atacarPersonaje(cazador);
        }
        
        if(cazador.getVida() == 0) return;
        Recurso carne = new Recurso("placeholder", comidaProporcionada);
        carne.setTipo(carne.getRandomMeat());
        cazador.agregarRecurso(carne);
        morir();
        cazador.levelUp();}
    

    public int getFuerza() {
        return fuerza;
    }

    public int getComidaProporcionada() {
        return comidaProporcionada;
    }

    public boolean isAgresivo() {
        return agresivo;
    }

    public String getTipoAnimal() {
        return tipoAnimal;
    }
    
    public JLabel getLabel() {
        return label;
    }
}
