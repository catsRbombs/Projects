package main.clases;

public class Lesion extends Debuff {
    public Lesion(int stage) {
        super(stage, "Lesion"); // Llamar al constructor de la clase padre
    }


    public void afectar(Personaje personaje) {
        personaje.reducirSalud(getStage()*10);
        personaje.reducirEnergia((getStage()*10)-5);
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
