package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import strategy.EstrategiaAtaque;

public class Guerrero implements Serializable {
	private static final long serialVersionUID = 1L;
    private String nombre;
    private Tipo tipo;
    private String imagen;
    private List<Arma> armas;
    private double vida;
	private boolean enUso; // Indica si el guerrero está en uso durante el combate
    private Jugador jugador; // Referencia al jugador propietario

    public Guerrero(Tipo tipo, Jugador jugador) {
        this.nombre = tipo.toString();
        this.tipo = tipo;
        this.imagen = tipo.getImagen();
        this.armas = new ArrayList<>();
        this.vida = 100.0;
		this.enUso = false;
        this.jugador = jugador; // Inicializamos la referencia al jugador
    }
    
	public Arma getArmaPorNombre(String nombre) {
		for (Arma arma : armas) {
			if (arma.getNombre().equalsIgnoreCase(nombre)) { // Uso correcto de comparación
				return arma;
			}
		}
		System.out.println("Error buscando arma: " + nombre);
		return null;
	}


    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

	public void setArmas(List<Arma> armas) {
		this.armas = armas;
	}
	
	public boolean isEnUso() {
        return enUso;
    }

	public void setVida(double vida) {
		this.vida = vida;
	}

    public void setEnUso(boolean enUso) {
        this.enUso = enUso;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public String getImagen() {
        return imagen;
    }

    public List<Arma> getArmas() {
        return armas;
    }

    public double getVida() {
        return vida;
    }

    public Jugador getJugador() {
        return jugador;
    }

    // Otros métodos (atacar, recibir daño, etc.)
    public void atacar(Guerrero objetivo, Arma arma, EstrategiaAtaque estrategia) {
        double danio = estrategia.ejecutarEstrategia(this, objetivo, arma);
        objetivo.recibirDanio(danio);
    }

    public void recibirDanio(double danio) {
        vida -= danio;
        if (vida < 0) {
            vida = 0;
        }
    }

    public boolean estaVivo() {
        return vida > 0;
    }
	
	public String toString() {
    return "Guerrero{" +
            "nombre='" + nombre + '\'' +
            ", tipo=" + tipo +
            ", vida=" + vida +
            ", armas=" + armas +
            '}';
}
}
