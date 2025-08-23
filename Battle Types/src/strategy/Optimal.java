package strategy;

import model.Arma;
import model.Guerrero;
import model.Jugador;


import java.util.List;

public class Optimal implements EstrategiaAtaque {
    private List<Jugador> jugadores;

    public Optimal(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    @Override
    public double ejecutarEstrategia(Guerrero atacante, Guerrero objetivo, Arma arma) {
        double mejorDanio = 0.0;

        for (Jugador jugador : jugadores) {
            for (Guerrero guerrero : jugador.getGuerreros()) {
                for (Arma a : guerrero.getArmas()) {
                    double danio = a.getDanioPorTipo().get(objetivo.getTipo());
                    if (danio > mejorDanio) {
                        mejorDanio = danio;
                    }
                }
            }
        }
        arma.usarArma(objetivo.getTipo());
        return mejorDanio;
    }
}
