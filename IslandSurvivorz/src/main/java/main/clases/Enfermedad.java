package main.clases;

public class Enfermedad extends Debuff {

    public Enfermedad(int stage) {
        super(stage, "Enfermedad"); // Llamar al constructor de la clase padre
    }

    @Override
    public void afectar(Personaje personaje) {
        personaje.reducirSalud(getStage()*10);
    }
    @Override
    public void desaparecer(Personaje personaje) {
        personaje.borrarEnfermedad();
}

    @Override
    public void subirStage() {
       if(getStage()==1) setStage(2);
    }
}