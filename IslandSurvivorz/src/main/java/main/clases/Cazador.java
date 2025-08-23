package main.clases;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Cazador extends Personaje {

    private int nivelCaza = 0;

    public Cazador(JLabel label) {
        super("Cazador", label);
    }

    public void levelUp() {
        nivelCaza++;
        System.out.println("¡Nivel de caza aumentado! Nivel actual: " + nivelCaza);
    }

    //@Override
    public void accionar(Animal animal) {
        cazar(animal);
    }

    public boolean cazar(Animal animal) {
        System.out.println("Intentando cazar a " + animal.getTipoAnimal());
        if(getEnergia()>=animal.getFuerza()){
            animal.serCazado(this);
            reducirEnergia(animal.getFuerza());  
            return true;
        }
        return false;
    }

    public void defender(Personaje personaje, Atacante atacante) {
        if(getEnergia()>=25){
            reducirSalud(atacante.getFuerza());
            reducirEnergia(25);
            atacante.morir();
        }
        else{
            personaje.reducirSalud(atacante.getFuerza());
        }
    }

    @Override
    public void comer(Recurso comida) {
        comida.usarMedicamento(comida, this);
        if (comida.isMeat(comida)) {
            set_haComido(true);
            recuperarEnergia(20);
            comida.usarRecurso(1);
            System.out.println("El cazador comio carne y recupero 20 de energia.");
        } else if (!( comida.isMedicamento(comida) || comida.isRemedio(comida))) { 
            System.out.println("El cazador no puede comer eso.");
            JOptionPane.showMessageDialog(null, "El personaje no puede comer eso", "Problema de dieta", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void descansar() {
        recuperarEnergia(15);
        recuperarSalud(10);
        System.out.println("El cazador descansó y recuperó 15 de energía y 10 de salud.");
    }

    @Override
    public void accionar(int opcion) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
