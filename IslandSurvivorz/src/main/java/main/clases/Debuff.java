package main.clases;

public abstract class Debuff {
    private int stage;
    private String tipo;

    public Debuff(int stage, String tipo) {
        this.stage = stage;
        this.tipo = tipo;
    }

    public int getStage() {
        return stage;
    }
    
    public String getTipo() {
        return tipo;
        
    }
    
    public void setStage(int nuevo) {
        stage = nuevo;
    }
    
    public abstract void afectar(Personaje personaje);

    public abstract void desaparecer(Personaje personaje);
    
    public abstract void subirStage();

}
