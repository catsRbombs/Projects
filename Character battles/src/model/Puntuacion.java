package model;

import java.io.Serializable;


public class Puntuacion implements Serializable{
	private static final long serialVersionUID = 1L;
    private int wins;
    private int loses;
    private int attacks;
    private int success;
    private int failed;
    private int giveup;

    public Puntuacion() {
        this.wins = 0;
        this.loses = 0;
        this.attacks = 0;
        this.success = 0;
        this.failed = 0;
        this.giveup = 0;
    }

    // Getters
    public int getWins() {
        return wins;
    }

    public int getLoses() {
        return loses;
    }

    public int getAttacks() {
        return attacks;
    }

    public int getSuccess() {
        return success;
    }

    public int getFailed() {
        return failed;
    }

    public int getGiveup() {
        return giveup;
    }

    // Setters
    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public void setAttacks(int attacks) {
        this.attacks = attacks;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public void setGiveup(int giveup) {
        this.giveup = giveup;
    }

    // Métodos para incrementar las estadísticas
    public void incrementarWins() {
        this.wins++;
    }

    public void incrementarLoses() {
        this.loses++;
    }

    public void incrementarAttacks() {
        this.attacks++;
    }

    public void incrementarSuccess() {
        this.success++;
    }

    public void incrementarFailed() {
        this.failed++;
    }

    public void incrementarGiveup() {
        this.giveup++;
    }

    // Métodos para reiniciar estadísticas (si es necesario)
    public void resetPuntuacion() {
        wins = 0;
        loses = 0;
        attacks = 0;
        success = 0;
        failed = 0;
        giveup = 0;
    }
	
	    // Método para calcular el puntaje total
    public int calcularPuntuacionTotal() {
        int puntuacion = 0;
        puntuacion += (wins * 30);
        puntuacion += (loses * 10);
        puntuacion += (giveup * 3);
        puntuacion += (success * 3);
        puntuacion += (failed * 1);
        return puntuacion;
    }
}
