package main.clases;

import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Curandero extends Personaje {

    private int habilidadCurar;

    public Curandero(JLabel label) {
        super("Curandero", label);
        this.habilidadCurar = 0; // Nivel inicial de habilidad de curación
    }

    // Incrementa la habilidad de curar
    public void levelUp() {
        habilidadCurar++;
    }

    // Método para curar a otro personaje usando plantas medicinales
    public boolean accionar(Personaje paciente) { 
        if(getEnergia()>=10){
            if (checkForMaterial("Agua") && checkForMaterial("Hojas")){
                if(removeMaterial("Agua", 2) && removeMaterial("Hojas", 2)){
            int saludRecuperada = new Random().nextInt(15, 21); // Recupera entre 15 y 20 de salud
            paciente.recuperarSalud(saludRecuperada);
            reducirEnergia(10); // Reduce 10 puntos de energía por curación
            if(paciente.statusEnfermedad()==1) paciente.eliminarEnfermedad();
            if(paciente.statusLesion()==1) paciente.borrarLesion();
            System.out.println(paciente.getNombre() + " ha sido curado. Salud restaurada: " + saludRecuperada);
            return true;
        }}}
        return false;
    }

    // Método para preparar remedios con plantas medicinales
    public boolean crearMedicamento() {
        if(getEnergia()>=10){
        if (checkForMaterial("Agua") && checkForMaterial("Hojas")){
            if(removeMaterial("Agua", 2) && removeMaterial("Hojas", 2)){
            reducirEnergia(10); 
            Recurso newmedicine = new Recurso("Remedio", 1);
            agregarRecurso(newmedicine);
            return true;
            }
        }}
        return false;
    }

    // Método para recolectar plantas (menos eficiente que el recolector)
    public void recolectar(Recurso planta) {
        agregarRecurso(planta); // Añade al inventario la planta recolectada
        reducirEnergia(10); // Reduce 10 puntos de energía por la recolección
    }

    // Método para comer y recuperar entre 10 y 15 de energía
    @Override
    public void comer(Recurso comida){
        comida.usarMedicamento(comida, this);
        if (comida.isMeat(comida) || comida.isFruit(comida)) {
            set_haComido(true);
            recuperarEnergia(5*(comida.getPeso(comida.getTipo()))+5); 
            comida.usarRecurso(1);
            System.out.println("El curandero comio y recupero " + (5*(comida.getPeso(comida.getTipo()))+5) + " de energia.");
        } else if (!( comida.isMedicamento(comida) || comida.isRemedio(comida)))
            JOptionPane.showMessageDialog(null, "El personaje no puede comer eso", "Problema de dieta", JOptionPane.WARNING_MESSAGE);             
    }

    // Método para descansar y recuperar 10 de energía
    public void descansar() {
        recuperarEnergia(10); // Recupera 10 puntos de energía
    }

    @Override
    public void accionar(int opcion) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
