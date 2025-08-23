package main.clases;

import javax.swing.JLabel;
import java.util.Random;
import javax.swing.JOptionPane;

public class Cientifico extends Personaje {

    private int habilidadCiencia;

    public Cientifico(JLabel label) {
        super("Cientifico", label);
        this.habilidadCiencia = 0; // Nivel inicial de habilidad científica
    }

    // Incrementa la habilidad científica
    public void levelUp() {
        habilidadCiencia++;
    }

    // Método para crear medicamentos
    public void crearMedicamento() {
        if (checkForMaterial("Agua") && checkForMaterial("Ciencia")){
            if(removeMaterial("Agua", 2) && removeMaterial("Ciencia", 1)){
            int exito = new Random().nextInt(0, 100);
            reducirEnergia(15); 

            if (exito > 30) { // El medicamento tiene un 70% de probabilidad de éxito
                Recurso newmedicine = new Recurso("Medicina", 1);
                agregarRecurso(newmedicine);
            }

            else System.out.println("fallo la creacion de medicamento!");
        }}
    }

    // Método para comer y recuperar entre 10 y 20 de energía
    @Override
    public void comer(Recurso comida){
        comida.usarMedicamento(comida, this);
        if (comida.isMeat(comida) || comida.isFruit(comida) ) {
            set_haComido(true);
            recuperarEnergia(5*(comida.getPeso(comida.getTipo()))+5); 
            comida.usarRecurso(1);
            System.out.println("El cientifico comio y recupero " + (5*(comida.getPeso(comida.getTipo()))+5) + " de energia.");
        } else if (!( comida.isMedicamento(comida) || comida.isRemedio(comida)))
            JOptionPane.showMessageDialog(null, "El personaje no puede comer eso", "Problema de dieta", JOptionPane.WARNING_MESSAGE);   
    }

    // Método para descansar y recuperar 15 de energía
    public void descansar() {
        recuperarEnergia(15); // Recupera 15 puntos de energía al descansar
    }
    
    @Override
    public void accionar(int opcion) {
        crearMedicamento();
    }
}
