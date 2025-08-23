
package main.clases;
import java.util.List;
import java.util.Random;
import javax.swing.JLabel;

public class Recurso {

    private List<String> smallFood = List.of(
        "Fig",               // 1
        "Avocado",           // 2
        "Plum",              // 4
        "Orange slice",      // 6
        "Lemon",             // 7
        "Rambutan",          // 19
        "Lychee",            // 14
        "Lime",              // 22
        "Kiwi",              // 24
        "Blueberry",         // 37
        "Pomegranate",       // 47
        "Snail-shaped fruit candy", // 40
        "Pear",              // 9
        "Peach",             // 5
        "Guava",             // 26
        "Chicken breast",    // 2 (Meat)
        "Drumstick",         // 16 (Meat)
        "Turkey breast",     // 26 (Meat)
        "Chicken wing"       // 39 (Meat)
    );

    private List<String> mediumFood = List.of(
        "Grapefruit",        // 8
        "Apple",             // 12
        "Papaya",            // 17
        "Mangosteen (open)", // 20
        "Starfruit",         // 21
        "Rose apple (Java apple)", // 28
        "Green mango",       // 42
        "Steak",             // 3 (Meat)
        "Pork chop",         // 4 (Meat)
        "Liver",             // 1 (Meat)
        "Lamb chop",         // 13 (Meat)
        "Beef strip",        // 14 (Meat)
        "Meat loaf",         // 17 (Meat)
        "Sausage link",      // 18 (Meat)
        "Rib roast",         // 19 (Meat)
        "Beef roast",        // 27 (Meat)
        "Meat shank",        // 29 (Meat)
        "Pork belly",        // 30 (Meat)
        "Brisket",           // 37 (Meat)
        "Beef cubes"         // 40 (Meat)
    );

    private List<String> bigFood = List.of(
        "Durian",            // 33
        "Jackfruit",         // 34
        "Chayote",           // 36
        "Pineapple",         // 13 (Fruit)
        "Mango",             // 23
        "Dragonfruit",       // 15
        "Ribs",              // 5 (Meat)
        "Rack of lamb",      // 6 (Meat)
        "Ham hock",          // 25 (Meat)
        "Leg of lamb",       // 28
        "Lamb leg",          // 38
        "Meat roll",         // 41
        "T-bone steak"       // 42
    );

    public static List<String> allFruits = List.of(
        "Fig",               // 1 (small)
        "Avocado",           // 2 (small)
        "Plum",              // 4 (small)
        "Orange slice",      // 6 (small)
        "Lemon",             // 7 (small)
        "Rambutan",          // 19 (small)
        "Lychee",            // 14 (small)
        "Lime",              // 22 (small)
        "Kiwi",              // 24 (small)
        "Blueberry",         // 37 (small)
        "Pomegranate",       // 47 (small)
        "Snail-shaped fruit candy", // 40 (small)
        "Pear",              // 9 (small)
        "Peach",             // 5 (small)
        "Guava",             // 26 (small)
        "Grapefruit",        // 8 (medium)
        "Apple",             // 12 (medium)
        "Dragonfruit",       // 15 (medium)
        "Papaya",            // 17 (medium)
        "Mangosteen (open)", // 20 (medium)
        "Starfruit",         // 21 (medium)
        "Rose apple (Java apple)", // 28 (medium)
        "Green mango",       // 42 (medium)
        "Durian",            // 33 (big)
        "Jackfruit",         // 34 (big)
        "Chayote",           // 36 (big)
        "Pineapple",         // 13 (big)
        "Mango"              // 23 (big)
    );

    public static List<String> allMeats = List.of(
        "Chicken breast",    // 2 (small)
        "Drumstick",         // 16 (small)
        "Turkey breast",     // 26 (small)
        "Chicken wing",      // 39 (small)
        "Steak",             // 3 (medium)
        "Pork chop",         // 4 (medium)
        "Liver",             // 1 (medium)
        "Lamb chop",         // 13 (medium)
        "Beef strip",        // 14 (medium)
        "Meat loaf",         // 17 (medium)
        "Sausage link",      // 18 (medium)
        "Rib roast",         // 19 (medium)
        "Beef roast",        // 27 (medium)
        "Meat shank",        // 29 (medium)
        "Pork belly",        // 30 (medium)
        "Brisket",           // 37 (medium)
        "Beef cubes",        // 40 (medium)
        "Ribs",              // 5 (big)
        "Rack of lamb",      // 6 (big)
        "Ham hock",          // 25 (big)
        "Leg of lamb",       // 28 (big)
        "Lamb leg",          // 38 (big)
        "Meat roll",         // 41 (big)
        "T-bone steak"       // 42 (big)
    );

    private String tipo;
    private int cantidad;
    
    //extra
    private JLabel label;

    public Recurso(String tipo, int cantidad) {
        this.tipo = tipo;
        this.cantidad = cantidad;
    }

    public Recurso(String tipo, int cantidad, JLabel label) {
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.label = label;
    }
    
    
    
    public void usarRecurso(int cantidadUsada){
        this.cantidad -= cantidadUsada;
        if (this.cantidad < 0) {
            this.cantidad = 0;
        }
    }
    
    public void agregarRecurso(int cantidadNueva){
        this.cantidad += cantidadNueva;
    }
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    public int getPeso(String tipo){

        if(smallFood.contains(tipo))
            return 1;
        
        if(mediumFood.contains(tipo))
            return 2;

        if(bigFood.contains(tipo))
            return 3;
        
        return 0; 
    }

    public boolean isFruit(Recurso recurso){
        return allFruits.contains(recurso.getTipo());
    }

    public boolean isMeat(Recurso recurso){
        return allMeats.contains(recurso.getTipo());
    }

    @Override
    public String toString() {
        return tipo + " x" + cantidad;
    }
    
    public void print(){
        System.out.println(tipo + " x" + cantidad);
    }
    
    public boolean isMadera(Recurso recurso){
        return getTipo() == "Madera";
    }

    public boolean isMedicamento(Recurso recurso){
        return getTipo() == "Medicina";
    }

    public boolean isRemedio(Recurso recurso){
        return getTipo() == "Remedio";
    }

    public void usarMedicamento(Recurso recurso, Personaje personaje){
        if(isRemedio(recurso)){
            recurso.usarRecurso(1);
            personaje.recuperarSalud(12);
            if(personaje.statusEnfermedad()==1) personaje.eliminarEnfermedad();
            if(personaje.statusEnfermedad()==1) personaje.borrarLesion();
            System.out.println("El remedio curo a " + personaje.getNombre());
        }
        if(isMedicamento(recurso)){
            recurso.usarRecurso(1);
            personaje.recuperarSalud(20);
            personaje.eliminarEnfermedad();
            personaje.borrarLesion();
            System.out.println("El medicamento curó a " + personaje.getNombre());
        }
    }

    public  List<String> getAllFruits() {
        return allFruits;
    }

    public List<String> getAllMeats() {
        return allMeats;
    }

    public JLabel getLabel() {
        return label;
    }
    
    public String getRandomMeat() {
        Random rand = new Random(); // Crear instancia de Random
        int randomIndex = rand.nextInt(allMeats.size()); // Generar un índice aleatorio entre 0 y el tamaño de la lista
        return allMeats.get(randomIndex); // Retornar el elemento en la posición aleatoria
    }

    
    
}

