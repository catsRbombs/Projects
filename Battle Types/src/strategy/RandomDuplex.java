package strategy;




import model.Arma;
import model.Guerrero;
import model.Tipo;

import java.util.Random;


public class RandomDuplex implements EstrategiaAtaque {
	
	@Override
	public double ejecutarEstrategia(Guerrero atacante, Guerrero objetivo, Arma arma) {
		
		Random random = new Random();
		Tipo[] tipos = Tipo.values();
		Tipo tipoAleatorio = tipos[random.nextInt(tipos.length)];
		
		double dmgBase = arma.usarArma(objetivo.getTipo());
		double dmgExtra = 0.0;
		
		if (tipoAleatorio == objetivo.getTipo()){
			dmgExtra = dmgBase;
		}
		
		return dmgBase + dmgExtra;
		
	}		
}
