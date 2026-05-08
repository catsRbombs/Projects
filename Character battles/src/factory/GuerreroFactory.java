package factory;

import model.Guerrero;
import model.Jugador;
import model.Tipo;

public interface GuerreroFactory {
    Guerrero crearGuerrero(Tipo tipo, Jugador jugador);
}
