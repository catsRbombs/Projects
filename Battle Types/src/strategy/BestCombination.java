package strategy;

import model.Arma;
import model.Guerrero;

import java.util.List;

public class BestCombination implements EstrategiaAtaque {
    @Override
    public double ejecutarEstrategia(Guerrero atacante, Guerrero objetivo, Arma arma) {
        double mejorDanio = 0.0;
        List<Guerrero> guerreros = atacante.getJugador().getGuerreros();

        for (Guerrero g : guerreros) {
            for (Arma a : g.getArmas()) {
                if (!a.isUsada()) {
                    double danio = a.getDanioPorTipo().get(objetivo.getTipo());
                    if (danio > mejorDanio) {
                        mejorDanio = danio;
                    }
                }
            }
        }
        // Marcar todas las armas utilizadas en esta estrategia como usadas
        for (Guerrero g : guerreros) {
            for (Arma a : g.getArmas()) {
                if (!a.isUsada() && a.getDanioPorTipo().get(objetivo.getTipo()) == mejorDanio) {
                    a.usarArma(objetivo.getTipo());
                    break;
                }
            }
        }
        return mejorDanio;
    }
}