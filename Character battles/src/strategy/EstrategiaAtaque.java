package strategy;



import model.Arma;
import model.Guerrero;

public interface EstrategiaAtaque {
    double ejecutarEstrategia(Guerrero atacante, Guerrero objetivo, Arma arma);
}
