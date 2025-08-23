package main.clases;

import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Recolector extends Personaje {

    private int habilidadRecoleccion;

    public Recolector(JLabel label) {
        super("Recolector", label);
        this.habilidadRecoleccion = 0; // Nivel inicial de habilidad de recolección
    }

    // Incrementa la habilidad de recolección
    public void levelUp() {
        habilidadRecoleccion++;
    }


    public void recolectar(Recurso recurso) {
        int energiaUsada = new Random().nextInt(10, 21); 
        if(getEnergia() >= energiaUsada){
        agregarRecurso(recurso); 
        reducirEnergia(energiaUsada); }
        }

    // Comer frutas para recuperar energía
    @Override
    public void comer(Recurso comida){
        comida.usarMedicamento(comida, this);
        if (comida.isFruit(comida)){
            set_haComido(true);
            recuperarEnergia(5*(comida.getPeso(comida.getTipo()))+5); 
            comida.usarRecurso(1);      
            return;
        } else if (!( comida.isMedicamento(comida) || comida.isRemedio(comida)))
        JOptionPane.showMessageDialog(null, "El personaje no puede comer eso", "Problema de dieta", JOptionPane.WARNING_MESSAGE);
    }

    // Método para descansar
    public void descansar() {
        recuperarEnergia(15); // Descanso da +15 de energía
    }

    @Override
    public void accionar(int opcion) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
