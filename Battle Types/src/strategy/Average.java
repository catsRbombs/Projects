package strategy;

import model.Arma;
import model.Guerrero;
import model.Jugador;

import java.util.List;

public class Average implements EstrategiaAtaque {
    private List<Jugador> jugadores;

    public Average(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    @Override
    public double ejecutarEstrategia(Guerrero atacante, Guerrero objetivo, Arma arma) {
        double sumaDanio = 0.0;
        int contador = 0;

        for (Jugador jugador : jugadores) {
            for (Guerrero guerrero : jugador.getGuerreros()) {
                for (Arma a : guerrero.getArmas()) {
                    sumaDanio += a.getDanioPorTipo().get(objetivo.getTipo());
                    contador++;
                }
            }
        }

        double promedioDanio = sumaDanio / contador;
        arma.usarArma(objetivo.getTipo());
        return promedioDanio;
    }
}
