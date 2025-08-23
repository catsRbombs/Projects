/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mariopoorty;

/**
 *
 * @author fluff
 */
public class Carta {
    private String valor;
    private String palo;

    public Carta(String valor, String palo) {
        this.valor = valor;
        this.palo = palo;
    }

    public String getValor() {
        return valor;
    }

    public String getPalo() {
        return palo;
    }

    // Método que devuelve el valor numérico de la carta para compararla
    public int obtenerValorNumerico() {
        switch (valor) {
            case "2": return 2;
            case "3": return 3;
            case "4": return 4;
            case "5": return 5;
            case "6": return 6;
            case "7": return 7;
            case "8": return 8;
            case "9": return 9;
            case "10": return 10;
            case "jack": return 11;
            case "queen": return 12;
            case "king": return 13;
            case "ace": return 14;
            default: return 0; // Esto no debería pasar
        }
    }

    // Método que devuelve la ruta de la imagen de la carta
    public String obtenerImagen() {
        return "/data/" + valor + "_of_" + palo + ".png";
    }
}
