package strategy;

import java.util.List;
import java.util.Random;
import model.Arma;
import model.Guerrero;


public class RandomCombination implements EstrategiaAtaque {

    public double ejecutarEstrategia(Guerrero atacante, Guerrero objetivo, Arma arma) {

        List<Guerrero> guerreros = atacante.getJugador().getGuerreros();
        Guerrero otroGuerrero;
        Random random = new Random();


        do {
            otroGuerrero = guerreros.get(random.nextInt(guerreros.size()));
        } while (otroGuerrero == atacante);


        List<Arma> armasOtroGuerrero = otroGuerrero.getArmas();
        Arma otraArma = armasOtroGuerrero.get(random.nextInt(armasOtroGuerrero.size()));


        double danioArmaAtacante = arma.getDanioPorTipo().get(objetivo.getTipo());
        double danioOtraArma = otraArma.getDanioPorTipo().get(objetivo.getTipo());


        double mejorDanio = Math.max(danioArmaAtacante, danioOtraArma);
        
		arma.usarArma(objetivo.getTipo());
        otraArma.usarArma(objetivo.getTipo());

        return mejorDanio;
	}
	
	
}
