package main.clases;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Explorador extends Personaje {
    
    int nivelExploracion=0; 

    public Explorador(JLabel label) {
        super ("Explorador", label);
    }

    public void levelUp() {
        nivelExploracion++;
    }

    public void comer(Recurso comida){
        comida.usarMedicamento(comida, this);
        if (comida.isMeat(comida) || comida.isFruit(comida)) {
            set_haComido(true);
            recuperarEnergia(5*(comida.getPeso(comida.getTipo()))+5); 
            comida.usarRecurso(1);
            System.out.println("El explorador comio y recupero " + (5*(comida.getPeso(comida.getTipo()))+5) + " de energia.");
        } else if (!( comida.isMedicamento(comida) || comida.isRemedio(comida)))
            JOptionPane.showMessageDialog(null, "El personaje no puede comer eso", "Problema de dieta", JOptionPane.WARNING_MESSAGE);          
    }

    public void accionar(int opcion){
        
    }
    

    public void descansar(){
        recuperarEnergia(20);
    }

    public void recolectar(Recurso item){
        if(getEnergia()>=5){
        agregarRecurso(item);
        reducirEnergia(5);
    }}
}


// P1 B: Explorador

// Explora la isla :0

// Atributos:

// nivelExploracion


// Acciones: 

// Accionar / Explorar: Descubre areas/recursos. La energia baja entre distancia y dificultad del terreno. 
// costo: 15 a 30 por exploracion exitosa
// Si recolecta durante expedicion, -5 energia

// Comer: 10 a 20 energia
// Su descanso le da +20 energia
