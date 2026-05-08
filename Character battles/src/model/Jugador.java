package model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Jugador implements Serializable {
	 private static final long serialVersionUID = 1L;
    private String nickname;
    private List<Guerrero> guerreros;
    private Puntuacion puntuacion;
	private boolean enCombate;

	private boolean tieneComodin;
    private boolean comodinPositivo;
    private int ataquesRestantes; // Para manejar ataques adicionales
	
    public Jugador(String nickname) {
        this.nickname = nickname;
        this.guerreros = new ArrayList<>();
        this.puntuacion = new Puntuacion();
		this.tieneComodin = false;
		this.enCombate = false; 
        this.comodinPositivo = false;
        this.ataquesRestantes = 0;
    }
	
	public boolean isEnCombate() {
        return enCombate;
    }
    
    public void setEnCombate(boolean enCombate) {
        this.enCombate = enCombate;
    }
	
	    // Getters y Setters
    public boolean isTieneComodin() {
        return tieneComodin;
    }

    public void setTieneComodin(boolean tieneComodin) {
        this.tieneComodin = tieneComodin;
    }

    public boolean isComodinPositivo() {
        return comodinPositivo;
    }

    public void setComodinPositivo(boolean comodinPositivo) {
        this.comodinPositivo = comodinPositivo;
    }

    public int getAtaquesRestantes() {
        return ataquesRestantes;
    }

    public void setAtaquesRestantes(int ataquesRestantes) {
        this.ataquesRestantes = ataquesRestantes;
    }

    public void decrementarAtaquesRestantes() {
        if (ataquesRestantes > 0) {
            ataquesRestantes--;
        }
    }

	public void addGuerrero(Guerrero g1) {
		this.guerreros.add(g1);
	}

	public String getNickname() {
		return nickname;
	}

    public List<Guerrero> getGuerreros() {
        return guerreros;
    }

    public Puntuacion getPuntuacion() {
        return puntuacion;
    }
	
	public Guerrero getGuerreroPorNombre(String Nombre) {
		System.out.println("Buscando guerrero: " + Nombre);
		for (Guerrero guerrero : guerreros) {
			if (guerrero.getNombre().equalsIgnoreCase(Nombre)) { // Uso correcto de comparación
				System.out.println("Guerrero encontrado " + guerrero.getNombre());
				return guerrero;
			}
		}
		System.out.println("Error buscando guerrero: " + Nombre);
		return null;
	}

    // Métodos para acceder a las estadísticas
    public int getWins() {
        return puntuacion.getWins();
    }

    public int getLoses() {
        return puntuacion.getLoses();
    }

    public int getAttacks() {
        return puntuacion.getAttacks();
    }

    public int getSuccess() {
        return puntuacion.getSuccess();
    }

    public int getFailed() {
        return puntuacion.getFailed();
    }

    public int getGiveup() {
        return puntuacion.getGiveup();
    }
	
	// Setters
    public void setWins(int wins) {
        puntuacion.setWins(wins);
    }

    public void setLoses(int loses) {
        puntuacion.setLoses(loses);
    }

    public void setAttacks(int attacks) {
        puntuacion.setAttacks(attacks);
    }

    public void setSuccess(int success) {
        puntuacion.setSuccess(success);
    }

    public void setFailed(int failed) {
        puntuacion.setFailed(failed);
    }

    public void setGiveup(int giveup) {
        puntuacion.setGiveup(giveup);
    }

    // Métodos para actualizar estadísticas
    public void incrementarWins() {
        puntuacion.incrementarWins();
    }

    public void incrementarLoses() {
        puntuacion.incrementarLoses();
    }

    public void incrementarAttacks() {
        puntuacion.incrementarAttacks();
    }

    public void incrementarSuccess() {
        puntuacion.incrementarSuccess();
    }

    public void incrementarFailed() {
        puntuacion.incrementarFailed();
    }

    public void incrementarGiveup() {
        puntuacion.incrementarGiveup();
    }


    // Método para seleccionar un guerrero disponible
    public Guerrero seleccionarGuerrero() {
        for (Guerrero guerrero : guerreros) {
            if (guerrero.getVida() > 0 && !guerrero.isEnUso()) {
                return guerrero;
            }
        }
        return null;
    }
	
	public boolean haSidoDerrotado() {
            int lossCounter = 0;
		for (Guerrero guerrero : guerreros){
			if (guerrero.getVida() <= 0 ){
				lossCounter++;
			}
		}
        if(lossCounter == 4) return true;
        return false;
    }
	
	@Override
	public String toString() {
    return "Jugador{" +
            "nickname='" + nickname + '\'' +
            ", wins=" + getWins() +
            ", loses=" + getLoses() +
            ", attacks=" + getAttacks() +
            ", success=" + getSuccess() +
            ", failed=" + getFailed() +
            ", giveup=" + getGiveup() +
            ", tieneComodin=" + tieneComodin +
            ", comodinPositivo=" + comodinPositivo +
            ", ataquesRestantes=" + ataquesRestantes +
            ", guerreros=" + guerreros +
            '}';
}
}
	