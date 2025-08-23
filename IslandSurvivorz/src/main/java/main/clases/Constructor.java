package main.clases;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Constructor extends Personaje {

    private int habilidadConstruccion;

    public Constructor(JLabel label) {
        super("Constructor", label);
        this.habilidadConstruccion = 0; // Nivel inicial de habilidad de construcción
    }

    // Incrementa la habilidad de construcción
    public void levelUp() {
        habilidadConstruccion++;
    }


    // Construir refugios 
    /// T O D O
    public boolean construir() {
        Recurso madera = buscarRecursoPorNombre("Madera");
        if (madera.getCantidad() <= 4) {
            return false;
        }
        madera.usarRecurso(5);
        removerRecursoSiTieneCero(madera);
        reducirEnergia(20);
        return true;
    }

    // Reparar refugios
    public void reparar(Refugio refugio) {
        if (checkForMaterial("Madera")) {
            reducirEnergia(15); // Reduce energía por la reparación
            refugio.reparar(); // Método en Refugio que representa la acción de reparación
            buscarRecursoPorNombre("Madera").usarRecurso(1);
            removerRecursoSiTieneCero(buscarRecursoPorNombre("Madera"));
        }
        
    }

    // Comer para recuperar entre 10 y 20 de energía
    @Override
    public void comer(Recurso comida) {
        comida.usarMedicamento(comida, this);
        if (comida.isMeat(comida) || comida.isFruit(comida)) {
            set_haComido(true);
            recuperarEnergia(5*(comida.getPeso(comida.getTipo()))+5); 
            comida.usarRecurso(1);
            System.out.println("El constructor comio y recupero " + (5*(comida.getPeso(comida.getTipo()))+5) + " de energia.");
        } else if (!( comida.isMedicamento(comida) || comida.isRemedio(comida)))
            JOptionPane.showMessageDialog(null, "El personaje no puede comer eso", "Problema de dieta", JOptionPane.WARNING_MESSAGE);
        
        
        /*int energiaRecuperada = new Random().nextInt(10, 21); // Recupera entre 10 y 20 de energía
        recuperarEnergia(energiaRecuperada);
        comida.usarRecurso(1); // Usa una unidad del recurso consumido*/
    }

    // Método para descansar
    public void descansar() {
        recuperarEnergia(20); // Descanso da +20 de energía
    }

    @Override
    public void accionar(int opcion) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
