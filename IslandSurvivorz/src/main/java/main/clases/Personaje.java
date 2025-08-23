/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.clases;

import java.awt.Point;

import javax.swing.JLabel;

/**
 *
 * @author Efraim Cuevas
 */
public abstract class Personaje {
    private String nombre;
    private int nivelEnergia;
    private int nivelSalud;
    private Recurso inventario[];
    private Refugio refugioActual;
    private JLabel label;
    private Debuff[] debuffs;
    private boolean haComido;
    private int nivelHambre;
    public int xDestino = -1;
    public int yDestino = -1;

    public Personaje(String nombre, JLabel label) {
        this.nombre = nombre;
        this.nivelEnergia = 100;
        this.nivelSalud = 100;
        this.inventario = new Recurso[9];
        this.label = label;
        refugioActual = null;
        this.debuffs = new Debuff[2];
        haComido = false;
        nivelHambre = 0;
    }

    public void set_haComido(boolean nuevo) { haComido = nuevo;}
    
    public int getNivelHambre() {return nivelHambre;}

    public void updateHambre() { if(!haComido) { nivelHambre++; reducirEnergia(nivelHambre*nivelHambre+5); } else nivelHambre=0; haComido = false;}

    public boolean checkForMaterial(String Material){
        for (int i = 0; i < 8; i++) {
            if (inventario[i]!= null && inventario[i].getTipo() == Material) {
                return true;
            }
        }
        return false;
    }

    public boolean removeMaterial(String Material ,int copies){
        for (int i = 0; i < 8; i++) {
            if (inventario[i]!= null && inventario[i].getTipo() == Material) {
                if (inventario[i].getCantidad() >= copies) {
                    inventario[i].usarRecurso(copies);
                    if(inventario[i].getCantidad()==0) removerRecursoSiTieneCero(inventario[i]);
                } else {
                    return false;
                }
                return true;
            }
        }   
        return false;
    }

    public void nuevaEnfermedad(Enfermedad nuevaEnfermedad) {
        if (debuffs[0] == null) {
            debuffs[0] = nuevaEnfermedad;
        }
    }

    public int statusEnfermedad(){
        if(debuffs[0]!=null) return debuffs[0].getStage();
        return 0;
    }

    public int statusLesion() {
        if(debuffs[1]!=null) return debuffs[1].getStage();
        return 0;
    }

    public void nuevaLesion(Lesion nuevaLesion) {
        if (debuffs[1] == null) {
            debuffs[1] = nuevaLesion;
        }
    }
    public void actualizarDebuffs() {//+
        for (int i = 0; i < 2; i++) {//+
            if (debuffs[i] != null) {//+
                debuffs[i].afectar(this);//+
                debuffs[i].subirStage();
            }
        }
    }


    public void eliminarEnfermedad() {
        if (debuffs[0] != null) {
            debuffs[0].desaparecer(this); 
            debuffs[0] = null;}
    }

    public void borrarLesion() {
        if (debuffs[1] != null) {
            debuffs[1] = null;
        }
    }
    public void borrarEnfermedad() {
        debuffs[0] = null;
    }
    
    public boolean isMovingX(){
        return xDestino != -1;
    }
    
    public boolean isMovingY(){
        return yDestino != -1;
    }
    
    public void checkArrival(){
        Point ubicacion = label.getLocation();

        int margen = 10;
    
        // Verifica si ubicacion.x está dentro del rango de tolerancia
        if (ubicacion.x >= xDestino - margen && ubicacion.x <= xDestino + margen) {
        xDestino = -1; // Marcamos xDestino como alcanzado
         }
    
        // Verifica si ubicacion.y está dentro del rango de tolerancia
        if (ubicacion.y >= yDestino - margen && ubicacion.y <= yDestino + margen) {
            yDestino = -1; // Marcamos yDestino como alcanzado
        }
        
    }
    
    public abstract void comer(Recurso comida);
    
    public void reducirEnergia(int cantidad){
        nivelEnergia -= cantidad;
        if (nivelEnergia < 0) {
            nivelEnergia = 0;
        }
    }
    
    public void reducirSalud(int cantidad){
        nivelSalud -= cantidad;
        if (nivelSalud < 0) {
            nivelSalud = 0;
        }
    }
    
    public void recuperarEnergia(int cantidad){
        nivelEnergia += cantidad;
        if (nivelEnergia > 100) {
            nivelEnergia = 100;
        }
    }
    
    public void recuperarSalud(int cantidad){
        nivelSalud += cantidad;
        if (nivelSalud > 100) {
            nivelSalud = 100;
        }
    }
    
    public void compartirRecurso(Personaje receptor, Recurso recurso){
        Recurso buscado = buscarRecursoPorNombre(recurso.getTipo());
        
        /*if (buscado != null && inventario.remove(buscado)) {
            receptor.inventario.add(recurso);
        }*/
        
        if (buscado != null) {
            receptor.agregarRecurso(buscado);
            
            int indexQuitado = buscarRecursoIndexPorNombre(recurso.getTipo());
            inventario[indexQuitado] = null;
        }
    }
    
    public boolean agregarRecurso(Recurso _recurso){
        for (int i = 0; i < 9; i++) {
            if (inventario[i] != null && buscarRecursoIndexPorNombre(_recurso.getTipo()) == i && checkForMaterial(_recurso.getTipo())) { //Añadir un recurso que ya haya en el inventario
                inventario[i].agregarRecurso(_recurso.getCantidad());
                return true;
            } else if (inventario[i] == null) { //Añadir si no existe
                inventario[i] = _recurso;
                return true;
            }
        }   
        return false;
    }
    
    public int estaEnfermo(){
        if(debuffs[0]!=null){
            if(debuffs[0].getStage() == 1) return 1;
            return 2;
        }
        return 0;
    }
    
    public int estaLesionado(){
        if(debuffs[1]!=null){
            if(debuffs[1].getStage() == 1) return 1;
            return 2;
        }
        return 0;
    }
    
    public boolean removerRecursoSiTieneCero(Recurso recurso){
        Recurso borrado = buscarRecursoPorNombre(recurso.getTipo());
        if (borrado != null && recurso.getCantidad() == 0) {
            int indexQuitado = buscarRecursoIndexPorNombre(recurso.getTipo());
            inventario[indexQuitado] = null;
            return true;
        }
        return false;
    }
    
        
    public Recurso buscarRecursoPorNombre(String nombre){
        for (Recurso recurso : inventario) {
            if (recurso != null && recurso.getTipo() == nombre) {
                return recurso;
            }
        }
        return null;
    }
    
    public int buscarRecursoIndexPorNombre(String nombre){
        for (int i = 0; i < 9; i++) {
            if (inventario[i] != null && inventario[i].getTipo() == nombre) {
                return i;
        }
        }
        return -1;
    }
    /*TODO: métodos y señales para que el personaje se mueva alrededor del mapa
      Lo vemos después */

    public void setRefugioActual(Refugio refugioActual) {
        this.refugioActual = refugioActual;
    }
    
    public boolean quedaEspacioEnInventario(){
        for (int i = 0; i < 9; i++) {
            if (inventario[i] == null) {
                return true;
            }
        }
        return false;
    }

    public String getNombre(){
        return nombre;
    }

    public Recurso[] getInventario(){
        return inventario;
    }

    public int getEnergia(){
        return nivelEnergia;
    }
    
    public int getVida(){
        return nivelSalud;
    }

    public JLabel getLabel() {
        return label;
    }

    public Refugio getRefugioActual() {
        return refugioActual;
    }
    
    
    
    public abstract void accionar(int accion);
 
}
 