package factory;

import model.Arma;
import model.Guerrero;
import model.Jugador;
import model.Tipo;
import java.util.ArrayList;
import java.util.List;



public class GuerreroFactoryImpl implements GuerreroFactory {
    @Override
    public Guerrero crearGuerrero(Tipo tipo, Jugador jugador) {
        Guerrero guerrero = new Guerrero(tipo, jugador);
        guerrero.setArmas(generarArmas(tipo.toString()));
        return guerrero;
    }

	public static List<Arma> generarArmas(String nombre) {
		List<Arma> listaArmas = new ArrayList<>();

		switch (nombre.toUpperCase()) {
			case "ACIDO": // Kog'Maw - Acido
				listaArmas.add(new Arma("Artilleri_viviente")); // Pasiva
				listaArmas.add(new Arma("Baba_corrosiva"));
				listaArmas.add(new Arma("Disparo_bioarcano"));
				listaArmas.add(new Arma("Rafaga_de_vacio"));
				listaArmas.add(new Arma("Explosion_icorosa"));
				break;
			case "AGUA": // Nami - Agua
				listaArmas.add(new Arma("Oleaje")); // Pasiva
				listaArmas.add(new Arma("Prision_de_agua"));
				listaArmas.add(new Arma("Bendicion_de_la_marea"));
				listaArmas.add(new Arma("Ebullicion"));
				listaArmas.add(new Arma("Maremoto"));
				break;
			case "AIRE": // Janna - Aire
				listaArmas.add(new Arma("Empuje_de_viento")); // Pasiva
				listaArmas.add(new Arma("Ventisca"));
				listaArmas.add(new Arma("Ojo_de_la_tormenta"));
				listaArmas.add(new Arma("Monzon"));
				listaArmas.add(new Arma("Zefiro"));
				break;
			case "ELECTRICIDAD": // Zeri - Electricidad
				listaArmas.add(new Arma("Bateria_cargada")); // Pasiva
				listaArmas.add(new Arma("Fuego_explosivo"));
				listaArmas.add(new Arma("Ultradescarga_laser"));
				listaArmas.add(new Arma("Subida_de_tension"));
				listaArmas.add(new Arma("Choque_de_rayos"));
				break;
			case "ESPIRITUALIDAD": // Aurora - Espiritualidad
				listaArmas.add(new Arma("Toque_espiritual")); // Pasiva
				listaArmas.add(new Arma("Refugio_mistico"));
				listaArmas.add(new Arma("Explosion_astral"));
				listaArmas.add(new Arma("Camino_etereo"));
				listaArmas.add(new Arma("Resonancia_divina"));
				break;
			case "FUEGO": // Brand - Fuego
				listaArmas.add(new Arma("Flama_abrasadora")); // Pasiva
				listaArmas.add(new Arma("Quemadura"));
				listaArmas.add(new Arma("Pilar_de_fuego"));
				listaArmas.add(new Arma("Conflagracion"));
				listaArmas.add(new Arma("Tormenta_ignea"));
				break;
			case "HIELO": // Anivia - Hielo
				listaArmas.add(new Arma("Rocio_helado")); // Pasiva
				listaArmas.add(new Arma("Lanza_de_hielo"));
				listaArmas.add(new Arma("Muro_glaciar"));
				listaArmas.add(new Arma("Ventisca"));
				listaArmas.add(new Arma("Tormenta_helada"));
				break;
			case "HIERRO": // Rell - Hierro
				listaArmas.add(new Arma("Rompe_moldes")); // Pasiva
				listaArmas.add(new Arma("Golpe_aplastante"));
				listaArmas.add(new Arma("Ferromancia"));
				listaArmas.add(new Arma("Atraccion_magnetica"));
				listaArmas.add(new Arma("Tempestad_de_hierro"));
				break;
			case "MAGIA_BLANCA": // Lux - Magia Blanca
				listaArmas.add(new Arma("Iluminacion")); // Pasiva
				listaArmas.add(new Arma("Prision_de_luz"));
				listaArmas.add(new Arma("Barrera_prismatica"));
				listaArmas.add(new Arma("Singularidad brillante"));
				listaArmas.add(new Arma("Chispa_final"));
				break;
			case "MAGIA_OSCURA": // Syndra - Magia Oscura
				listaArmas.add(new Arma("Transcendencia_oscura")); // Pasiva
				listaArmas.add(new Arma("Esfera_oscura"));
				listaArmas.add(new Arma("Voluntad_imperial"));
				listaArmas.add(new Arma("Dispersion_de_los_debiles"));
				listaArmas.add(new Arma("Desencadenar_poder"));
				break;
			default:
				System.out.println("Personaje no reconocido. No se generaron armas.");
				break;
		}

		return listaArmas;
	}

}
