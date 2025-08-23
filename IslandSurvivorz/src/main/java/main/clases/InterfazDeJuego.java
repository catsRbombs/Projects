/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package main.clases;

import java.awt.Desktop;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedWriter;
import static java.lang.Thread.sleep;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import main.clases.threads.ThreadAtacante;
import main.clases.threads.ThreadCazador;
import main.clases.threads.ThreadCientifico;
import main.clases.threads.ThreadConstructor;
import main.clases.threads.ThreadCurandero;
import main.clases.threads.ThreadExplorador;
import main.clases.threads.ThreadRecolector;

/**
 *.
 * @author Efraim Cuevas
 */
public class InterfazDeJuego extends javax.swing.JFrame {
    public ArrayList<Personaje> personajes;
    ArrayList<Thread> threadsPersonajes;
    ArrayList<Refugio> refugios;
    
    JLabel refugioEnMapa;
    
    int dia;
    
    // Esto va a ser para mantener un display constante del personaje focuseado en la consola
    boolean isCheckingStats;
    Personaje revisandoStats = null;
    
    //Esto va a ser para mantener  un display constante del personaje focuseado en la consola
    boolean isCheckingHut;
    Refugio revisandoRefugio = null;
    
    Thread statsThread;
    Thread hutStatsThread;
    
    //Al apretar un slot de inventario, el item que está en ese índice para utilizarlo, consumirlo o tradearlo
    Recurso invSlotActualCazador = null;
    Recurso invSlotActualCientifico = null;
    Recurso invSlotActualConstructor = null;
    Recurso invSlotActualExplorador = null;
    Recurso invSlotActualCurandero = null;
    Recurso invSlotActualRecolector = null;
    
    ArrayList<Recurso> invSlotsSeleccionadas;
    
    //Un ArrayList para los labels que bloquean las áreas del mapa
    ArrayList<JLabel> zonasBloqueadas;
    JLabel zonaSeleccionada = null;
    
    //Un ArrayList para los recursos que aparecen en el mapa
    ArrayList<Recurso> recursosEnMapa;
    
    //Arraylist para animales en mapa
    ArrayList<Animal> animalesEnMapa;
    //Slot para el label del recurso que se apretó en pantalla
    JLabel recursoEnMapa = null;
    JLabel animalEnMapa = null;
    
    int tormenta;
    int diasTormenta;
    
    /*Creates new form InterfazDeJuego*/
    public InterfazDeJuego() {
        initComponents();
        dia = 1;
        tormenta = 0;
        diasTormenta = 0;
        
        //Inicializo el ArrayList de los refugios
        refugios = new ArrayList<Refugio>();
        
        //Inicializando los personajes
        personajes = new ArrayList<Personaje>();
        personajes.add(new Cazador(lblCazadorMapa));
        personajes.add(new Cientifico(lblCientificoMapa));
        personajes.add(new Constructor(lblConstructorMapa));
        personajes.add(new Explorador(lblExploradorMapa));
        personajes.add(new Curandero(lblCuranderoMapa));
        personajes.add(new Recolector(lblRecolectorMapa));
        
        //Inicializar thread del Status bar
        isCheckingStats = false;
        isCheckingHut = false;
        initStatsThread();
        
        //Inicializando threads de cada personaje
        threadsPersonajes = new ArrayList<Thread>();
        threadsPersonajes.add(new ThreadCazador(this, (Cazador) personajes.get(0)));
        threadsPersonajes.add(new ThreadCientifico(this, (Cientifico) personajes.get(1)));
        threadsPersonajes.add(new ThreadConstructor(this, (Constructor) personajes.get(2)));
        threadsPersonajes.add(new ThreadExplorador(this, (Explorador) personajes.get(3)));
        threadsPersonajes.add(new ThreadCurandero(this, (Curandero) personajes.get(4)));
        threadsPersonajes.add(new ThreadRecolector(this, (Recolector) personajes.get(5)));
        
        for (Thread thread : threadsPersonajes) {
            thread.start();
        }
        
        //Inicializo el arrayList de los invSlots
        invSlotsSeleccionadas = new ArrayList<Recurso>();
        invSlotsSeleccionadas.add(invSlotActualCazador);
        invSlotsSeleccionadas.add(invSlotActualCientifico);
        invSlotsSeleccionadas.add(invSlotActualConstructor);
        invSlotsSeleccionadas.add(invSlotActualExplorador);
        invSlotsSeleccionadas.add(invSlotActualCurandero);
        invSlotsSeleccionadas.add(invSlotActualRecolector);
        
        //Inicializo el arraylist de las zonas bloqueadas del mapa
        zonasBloqueadas = new ArrayList<JLabel>();
        zonasBloqueadas.add(lblCubre00);
        zonasBloqueadas.add(lblCubre10);
        zonasBloqueadas.add(lblCubre20);
        zonasBloqueadas.add(lblCubre30);
        zonasBloqueadas.add(lblCubre40);
        zonasBloqueadas.add(lblCubre50);
        
        zonasBloqueadas.add(lblCubre11);
        zonasBloqueadas.add(lblCubre01);
        zonasBloqueadas.add(lblCubre21);
        zonasBloqueadas.add(lblCubre31);
        zonasBloqueadas.add(lblCubre41);
        zonasBloqueadas.add(lblCubre51);
        
        zonasBloqueadas.add(lblCubre02);
        zonasBloqueadas.add(lblCubre12);
        zonasBloqueadas.add(lblCubre22);
        zonasBloqueadas.add(lblCubre52);
        
        zonasBloqueadas.add(lblCubre03);
        zonasBloqueadas.add(lblCubre13);
        zonasBloqueadas.add(lblCubre23);
        zonasBloqueadas.add(lblCubre53);
        
        zonasBloqueadas.add(lblCubre04);
        zonasBloqueadas.add(lblCubre14);
        zonasBloqueadas.add(lblCubre24);
        zonasBloqueadas.add(lblCubre34);
        zonasBloqueadas.add(lblCubre44);
        zonasBloqueadas.add(lblCubre54);
        
        zonasBloqueadas.add(lblCubre05);
        zonasBloqueadas.add(lblCubre15);
        zonasBloqueadas.add(lblCubre25);
        zonasBloqueadas.add(lblCubre35);
        zonasBloqueadas.add(lblCubre45);
        zonasBloqueadas.add(lblCubre55);
        
        //-----------------------------------------------------------
        
        //Inicializo el ArrayList de los recursos que aparecerán en el ma pa
        recursosEnMapa = new ArrayList<>();
        animalesEnMapa = new ArrayList<>();
        
        //Limpiar logs
        limpiarLogs();
        
        //--------------TESTEOS--------------
        
        
        //Probar que ningún personaje pueda realizar acciones si su vida está en 0
//        personajes.get(3).reducirSalud(100); //explorador
//        personajes.get(0).reducirSalud(100); //cazador
//        personajes.get(1).reducirSalud(100); //cientifico
//        personajes.get(2).reducirSalud(100); //constructor
//        personajes.get(4).reducirSalud(100); //curandero
//        personajes.get(5).reducirSalud(100); //recolector
        
        //Probar que el científico y el curandero puedan crear medicinas, remedios y que el curandero cure
        personajes.get(1).agregarRecurso(new Recurso("Ciencia", 3));
        personajes.get(1).agregarRecurso(new Recurso("Agua", 6));
//        personajes.get(2).reducirSalud(63); //constructor
        personajes.get(4).agregarRecurso(new Recurso("Hojas",3));
        personajes.get(4).agregarRecurso(new Recurso("Agua",2));
        
        
        //Prueba de la dieta del cazador y el recolector
        personajes.get(0).agregarRecurso(new Recurso("Steak",3));
        personajes.get(0).agregarRecurso(new Recurso("Fig",2));
        
        personajes.get(5).agregarRecurso(new Recurso("Drumstick",3));
        personajes.get(5).agregarRecurso(new Recurso("Peach",2));
        
        //Prueba de que todos los personajes pueden comer
        personajes.get(1).agregarRecurso(new Recurso("Drumstick",3));
        personajes.get(1).agregarRecurso(new Recurso("Fig",2));
        
        personajes.get(2).agregarRecurso(new Recurso("Ribs",3));
        
        personajes.get(3).agregarRecurso(new Recurso("Chicken wing",3));
        personajes.get(3).agregarRecurso(new Recurso("Lychee",2));
        
        personajes.get(4).agregarRecurso(new Recurso("Hojas",3));
        personajes.get(4).agregarRecurso(new Recurso("Agua",2));
        
        //Prueba de que el constructor puede construir y reparar refugios
        personajes.get(2).agregarRecurso(new Recurso("Madera",12));
        
        for (int i = 0; i < 6; i++) {
                actualizarIconosDeInventarioPersonaje(i);
                guardarMensajeEnLog(i, "Día 1");
            }
    }

    public void initStatsThread(){
        statsThread = new Thread(){
            public void run(){
                while(true){
                    if (isCheckingStats) { //Entra acá si se están mostrando los stats de un personaje
                        try {
                            lblCurrentSelectedCharacter.setText(revisandoStats.getNombre());
                            barraDeVida.setValue(revisandoStats.getVida());
                            barraDeEnergia.setValue(revisandoStats.getEnergia());
                            //Actualizar status icons
                            statsThread.sleep(250);
                            
                            //Actualizar el inventario
                            mostrarInventario(revisandoStats);
                            
                            //Cambiar los statuses de ser necesario
                            //Está dentro de refugio o no
                            if (revisandoStats.getRefugioActual() == null) lblStatusPersonajeEnRefugio.setText("Afuera");
                            else lblStatusPersonajeEnRefugio.setText("Dentro de un refugio");
                            
                            //Status de salud (enfermedad)
                            if (revisandoStats.estaEnfermo()== 0) lblStatusPersonajeSalud.setText("Saludable");
                            if (revisandoStats.estaEnfermo() == 1) lblStatusPersonajeSalud.setText("Enfermo");
                            if (revisandoStats.estaEnfermo() == 2) lblStatusPersonajeSalud.setText("Enfermedad Grave");
                            
                            //Herido o no
                            if (revisandoStats.estaLesionado() == 0) lblStatusPersonajeHeridas.setText("Sin heridas");
                            if (revisandoStats.estaLesionado() == 1) lblStatusPersonajeHeridas.setText("Lesionado");
                            if (revisandoStats.estaLesionado() == 2) lblStatusPersonajeHeridas.setText("Lesion Grave");
                            
                        } catch (InterruptedException ex) {}
                        
                    } else
                        try {
                            statsThread.sleep(250);
                        } catch (InterruptedException ex) {}
                }
            }
        };
        
        hutStatsThread = new Thread(){
            public void run(){
                while(true){
                    if (isCheckingHut) { //Entra acá si se están mostrando los stats de un refugio en específico
                        try {
                            barraDeEstabilidad.setValue(revisandoRefugio.evaluarEstabilidad());
                            
                            if (revisandoRefugio.getPersonajesAdentro().size() == 0) {
                                lblStatusPersonajeEnRefugio_1.setText("");
                                lblStatusPersonajeEnRefugio_2.setText("");
                                lblStatusPersonajeEnRefugio_3.setText("");
                            } else if (revisandoRefugio.getPersonajesAdentro().size() < 3) {
                                lblStatusPersonajeEnRefugio_1.setText(revisandoRefugio.getPersonajesAdentro().get(0).getNombre());
                                lblStatusPersonajeEnRefugio_2.setText(revisandoRefugio.getPersonajesAdentro().size() < 2? "": revisandoRefugio.getPersonajesAdentro().get(1).getNombre() );
                                lblStatusPersonajeEnRefugio_3.setText("");
                            } else {
                                lblStatusPersonajeEnRefugio_1.setText(revisandoRefugio.getPersonajesAdentro().get(0).getNombre());
                                lblStatusPersonajeEnRefugio_2.setText(revisandoRefugio.getPersonajesAdentro().get(1).getNombre());
                                lblStatusPersonajeEnRefugio_3.setText(revisandoRefugio.getPersonajesAdentro().get(2).getNombre());
                            }
                            
                            
                            hutStatsThread.sleep(250);
                        } catch (InterruptedException ex) {}
                        
                    } else
                        try {
                            hutStatsThread.sleep(250);
                        } catch (InterruptedException ex) {}
                }
            }
        };
        
        statsThread.start();
        hutStatsThread.start();
        
    }
    
    
    public static void guardarMensajeEnLog(int personaje, String mensaje) {
    String rutaArchivo = "";
    
    switch (personaje) {
        case 0: // Cazador
            rutaArchivo = "/logs/cazador.txt";
            break;
        case 1: // Científico
            rutaArchivo = "/logs/cientifico.txt";
            break;
        case 2: // Constructor
            rutaArchivo = "/logs/constructor.txt";
            break;
        case 3: // Explorador
            rutaArchivo = "/logs/explorador.txt";
            break;
        case 4: // Curandero
            rutaArchivo = "/logs/curandero.txt";
            break;
        case 5: // Recolector
            rutaArchivo = "/logs/recolector.txt";
            break;
        default:
            System.out.println("Personaje no válido.");
            return;
    }

    try {
        // Obtener la ruta absoluta del archivo desde el directorio de recursos
        File archivo = new File(InterfazDeJuego.class.getResource(rutaArchivo).toURI());
        
        // Usar BufferedWriter para escribir en el archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, true))) {
            writer.write(mensaje);  // Escribe el mensaje en el archivo
            writer.newLine();       // Agrega una nueva línea
        }
    } catch (IOException | URISyntaxException e) {
        e.printStackTrace();
    }
}
    
    public void abrirLog(int personaje) {
    String rutaArchivo = "";
    switch (personaje) {
        case 1:
            rutaArchivo = "/logs/cazador.txt";
            break;
        case 2:
            rutaArchivo = "/logs/cientifico.txt";
            break;
        case 3:
            rutaArchivo = "/logs/constructor.txt";
            break;
        case 4:
            rutaArchivo = "/logs/explorador.txt";
            break;
        case 5:
            rutaArchivo = "/logs/curandero.txt";
            break;
        case 6:
            rutaArchivo = "/logs/recolector.txt";
            break;
        default:
            System.out.println("Personaje no válido.");
            return;
    }

    try {
        // Obtener la ruta absoluta del archivo desde el directorio de recursos
        File archivo = new File(getClass().getResource(rutaArchivo).toURI());
        
        // Comprobar si se puede usar Desktop para abrir el archivo
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (archivo.exists()) {
                desktop.open(archivo); // Abre el archivo con la aplicación predeterminada del sistema
            } else {
                System.out.println("El archivo no existe.");
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    public void limpiarLogs() {
    String[] rutasArchivos = {
        "/logs/cazador.txt",
        "/logs/cientifico.txt",
        "/logs/constructor.txt",
        "/logs/explorador.txt",
        "/logs/curandero.txt",
        "/logs/recolector.txt"
    };

    for (String ruta : rutasArchivos) {
        try {
            // Obtener la ruta absoluta del archivo desde el directorio de recursos
            File archivo = new File(getClass().getResource(ruta).toURI());
            
            // Abrir el archivo en modo de escritura para limpiar su contenido
            try (FileWriter writer = new FileWriter(archivo)) {
                writer.write("");  // Escribir una cadena vacía, limpiando el archivo
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
    
    public void crearTormenta(){
        tormenta = new Random().nextInt(1, 3);
        diasTormenta = new Random().nextInt(1, 3);
        lblEstadoClima.setText(tormenta < 2? "Tormenta leve":"Tormenta grave");
        lblDuracionTormenta.setText(Integer.toString(diasTormenta));
        
        for (int i = 0; i < 6; i++) {
                guardarMensajeEnLog(i, "Apareció una " + lblEstadoClima.getText());
            }
    }
    
    public void actualizarTormenta(){
        diasTormenta--;
        if (diasTormenta <= 0) {
            tormenta = 0;
            lblEstadoClima.setText("Despejado");
        }
        lblDuracionTormenta.setText(Integer.toString(diasTormenta));
    }
    
    
    public void ponerDestino(int i){
        personajes.get(i).xDestino = Integer.parseInt(lblCoordsX.getText());
        personajes.get(i).yDestino = Integer.parseInt(lblCoordsY.getText());
    }
    
    public Point getPuntoCentralLabel(JLabel label){
        int x = label.getX();
        int y = label.getY();
        int width = label.getWidth();
        int height = label.getHeight();

        // Calcular las distancias entre los centros de los dos JLabels
        int centroX = x + width / 2;
        int centroY = y + height / 2;
        return new Point(centroX, centroY);
    }
    
    public void moverPersonaje(JLabel label, int x, int y){
        SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            label.setLocation(x, y);
            label.getParent().repaint();
                }
        });
                }
    
    public void mover(int pj){ //Este mover no permitirá mover gente si es a un lugar no desbloqueado o si está en un refugio
        if (!esZonaBloqueada() && personajes.get(pj).getRefugioActual() == null) {
            ponerDestino(pj);
            System.out.println("El personaje " + personajes.get(pj).getNombre() + " se mueve a (" + lblCoordsX.getText() + ", " + lblCoordsY.getText() + ")");
            guardarMensajeEnLog(pj, "El personaje " + personajes.get(pj).getNombre() + " se mueve a (" + lblCoordsX.getText() + ", " + lblCoordsY.getText() + ")");
        } else {
            JOptionPane.showMessageDialog(null, "El personaje no se puede mover", "Movimiento Restringido", JOptionPane.WARNING_MESSAGE);
        }
        
    }
    
    public void setXY(Personaje champ, int x, int y){
        champ.xDestino = x;
        champ.yDestino = y;
    }
    
    public void moverPersonaje(Personaje pj){
        int posX = pj.getLabel().getX();
        int posY = pj.getLabel().getY();
        
        if (pj.isMovingX()) {
            if (posX < pj.xDestino) posX+= 10;
            else if (posX > pj.xDestino) posX-= 10;
        }
        
        // Mueve en Y
        if (pj.isMovingY()) {
            if (posY < pj.yDestino) posY+= 10;
            else if (posY > pj.yDestino) posY-= 10;
        }
        pj.getLabel().setLocation(posX, posY);
        pj.checkArrival();
    }
    
    public boolean esZonaBloqueada(){
        Point pt = new Point(Integer.parseInt(lblCoordsX.getText()), 
                             Integer.parseInt(lblCoordsY.getText()));
        if (zonaSeleccionada != null && zonasBloqueadas.contains(zonaSeleccionada)) {
            Rectangle bounds = zonaSeleccionada.getBounds(); // Obtiene el rectángulo del JLabel
            Point labelLocation = zonaSeleccionada.getLocation(); // Posición en el mapa
            
            // Ajustar el rectángulo al espacio real en el mapa
            Rectangle boundsOnMap = new Rectangle(labelLocation.x, labelLocation.y, bounds.width, bounds.height);
            if (boundsOnMap.contains(pt)) return true;
        }
        return false;
    }
    
    public boolean estanCerca(JLabel label1, JLabel label2, int rango) {
        // Obtener las posiciones de los JLabels
        Point p1 = getPuntoCentralLabel(label1);
        Point p2 = getPuntoCentralLabel(label2);

        // Calcular la distancia euclidiana entre los dos puntos centrales
        double distancia = Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));

        // Verificar si la distancia es menor o igual al rango de proximidad
        return distancia <= rango;
}
    
    
    public void seleccionarSlotInventario(int personaje, int slot){
        Recurso recursoSlot = invSlotsSeleccionadas.get(personaje);
        recursoSlot = personajes.get(personaje).getInventario()[slot];
        invSlotsSeleccionadas.set(personaje, recursoSlot);
        if(recursoSlot != null) recursoSlot.print();
        else System.out.println("Slot vacia");
    }
    
    public void mostrarInventario(Personaje personaje){
        String inventarioString = "";
        int cont = 1;
        for (Recurso slot : personaje.getInventario()) {
            if (slot != null) inventarioString += Integer.toString(cont++) + " )- " + slot.toString() + "\n";
            else              inventarioString += Integer.toString(cont++) + " )-\n";
        }
        txfInventarioDisplay.setText(inventarioString);
    }
    
    public void actualizarIconoInventario(JButton boton, int personaje, int slot){
        Recurso itemEnSlot = personajes.get(personaje).getInventario()[slot];
        
        if (itemEnSlot == null) {
            boton.setIcon(null);
        } else if (itemEnSlot.isFruit(itemEnSlot)) {
            boton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/For the inventory/fruit.png")));
        } else if (itemEnSlot.isMeat(itemEnSlot)) {
            boton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/For the inventory/meat.png")));
        } else if (itemEnSlot.getTipo() == "Hojas") {
            boton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/For the inventory/herb.png")));
        } else if (itemEnSlot.getTipo() == "Agua") {
            boton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/For the inventory/water.png")));
        } else if (itemEnSlot.getTipo() == "Medicina") {
            boton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/For the inventory/med.png")));
        } else if (itemEnSlot.getTipo() == "Remedio") {
            boton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/For the inventory/unguento.png")));
        } else if (itemEnSlot.getTipo() == "Madera") {
            boton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/For the inventory/wood.png")));
        } else if (itemEnSlot.getTipo() == "Ciencia") {
            boton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/For the inventory/ciencia.png")));
        }
    }
    
    
    public void actualizarIconosDeInventarioPersonaje(int personaje){
        for (int i = 0; i < 9; i++) {
        switch (personaje) {
            case 0: 
                actualizarIconoInventario(btnInventarioCazador_1, personaje, 0);
                actualizarIconoInventario(btnInventarioCazador_2, personaje, 1);
                actualizarIconoInventario(btnInventarioCazador_3, personaje, 2);
                actualizarIconoInventario(btnInventarioCazador_4, personaje, 3);
                actualizarIconoInventario(btnInventarioCazador_5, personaje,4);
                actualizarIconoInventario(btnInventarioCazador_6, personaje,5);
                actualizarIconoInventario(btnInventarioCazador_7, personaje,6);
                actualizarIconoInventario(btnInventarioCazador_8, personaje,7);
                actualizarIconoInventario(btnInventarioCazador_9, personaje, 8);
                break;
            case 1: 
                actualizarIconoInventario(btnInventarioCientifico_1, personaje, 0);
                actualizarIconoInventario(btnInventarioCientifico_2, personaje, 1);
                actualizarIconoInventario(btnInventarioCientifico_3, personaje, 2);
                actualizarIconoInventario(btnInventarioCientifico_4, personaje, 3);
                actualizarIconoInventario(btnInventarioCientifico_5, personaje,4);
                actualizarIconoInventario(btnInventarioCientifico_6, personaje,5);
                actualizarIconoInventario(btnInventarioCientifico_7, personaje,6);
                actualizarIconoInventario(btnInventarioCientifico_8, personaje,7);
                actualizarIconoInventario(btnInventarioCientifico_9, personaje, 8); 
                break;
            case 2: 
                actualizarIconoInventario(btnInventarioConstructor_1, personaje, 0);
                actualizarIconoInventario(btnInventarioConstructor_2, personaje, 1);
                actualizarIconoInventario(btnInventarioConstructor_3, personaje, 2);
                actualizarIconoInventario(btnInventarioConstructor_4, personaje, 3);
                actualizarIconoInventario(btnInventarioConstructor_5, personaje,4);
                actualizarIconoInventario(btnInventarioConstructor_6, personaje,5);
                actualizarIconoInventario(btnInventarioConstructor_7, personaje,6);
                actualizarIconoInventario(btnInventarioConstructor_8, personaje,7);
                actualizarIconoInventario(btnInventarioConstructor_9, personaje, 8);
                break;
            case 3: 
                actualizarIconoInventario(btnInventarioExplorador_1, personaje, 0);
                actualizarIconoInventario(btnInventarioExplorador_2, personaje, 1);
                actualizarIconoInventario(btnInventarioExplorador_3, personaje, 2);
                actualizarIconoInventario(btnInventarioExplorador_4, personaje, 3);
                actualizarIconoInventario(btnInventarioExplorador_5, personaje,4);
                actualizarIconoInventario(btnInventarioExplorador_6, personaje,5);
                actualizarIconoInventario(btnInventarioExplorador_7, personaje,6);
                actualizarIconoInventario(btnInventarioExplorador_8, personaje,7);
                actualizarIconoInventario(btnInventarioExplorador_9, personaje, 8);
                break;
            case 4: 
                actualizarIconoInventario(btnInventarioCurandero_1, personaje, 0);
                actualizarIconoInventario(btnInventarioCurandero_2, personaje, 1);
                actualizarIconoInventario(btnInventarioCurandero_3, personaje, 2);
                actualizarIconoInventario(btnInventarioCurandero_4, personaje, 3);
                actualizarIconoInventario(btnInventarioCurandero_5, personaje,4);
                actualizarIconoInventario(btnInventarioCurandero_6, personaje,5);
                actualizarIconoInventario(btnInventarioCurandero_7, personaje,6);
                actualizarIconoInventario(btnInventarioCurandero_8, personaje,7);
                actualizarIconoInventario(btnInventarioCurandero_9, personaje, 8);
                break;
            case 5: 
                actualizarIconoInventario(btnInventarioRecolector_1, personaje, 0);
                actualizarIconoInventario(btnInventarioRecolector_2, personaje, 1);
                actualizarIconoInventario(btnInventarioRecolector_3, personaje, 2);
                actualizarIconoInventario(btnInventarioRecolector_4, personaje, 3);
                actualizarIconoInventario(btnInventarioRecolector_5, personaje,4);
                actualizarIconoInventario(btnInventarioRecolector_6, personaje,5);
                actualizarIconoInventario(btnInventarioRecolector_7, personaje,6);
                actualizarIconoInventario(btnInventarioRecolector_8, personaje,7);
                actualizarIconoInventario(btnInventarioRecolector_9, personaje, 8);
                break;
        }
        
    }
    }
    
    public void comer(int personaje){
        
        Recurso recursoSlot = invSlotsSeleccionadas.get(personaje);
        if (recursoSlot != null) {
            actualizarIconosDeInventarioPersonaje(personaje);
            if (recursoSlot.isFruit(recursoSlot) || recursoSlot.isMeat(recursoSlot) 
                || recursoSlot.isMedicamento(recursoSlot) || recursoSlot.isRemedio(recursoSlot)) { 
                guardarMensajeEnLog(personaje, "El personaje intento consumir: " + recursoSlot.getTipo());}
            personajes.get(personaje).comer(recursoSlot);
            if (personajes.get(personaje).removerRecursoSiTieneCero(recursoSlot)) {
                invSlotsSeleccionadas.set(personaje, null);;
                actualizarIconosDeInventarioPersonaje(personaje);
            }
        } else{
            System.out.println("No hay comida en ese slot.");
            actualizarIconosDeInventarioPersonaje(personaje);
        }
    }
    
    public void intercambiarRecurso(int personaje){
        if (revisandoStats != null && invSlotsSeleccionadas.get(personaje) != null && estanCerca(revisandoStats.getLabel(), personajes.get(personaje).getLabel(), 40)) {
            personajes.get(personaje).compartirRecurso(revisandoStats, invSlotsSeleccionadas.get(personaje));
            System.out.println("Recurso " + invSlotsSeleccionadas.get(personaje).getTipo() + " intercambiado!");
            guardarMensajeEnLog(personaje, "Entregó " + invSlotsSeleccionadas.get(personaje).getTipo() + " a " + revisandoStats.getNombre());
            guardarMensajeEnLog(personajes.indexOf(revisandoStats), "Recibió " + invSlotsSeleccionadas.get(personaje).getTipo() + " de " + personajes.get(personaje).getNombre());
            for (int i = 0; i < 6; i++) {
                actualizarIconosDeInventarioPersonaje(i);
            }
        } else System.out.println("No se pudo hacer el intercambio");
    }
    
    public void crearRecurso(int tipo, int cantidad){
        
        JLabel newRecurso = new javax.swing.JLabel();
        
        newRecurso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblRecursoMousePressed(evt);
            }
        });
        pnlMap.add(newRecurso);
        pnlMap.setComponentZOrder(newRecurso, 0);
        newRecurso.setBounds(Integer.parseInt(lblCoordsX.getText()), Integer.parseInt(lblCoordsY.getText()), 20, 20);
        
        Recurso nuevo = null;
        Random random = new Random();
        int randomNum;
        
        switch (tipo) {
            case 1:
                newRecurso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/ForTheMap/ciencia.png")));
                nuevo = new Recurso("Ciencia", cantidad,newRecurso);
                break;
            case 2:
                newRecurso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/ForTheMap/madera.png")));
                nuevo = new Recurso("Madera", cantidad,newRecurso);
                break;
            case 3:
                newRecurso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/ForTheMap/agua.png")));
                nuevo = new Recurso("Agua", cantidad,newRecurso);
                break;
            case 4:
                newRecurso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/ForTheMap/hierbas.png")));
                nuevo = new Recurso("Hojas", cantidad,newRecurso);
                break;
            case 5:
                newRecurso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/ForTheMap/fruit.png")));
                
                randomNum = random.nextInt(28);
                nuevo = new Recurso(Recurso.allFruits.get(randomNum), cantidad,newRecurso);
                break;
            case 6:
                newRecurso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/ForTheMap/meat.png")));
                randomNum = random.nextInt(24);
                nuevo = new Recurso(Recurso.allMeats.get(randomNum), cantidad,newRecurso);
                break;  
        }
        recursosEnMapa.add(nuevo);
        System.out.println("Recurso " + nuevo.toString()+ " creado");
    }
    
    public void crearRecurso(int tipo, int x, int y){
        
        JLabel newRecurso = new javax.swing.JLabel();
        
        newRecurso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblRecursoMousePressed(evt);
            }
        });
        
        pnlMap.add(newRecurso);
        pnlMap.setComponentZOrder(newRecurso, 0);
        newRecurso.setBounds(x, y, 20, 20);
        
        Recurso nuevo = null;
        Random random = new Random();
        int randomNum;
        int cantidad = random.nextInt(1, 6);
        
        switch (tipo) {
            case 1:
                newRecurso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/ForTheMap/ciencia.png")));
                nuevo = new Recurso("Ciencia", cantidad,newRecurso);
                break;
            case 2:
                newRecurso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/ForTheMap/madera.png")));
                nuevo = new Recurso("Madera", cantidad,newRecurso);
                break;
            case 3:
                newRecurso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/ForTheMap/agua.png")));
                nuevo = new Recurso("Agua", cantidad,newRecurso);
                break;
            case 4:
                newRecurso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/ForTheMap/hierbas.png")));
                nuevo = new Recurso("Hojas", cantidad,newRecurso);
                break;
            case 5:
                newRecurso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/ForTheMap/fruit.png")));
                
                randomNum = random.nextInt(28);
                nuevo = new Recurso(Recurso.allFruits.get(randomNum), cantidad,newRecurso);
                break;
            case 6:
                newRecurso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/ForTheMap/meat.png")));
                randomNum = random.nextInt(24);
                nuevo = new Recurso(Recurso.allMeats.get(randomNum), cantidad,newRecurso);
                break;  
        }
        recursosEnMapa.add(nuevo);
        System.out.println("Recurso " + nuevo.toString()+ " creado");
    }
    
    public void crearAnimal (int tipo, int x, int y){
        
        JLabel newAnimal = new javax.swing.JLabel();
        
        newAnimal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblAnimalMousePressed(evt);
            }
        });
        pnlMap.add(newAnimal);
        pnlMap.setComponentZOrder(newAnimal, 0);
        newAnimal.setBounds(x, y, 30, 30);
        
        Animal nuevo = null;
        int randomNum;
        switch (tipo) {
            case 1:
                newAnimal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/ForTheMap/cerdo.png")));
                nuevo = new Animal("Cerdo", newAnimal);
                break;
            case 2:
                newAnimal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/ForTheMap/lobo.png")));
                nuevo = new Animal("Lobo", newAnimal);
                break;
            case 3:
                newAnimal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/ForTheMap/oso.png")));
                nuevo = new Animal("Oso", newAnimal);
                break;
        }
        animalesEnMapa.add(nuevo);
    }
    
    public void crearRecursosAlDesbloquearArea(JLabel label){
        Random random = new Random();
        int cuantosSpawnear = random.nextInt(6);
        int cuantosSpawnearAnimales = random.nextInt(3);
        int cantidadASpawnear;
        List<Point> puntos = generarPuntosAleatorios(zonaSeleccionada, cuantosSpawnear);
        List<Point> puntosAnimal = generarPuntosAleatorios(zonaSeleccionada, cuantosSpawnearAnimales);
        
        for (Point pt : puntos) {
            cantidadASpawnear = random.nextInt(2, 6);
            
            crearRecurso(random.nextInt(1, 7), pt.x, pt.y);
        }
        
        for (Point pt : puntosAnimal) { 
            
            crearAnimal(random.nextInt(1,4), pt.x, pt.y);
        }
    }
    
    public List<Point> generarPuntosAleatorios(JLabel label, int n) {
        List<Point> puntos = new ArrayList<>();
        Random random = new Random();
    
        // Obtiene la posición y tamaño del JLabel
        int xInicio = label.getX();
        int yInicio = label.getY();
        int ancho = label.getWidth();
        int alto = label.getHeight();
    
        // Genera 'n' puntos aleatorios dentro del área del JLabel
        for (int i = 0; i < n; i++) {
            int xAleatorio = xInicio + random.nextInt(ancho);
            int yAleatorio = yInicio + random.nextInt(alto);
            puntos.add(new Point(xAleatorio, yAleatorio));
        }
    
        return puntos;
    }
    
    public int buscarIndexRecursoConUnLabel (JLabel label){
        for (int i=0; i < recursosEnMapa.size(); i++) {
            if(recursosEnMapa.get(i).getLabel() == recursoEnMapa){
                return i;
            }
        }
        return -1;
    }
    
    public int buscarIndexAnimalConUnLabel (JLabel label){
        for (int i=0; i<animalesEnMapa.size(); i++){
        if(animalesEnMapa.get(i).getLabel() == animalEnMapa){
            return i;
        }
    }
        System.out.println("ERROR AL BUSCAR LABEL DE ANIMAL");
        return -1;
    }
    
    public int buscarIndexRefugioConUnLabel (JLabel label){
        for (int i=0; i < refugios.size(); i++) {
            if(refugios.get(i).label == refugioEnMapa){
                return i;
            }
        }
        return -1;
    }
    
    public void recolectarRecurso(int personaje){
        if (personajes.get(personaje).getVida() == 0) {
            System.out.println("No tiene vida para hacer una recoleccion");
            JOptionPane.showMessageDialog(null, "El personaje no tiene vida para realizar acciones", "Falta de vida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int energiaAConsumir = 0;
        if (personaje == 3) { //recolector
            energiaAConsumir = 5;
        } else if (personaje == 4 || personaje == 5) {//Curandero o Recolector
            energiaAConsumir = 10;
        }
        
        if (recursoEnMapa != null && estanCerca(personajes.get(personaje).getLabel(), recursoEnMapa, 40) && personajes.get(personaje).getEnergia() >= energiaAConsumir) {
            Recurso encontrado = recursosEnMapa.get(buscarIndexRecursoConUnLabel(recursoEnMapa));
            
            if (encontrado != null && personajes.get(personaje).agregarRecurso(encontrado)) {
                recursosEnMapa.remove(encontrado);
                recursoEnMapa.setVisible(false);
                recursoEnMapa = null;
                
                personajes.get(personaje).reducirEnergia(energiaAConsumir);
                actualizarIconosDeInventarioPersonaje(personaje);
                System.out.println("Recurso " + encontrado.toString() + " recolectado.");
                guardarMensajeEnLog(personaje, "Recolectó el recurso " + encontrado.toString());
            }
        } else System.out.println("No hay un recurso cerca para recolectar");
    }
    
    public void cazarAnimal(){
            
        Cazador cazador = (Cazador) personajes.get(0);
        if (cazador.getVida() == 0) {
            System.out.println("No tiene vida para cazar");
            JOptionPane.showMessageDialog(null, "El personaje no tiene vida para realizar acciones", "Falta de vida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if(animalesEnMapa!=null && animalEnMapa != null){
            Animal encontrado = animalesEnMapa.get(buscarIndexAnimalConUnLabel(animalEnMapa));
            System.out.println("encontro un animal");
            if ( estanCerca(cazador.getLabel(), animalEnMapa, 40) && cazador.getEnergia()>=encontrado.getFuerza()) {
                System.out.println("encontro un animal cercano");
                if ( cazador.cazar(encontrado)) {
                    System.out.println("intento cazar");
                    animalesEnMapa.remove(encontrado);
                    animalEnMapa.setVisible(false);
                    animalEnMapa = null;

                    actualizarIconosDeInventarioPersonaje(0);
                    System.out.println("Animal " + encontrado.getTipoAnimal() + " cazado");
                    guardarMensajeEnLog(0, "Cazó un animal");
                }
            } 
        }
        else System.out.println("No hay animal cerca por cazar");}
        
    public void refugiarse(int personaje){
        Personaje pj = personajes.get(personaje);
        if (pj.getRefugioActual() == null 
            && estanCerca(pj.getLabel(), refugioEnMapa, 40) 
            && revisandoRefugio.getPersonajesAdentro().size() < revisandoRefugio.getCapacidad()) {
            
            revisandoRefugio.addPersonaje(pj);
            pj.getLabel().setVisible(false);
            
            guardarMensajeEnLog(personaje, "Entró a un refugio");
            
        } else if (pj.getRefugioActual() != null) {
            pj.getRefugioActual().eliminarPersonaje(pj);
            pj.getLabel().setVisible(true);
            guardarMensajeEnLog(personaje, "Salió del refugio");
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMap = new javax.swing.JPanel();
        lblCientificoMapa = new javax.swing.JLabel();
        lblCazadorMapa = new javax.swing.JLabel();
        lblRecolectorMapa = new javax.swing.JLabel();
        lblConstructorMapa = new javax.swing.JLabel();
        lblCuranderoMapa = new javax.swing.JLabel();
        lblExploradorMapa = new javax.swing.JLabel();
        lblCubre00 = new javax.swing.JLabel();
        lblCubre10 = new javax.swing.JLabel();
        lblCubre20 = new javax.swing.JLabel();
        lblCubre30 = new javax.swing.JLabel();
        lblCubre40 = new javax.swing.JLabel();
        lblCubre50 = new javax.swing.JLabel();
        lblCubre01 = new javax.swing.JLabel();
        lblCubre11 = new javax.swing.JLabel();
        lblCubre21 = new javax.swing.JLabel();
        lblCubre31 = new javax.swing.JLabel();
        lblCubre41 = new javax.swing.JLabel();
        lblCubre51 = new javax.swing.JLabel();
        lblCubre02 = new javax.swing.JLabel();
        lblCubre12 = new javax.swing.JLabel();
        lblCubre22 = new javax.swing.JLabel();
        lblCubre52 = new javax.swing.JLabel();
        lblCubre03 = new javax.swing.JLabel();
        lblCubre13 = new javax.swing.JLabel();
        lblCubre23 = new javax.swing.JLabel();
        lblCubre53 = new javax.swing.JLabel();
        lblCubre04 = new javax.swing.JLabel();
        lblCubre14 = new javax.swing.JLabel();
        lblCubre24 = new javax.swing.JLabel();
        lblCubre34 = new javax.swing.JLabel();
        lblCubre44 = new javax.swing.JLabel();
        lblCubre54 = new javax.swing.JLabel();
        lblCubre05 = new javax.swing.JLabel();
        lblCubre15 = new javax.swing.JLabel();
        lblCubre25 = new javax.swing.JLabel();
        lblCubre35 = new javax.swing.JLabel();
        lblCubre45 = new javax.swing.JLabel();
        lblCubre55 = new javax.swing.JLabel();
        lblMap = new javax.swing.JLabel();
        pnlContainer = new javax.swing.JPanel();
        pnlOpcionesDios = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblDiaActual = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblCoordsY = new javax.swing.JLabel();
        lblCoordsX = new javax.swing.JLabel();
        btnNextDay = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pnlMedicinas = new javax.swing.JPanel();
        btnSpawnearMedicinaInventario = new javax.swing.JButton();
        btnSpawnearUnguentoInventario = new javax.swing.JButton();
        pnlAnimales = new javax.swing.JPanel();
        botonSpawnearCerdo = new javax.swing.JButton();
        botonSpawnearLobo = new javax.swing.JButton();
        botonSpawnearOso = new javax.swing.JButton();
        pnlRecursos = new javax.swing.JPanel();
        btnSpawnearHierbas = new javax.swing.JButton();
        btnSpawnearMadera = new javax.swing.JButton();
        btnSpawnearCiencia = new javax.swing.JButton();
        btnSpawnearAgua = new javax.swing.JButton();
        pnlComida = new javax.swing.JPanel();
        btnSpawnearFrutas = new javax.swing.JButton();
        btnSpawnearCarnes = new javax.swing.JButton();
        pnlBitacoras = new javax.swing.JPanel();
        btnBitCazador = new javax.swing.JButton();
        btnBitCientifico = new javax.swing.JButton();
        btnBitConstructor = new javax.swing.JButton();
        btnBitExplorador = new javax.swing.JButton();
        btnBitCurandero = new javax.swing.JButton();
        btnBitRecolector = new javax.swing.JButton();
        btnSpawnearTormenta = new javax.swing.JButton();
        btnHerirPersonaje = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cmbCantidadASpawnear = new javax.swing.JComboBox<>();
        btnEnfermarPersonaje = new javax.swing.JButton();
        btnAtaqueAnimal = new javax.swing.JButton();
        btnCurarDebuffsPersonaje = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lblEstadoClima = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblDuracionTormenta = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        pnlCazador = new javax.swing.JPanel();
        pnlInventarioCazador = new javax.swing.JPanel();
        btnInventarioCazador_1 = new javax.swing.JButton();
        btnInventarioCazador_2 = new javax.swing.JButton();
        btnInventarioCazador_3 = new javax.swing.JButton();
        btnInventarioCazador_4 = new javax.swing.JButton();
        btnInventarioCazador_5 = new javax.swing.JButton();
        btnInventarioCazador_6 = new javax.swing.JButton();
        btnInventarioCazador_7 = new javax.swing.JButton();
        btnInventarioCazador_8 = new javax.swing.JButton();
        btnInventarioCazador_9 = new javax.swing.JButton();
        lblCazadorSelect = new javax.swing.JLabel();
        btnMoverCazador = new javax.swing.JButton();
        btnComerCazador = new javax.swing.JButton();
        btnIrRefugioCazador = new javax.swing.JButton();
        btnIntercambiarCazador = new javax.swing.JButton();
        btnCazarCazador = new javax.swing.JButton();
        pnlDatos = new javax.swing.JPanel();
        pnlDatosPersonaje = new javax.swing.JPanel();
        lbl_statusText = new javax.swing.JLabel();
        barraDeEnergia = new javax.swing.JProgressBar();
        lbl_energiaText = new javax.swing.JLabel();
        barraDeVida = new javax.swing.JProgressBar();
        lbl_vidaText = new javax.swing.JLabel();
        lblCurrentSelectedCharacter = new javax.swing.JLabel();
        lblAvatar = new javax.swing.JLabel();
        lblStatusPersonajeEnRefugio = new javax.swing.JLabel();
        lblStatusPersonajeHeridas = new javax.swing.JLabel();
        lblStatusPersonajeSalud = new javax.swing.JLabel();
        pnlRecolector = new javax.swing.JPanel();
        pnlInventariorecolector = new javax.swing.JPanel();
        btnInventarioRecolector_1 = new javax.swing.JButton();
        btnInventarioRecolector_2 = new javax.swing.JButton();
        btnInventarioRecolector_3 = new javax.swing.JButton();
        btnInventarioRecolector_4 = new javax.swing.JButton();
        btnInventarioRecolector_5 = new javax.swing.JButton();
        btnInventarioRecolector_6 = new javax.swing.JButton();
        btnInventarioRecolector_7 = new javax.swing.JButton();
        btnInventarioRecolector_8 = new javax.swing.JButton();
        btnInventarioRecolector_9 = new javax.swing.JButton();
        lblRecolectorSelect = new javax.swing.JLabel();
        btnMoverRecolector = new javax.swing.JButton();
        btnComerRecolector = new javax.swing.JButton();
        btnIrRefugioRecolector = new javax.swing.JButton();
        btnEntregarRecolector = new javax.swing.JButton();
        btnRecolectarRecolector = new javax.swing.JButton();
        pnlConstructor = new javax.swing.JPanel();
        pnlInventarioConstructor = new javax.swing.JPanel();
        btnInventarioConstructor_1 = new javax.swing.JButton();
        btnInventarioConstructor_2 = new javax.swing.JButton();
        btnInventarioConstructor_3 = new javax.swing.JButton();
        btnInventarioConstructor_4 = new javax.swing.JButton();
        btnInventarioConstructor_7 = new javax.swing.JButton();
        btnInventarioConstructor_8 = new javax.swing.JButton();
        btnInventarioConstructor_5 = new javax.swing.JButton();
        btnInventarioConstructor_9 = new javax.swing.JButton();
        btnInventarioConstructor_6 = new javax.swing.JButton();
        lblConstructorSelect = new javax.swing.JLabel();
        btnMoverConstructor = new javax.swing.JButton();
        btnComerConstructor = new javax.swing.JButton();
        btnIrRefugioConstructor = new javax.swing.JButton();
        btnIntercambiarConstructor = new javax.swing.JButton();
        btnCrearRefugioConstructor = new javax.swing.JButton();
        btnRepararRefugioConstructor1 = new javax.swing.JButton();
        pnlCientifico = new javax.swing.JPanel();
        pnlInventarioCientifico = new javax.swing.JPanel();
        btnInventarioCientifico_1 = new javax.swing.JButton();
        btnInventarioCientifico_2 = new javax.swing.JButton();
        btnInventarioCientifico_3 = new javax.swing.JButton();
        btnInventarioCientifico_4 = new javax.swing.JButton();
        btnInventarioCientifico_5 = new javax.swing.JButton();
        btnInventarioCientifico_6 = new javax.swing.JButton();
        btnInventarioCientifico_7 = new javax.swing.JButton();
        btnInventarioCientifico_8 = new javax.swing.JButton();
        btnInventarioCientifico_9 = new javax.swing.JButton();
        lblCientificoSelect = new javax.swing.JLabel();
        btnMoverCientifico = new javax.swing.JButton();
        btnComerCientifico = new javax.swing.JButton();
        btnIrRefugioCientifico = new javax.swing.JButton();
        btnIntercambiarCientifico = new javax.swing.JButton();
        btnCrearMedicamentoCientifico = new javax.swing.JButton();
        pnlCurandero = new javax.swing.JPanel();
        pnlInventarioCurandero = new javax.swing.JPanel();
        btnInventarioCurandero_1 = new javax.swing.JButton();
        btnInventarioCurandero_2 = new javax.swing.JButton();
        btnInventarioCurandero_3 = new javax.swing.JButton();
        btnInventarioCurandero_4 = new javax.swing.JButton();
        btnInventarioCurandero_5 = new javax.swing.JButton();
        btnInventarioCurandero_6 = new javax.swing.JButton();
        btnInventarioCurandero_7 = new javax.swing.JButton();
        btnInventarioCurandero_8 = new javax.swing.JButton();
        btnInventarioCurandero_9 = new javax.swing.JButton();
        lblCuranderoSelect = new javax.swing.JLabel();
        btnMoverCurandero = new javax.swing.JButton();
        btnComerCurandero = new javax.swing.JButton();
        btnIrRefugioCurandero = new javax.swing.JButton();
        btnIntercambiarCurandero = new javax.swing.JButton();
        btnCurarCurandero = new javax.swing.JButton();
        btnCrearRemedioCurandero = new javax.swing.JButton();
        btnRecolectarCurandero = new javax.swing.JButton();
        pnlExplorador = new javax.swing.JPanel();
        pnlInventarioExplorador = new javax.swing.JPanel();
        btnInventarioExplorador_1 = new javax.swing.JButton();
        btnInventarioExplorador_2 = new javax.swing.JButton();
        btnInventarioExplorador_3 = new javax.swing.JButton();
        btnInventarioExplorador_4 = new javax.swing.JButton();
        btnInventarioExplorador_5 = new javax.swing.JButton();
        btnInventarioExplorador_6 = new javax.swing.JButton();
        btnInventarioExplorador_7 = new javax.swing.JButton();
        btnInventarioExplorador_8 = new javax.swing.JButton();
        btnInventarioExplorador_9 = new javax.swing.JButton();
        lblExploradorSelect = new javax.swing.JLabel();
        btnMoverExplorador = new javax.swing.JButton();
        btnComerExplorador = new javax.swing.JButton();
        btnIrRefugioExplorador = new javax.swing.JButton();
        btnIntercambiarExplorador = new javax.swing.JButton();
        btnExplorarExplorador = new javax.swing.JButton();
        btnRecolectarExplorador = new javax.swing.JButton();
        pnlDatosRefugio = new javax.swing.JPanel();
        lbl_vidaText1 = new javax.swing.JLabel();
        barraDeEstabilidad = new javax.swing.JProgressBar();
        lblStatusPersonajeEnRefugio_1 = new javax.swing.JLabel();
        lblStatusPersonajeEnRefugio_2 = new javax.swing.JLabel();
        lblStatusPersonajeEnRefugio_3 = new javax.swing.JLabel();
        lbl_statusText1 = new javax.swing.JLabel();
        pnlDatosRefugio1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txfInventarioDisplay = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(51, 51, 51));
        setMinimumSize(new java.awt.Dimension(1540, 677));
        getContentPane().setLayout(null);

        pnlMap.setLayout(null);

        lblCientificoMapa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Characters/Cientifico.png"))); // NOI18N
        lblCientificoMapa.setText("Cientifico");
        pnlMap.add(lblCientificoMapa);
        lblCientificoMapa.setBounds(460, 290, 10, 22);

        lblCazadorMapa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Characters/Cazador.png"))); // NOI18N
        lblCazadorMapa.setText("Cazador");
        pnlMap.add(lblCazadorMapa);
        lblCazadorMapa.setBounds(480, 280, 10, 22);

        lblRecolectorMapa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Characters/Recolector.png"))); // NOI18N
        lblRecolectorMapa.setText("Recolector");
        pnlMap.add(lblRecolectorMapa);
        lblRecolectorMapa.setBounds(490, 360, 10, 22);

        lblConstructorMapa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Characters/Constructor.png"))); // NOI18N
        lblConstructorMapa.setText("Constructor");
        pnlMap.add(lblConstructorMapa);
        lblConstructorMapa.setBounds(470, 350, 10, 22);

        lblCuranderoMapa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Characters/Curandero.png"))); // NOI18N
        lblCuranderoMapa.setText("Curandero");
        pnlMap.add(lblCuranderoMapa);
        lblCuranderoMapa.setBounds(450, 340, 10, 22);

        lblExploradorMapa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Characters/Explorador.png"))); // NOI18N
        lblExploradorMapa.setText("Explorador");
        pnlMap.add(lblExploradorMapa);
        lblExploradorMapa.setBounds(450, 310, 10, 22);

        lblCubre00.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre00.setText("0,0");
        lblCubre00.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre00.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre00.setOpaque(true);
        lblCubre00.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre00MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre00);
        lblCubre00.setBounds(0, 0, 110, 110);

        lblCubre10.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre10.setText("1,0");
        lblCubre10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre10.setOpaque(true);
        lblCubre10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre10MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre10);
        lblCubre10.setBounds(110, 0, 110, 110);

        lblCubre20.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre20.setText("2,0");
        lblCubre20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre20.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre20.setOpaque(true);
        lblCubre20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre20MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre20);
        lblCubre20.setBounds(220, 0, 110, 110);

        lblCubre30.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre30.setText("3,0");
        lblCubre30.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre30.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre30.setOpaque(true);
        lblCubre30.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre30MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre30);
        lblCubre30.setBounds(330, 0, 110, 110);

        lblCubre40.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre40.setText("4,0");
        lblCubre40.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre40.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre40.setOpaque(true);
        lblCubre40.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre40MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre40);
        lblCubre40.setBounds(440, 0, 110, 110);

        lblCubre50.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre50.setText("5,0");
        lblCubre50.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre50.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre50.setOpaque(true);
        lblCubre50.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre50MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre50);
        lblCubre50.setBounds(550, 0, 120, 110);

        lblCubre01.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre01.setText("0,1");
        lblCubre01.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre01.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre01.setOpaque(true);
        lblCubre01.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre01MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre01);
        lblCubre01.setBounds(0, 110, 110, 110);

        lblCubre11.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre11.setText("1,1");
        lblCubre11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre11.setOpaque(true);
        lblCubre11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre11MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre11);
        lblCubre11.setBounds(110, 110, 110, 110);

        lblCubre21.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre21.setText("2,1");
        lblCubre21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre21.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre21.setOpaque(true);
        lblCubre21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre21MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre21);
        lblCubre21.setBounds(220, 110, 110, 110);

        lblCubre31.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre31.setText("3,1");
        lblCubre31.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre31.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre31.setOpaque(true);
        lblCubre31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre31MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre31);
        lblCubre31.setBounds(330, 110, 110, 110);

        lblCubre41.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre41.setText("4,1");
        lblCubre41.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre41.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre41.setOpaque(true);
        lblCubre41.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre41MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre41);
        lblCubre41.setBounds(440, 110, 110, 110);

        lblCubre51.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre51.setText("5,1");
        lblCubre51.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre51.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre51.setOpaque(true);
        lblCubre51.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre51MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre51);
        lblCubre51.setBounds(550, 110, 120, 110);

        lblCubre02.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre02.setText("0.2");
        lblCubre02.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre02.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre02.setOpaque(true);
        lblCubre02.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre02MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre02);
        lblCubre02.setBounds(0, 220, 110, 110);

        lblCubre12.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre12.setText("1.2");
        lblCubre12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre12.setOpaque(true);
        lblCubre12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre12MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre12);
        lblCubre12.setBounds(110, 220, 110, 110);

        lblCubre22.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre22.setText("2.2");
        lblCubre22.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre22.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre22.setOpaque(true);
        lblCubre22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre22MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre22);
        lblCubre22.setBounds(220, 220, 110, 110);

        lblCubre52.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre52.setText("5.2");
        lblCubre52.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre52.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre52.setOpaque(true);
        lblCubre52.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre52MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre52);
        lblCubre52.setBounds(550, 220, 120, 110);

        lblCubre03.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre03.setText("0,3");
        lblCubre03.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre03.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre03.setOpaque(true);
        lblCubre03.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre03MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre03);
        lblCubre03.setBounds(0, 330, 110, 110);

        lblCubre13.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre13.setText("1,3");
        lblCubre13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre13.setOpaque(true);
        lblCubre13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre13MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre13);
        lblCubre13.setBounds(110, 330, 110, 110);

        lblCubre23.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre23.setText("2,3");
        lblCubre23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre23.setOpaque(true);
        lblCubre23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre23MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre23);
        lblCubre23.setBounds(220, 330, 110, 110);

        lblCubre53.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre53.setText("5,3");
        lblCubre53.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre53.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre53.setOpaque(true);
        lblCubre53.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre53MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre53);
        lblCubre53.setBounds(550, 330, 120, 110);

        lblCubre04.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre04.setText("0,4");
        lblCubre04.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre04.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre04.setOpaque(true);
        lblCubre04.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre04MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre04);
        lblCubre04.setBounds(0, 440, 110, 110);

        lblCubre14.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre14.setText("1,4");
        lblCubre14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre14.setOpaque(true);
        lblCubre14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre14MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre14);
        lblCubre14.setBounds(110, 440, 110, 110);

        lblCubre24.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre24.setText("2,4");
        lblCubre24.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre24.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre24.setOpaque(true);
        lblCubre24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre24MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre24);
        lblCubre24.setBounds(220, 440, 110, 110);

        lblCubre34.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre34.setText("3,4");
        lblCubre34.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre34.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre34.setOpaque(true);
        lblCubre34.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre34MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre34);
        lblCubre34.setBounds(330, 440, 110, 110);

        lblCubre44.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre44.setText("4,4");
        lblCubre44.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre44.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre44.setOpaque(true);
        lblCubre44.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre44MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre44);
        lblCubre44.setBounds(440, 440, 110, 110);

        lblCubre54.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre54.setText("5,4");
        lblCubre54.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre54.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre54.setOpaque(true);
        lblCubre54.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre54MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre54);
        lblCubre54.setBounds(550, 440, 120, 110);

        lblCubre05.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre05.setText("0,5");
        lblCubre05.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre05.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre05.setOpaque(true);
        lblCubre05.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre05MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre05);
        lblCubre05.setBounds(0, 550, 110, 110);

        lblCubre15.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre15.setText("1,5");
        lblCubre15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre15.setOpaque(true);
        lblCubre15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre15MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre15);
        lblCubre15.setBounds(110, 550, 110, 110);

        lblCubre25.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre25.setText("2,5");
        lblCubre25.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre25.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre25.setOpaque(true);
        lblCubre25.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre25MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre25);
        lblCubre25.setBounds(220, 550, 110, 110);

        lblCubre35.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre35.setText("3,5");
        lblCubre35.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre35.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre35.setOpaque(true);
        lblCubre35.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre35MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre35);
        lblCubre35.setBounds(330, 550, 110, 110);

        lblCubre45.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre45.setText("4,5");
        lblCubre45.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre45.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre45.setOpaque(true);
        lblCubre45.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre45MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre45);
        lblCubre45.setBounds(440, 550, 110, 110);

        lblCubre55.setBackground(new java.awt.Color(0, 51, 51));
        lblCubre55.setText("0,5");
        lblCubre55.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lblCubre55.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblCubre55.setOpaque(true);
        lblCubre55.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblCubre55MousePressed(evt);
            }
        });
        pnlMap.add(lblCubre55);
        lblCubre55.setBounds(550, 550, 120, 110);

        lblMap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/map.jpg"))); // NOI18N
        lblMap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMapMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblMapMousePressed(evt);
            }
        });
        pnlMap.add(lblMap);
        lblMap.setBounds(0, 0, 670, 620);

        getContentPane().add(pnlMap);
        pnlMap.setBounds(10, 10, 670, 620);

        pnlContainer.setBackground(new java.awt.Color(51, 51, 51));
        pnlContainer.setLayout(null);

        pnlOpcionesDios.setBackground(new java.awt.Color(130, 130, 130));
        pnlOpcionesDios.setPreferredSize(new java.awt.Dimension(190, 136));
        pnlOpcionesDios.setLayout(null);

        jLabel6.setText("días");
        pnlOpcionesDios.add(jLabel6);
        jLabel6.setBounds(480, 90, 60, 16);

        lblDiaActual.setText("1");
        pnlOpcionesDios.add(lblDiaActual);
        lblDiaActual.setBounds(730, 10, 70, 16);

        jLabel16.setText("Coords.");
        pnlOpcionesDios.add(jLabel16);
        jLabel16.setBounds(650, 30, 41, 16);

        lblCoordsY.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblCoordsY.setText("320");
        pnlOpcionesDios.add(lblCoordsY);
        lblCoordsY.setBounds(750, 20, 50, 30);

        lblCoordsX.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblCoordsX.setText("500");
        pnlOpcionesDios.add(lblCoordsX);
        lblCoordsX.setBounds(700, 20, 40, 30);

        btnNextDay.setText("Avanzar día");
        btnNextDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextDayActionPerformed(evt);
            }
        });
        pnlOpcionesDios.add(btnNextDay);
        btnNextDay.setBounds(590, 10, 100, 20);

        jTabbedPane1.setBackground(new java.awt.Color(130, 130, 130));

        pnlMedicinas.setBackground(new java.awt.Color(130, 130, 130));

        btnSpawnearMedicinaInventario.setText("Medicina");
        btnSpawnearMedicinaInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpawnearMedicinaInventarioActionPerformed(evt);
            }
        });

        btnSpawnearUnguentoInventario.setText("Remedio");
        btnSpawnearUnguentoInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpawnearUnguentoInventarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMedicinasLayout = new javax.swing.GroupLayout(pnlMedicinas);
        pnlMedicinas.setLayout(pnlMedicinasLayout);
        pnlMedicinasLayout.setHorizontalGroup(
            pnlMedicinasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMedicinasLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(pnlMedicinasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnSpawnearUnguentoInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSpawnearMedicinaInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(194, Short.MAX_VALUE))
        );
        pnlMedicinasLayout.setVerticalGroup(
            pnlMedicinasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMedicinasLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(btnSpawnearMedicinaInventario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(btnSpawnearUnguentoInventario)
                .addGap(17, 17, 17))
        );

        jTabbedPane1.addTab("Medicina", pnlMedicinas);

        pnlAnimales.setBackground(new java.awt.Color(130, 130, 130));

        botonSpawnearCerdo.setText("Cerdo");
        botonSpawnearCerdo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonSpawnearCerdoMouseClicked(evt);
            }
        });
        botonSpawnearCerdo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSpawnearCerdoActionPerformed(evt);
            }
        });

        botonSpawnearLobo.setText("Lobo");
        botonSpawnearLobo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSpawnearLoboActionPerformed(evt);
            }
        });

        botonSpawnearOso.setText("Oso");
        botonSpawnearOso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSpawnearOsoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlAnimalesLayout = new javax.swing.GroupLayout(pnlAnimales);
        pnlAnimales.setLayout(pnlAnimalesLayout);
        pnlAnimalesLayout.setHorizontalGroup(
            pnlAnimalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAnimalesLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(pnlAnimalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonSpawnearOso, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonSpawnearLobo, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonSpawnearCerdo, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(193, Short.MAX_VALUE))
        );
        pnlAnimalesLayout.setVerticalGroup(
            pnlAnimalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAnimalesLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(botonSpawnearCerdo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonSpawnearLobo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonSpawnearOso)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Animales", pnlAnimales);

        pnlRecursos.setBackground(new java.awt.Color(130, 130, 130));

        btnSpawnearHierbas.setText("Hierbas medicinales");
        btnSpawnearHierbas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpawnearHierbasActionPerformed(evt);
            }
        });

        btnSpawnearMadera.setText("Madera");
        btnSpawnearMadera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpawnearMaderaActionPerformed(evt);
            }
        });

        btnSpawnearCiencia.setText("Ciencia");
        btnSpawnearCiencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpawnearCienciaActionPerformed(evt);
            }
        });

        btnSpawnearAgua.setText("Agua");
        btnSpawnearAgua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpawnearAguaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlRecursosLayout = new javax.swing.GroupLayout(pnlRecursos);
        pnlRecursos.setLayout(pnlRecursosLayout);
        pnlRecursosLayout.setHorizontalGroup(
            pnlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRecursosLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(pnlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSpawnearHierbas, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlRecursosLayout.createSequentialGroup()
                        .addComponent(btnSpawnearMadera, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSpawnearAgua, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnSpawnearCiencia, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        pnlRecursosLayout.setVerticalGroup(
            pnlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRecursosLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(btnSpawnearHierbas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSpawnearMadera)
                    .addComponent(btnSpawnearAgua))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSpawnearCiencia)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Recursos", pnlRecursos);

        pnlComida.setBackground(new java.awt.Color(130, 130, 130));

        btnSpawnearFrutas.setText("Fruta");
        btnSpawnearFrutas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpawnearFrutasActionPerformed(evt);
            }
        });

        btnSpawnearCarnes.setText("Carne");
        btnSpawnearCarnes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpawnearCarnesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlComidaLayout = new javax.swing.GroupLayout(pnlComida);
        pnlComida.setLayout(pnlComidaLayout);
        pnlComidaLayout.setHorizontalGroup(
            pnlComidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlComidaLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(pnlComidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnSpawnearCarnes, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSpawnearFrutas, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(194, Short.MAX_VALUE))
        );
        pnlComidaLayout.setVerticalGroup(
            pnlComidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlComidaLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(btnSpawnearFrutas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(btnSpawnearCarnes)
                .addGap(17, 17, 17))
        );

        jTabbedPane1.addTab("Comida", pnlComida);

        pnlBitacoras.setBackground(new java.awt.Color(130, 130, 130));

        btnBitCazador.setText("Cazador");
        btnBitCazador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBitCazadorActionPerformed(evt);
            }
        });

        btnBitCientifico.setText("Científico");
        btnBitCientifico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBitCientificoActionPerformed(evt);
            }
        });

        btnBitConstructor.setText("Constructor");
        btnBitConstructor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBitConstructorActionPerformed(evt);
            }
        });

        btnBitExplorador.setText("Explorador");
        btnBitExplorador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBitExploradorActionPerformed(evt);
            }
        });

        btnBitCurandero.setText("Curandero");
        btnBitCurandero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBitCuranderoActionPerformed(evt);
            }
        });

        btnBitRecolector.setText("Recolector");
        btnBitRecolector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBitRecolectorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlBitacorasLayout = new javax.swing.GroupLayout(pnlBitacoras);
        pnlBitacoras.setLayout(pnlBitacorasLayout);
        pnlBitacorasLayout.setHorizontalGroup(
            pnlBitacorasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBitacorasLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(pnlBitacorasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlBitacorasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnBitConstructor, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBitCientifico, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnBitCazador, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlBitacorasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBitRecolector, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBitCurandero, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBitExplorador, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        pnlBitacorasLayout.setVerticalGroup(
            pnlBitacorasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBitacorasLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(pnlBitacorasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlBitacorasLayout.createSequentialGroup()
                        .addGroup(pnlBitacorasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnBitExplorador)
                            .addComponent(btnBitCazador))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBitCurandero)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBitRecolector))
                    .addGroup(pnlBitacorasLayout.createSequentialGroup()
                        .addComponent(btnBitCientifico)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBitConstructor)))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Bitácoras", pnlBitacoras);

        pnlOpcionesDios.add(jTabbedPane1);
        jTabbedPane1.setBounds(0, 0, 360, 160);

        btnSpawnearTormenta.setText("Tormenta");
        btnSpawnearTormenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpawnearTormentaActionPerformed(evt);
            }
        });
        pnlOpcionesDios.add(btnSpawnearTormenta);
        btnSpawnearTormenta.setBounds(680, 140, 120, 20);

        btnHerirPersonaje.setText("Herir");
        btnHerirPersonaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHerirPersonajeActionPerformed(evt);
            }
        });
        pnlOpcionesDios.add(btnHerirPersonaje);
        btnHerirPersonaje.setBounds(680, 60, 120, 20);

        jLabel1.setText("Cantidad");
        pnlOpcionesDios.add(jLabel1);
        jLabel1.setBounds(600, 70, 50, 16);

        cmbCantidadASpawnear.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5" }));
        pnlOpcionesDios.add(cmbCantidadASpawnear);
        cmbCantidadASpawnear.setBounds(590, 100, 60, 22);

        btnEnfermarPersonaje.setText("Enfermar");
        btnEnfermarPersonaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnfermarPersonajeActionPerformed(evt);
            }
        });
        pnlOpcionesDios.add(btnEnfermarPersonaje);
        btnEnfermarPersonaje.setBounds(680, 80, 120, 20);

        btnAtaqueAnimal.setText("Ataque");
        btnAtaqueAnimal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAtaqueAnimalMouseClicked(evt);
            }
        });
        btnAtaqueAnimal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtaqueAnimalActionPerformed(evt);
            }
        });
        pnlOpcionesDios.add(btnAtaqueAnimal);
        btnAtaqueAnimal.setBounds(680, 120, 120, 20);

        btnCurarDebuffsPersonaje.setText("Curar debuffs");
        btnCurarDebuffsPersonaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCurarDebuffsPersonajeActionPerformed(evt);
            }
        });
        pnlOpcionesDios.add(btnCurarDebuffsPersonaje);
        btnCurarDebuffsPersonaje.setBounds(680, 100, 120, 20);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("CLIMA");
        pnlOpcionesDios.add(jLabel2);
        jLabel2.setBounds(440, 20, 60, 30);

        lblEstadoClima.setText("Despejado");
        pnlOpcionesDios.add(lblEstadoClima);
        lblEstadoClima.setBounds(420, 60, 100, 16);

        jLabel7.setText("Día - ");
        pnlOpcionesDios.add(jLabel7);
        jLabel7.setBounds(700, 10, 37, 16);

        lblDuracionTormenta.setText("0");
        pnlOpcionesDios.add(lblDuracionTormenta);
        lblDuracionTormenta.setBounds(460, 90, 30, 16);

        jLabel8.setText("Duración: ");
        pnlOpcionesDios.add(jLabel8);
        jLabel8.setBounds(400, 90, 70, 16);

        pnlContainer.add(pnlOpcionesDios);
        pnlOpcionesDios.setBounds(0, 0, 810, 164);

        pnlCazador.setBackground(new java.awt.Color(130, 130, 130));
        pnlCazador.setForeground(new java.awt.Color(153, 153, 153));
        pnlCazador.setPreferredSize(new java.awt.Dimension(190, 136));

        pnlInventarioCazador.setBackground(new java.awt.Color(153, 153, 153));
        pnlInventarioCazador.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.gray, java.awt.Color.gray, null, null));

        btnInventarioCazador_1.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCazador_1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCazador_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCazador_1ActionPerformed(evt);
            }
        });

        btnInventarioCazador_2.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCazador_2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCazador_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCazador_2ActionPerformed(evt);
            }
        });

        btnInventarioCazador_3.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCazador_3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCazador_3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCazador_3ActionPerformed(evt);
            }
        });

        btnInventarioCazador_4.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCazador_4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCazador_4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCazador_4ActionPerformed(evt);
            }
        });

        btnInventarioCazador_5.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCazador_5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCazador_5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCazador_5ActionPerformed(evt);
            }
        });

        btnInventarioCazador_6.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCazador_6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCazador_6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCazador_6ActionPerformed(evt);
            }
        });

        btnInventarioCazador_7.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCazador_7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCazador_7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCazador_7ActionPerformed(evt);
            }
        });

        btnInventarioCazador_8.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCazador_8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCazador_8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCazador_8ActionPerformed(evt);
            }
        });

        btnInventarioCazador_9.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCazador_9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCazador_9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCazador_9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlInventarioCazadorLayout = new javax.swing.GroupLayout(pnlInventarioCazador);
        pnlInventarioCazador.setLayout(pnlInventarioCazadorLayout);
        pnlInventarioCazadorLayout.setHorizontalGroup(
            pnlInventarioCazadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventarioCazadorLayout.createSequentialGroup()
                .addGroup(pnlInventarioCazadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnInventarioCazador_4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioCazador_7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioCazador_1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInventarioCazadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInventarioCazadorLayout.createSequentialGroup()
                        .addGroup(pnlInventarioCazadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnInventarioCazador_5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnInventarioCazador_8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlInventarioCazadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnInventarioCazador_6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnInventarioCazador_9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInventarioCazadorLayout.createSequentialGroup()
                        .addComponent(btnInventarioCazador_2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnInventarioCazador_3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        pnlInventarioCazadorLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnInventarioCazador_2, btnInventarioCazador_3});

        pnlInventarioCazadorLayout.setVerticalGroup(
            pnlInventarioCazadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventarioCazadorLayout.createSequentialGroup()
                .addGroup(pnlInventarioCazadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInventarioCazadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnInventarioCazador_2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnInventarioCazador_3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnInventarioCazador_1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInventarioCazadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInventarioCazadorLayout.createSequentialGroup()
                        .addComponent(btnInventarioCazador_4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInventarioCazador_7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlInventarioCazadorLayout.createSequentialGroup()
                        .addComponent(btnInventarioCazador_5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInventarioCazador_8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlInventarioCazadorLayout.createSequentialGroup()
                        .addComponent(btnInventarioCazador_6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInventarioCazador_9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        lblCazadorSelect.setText("Cazador");
        lblCazadorSelect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCazadorSelectMouseClicked(evt);
            }
        });

        btnMoverCazador.setBackground(new java.awt.Color(102, 102, 102));
        btnMoverCazador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/mover.png"))); // NOI18N
        btnMoverCazador.setToolTipText("Moverse");
        btnMoverCazador.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnMoverCazador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoverCazadorActionPerformed(evt);
            }
        });

        btnComerCazador.setBackground(new java.awt.Color(102, 102, 102));
        btnComerCazador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/comer.png"))); // NOI18N
        btnComerCazador.setToolTipText("Comer");
        btnComerCazador.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnComerCazador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComerCazadorActionPerformed(evt);
            }
        });

        btnIrRefugioCazador.setBackground(new java.awt.Color(102, 102, 102));
        btnIrRefugioCazador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/refugiarse.png"))); // NOI18N
        btnIrRefugioCazador.setToolTipText("Refugiarse");
        btnIrRefugioCazador.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnIrRefugioCazador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIrRefugioCazadorActionPerformed(evt);
            }
        });

        btnIntercambiarCazador.setBackground(new java.awt.Color(102, 102, 102));
        btnIntercambiarCazador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/intercambiar.png"))); // NOI18N
        btnIntercambiarCazador.setToolTipText("Intercambiar Recurso");
        btnIntercambiarCazador.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnIntercambiarCazador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIntercambiarCazadorActionPerformed(evt);
            }
        });

        btnCazarCazador.setBackground(new java.awt.Color(102, 102, 102));
        btnCazarCazador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/cazar.png"))); // NOI18N
        btnCazarCazador.setToolTipText("Cazar");
        btnCazarCazador.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnCazarCazador.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCazarCazadorMouseClicked(evt);
            }
        });
        btnCazarCazador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCazarCazadorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlCazadorLayout = new javax.swing.GroupLayout(pnlCazador);
        pnlCazador.setLayout(pnlCazadorLayout);
        pnlCazadorLayout.setHorizontalGroup(
            pnlCazadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCazadorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlInventarioCazador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlCazadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCazadorLayout.createSequentialGroup()
                        .addComponent(btnIntercambiarCazador, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCazarCazador, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCazadorLayout.createSequentialGroup()
                        .addGroup(pnlCazadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlCazadorLayout.createSequentialGroup()
                                .addComponent(btnMoverCazador, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnComerCazador, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnIrRefugioCazador, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblCazadorSelect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(41, 41, 41))))
        );
        pnlCazadorLayout.setVerticalGroup(
            pnlCazadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCazadorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCazadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCazadorLayout.createSequentialGroup()
                        .addComponent(lblCazadorSelect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlCazadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnIrRefugioCazador, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlCazadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnMoverCazador, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnComerCazador, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlCazadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnIntercambiarCazador, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCazarCazador, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(pnlInventarioCazador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pnlContainer.add(pnlCazador);
        pnlCazador.setBounds(6, 176, 272, 136);

        pnlDatos.setBackground(new java.awt.Color(130, 130, 130));
        pnlDatos.setForeground(new java.awt.Color(153, 153, 153));
        pnlDatos.setPreferredSize(new java.awt.Dimension(190, 136));
        pnlDatos.setLayout(null);

        pnlDatosPersonaje.setBackground(new java.awt.Color(130, 130, 130));
        pnlDatosPersonaje.setLayout(null);

        lbl_statusText.setText("Status");
        pnlDatosPersonaje.add(lbl_statusText);
        lbl_statusText.setBounds(60, 90, 40, 16);

        barraDeEnergia.setForeground(new java.awt.Color(51, 204, 0));
        barraDeEnergia.setToolTipText("");
        barraDeEnergia.setStringPainted(true);
        pnlDatosPersonaje.add(barraDeEnergia);
        barraDeEnergia.setBounds(65, 43, 185, 13);

        lbl_energiaText.setText("Energía");
        pnlDatosPersonaje.add(lbl_energiaText);
        lbl_energiaText.setBounds(9, 40, 50, 16);

        barraDeVida.setForeground(new java.awt.Color(255, 0, 0));
        barraDeVida.setStringPainted(true);
        pnlDatosPersonaje.add(barraDeVida);
        barraDeVida.setBounds(65, 21, 184, 13);

        lbl_vidaText.setText("Vida");
        pnlDatosPersonaje.add(lbl_vidaText);
        lbl_vidaText.setBounds(9, 18, 50, 16);

        lblCurrentSelectedCharacter.setText("Nombre");
        pnlDatosPersonaje.add(lblCurrentSelectedCharacter);
        lblCurrentSelectedCharacter.setBounds(93, 0, 90, 16);
        pnlDatosPersonaje.add(lblAvatar);
        lblAvatar.setBounds(10, 70, 27, 50);

        lblStatusPersonajeEnRefugio.setBackground(new java.awt.Color(0, 0, 0));
        lblStatusPersonajeEnRefugio.setText("Afuera");
        lblStatusPersonajeEnRefugio.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        pnlDatosPersonaje.add(lblStatusPersonajeEnRefugio);
        lblStatusPersonajeEnRefugio.setBounds(130, 70, 120, 20);

        lblStatusPersonajeHeridas.setBackground(new java.awt.Color(0, 0, 0));
        lblStatusPersonajeHeridas.setText("Sin heridas");
        lblStatusPersonajeHeridas.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        pnlDatosPersonaje.add(lblStatusPersonajeHeridas);
        lblStatusPersonajeHeridas.setBounds(130, 90, 120, 20);

        lblStatusPersonajeSalud.setBackground(new java.awt.Color(0, 0, 0));
        lblStatusPersonajeSalud.setText("Saludable");
        lblStatusPersonajeSalud.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        pnlDatosPersonaje.add(lblStatusPersonajeSalud);
        lblStatusPersonajeSalud.setBounds(130, 110, 120, 20);

        pnlDatos.add(pnlDatosPersonaje);
        pnlDatosPersonaje.setBounds(0, 0, 260, 140);

        pnlContainer.add(pnlDatos);
        pnlDatos.setBounds(284, 176, 260, 136);

        pnlRecolector.setBackground(new java.awt.Color(130, 130, 130));
        pnlRecolector.setForeground(new java.awt.Color(153, 153, 153));
        pnlRecolector.setPreferredSize(new java.awt.Dimension(190, 136));

        pnlInventariorecolector.setBackground(new java.awt.Color(153, 153, 153));
        pnlInventariorecolector.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.gray, java.awt.Color.gray, null, null));

        btnInventarioRecolector_1.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioRecolector_1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioRecolector_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioRecolector_1ActionPerformed(evt);
            }
        });

        btnInventarioRecolector_2.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioRecolector_2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioRecolector_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioRecolector_2ActionPerformed(evt);
            }
        });

        btnInventarioRecolector_3.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioRecolector_3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioRecolector_3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioRecolector_3ActionPerformed(evt);
            }
        });

        btnInventarioRecolector_4.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioRecolector_4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioRecolector_4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioRecolector_4ActionPerformed(evt);
            }
        });

        btnInventarioRecolector_5.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioRecolector_5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioRecolector_5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioRecolector_5ActionPerformed(evt);
            }
        });

        btnInventarioRecolector_6.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioRecolector_6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioRecolector_6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioRecolector_6ActionPerformed(evt);
            }
        });

        btnInventarioRecolector_7.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioRecolector_7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioRecolector_7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioRecolector_7ActionPerformed(evt);
            }
        });

        btnInventarioRecolector_8.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioRecolector_8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioRecolector_8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioRecolector_8ActionPerformed(evt);
            }
        });

        btnInventarioRecolector_9.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioRecolector_9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioRecolector_9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioRecolector_9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlInventariorecolectorLayout = new javax.swing.GroupLayout(pnlInventariorecolector);
        pnlInventariorecolector.setLayout(pnlInventariorecolectorLayout);
        pnlInventariorecolectorLayout.setHorizontalGroup(
            pnlInventariorecolectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventariorecolectorLayout.createSequentialGroup()
                .addComponent(btnInventarioRecolector_1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnInventarioRecolector_2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnInventarioRecolector_3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlInventariorecolectorLayout.createSequentialGroup()
                .addGroup(pnlInventariorecolectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnInventarioRecolector_4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioRecolector_7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInventariorecolectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnInventarioRecolector_5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioRecolector_8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInventariorecolectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnInventarioRecolector_6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioRecolector_9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        pnlInventariorecolectorLayout.setVerticalGroup(
            pnlInventariorecolectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventariorecolectorLayout.createSequentialGroup()
                .addGroup(pnlInventariorecolectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInventarioRecolector_1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioRecolector_2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioRecolector_3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInventariorecolectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInventariorecolectorLayout.createSequentialGroup()
                        .addComponent(btnInventarioRecolector_4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInventarioRecolector_7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlInventariorecolectorLayout.createSequentialGroup()
                        .addComponent(btnInventarioRecolector_5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInventarioRecolector_8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlInventariorecolectorLayout.createSequentialGroup()
                        .addComponent(btnInventarioRecolector_6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInventarioRecolector_9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        lblRecolectorSelect.setText("Recolector");
        lblRecolectorSelect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblRecolectorSelectMouseClicked(evt);
            }
        });

        btnMoverRecolector.setBackground(new java.awt.Color(102, 102, 102));
        btnMoverRecolector.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/mover.png"))); // NOI18N
        btnMoverRecolector.setToolTipText("Moverse");
        btnMoverRecolector.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnMoverRecolector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoverRecolectorActionPerformed(evt);
            }
        });

        btnComerRecolector.setBackground(new java.awt.Color(102, 102, 102));
        btnComerRecolector.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/comer.png"))); // NOI18N
        btnComerRecolector.setToolTipText("Comer");
        btnComerRecolector.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnComerRecolector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComerRecolectorActionPerformed(evt);
            }
        });

        btnIrRefugioRecolector.setBackground(new java.awt.Color(102, 102, 102));
        btnIrRefugioRecolector.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/refugiarse.png"))); // NOI18N
        btnIrRefugioRecolector.setToolTipText("Refugiarse");
        btnIrRefugioRecolector.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnIrRefugioRecolector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIrRefugioRecolectorActionPerformed(evt);
            }
        });

        btnEntregarRecolector.setBackground(new java.awt.Color(102, 102, 102));
        btnEntregarRecolector.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/intercambiar.png"))); // NOI18N
        btnEntregarRecolector.setToolTipText("Entregar Recurso");
        btnEntregarRecolector.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnEntregarRecolector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntregarRecolectorActionPerformed(evt);
            }
        });

        btnRecolectarRecolector.setBackground(new java.awt.Color(102, 102, 102));
        btnRecolectarRecolector.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/recolectar.png"))); // NOI18N
        btnRecolectarRecolector.setToolTipText("Recolectar");
        btnRecolectarRecolector.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnRecolectarRecolector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecolectarRecolectorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlRecolectorLayout = new javax.swing.GroupLayout(pnlRecolector);
        pnlRecolector.setLayout(pnlRecolectorLayout);
        pnlRecolectorLayout.setHorizontalGroup(
            pnlRecolectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRecolectorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlInventariorecolector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlRecolectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRecolectorLayout.createSequentialGroup()
                        .addComponent(lblRecolectorSelect, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                        .addGap(18, 18, 18))
                    .addGroup(pnlRecolectorLayout.createSequentialGroup()
                        .addGroup(pnlRecolectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlRecolectorLayout.createSequentialGroup()
                                .addComponent(btnMoverRecolector, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnComerRecolector, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnIrRefugioRecolector, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlRecolectorLayout.createSequentialGroup()
                                .addComponent(btnEntregarRecolector, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRecolectarRecolector, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        pnlRecolectorLayout.setVerticalGroup(
            pnlRecolectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRecolectorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRecolectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRecolectorLayout.createSequentialGroup()
                        .addComponent(lblRecolectorSelect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlRecolectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnMoverRecolector, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnComerRecolector, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnIrRefugioRecolector, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlRecolectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEntregarRecolector, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRecolectarRecolector, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(pnlInventariorecolector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pnlContainer.add(pnlRecolector);
        pnlRecolector.setBounds(6, 318, 272, 136);

        pnlConstructor.setBackground(new java.awt.Color(130, 130, 130));
        pnlConstructor.setForeground(new java.awt.Color(153, 153, 153));
        pnlConstructor.setPreferredSize(new java.awt.Dimension(190, 136));

        pnlInventarioConstructor.setBackground(new java.awt.Color(153, 153, 153));
        pnlInventarioConstructor.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.gray, java.awt.Color.gray, null, null));

        btnInventarioConstructor_1.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioConstructor_1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioConstructor_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioConstructor_1ActionPerformed(evt);
            }
        });

        btnInventarioConstructor_2.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioConstructor_2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioConstructor_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioConstructor_2ActionPerformed(evt);
            }
        });

        btnInventarioConstructor_3.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioConstructor_3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioConstructor_3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioConstructor_3ActionPerformed(evt);
            }
        });

        btnInventarioConstructor_4.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioConstructor_4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioConstructor_4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioConstructor_4ActionPerformed(evt);
            }
        });

        btnInventarioConstructor_7.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioConstructor_7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioConstructor_7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioConstructor_7ActionPerformed(evt);
            }
        });

        btnInventarioConstructor_8.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioConstructor_8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioConstructor_8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioConstructor_8ActionPerformed(evt);
            }
        });

        btnInventarioConstructor_5.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioConstructor_5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioConstructor_5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioConstructor_5ActionPerformed(evt);
            }
        });

        btnInventarioConstructor_9.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioConstructor_9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioConstructor_9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioConstructor_9ActionPerformed(evt);
            }
        });

        btnInventarioConstructor_6.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioConstructor_6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioConstructor_6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioConstructor_6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlInventarioConstructorLayout = new javax.swing.GroupLayout(pnlInventarioConstructor);
        pnlInventarioConstructor.setLayout(pnlInventarioConstructorLayout);
        pnlInventarioConstructorLayout.setHorizontalGroup(
            pnlInventarioConstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventarioConstructorLayout.createSequentialGroup()
                .addComponent(btnInventarioConstructor_1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnInventarioConstructor_2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnInventarioConstructor_3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlInventarioConstructorLayout.createSequentialGroup()
                .addGroup(pnlInventarioConstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnInventarioConstructor_4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioConstructor_7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInventarioConstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnInventarioConstructor_5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioConstructor_8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInventarioConstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnInventarioConstructor_6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioConstructor_9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        pnlInventarioConstructorLayout.setVerticalGroup(
            pnlInventarioConstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventarioConstructorLayout.createSequentialGroup()
                .addGroup(pnlInventarioConstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInventarioConstructor_1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioConstructor_2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioConstructor_3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInventarioConstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInventarioConstructorLayout.createSequentialGroup()
                        .addComponent(btnInventarioConstructor_4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInventarioConstructor_7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlInventarioConstructorLayout.createSequentialGroup()
                        .addComponent(btnInventarioConstructor_5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInventarioConstructor_8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlInventarioConstructorLayout.createSequentialGroup()
                        .addComponent(btnInventarioConstructor_6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInventarioConstructor_9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        lblConstructorSelect.setText("Constructor");
        lblConstructorSelect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblConstructorSelectMouseClicked(evt);
            }
        });

        btnMoverConstructor.setBackground(new java.awt.Color(102, 102, 102));
        btnMoverConstructor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/mover.png"))); // NOI18N
        btnMoverConstructor.setToolTipText("Moverse");
        btnMoverConstructor.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnMoverConstructor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoverConstructorActionPerformed(evt);
            }
        });

        btnComerConstructor.setBackground(new java.awt.Color(102, 102, 102));
        btnComerConstructor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/comer.png"))); // NOI18N
        btnComerConstructor.setToolTipText("Comer");
        btnComerConstructor.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnComerConstructor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComerConstructorActionPerformed(evt);
            }
        });

        btnIrRefugioConstructor.setBackground(new java.awt.Color(102, 102, 102));
        btnIrRefugioConstructor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/refugiarse.png"))); // NOI18N
        btnIrRefugioConstructor.setToolTipText("Refugiarse");
        btnIrRefugioConstructor.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnIrRefugioConstructor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIrRefugioConstructorActionPerformed(evt);
            }
        });

        btnIntercambiarConstructor.setBackground(new java.awt.Color(102, 102, 102));
        btnIntercambiarConstructor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/intercambiar.png"))); // NOI18N
        btnIntercambiarConstructor.setToolTipText("Intercambiar Recurso");
        btnIntercambiarConstructor.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnIntercambiarConstructor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIntercambiarConstructorActionPerformed(evt);
            }
        });

        btnCrearRefugioConstructor.setBackground(new java.awt.Color(102, 102, 102));
        btnCrearRefugioConstructor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/construir.png"))); // NOI18N
        btnCrearRefugioConstructor.setToolTipText("Construir refugio");
        btnCrearRefugioConstructor.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnCrearRefugioConstructor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearRefugioConstructorActionPerformed(evt);
            }
        });

        btnRepararRefugioConstructor1.setBackground(new java.awt.Color(102, 102, 102));
        btnRepararRefugioConstructor1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/arreglar.png"))); // NOI18N
        btnRepararRefugioConstructor1.setToolTipText("Reparar refugio");
        btnRepararRefugioConstructor1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnRepararRefugioConstructor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRepararRefugioConstructor1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlConstructorLayout = new javax.swing.GroupLayout(pnlConstructor);
        pnlConstructor.setLayout(pnlConstructorLayout);
        pnlConstructorLayout.setHorizontalGroup(
            pnlConstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlConstructorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlInventarioConstructor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlConstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblConstructorSelect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlConstructorLayout.createSequentialGroup()
                        .addComponent(btnMoverConstructor, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnComerConstructor, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIrRefugioConstructor, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlConstructorLayout.createSequentialGroup()
                        .addComponent(btnIntercambiarConstructor, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCrearRefugioConstructor, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRepararRefugioConstructor1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(88, 88, 88)))
                .addContainerGap())
        );
        pnlConstructorLayout.setVerticalGroup(
            pnlConstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlConstructorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlConstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlInventarioConstructor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlConstructorLayout.createSequentialGroup()
                        .addComponent(lblConstructorSelect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlConstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnMoverConstructor, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnComerConstructor, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnIrRefugioConstructor, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlConstructorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnIntercambiarConstructor, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCrearRefugioConstructor, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRepararRefugioConstructor1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pnlContainer.add(pnlConstructor);
        pnlConstructor.setBounds(284, 318, 260, 136);

        pnlCientifico.setBackground(new java.awt.Color(130, 130, 130));
        pnlCientifico.setForeground(new java.awt.Color(153, 153, 153));
        pnlCientifico.setPreferredSize(new java.awt.Dimension(190, 136));

        pnlInventarioCientifico.setBackground(new java.awt.Color(153, 153, 153));
        pnlInventarioCientifico.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.gray, java.awt.Color.gray, null, null));

        btnInventarioCientifico_1.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCientifico_1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCientifico_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCientifico_1ActionPerformed(evt);
            }
        });

        btnInventarioCientifico_2.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCientifico_2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCientifico_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCientifico_2ActionPerformed(evt);
            }
        });

        btnInventarioCientifico_3.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCientifico_3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCientifico_3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCientifico_3ActionPerformed(evt);
            }
        });

        btnInventarioCientifico_4.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCientifico_4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCientifico_4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCientifico_4ActionPerformed(evt);
            }
        });

        btnInventarioCientifico_5.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCientifico_5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCientifico_5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCientifico_5ActionPerformed(evt);
            }
        });

        btnInventarioCientifico_6.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCientifico_6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCientifico_6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCientifico_6ActionPerformed(evt);
            }
        });

        btnInventarioCientifico_7.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCientifico_7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCientifico_7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCientifico_7ActionPerformed(evt);
            }
        });

        btnInventarioCientifico_8.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCientifico_8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCientifico_8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCientifico_8ActionPerformed(evt);
            }
        });

        btnInventarioCientifico_9.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCientifico_9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCientifico_9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCientifico_9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlInventarioCientificoLayout = new javax.swing.GroupLayout(pnlInventarioCientifico);
        pnlInventarioCientifico.setLayout(pnlInventarioCientificoLayout);
        pnlInventarioCientificoLayout.setHorizontalGroup(
            pnlInventarioCientificoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventarioCientificoLayout.createSequentialGroup()
                .addComponent(btnInventarioCientifico_1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnInventarioCientifico_2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnInventarioCientifico_3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlInventarioCientificoLayout.createSequentialGroup()
                .addGroup(pnlInventarioCientificoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnInventarioCientifico_4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioCientifico_7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInventarioCientificoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnInventarioCientifico_5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioCientifico_8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInventarioCientificoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnInventarioCientifico_6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioCientifico_9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        pnlInventarioCientificoLayout.setVerticalGroup(
            pnlInventarioCientificoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventarioCientificoLayout.createSequentialGroup()
                .addGroup(pnlInventarioCientificoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInventarioCientifico_1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioCientifico_2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioCientifico_3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInventarioCientificoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInventarioCientificoLayout.createSequentialGroup()
                        .addComponent(btnInventarioCientifico_4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInventarioCientifico_7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlInventarioCientificoLayout.createSequentialGroup()
                        .addComponent(btnInventarioCientifico_5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInventarioCientifico_8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlInventarioCientificoLayout.createSequentialGroup()
                        .addComponent(btnInventarioCientifico_6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInventarioCientifico_9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        lblCientificoSelect.setText("Científico");
        lblCientificoSelect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCientificoSelectMouseClicked(evt);
            }
        });
        lblCientificoSelect.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lblCientificoSelectKeyPressed(evt);
            }
        });

        btnMoverCientifico.setBackground(new java.awt.Color(102, 102, 102));
        btnMoverCientifico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/mover.png"))); // NOI18N
        btnMoverCientifico.setToolTipText("Moverse");
        btnMoverCientifico.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnMoverCientifico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoverCientificoActionPerformed(evt);
            }
        });

        btnComerCientifico.setBackground(new java.awt.Color(102, 102, 102));
        btnComerCientifico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/comer.png"))); // NOI18N
        btnComerCientifico.setToolTipText("Comer");
        btnComerCientifico.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnComerCientifico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComerCientificoActionPerformed(evt);
            }
        });

        btnIrRefugioCientifico.setBackground(new java.awt.Color(102, 102, 102));
        btnIrRefugioCientifico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/refugiarse.png"))); // NOI18N
        btnIrRefugioCientifico.setToolTipText("Refugiarse");
        btnIrRefugioCientifico.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnIrRefugioCientifico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIrRefugioCientificoActionPerformed(evt);
            }
        });

        btnIntercambiarCientifico.setBackground(new java.awt.Color(102, 102, 102));
        btnIntercambiarCientifico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/intercambiar.png"))); // NOI18N
        btnIntercambiarCientifico.setToolTipText("Intercambiar Recurso");
        btnIntercambiarCientifico.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnIntercambiarCientifico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIntercambiarCientificoActionPerformed(evt);
            }
        });

        btnCrearMedicamentoCientifico.setBackground(new java.awt.Color(102, 102, 102));
        btnCrearMedicamentoCientifico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/hacer medicina.png"))); // NOI18N
        btnCrearMedicamentoCientifico.setToolTipText("Crear Medicina");
        btnCrearMedicamentoCientifico.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnCrearMedicamentoCientifico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearMedicamentoCientificoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlCientificoLayout = new javax.swing.GroupLayout(pnlCientifico);
        pnlCientifico.setLayout(pnlCientificoLayout);
        pnlCientificoLayout.setHorizontalGroup(
            pnlCientificoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCientificoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlInventarioCientifico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlCientificoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCientificoLayout.createSequentialGroup()
                        .addComponent(lblCientificoSelect, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                        .addGap(12, 12, 12))
                    .addGroup(pnlCientificoLayout.createSequentialGroup()
                        .addGroup(pnlCientificoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlCientificoLayout.createSequentialGroup()
                                .addComponent(btnMoverCientifico, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnComerCientifico, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnIrRefugioCientifico, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlCientificoLayout.createSequentialGroup()
                                .addComponent(btnIntercambiarCientifico, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCrearMedicamentoCientifico, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        pnlCientificoLayout.setVerticalGroup(
            pnlCientificoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCientificoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCientificoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlInventarioCientifico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlCientificoLayout.createSequentialGroup()
                        .addComponent(lblCientificoSelect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlCientificoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnMoverCientifico, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnComerCientifico, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnIrRefugioCientifico, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlCientificoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnIntercambiarCientifico, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCrearMedicamentoCientifico, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pnlContainer.add(pnlCientifico);
        pnlCientifico.setBounds(6, 460, 272, 136);

        pnlCurandero.setBackground(new java.awt.Color(130, 130, 130));
        pnlCurandero.setForeground(new java.awt.Color(153, 153, 153));
        pnlCurandero.setPreferredSize(new java.awt.Dimension(190, 136));

        pnlInventarioCurandero.setBackground(new java.awt.Color(153, 153, 153));
        pnlInventarioCurandero.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.gray, java.awt.Color.gray, null, null));

        btnInventarioCurandero_1.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCurandero_1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCurandero_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCurandero_1ActionPerformed(evt);
            }
        });

        btnInventarioCurandero_2.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCurandero_2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCurandero_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCurandero_2ActionPerformed(evt);
            }
        });

        btnInventarioCurandero_3.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCurandero_3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCurandero_3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCurandero_3ActionPerformed(evt);
            }
        });

        btnInventarioCurandero_4.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCurandero_4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCurandero_4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCurandero_4ActionPerformed(evt);
            }
        });

        btnInventarioCurandero_5.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCurandero_5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCurandero_5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCurandero_5ActionPerformed(evt);
            }
        });

        btnInventarioCurandero_6.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCurandero_6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCurandero_6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCurandero_6ActionPerformed(evt);
            }
        });

        btnInventarioCurandero_7.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCurandero_7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCurandero_7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCurandero_7ActionPerformed(evt);
            }
        });

        btnInventarioCurandero_8.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCurandero_8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCurandero_8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCurandero_8ActionPerformed(evt);
            }
        });

        btnInventarioCurandero_9.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioCurandero_9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioCurandero_9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioCurandero_9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlInventarioCuranderoLayout = new javax.swing.GroupLayout(pnlInventarioCurandero);
        pnlInventarioCurandero.setLayout(pnlInventarioCuranderoLayout);
        pnlInventarioCuranderoLayout.setHorizontalGroup(
            pnlInventarioCuranderoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventarioCuranderoLayout.createSequentialGroup()
                .addComponent(btnInventarioCurandero_1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnInventarioCurandero_2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnInventarioCurandero_3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlInventarioCuranderoLayout.createSequentialGroup()
                .addGroup(pnlInventarioCuranderoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnInventarioCurandero_4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioCurandero_7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInventarioCuranderoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnInventarioCurandero_5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioCurandero_8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInventarioCuranderoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnInventarioCurandero_6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioCurandero_9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        pnlInventarioCuranderoLayout.setVerticalGroup(
            pnlInventarioCuranderoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventarioCuranderoLayout.createSequentialGroup()
                .addGroup(pnlInventarioCuranderoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInventarioCurandero_1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioCurandero_2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioCurandero_3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInventarioCuranderoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInventarioCuranderoLayout.createSequentialGroup()
                        .addComponent(btnInventarioCurandero_4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInventarioCurandero_7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlInventarioCuranderoLayout.createSequentialGroup()
                        .addComponent(btnInventarioCurandero_5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInventarioCurandero_8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlInventarioCuranderoLayout.createSequentialGroup()
                        .addComponent(btnInventarioCurandero_6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInventarioCurandero_9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        lblCuranderoSelect.setText("Curandero");
        lblCuranderoSelect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCuranderoSelectMouseClicked(evt);
            }
        });

        btnMoverCurandero.setBackground(new java.awt.Color(102, 102, 102));
        btnMoverCurandero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/mover.png"))); // NOI18N
        btnMoverCurandero.setToolTipText("Moverse");
        btnMoverCurandero.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnMoverCurandero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoverCuranderoActionPerformed(evt);
            }
        });

        btnComerCurandero.setBackground(new java.awt.Color(102, 102, 102));
        btnComerCurandero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/comer.png"))); // NOI18N
        btnComerCurandero.setToolTipText("Comer");
        btnComerCurandero.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnComerCurandero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComerCuranderoActionPerformed(evt);
            }
        });

        btnIrRefugioCurandero.setBackground(new java.awt.Color(102, 102, 102));
        btnIrRefugioCurandero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/refugiarse.png"))); // NOI18N
        btnIrRefugioCurandero.setToolTipText("Refugiarse");
        btnIrRefugioCurandero.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnIrRefugioCurandero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIrRefugioCuranderoActionPerformed(evt);
            }
        });

        btnIntercambiarCurandero.setBackground(new java.awt.Color(102, 102, 102));
        btnIntercambiarCurandero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/intercambiar.png"))); // NOI18N
        btnIntercambiarCurandero.setToolTipText("Intercambiar Recurso");
        btnIntercambiarCurandero.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnIntercambiarCurandero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIntercambiarCuranderoActionPerformed(evt);
            }
        });

        btnCurarCurandero.setBackground(new java.awt.Color(102, 102, 102));
        btnCurarCurandero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/curar.png"))); // NOI18N
        btnCurarCurandero.setToolTipText("Curar");
        btnCurarCurandero.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnCurarCurandero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCurarCuranderoActionPerformed(evt);
            }
        });

        btnCrearRemedioCurandero.setBackground(new java.awt.Color(102, 102, 102));
        btnCrearRemedioCurandero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/hacer remedio.png"))); // NOI18N
        btnCrearRemedioCurandero.setToolTipText("Crear Remedio");
        btnCrearRemedioCurandero.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnCrearRemedioCurandero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearRemedioCuranderoActionPerformed(evt);
            }
        });

        btnRecolectarCurandero.setBackground(new java.awt.Color(102, 102, 102));
        btnRecolectarCurandero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/recolectar.png"))); // NOI18N
        btnRecolectarCurandero.setToolTipText("Recolectar");
        btnRecolectarCurandero.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnRecolectarCurandero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecolectarCuranderoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlCuranderoLayout = new javax.swing.GroupLayout(pnlCurandero);
        pnlCurandero.setLayout(pnlCuranderoLayout);
        pnlCuranderoLayout.setHorizontalGroup(
            pnlCuranderoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCuranderoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlInventarioCurandero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlCuranderoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCuranderoSelect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlCuranderoLayout.createSequentialGroup()
                        .addComponent(btnMoverCurandero, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnComerCurandero, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIrRefugioCurandero, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlCuranderoLayout.createSequentialGroup()
                        .addGroup(pnlCuranderoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlCuranderoLayout.createSequentialGroup()
                                .addComponent(btnIntercambiarCurandero, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCurarCurandero, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCrearRemedioCurandero, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnRecolectarCurandero, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(88, 88, 88)))
                .addContainerGap())
        );
        pnlCuranderoLayout.setVerticalGroup(
            pnlCuranderoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCuranderoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCuranderoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCuranderoLayout.createSequentialGroup()
                        .addComponent(lblCuranderoSelect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlCuranderoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnMoverCurandero, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnComerCurandero, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnIrRefugioCurandero, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlCuranderoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlCuranderoLayout.createSequentialGroup()
                                .addComponent(btnIntercambiarCurandero, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRecolectarCurandero, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnCurarCurandero, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCrearRemedioCurandero, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(pnlInventarioCurandero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlContainer.add(pnlCurandero);
        pnlCurandero.setBounds(284, 460, 260, 150);

        pnlExplorador.setBackground(new java.awt.Color(130, 130, 130));
        pnlExplorador.setForeground(new java.awt.Color(153, 153, 153));
        pnlExplorador.setPreferredSize(new java.awt.Dimension(190, 136));

        pnlInventarioExplorador.setBackground(new java.awt.Color(153, 153, 153));
        pnlInventarioExplorador.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.gray, java.awt.Color.gray, null, null));

        btnInventarioExplorador_1.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioExplorador_1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioExplorador_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioExplorador_1ActionPerformed(evt);
            }
        });

        btnInventarioExplorador_2.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioExplorador_2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioExplorador_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioExplorador_2ActionPerformed(evt);
            }
        });

        btnInventarioExplorador_3.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioExplorador_3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioExplorador_3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioExplorador_3ActionPerformed(evt);
            }
        });

        btnInventarioExplorador_4.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioExplorador_4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioExplorador_4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioExplorador_4ActionPerformed(evt);
            }
        });

        btnInventarioExplorador_5.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioExplorador_5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioExplorador_5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioExplorador_5ActionPerformed(evt);
            }
        });

        btnInventarioExplorador_6.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioExplorador_6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioExplorador_6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioExplorador_6ActionPerformed(evt);
            }
        });

        btnInventarioExplorador_7.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioExplorador_7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioExplorador_7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioExplorador_7ActionPerformed(evt);
            }
        });

        btnInventarioExplorador_8.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioExplorador_8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioExplorador_8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioExplorador_8ActionPerformed(evt);
            }
        });

        btnInventarioExplorador_9.setBackground(new java.awt.Color(102, 102, 102));
        btnInventarioExplorador_9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnInventarioExplorador_9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioExplorador_9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlInventarioExploradorLayout = new javax.swing.GroupLayout(pnlInventarioExplorador);
        pnlInventarioExplorador.setLayout(pnlInventarioExploradorLayout);
        pnlInventarioExploradorLayout.setHorizontalGroup(
            pnlInventarioExploradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventarioExploradorLayout.createSequentialGroup()
                .addComponent(btnInventarioExplorador_1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnInventarioExplorador_2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnInventarioExplorador_3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlInventarioExploradorLayout.createSequentialGroup()
                .addGroup(pnlInventarioExploradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnInventarioExplorador_4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioExplorador_7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInventarioExploradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnInventarioExplorador_5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioExplorador_8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInventarioExploradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnInventarioExplorador_6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioExplorador_9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        pnlInventarioExploradorLayout.setVerticalGroup(
            pnlInventarioExploradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventarioExploradorLayout.createSequentialGroup()
                .addGroup(pnlInventarioExploradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInventarioExplorador_1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioExplorador_2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInventarioExplorador_3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInventarioExploradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInventarioExploradorLayout.createSequentialGroup()
                        .addComponent(btnInventarioExplorador_4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInventarioExplorador_7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlInventarioExploradorLayout.createSequentialGroup()
                        .addComponent(btnInventarioExplorador_5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInventarioExplorador_8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlInventarioExploradorLayout.createSequentialGroup()
                        .addComponent(btnInventarioExplorador_6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInventarioExplorador_9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        lblExploradorSelect.setText("Explorador");
        lblExploradorSelect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblExploradorSelectMouseClicked(evt);
            }
        });

        btnMoverExplorador.setBackground(new java.awt.Color(102, 102, 102));
        btnMoverExplorador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/mover.png"))); // NOI18N
        btnMoverExplorador.setToolTipText("Moverse");
        btnMoverExplorador.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnMoverExplorador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoverExploradorActionPerformed(evt);
            }
        });

        btnComerExplorador.setBackground(new java.awt.Color(102, 102, 102));
        btnComerExplorador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/comer.png"))); // NOI18N
        btnComerExplorador.setToolTipText("Comer");
        btnComerExplorador.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnComerExplorador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComerExploradorActionPerformed(evt);
            }
        });

        btnIrRefugioExplorador.setBackground(new java.awt.Color(102, 102, 102));
        btnIrRefugioExplorador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/refugiarse.png"))); // NOI18N
        btnIrRefugioExplorador.setToolTipText("Refugiarse");
        btnIrRefugioExplorador.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnIrRefugioExplorador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIrRefugioExploradorActionPerformed(evt);
            }
        });

        btnIntercambiarExplorador.setBackground(new java.awt.Color(102, 102, 102));
        btnIntercambiarExplorador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/intercambiar.png"))); // NOI18N
        btnIntercambiarExplorador.setToolTipText("Intercambiar Recurso");
        btnIntercambiarExplorador.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnIntercambiarExplorador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIntercambiarExploradorActionPerformed(evt);
            }
        });

        btnExplorarExplorador.setBackground(new java.awt.Color(102, 102, 102));
        btnExplorarExplorador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/explorar.png"))); // NOI18N
        btnExplorarExplorador.setToolTipText("Explorar Mapa");
        btnExplorarExplorador.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnExplorarExplorador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExplorarExploradorActionPerformed(evt);
            }
        });

        btnRecolectarExplorador.setBackground(new java.awt.Color(102, 102, 102));
        btnRecolectarExplorador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Menu and UI/recolectar.png"))); // NOI18N
        btnRecolectarExplorador.setToolTipText("Recolectar");
        btnRecolectarExplorador.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.darkGray, java.awt.Color.gray));
        btnRecolectarExplorador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecolectarExploradorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlExploradorLayout = new javax.swing.GroupLayout(pnlExplorador);
        pnlExplorador.setLayout(pnlExploradorLayout);
        pnlExploradorLayout.setHorizontalGroup(
            pnlExploradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlExploradorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlInventarioExplorador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlExploradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblExploradorSelect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlExploradorLayout.createSequentialGroup()
                        .addComponent(btnMoverExplorador, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnComerExplorador, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIrRefugioExplorador, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlExploradorLayout.createSequentialGroup()
                        .addComponent(btnIntercambiarExplorador, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExplorarExplorador, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRecolectarExplorador, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(88, 88, 88)))
                .addContainerGap())
        );
        pnlExploradorLayout.setVerticalGroup(
            pnlExploradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlExploradorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlExploradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlExploradorLayout.createSequentialGroup()
                        .addComponent(lblExploradorSelect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlExploradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnMoverExplorador, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnComerExplorador, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnIrRefugioExplorador, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlExploradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnIntercambiarExplorador, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnExplorarExplorador, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRecolectarExplorador, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(pnlInventarioExplorador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pnlContainer.add(pnlExplorador);
        pnlExplorador.setBounds(550, 460, 260, 136);

        pnlDatosRefugio.setBackground(new java.awt.Color(130, 130, 130));

        lbl_vidaText1.setText("Estabilidad");

        barraDeEstabilidad.setForeground(new java.awt.Color(0, 51, 255));
        barraDeEstabilidad.setStringPainted(true);

        lblStatusPersonajeEnRefugio_1.setBackground(new java.awt.Color(0, 0, 0));
        lblStatusPersonajeEnRefugio_1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        lblStatusPersonajeEnRefugio_2.setBackground(new java.awt.Color(0, 0, 0));
        lblStatusPersonajeEnRefugio_2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        lblStatusPersonajeEnRefugio_3.setBackground(new java.awt.Color(0, 0, 0));
        lblStatusPersonajeEnRefugio_3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        lbl_statusText1.setText("Inquilinos");

        javax.swing.GroupLayout pnlDatosRefugioLayout = new javax.swing.GroupLayout(pnlDatosRefugio);
        pnlDatosRefugio.setLayout(pnlDatosRefugioLayout);
        pnlDatosRefugioLayout.setHorizontalGroup(
            pnlDatosRefugioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosRefugioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_vidaText1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barraDeEstabilidad, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDatosRefugioLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_statusText1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlDatosRefugioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStatusPersonajeEnRefugio_1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStatusPersonajeEnRefugio_2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStatusPersonajeEnRefugio_3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33))
        );
        pnlDatosRefugioLayout.setVerticalGroup(
            pnlDatosRefugioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosRefugioLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(pnlDatosRefugioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_vidaText1)
                    .addComponent(barraDeEstabilidad, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(lblStatusPersonajeEnRefugio_1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(pnlDatosRefugioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatusPersonajeEnRefugio_2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_statusText1))
                .addGap(0, 0, 0)
                .addComponent(lblStatusPersonajeEnRefugio_3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        pnlContainer.add(pnlDatosRefugio);
        pnlDatosRefugio.setBounds(550, 320, 260, 140);

        pnlDatosRefugio1.setBackground(new java.awt.Color(130, 130, 130));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txfInventarioDisplay.setEditable(false);
        txfInventarioDisplay.setBackground(new java.awt.Color(102, 102, 102));
        txfInventarioDisplay.setColumns(20);
        txfInventarioDisplay.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        txfInventarioDisplay.setRows(5);
        txfInventarioDisplay.setText("1 )-\n2 )- \n3 )-\n4 )-\n5 )-\n6 )-\n7 )-\n8 )-\n9 )-");
        jScrollPane1.setViewportView(txfInventarioDisplay);

        javax.swing.GroupLayout pnlDatosRefugio1Layout = new javax.swing.GroupLayout(pnlDatosRefugio1);
        pnlDatosRefugio1.setLayout(pnlDatosRefugio1Layout);
        pnlDatosRefugio1Layout.setHorizontalGroup(
            pnlDatosRefugio1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
        );
        pnlDatosRefugio1Layout.setVerticalGroup(
            pnlDatosRefugio1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
        );

        pnlContainer.add(pnlDatosRefugio1);
        pnlDatosRefugio1.setBounds(550, 170, 260, 140);

        getContentPane().add(pnlContainer);
        pnlContainer.setBounds(690, 10, 820, 620);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInventarioCazador_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCazador_1ActionPerformed
        seleccionarSlotInventario(0, 0);
    }//GEN-LAST:event_btnInventarioCazador_1ActionPerformed

    private void btnInventarioCazador_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCazador_2ActionPerformed
        seleccionarSlotInventario(0, 1);
    }//GEN-LAST:event_btnInventarioCazador_2ActionPerformed

    private void btnInventarioCazador_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCazador_3ActionPerformed
        seleccionarSlotInventario(0, 2);
    }//GEN-LAST:event_btnInventarioCazador_3ActionPerformed

    private void btnInventarioCazador_4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCazador_4ActionPerformed
        seleccionarSlotInventario(0, 3);
    }//GEN-LAST:event_btnInventarioCazador_4ActionPerformed

    private void btnInventarioCazador_5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCazador_5ActionPerformed
        seleccionarSlotInventario(0, 4);
    }//GEN-LAST:event_btnInventarioCazador_5ActionPerformed
    
    private void btnInventarioCazador_6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCazador_6ActionPerformed
        seleccionarSlotInventario(0, 5);
    }//GEN-LAST:event_btnInventarioCazador_6ActionPerformed

    private void btnInventarioCazador_7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCazador_7ActionPerformed
        seleccionarSlotInventario(0, 6);
    }//GEN-LAST:event_btnInventarioCazador_7ActionPerformed

    private void btnInventarioCazador_8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCazador_8ActionPerformed
        seleccionarSlotInventario(0, 7);
    }//GEN-LAST:event_btnInventarioCazador_8ActionPerformed

    private void btnInventarioCazador_9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCazador_9ActionPerformed
        seleccionarSlotInventario(0, 8);
    }//GEN-LAST:event_btnInventarioCazador_9ActionPerformed


    private void btnInventarioRecolector_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioRecolector_1ActionPerformed
        seleccionarSlotInventario(5, 0);
    }//GEN-LAST:event_btnInventarioRecolector_1ActionPerformed

    private void btnInventarioRecolector_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioRecolector_2ActionPerformed
        seleccionarSlotInventario(5, 1);
    }//GEN-LAST:event_btnInventarioRecolector_2ActionPerformed

    private void btnInventarioRecolector_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioRecolector_3ActionPerformed
        seleccionarSlotInventario(5, 2);
    }//GEN-LAST:event_btnInventarioRecolector_3ActionPerformed

    private void btnInventarioRecolector_4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioRecolector_4ActionPerformed
        seleccionarSlotInventario(5, 3);
    }//GEN-LAST:event_btnInventarioRecolector_4ActionPerformed

    private void btnInventarioRecolector_5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioRecolector_5ActionPerformed
        seleccionarSlotInventario(5, 4);
    }//GEN-LAST:event_btnInventarioRecolector_5ActionPerformed

    private void btnInventarioRecolector_6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioRecolector_6ActionPerformed
        seleccionarSlotInventario(5, 5);
    }//GEN-LAST:event_btnInventarioRecolector_6ActionPerformed

    private void btnInventarioRecolector_7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioRecolector_7ActionPerformed
        seleccionarSlotInventario(5, 6);
    }//GEN-LAST:event_btnInventarioRecolector_7ActionPerformed

    private void btnInventarioRecolector_8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioRecolector_8ActionPerformed
        seleccionarSlotInventario(5, 7);
    }//GEN-LAST:event_btnInventarioRecolector_8ActionPerformed

    private void btnInventarioRecolector_9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioRecolector_9ActionPerformed
        seleccionarSlotInventario(5, 8);
    }//GEN-LAST:event_btnInventarioRecolector_9ActionPerformed

    
    private void btnInventarioCientifico_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCientifico_1ActionPerformed
        seleccionarSlotInventario(1, 0);
    }//GEN-LAST:event_btnInventarioCientifico_1ActionPerformed

    private void btnInventarioCientifico_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCientifico_2ActionPerformed
        seleccionarSlotInventario(1, 1);
    }//GEN-LAST:event_btnInventarioCientifico_2ActionPerformed

    private void btnInventarioCientifico_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCientifico_3ActionPerformed
        seleccionarSlotInventario(1, 2);
    }//GEN-LAST:event_btnInventarioCientifico_3ActionPerformed

    private void btnInventarioCientifico_4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCientifico_4ActionPerformed
        seleccionarSlotInventario(1, 3);
    }//GEN-LAST:event_btnInventarioCientifico_4ActionPerformed

    private void btnInventarioCientifico_5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCientifico_5ActionPerformed
        seleccionarSlotInventario(1, 4);
    }//GEN-LAST:event_btnInventarioCientifico_5ActionPerformed

    private void btnInventarioCientifico_6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCientifico_6ActionPerformed
        seleccionarSlotInventario(1, 5);
    }//GEN-LAST:event_btnInventarioCientifico_6ActionPerformed

    private void btnInventarioCientifico_7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCientifico_7ActionPerformed
        seleccionarSlotInventario(1, 6);
    }//GEN-LAST:event_btnInventarioCientifico_7ActionPerformed

    private void btnInventarioCientifico_8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCientifico_8ActionPerformed
        seleccionarSlotInventario(1, 7);
    }//GEN-LAST:event_btnInventarioCientifico_8ActionPerformed

    private void btnInventarioCientifico_9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCientifico_9ActionPerformed
        seleccionarSlotInventario(1, 8);
    }//GEN-LAST:event_btnInventarioCientifico_9ActionPerformed

    
    private void btnInventarioConstructor_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioConstructor_1ActionPerformed
        seleccionarSlotInventario(2, 0);
    }//GEN-LAST:event_btnInventarioConstructor_1ActionPerformed

    private void btnInventarioConstructor_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioConstructor_2ActionPerformed
        seleccionarSlotInventario(2, 1);
    }//GEN-LAST:event_btnInventarioConstructor_2ActionPerformed

    private void btnInventarioConstructor_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioConstructor_3ActionPerformed
        seleccionarSlotInventario(2, 2);
    }//GEN-LAST:event_btnInventarioConstructor_3ActionPerformed

    private void btnInventarioConstructor_4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioConstructor_4ActionPerformed
        seleccionarSlotInventario(2, 3);
    }//GEN-LAST:event_btnInventarioConstructor_4ActionPerformed

    private void btnInventarioConstructor_5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioConstructor_5ActionPerformed
        seleccionarSlotInventario(2, 4);
    }//GEN-LAST:event_btnInventarioConstructor_5ActionPerformed

    private void btnInventarioConstructor_6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioConstructor_6ActionPerformed
        seleccionarSlotInventario(2, 5);
    }//GEN-LAST:event_btnInventarioConstructor_6ActionPerformed

    private void btnInventarioConstructor_7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioConstructor_7ActionPerformed
        seleccionarSlotInventario(2, 6);
    }//GEN-LAST:event_btnInventarioConstructor_7ActionPerformed

    private void btnInventarioConstructor_8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioConstructor_8ActionPerformed
        seleccionarSlotInventario(2, 7);
    }//GEN-LAST:event_btnInventarioConstructor_8ActionPerformed
    private void btnInventarioConstructor_9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioConstructor_9ActionPerformed
        seleccionarSlotInventario(2, 8);
    }//GEN-LAST:event_btnInventarioConstructor_9ActionPerformed

    
    private void btnInventarioCurandero_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCurandero_1ActionPerformed
        seleccionarSlotInventario(4, 0);
    }//GEN-LAST:event_btnInventarioCurandero_1ActionPerformed

    private void btnInventarioCurandero_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCurandero_2ActionPerformed
        seleccionarSlotInventario(4, 1);
    }//GEN-LAST:event_btnInventarioCurandero_2ActionPerformed

    private void btnInventarioCurandero_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCurandero_3ActionPerformed
        seleccionarSlotInventario(4, 2);
    }//GEN-LAST:event_btnInventarioCurandero_3ActionPerformed

    private void btnInventarioCurandero_4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCurandero_4ActionPerformed
        seleccionarSlotInventario(4, 3);
    }//GEN-LAST:event_btnInventarioCurandero_4ActionPerformed

    private void btnInventarioCurandero_5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCurandero_5ActionPerformed
        seleccionarSlotInventario(4, 4);
    }//GEN-LAST:event_btnInventarioCurandero_5ActionPerformed
    
    private void btnInventarioCurandero_6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCurandero_6ActionPerformed
        seleccionarSlotInventario(4, 5);
    }//GEN-LAST:event_btnInventarioCurandero_6ActionPerformed

    private void btnInventarioCurandero_7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCurandero_7ActionPerformed
        seleccionarSlotInventario(4, 6);
    }//GEN-LAST:event_btnInventarioCurandero_7ActionPerformed

    private void btnInventarioCurandero_8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCurandero_8ActionPerformed
        seleccionarSlotInventario(4, 7);
    }//GEN-LAST:event_btnInventarioCurandero_8ActionPerformed

    private void btnInventarioCurandero_9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioCurandero_9ActionPerformed
        seleccionarSlotInventario(4, 8);
    }//GEN-LAST:event_btnInventarioCurandero_9ActionPerformed

    
    private void btnInventarioExplorador_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioExplorador_1ActionPerformed
        seleccionarSlotInventario(3, 0);
    }//GEN-LAST:event_btnInventarioExplorador_1ActionPerformed

    private void btnInventarioExplorador_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioExplorador_2ActionPerformed
        seleccionarSlotInventario(3, 1);
    }//GEN-LAST:event_btnInventarioExplorador_2ActionPerformed

    private void btnInventarioExplorador_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioExplorador_3ActionPerformed
        seleccionarSlotInventario(3, 2);
    }//GEN-LAST:event_btnInventarioExplorador_3ActionPerformed

    private void btnInventarioExplorador_4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioExplorador_4ActionPerformed
        seleccionarSlotInventario(3, 3);
    }//GEN-LAST:event_btnInventarioExplorador_4ActionPerformed

    private void btnInventarioExplorador_5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioExplorador_5ActionPerformed
        seleccionarSlotInventario(3, 4);
    }//GEN-LAST:event_btnInventarioExplorador_5ActionPerformed

    private void btnInventarioExplorador_6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioExplorador_6ActionPerformed
        seleccionarSlotInventario(3, 5);
    }//GEN-LAST:event_btnInventarioExplorador_6ActionPerformed

    private void btnInventarioExplorador_7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioExplorador_7ActionPerformed
        seleccionarSlotInventario(3, 6);
    }//GEN-LAST:event_btnInventarioExplorador_7ActionPerformed

    private void btnInventarioExplorador_8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioExplorador_8ActionPerformed
        seleccionarSlotInventario(3, 7);
    }//GEN-LAST:event_btnInventarioExplorador_8ActionPerformed

    private void btnInventarioExplorador_9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioExplorador_9ActionPerformed
        seleccionarSlotInventario(3, 8);
    }//GEN-LAST:event_btnInventarioExplorador_9ActionPerformed

    
    private void btnNextDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextDayActionPerformed
        lblDiaActual.setText(Integer.toString((dia++)+1));
        
        for (int i = 0; i < 6; i++) { //Bitacoras
            guardarMensajeEnLog(i, "Día " + lblDiaActual.getText());
        }
        
        for (Personaje personaje : personajes) {
            personaje.updateHambre();
            if (personaje.estaEnfermo() > 0) guardarMensajeEnLog(personajes.indexOf(personaje), "El personaje sigue afligido con "  + (personaje.estaEnfermo() > 1? "Enfermedad":"Enfermedad grave"));
            if (personaje.estaLesionado()> 0) guardarMensajeEnLog(personajes.indexOf(personaje), "El personaje sigue afligido con "  + (personaje.estaLesionado()> 1? "Lesión":"Lesión grave"));
            personaje.reducirSalud(10*personaje.statusEnfermedad());
            personaje.reducirSalud(10*personaje.statusLesion());
        }
        if (tormenta > 0) {//Si hay tormenta
            for (Refugio refugio : refugios) {
                refugio.reducirEnergiaTormenta();
                refugio.reducirEstabilidad(tormenta*20);
            }
            for (Personaje personaje : personajes) {
                if (personaje.getRefugioActual() == null) {
                    personaje.reducirEnergia(15);
                    personaje.reducirSalud(10);
                }
            }
        } else {
            for (Refugio refugio : refugios) {
                refugio.reducirEstabilidad(5);
                refugio.descansarPersonajes();
            }
        }
        
        for (int i = 0; i < 6; i++) { //Bitacoras
            if (tormenta > 0 && personajes.get(i).getRefugioActual() == null) guardarMensajeEnLog(i, "Pasó la noche afuera de un refugio en tormenta");
            else if (tormenta > 0 && personajes.get(i).getRefugioActual() != null) guardarMensajeEnLog(i, "Pasó la noche en un refugio en tormenta");
            guardarMensajeEnLog(i, "Nivel de hambre: " + personajes.get(i).getNivelHambre());
        }
        
        actualizarTormenta();
        if(Integer.parseInt(lblDuracionTormenta.getText()) <= 0) lblDuracionTormenta.setText("0");
        
        for (int i = 0; i < 6; i++) { //Bitacoras
            guardarMensajeEnLog(i, lblEstadoClima.getText());
        }
        
    }//GEN-LAST:event_btnNextDayActionPerformed

    private void btnMoverCazadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoverCazadorActionPerformed
        mover(0);
    }//GEN-LAST:event_btnMoverCazadorActionPerformed
    
    private void btnIrRefugioCazadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIrRefugioCazadorActionPerformed
        refugiarse(0);
    }//GEN-LAST:event_btnIrRefugioCazadorActionPerformed

    private void btnMoverCientificoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoverCientificoActionPerformed
        mover(1);
    }//GEN-LAST:event_btnMoverCientificoActionPerformed
    
    private void btnMoverConstructorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoverConstructorActionPerformed
        mover(2);
    }//GEN-LAST:event_btnMoverConstructorActionPerformed
    
    private void btnMoverExploradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoverExploradorActionPerformed
        mover(3);
    }//GEN-LAST:event_btnMoverExploradorActionPerformed
    
    private void btnMoverCuranderoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoverCuranderoActionPerformed
        mover(4);
    }//GEN-LAST:event_btnMoverCuranderoActionPerformed
    
    private void btnMoverRecolectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoverRecolectorActionPerformed
        mover(5);
    }//GEN-LAST:event_btnMoverRecolectorActionPerformed

    private void btnComerCazadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComerCazadorActionPerformed
        comer(0);
    }//GEN-LAST:event_btnComerCazadorActionPerformed

    private void btnComerCientificoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComerCientificoActionPerformed
        comer(1);
    }//GEN-LAST:event_btnComerCientificoActionPerformed

    private void btnComerConstructorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComerConstructorActionPerformed
        comer(2);
    }//GEN-LAST:event_btnComerConstructorActionPerformed
    
    private void btnComerExploradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComerExploradorActionPerformed
        comer(3);
    }//GEN-LAST:event_btnComerExploradorActionPerformed
    
    private void btnComerCuranderoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComerCuranderoActionPerformed
        comer(4);
    }//GEN-LAST:event_btnComerCuranderoActionPerformed
    
    private void btnComerRecolectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComerRecolectorActionPerformed
        comer(5);
    }//GEN-LAST:event_btnComerRecolectorActionPerformed

    private void btnIntercambiarCazadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIntercambiarCazadorActionPerformed
        intercambiarRecurso(0);
    }//GEN-LAST:event_btnIntercambiarCazadorActionPerformed

    private void btnIntercambiarCientificoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIntercambiarCientificoActionPerformed
        intercambiarRecurso(1);
    }//GEN-LAST:event_btnIntercambiarCientificoActionPerformed

    private void btnIntercambiarConstructorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIntercambiarConstructorActionPerformed
        intercambiarRecurso(2);
    }//GEN-LAST:event_btnIntercambiarConstructorActionPerformed
    
    private void btnIntercambiarExploradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIntercambiarExploradorActionPerformed
        intercambiarRecurso(3);
    }//GEN-LAST:event_btnIntercambiarExploradorActionPerformed
    
    private void btnIntercambiarCuranderoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIntercambiarCuranderoActionPerformed
        intercambiarRecurso(4);
    }//GEN-LAST:event_btnIntercambiarCuranderoActionPerformed

    private void btnEntregarRecolectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntregarRecolectorActionPerformed
        intercambiarRecurso(5);
    }//GEN-LAST:event_btnEntregarRecolectorActionPerformed

    private void btnIrRefugioRecolectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIrRefugioRecolectorActionPerformed
        refugiarse(5);
    }//GEN-LAST:event_btnIrRefugioRecolectorActionPerformed

    private void btnIrRefugioCientificoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIrRefugioCientificoActionPerformed
        refugiarse(1);
    }//GEN-LAST:event_btnIrRefugioCientificoActionPerformed

    private void btnIrRefugioConstructorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIrRefugioConstructorActionPerformed
        refugiarse(2);
    }//GEN-LAST:event_btnIrRefugioConstructorActionPerformed

    private void btnIrRefugioCuranderoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIrRefugioCuranderoActionPerformed
        refugiarse(4);
    }//GEN-LAST:event_btnIrRefugioCuranderoActionPerformed

    private void btnAtaqueAnimalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtaqueAnimalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAtaqueAnimalActionPerformed

    private void btnHerirPersonajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHerirPersonajeActionPerformed
        Random random = new Random();
        if (revisandoStats != null) revisandoStats.nuevaLesion(new Lesion(random.nextInt(1, 3)));
    }//GEN-LAST:event_btnHerirPersonajeActionPerformed

    private void btnEnfermarPersonajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnfermarPersonajeActionPerformed
        Random random = new Random();
        if (revisandoStats != null) revisandoStats.nuevaEnfermedad(new Enfermedad(random.nextInt(1, 3)));
    }//GEN-LAST:event_btnEnfermarPersonajeActionPerformed

    private void btnSpawnearTormentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSpawnearTormentaActionPerformed
        crearTormenta();
    }//GEN-LAST:event_btnSpawnearTormentaActionPerformed

    private void lblMapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMapMouseClicked
        
    }//GEN-LAST:event_lblMapMouseClicked
    
    private void lblCientificoSelectKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lblCientificoSelectKeyPressed
        
    }//GEN-LAST:event_lblCientificoSelectKeyPressed

    private void lblCazadorSelectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCazadorSelectMouseClicked
        isCheckingStats = true;
        revisandoStats = personajes.get(0);
        lblAvatar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Characters/Originales/Cazador.png")));
    }//GEN-LAST:event_lblCazadorSelectMouseClicked

    private void lblCientificoSelectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCientificoSelectMouseClicked
        isCheckingStats = true;
        revisandoStats = personajes.get(1);
        lblAvatar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Characters/Originales/Cientifico.png")));
    }//GEN-LAST:event_lblCientificoSelectMouseClicked

    private void lblConstructorSelectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblConstructorSelectMouseClicked
        isCheckingStats = true;
        revisandoStats = personajes.get(2);
        lblAvatar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Characters/Originales/Constructor.png")));    
    }//GEN-LAST:event_lblConstructorSelectMouseClicked
    
    private void lblExploradorSelectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblExploradorSelectMouseClicked
        isCheckingStats = true;
        revisandoStats = personajes.get(3);
        lblAvatar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Characters/Originales/Explorador.png")));
    }//GEN-LAST:event_lblExploradorSelectMouseClicked
    
    private void lblCuranderoSelectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCuranderoSelectMouseClicked
        isCheckingStats = true;
        revisandoStats = personajes.get(4);
        lblAvatar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Characters/Originales/Curandero.png")));
    }//GEN-LAST:event_lblCuranderoSelectMouseClicked

    private void lblRecolectorSelectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRecolectorSelectMouseClicked
        isCheckingStats = true;
        revisandoStats = personajes.get(5);
        lblAvatar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Characters/Originales/Recolector.png")));
    }//GEN-LAST:event_lblRecolectorSelectMouseClicked

    private void btnIrRefugioExploradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIrRefugioExploradorActionPerformed
        refugiarse(3);
    }//GEN-LAST:event_btnIrRefugioExploradorActionPerformed

    private void btnCazarCazadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCazarCazadorActionPerformed
        if (personajes.get(0).getVida() > 0) {
            
        }
    }//GEN-LAST:event_btnCazarCazadorActionPerformed

    private void btnCrearRefugioConstructorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearRefugioConstructorActionPerformed
        if (personajes.get(2).getVida() > 0) {
            Constructor constructor = (Constructor) personajes.get(2);
            if (constructor.construir()) {
                JLabel newRefugio = new javax.swing.JLabel();
            
                newRefugio.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        lblRefugioMousePressed(evt);
                    }
                });
            
                pnlMap.add(newRefugio);
                pnlMap.setComponentZOrder(newRefugio, 0);
                newRefugio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/ForTheMap/refugio.png")));
                newRefugio.setBounds(Integer.parseInt(lblCoordsX.getText()), Integer.parseInt(lblCoordsY.getText()), 35, 35);
            
                Refugio nuevo = new Refugio(newRefugio);
                System.out.println("Se hizo el refugio");
                refugios.add(nuevo);
                actualizarIconosDeInventarioPersonaje(2);
                
                guardarMensajeEnLog(2, "Construyó un refugio");
            } else System.out.println("No hay suficientes materiales para construir el refugio");
        }
        if (personajes.get(2).getVida() == 0) {
            System.out.println("No tiene vida para construir");
            JOptionPane.showMessageDialog(null, "El personaje no tiene vida para realizar acciones", "Falta de vida", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }//GEN-LAST:event_btnCrearRefugioConstructorActionPerformed

    private void btnRepararRefugioConstructor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRepararRefugioConstructor1ActionPerformed
        Constructor constructor = (Constructor) personajes.get(2);
        
        if (personajes.get(2).getVida() == 0) {
            System.out.println("No tiene vida para hacer una reparacion");
            JOptionPane.showMessageDialog(null, "El personaje no tiene vida para realizar acciones", "Falta de vida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (personajes.get(2).getVida() > 0) {
            constructor.reparar(revisandoRefugio);
            System.out.println("Refugio reparado + 5");
            guardarMensajeEnLog(2, "Le reparó 5 de estabilidad a un refugio");
        }
    }//GEN-LAST:event_btnRepararRefugioConstructor1ActionPerformed

    private void btnCurarCuranderoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCurarCuranderoActionPerformed
        Curandero cur = (Curandero) personajes.get(4);
        
        if (personajes.get(4).getVida() == 0) {
            System.out.println("No tiene vida para hacer un remedio");
            JOptionPane.showMessageDialog(null, "El personaje no tiene vida para realizar acciones", "Falta de vida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (revisandoStats != null) {
            if (cur.accionar(revisandoStats)){
                actualizarIconosDeInventarioPersonaje(4);
                guardarMensajeEnLog(4, "El curandero curó a " + revisandoStats.getNombre());
            }
        }
        
        
    }//GEN-LAST:event_btnCurarCuranderoActionPerformed

    private void btnCrearRemedioCuranderoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearRemedioCuranderoActionPerformed
        Curandero cur = (Curandero) personajes.get(4);
        
        if (personajes.get(4).getVida() == 0) {
            System.out.println("No tiene vida para hacer un remedio");
            JOptionPane.showMessageDialog(null, "El personaje no tiene vida para realizar acciones", "Falta de vida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        
        if (cur.crearMedicamento()) {
            System.out.println("Se ha creado un medicamento");
            guardarMensajeEnLog(4,"Creó un remedio");
            actualizarIconosDeInventarioPersonaje(4);
        }
        
    }//GEN-LAST:event_btnCrearRemedioCuranderoActionPerformed

    private void btnRecolectarCuranderoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecolectarCuranderoActionPerformed
        recolectarRecurso(4);
    }//GEN-LAST:event_btnRecolectarCuranderoActionPerformed

    private void btnRecolectarRecolectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecolectarRecolectorActionPerformed
        recolectarRecurso(5);
    }//GEN-LAST:event_btnRecolectarRecolectorActionPerformed

    private void btnExplorarExploradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExplorarExploradorActionPerformed
        if (personajes.get(3).getVida() == 0) {
            System.out.println("No tiene vida para hacer una exploracion");
            JOptionPane.showMessageDialog(null, "El personaje no tiene vida para realizar acciones", "Falta de vida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int energiaAConsumir = new Random().nextInt(15, 31);
        if (zonaSeleccionada != null && zonasBloqueadas.contains(zonaSeleccionada) 
            && estanCerca(lblExploradorMapa, zonaSeleccionada, 120)
            && personajes.get(3).getEnergia() >= energiaAConsumir) {
            zonasBloqueadas.remove(zonaSeleccionada);
            zonaSeleccionada.setVisible(false);
            
            crearRecursosAlDesbloquearArea(zonaSeleccionada);
            guardarMensajeEnLog(3, "El personaje desbloqueó la zona " + zonaSeleccionada.getText());
            
            zonaSeleccionada = null;
            
            personajes.get(3).reducirEnergia(energiaAConsumir);
            
            System.out.println("Zona desbloqueada");
        } else System.out.println("No hay zona por explorar/esta muy lejos");
    }//GEN-LAST:event_btnExplorarExploradorActionPerformed

    private void btnRecolectarExploradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecolectarExploradorActionPerformed
        recolectarRecurso(3);
    }//GEN-LAST:event_btnRecolectarExploradorActionPerformed

    private void btnCrearMedicamentoCientificoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearMedicamentoCientificoActionPerformed
        if (personajes.get(1).getVida() == 0) {
            System.out.println("No tiene vida para hacer medicina");
            JOptionPane.showMessageDialog(null, "El personaje no tiene vida para realizar acciones", "Falta de vida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        personajes.get(1).accionar(0);
        guardarMensajeEnLog(1,"Intento de crear Medicina");
        actualizarIconosDeInventarioPersonaje(1);
    }//GEN-LAST:event_btnCrearMedicamentoCientificoActionPerformed

    private void lblMapMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMapMousePressed
        Point pto = evt.getPoint();
        SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            lblCoordsX.setText(Integer.toString(pto.x));
            lblCoordsY.setText(Integer.toString(pto.y));
        }
        });
    }//GEN-LAST:event_lblMapMousePressed

    private void btnCurarDebuffsPersonajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCurarDebuffsPersonajeActionPerformed
        if (revisandoStats != null) {
            revisandoStats.eliminarEnfermedad();
            revisandoStats.borrarLesion();
        }
    }//GEN-LAST:event_btnCurarDebuffsPersonajeActionPerformed

    public Personaje conseguirChampRandom() {
        Random rand = new Random();
        int randomIndex = rand.nextInt(6); 
        return personajes.get(randomIndex); 
    }
    
    private void btnSpawnearMedicinaInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSpawnearMedicinaInventarioActionPerformed
        String seleccion = (String) cmbCantidadASpawnear.getSelectedItem();
        int cantidadASpawnear = Integer.parseInt(seleccion);
        Recurso medicina = new Recurso("Medicina", cantidadASpawnear);
        
        if (revisandoStats != null && revisandoStats.agregarRecurso(medicina)) {
            System.out.println("Se genero " + medicina.toString() + " para " + revisandoStats.getNombre());
            actualizarIconosDeInventarioPersonaje(personajes.indexOf(revisandoStats));
        } else System.out.println("El inventario esta lleno");
    }//GEN-LAST:event_btnSpawnearMedicinaInventarioActionPerformed

    private void lblCubre00MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre00MousePressed
        zonaSeleccionada = lblCubre00;
    }//GEN-LAST:event_lblCubre00MousePressed

    private void lblCubre10MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre10MousePressed
        zonaSeleccionada = lblCubre10;
    }//GEN-LAST:event_lblCubre10MousePressed

    private void lblCubre20MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre20MousePressed
        zonaSeleccionada = lblCubre20;
    }//GEN-LAST:event_lblCubre20MousePressed

    private void lblCubre30MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre30MousePressed
        zonaSeleccionada = lblCubre30;
    }//GEN-LAST:event_lblCubre30MousePressed

    private void lblCubre40MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre40MousePressed
        zonaSeleccionada = lblCubre40;
    }//GEN-LAST:event_lblCubre40MousePressed

    private void lblCubre50MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre50MousePressed
        zonaSeleccionada = lblCubre50;
    }//GEN-LAST:event_lblCubre50MousePressed

    private void lblCubre01MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre01MousePressed
        zonaSeleccionada = lblCubre01;
    }//GEN-LAST:event_lblCubre01MousePressed

    private void lblCubre11MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre11MousePressed
        zonaSeleccionada = lblCubre11;
    }//GEN-LAST:event_lblCubre11MousePressed

    private void lblCubre21MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre21MousePressed
        zonaSeleccionada = lblCubre21;
    }//GEN-LAST:event_lblCubre21MousePressed

    private void lblCubre31MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre31MousePressed
        zonaSeleccionada = lblCubre31;
    }//GEN-LAST:event_lblCubre31MousePressed

    private void lblCubre41MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre41MousePressed
        zonaSeleccionada = lblCubre41;
    }//GEN-LAST:event_lblCubre41MousePressed

    private void lblCubre51MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre51MousePressed
        zonaSeleccionada = lblCubre51;
    }//GEN-LAST:event_lblCubre51MousePressed

    private void lblCubre02MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre02MousePressed
        zonaSeleccionada = lblCubre02;
    }//GEN-LAST:event_lblCubre02MousePressed

    private void lblCubre12MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre12MousePressed
        zonaSeleccionada = lblCubre12;
    }//GEN-LAST:event_lblCubre12MousePressed

    private void lblCubre22MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre22MousePressed
        zonaSeleccionada = lblCubre22;
    }//GEN-LAST:event_lblCubre22MousePressed

    private void lblCubre52MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre52MousePressed
        zonaSeleccionada = lblCubre52;
    }//GEN-LAST:event_lblCubre52MousePressed

    private void lblCubre03MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre03MousePressed
        zonaSeleccionada = lblCubre03;
    }//GEN-LAST:event_lblCubre03MousePressed

    private void lblCubre13MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre13MousePressed
        zonaSeleccionada = lblCubre13;
    }//GEN-LAST:event_lblCubre13MousePressed

    private void lblCubre23MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre23MousePressed
        zonaSeleccionada = lblCubre23;
    }//GEN-LAST:event_lblCubre23MousePressed

    private void lblCubre53MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre53MousePressed
        zonaSeleccionada = lblCubre53;
    }//GEN-LAST:event_lblCubre53MousePressed

    private void lblCubre04MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre04MousePressed
        zonaSeleccionada = lblCubre04;
    }//GEN-LAST:event_lblCubre04MousePressed

    private void lblCubre14MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre14MousePressed
        zonaSeleccionada = lblCubre14;
    }//GEN-LAST:event_lblCubre14MousePressed

    private void lblCubre24MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre24MousePressed
        zonaSeleccionada = lblCubre24;
    }//GEN-LAST:event_lblCubre24MousePressed

    private void lblCubre34MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre34MousePressed
        zonaSeleccionada = lblCubre34;
    }//GEN-LAST:event_lblCubre34MousePressed

    private void lblCubre44MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre44MousePressed
        zonaSeleccionada = lblCubre44;
    }//GEN-LAST:event_lblCubre44MousePressed

    private void lblCubre54MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre54MousePressed
        zonaSeleccionada = lblCubre54;
    }//GEN-LAST:event_lblCubre54MousePressed

    private void lblCubre05MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre05MousePressed
        zonaSeleccionada = lblCubre05;
    }//GEN-LAST:event_lblCubre05MousePressed

    private void lblCubre15MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre15MousePressed
        zonaSeleccionada = lblCubre15;
    }//GEN-LAST:event_lblCubre15MousePressed

    private void lblCubre25MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre25MousePressed
        zonaSeleccionada = lblCubre25;
    }//GEN-LAST:event_lblCubre25MousePressed

    private void lblCubre35MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre35MousePressed
        zonaSeleccionada = lblCubre35;
    }//GEN-LAST:event_lblCubre35MousePressed

    private void lblCubre45MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre45MousePressed
        zonaSeleccionada = lblCubre45;
    }//GEN-LAST:event_lblCubre45MousePressed

    private void lblCubre55MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCubre55MousePressed
        zonaSeleccionada = lblCubre55;
    }//GEN-LAST:event_lblCubre55MousePressed

    private void btnSpawnearHierbasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSpawnearHierbasActionPerformed
        String seleccion = (String) cmbCantidadASpawnear.getSelectedItem();
        int cantidadASpawnear = Integer.parseInt(seleccion);
        crearRecurso(4, cantidadASpawnear);
    }//GEN-LAST:event_btnSpawnearHierbasActionPerformed

    private void btnSpawnearMaderaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSpawnearMaderaActionPerformed
        String seleccion = (String) cmbCantidadASpawnear.getSelectedItem();
        int cantidadASpawnear = Integer.parseInt(seleccion);
        crearRecurso(2, cantidadASpawnear);
    }//GEN-LAST:event_btnSpawnearMaderaActionPerformed

    private void btnSpawnearCienciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSpawnearCienciaActionPerformed
        String seleccion = (String) cmbCantidadASpawnear.getSelectedItem();
        int cantidadASpawnear = Integer.parseInt(seleccion);
        crearRecurso(1, cantidadASpawnear);
    }//GEN-LAST:event_btnSpawnearCienciaActionPerformed

    private void btnSpawnearFrutasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSpawnearFrutasActionPerformed
        String seleccion = (String) cmbCantidadASpawnear.getSelectedItem();
        int cantidadASpawnear = Integer.parseInt(seleccion);
        crearRecurso(5, cantidadASpawnear);
    }//GEN-LAST:event_btnSpawnearFrutasActionPerformed

    private void btnSpawnearCarnesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSpawnearCarnesActionPerformed
        String seleccion = (String) cmbCantidadASpawnear.getSelectedItem();
        int cantidadASpawnear = Integer.parseInt(seleccion);
        crearRecurso(6, cantidadASpawnear);
    }//GEN-LAST:event_btnSpawnearCarnesActionPerformed

    private void btnSpawnearAguaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSpawnearAguaActionPerformed
        String seleccion = (String) cmbCantidadASpawnear.getSelectedItem();
        int cantidadASpawnear = Integer.parseInt(seleccion);
        crearRecurso(3, cantidadASpawnear);
    }//GEN-LAST:event_btnSpawnearAguaActionPerformed

    private void btnAtaqueAnimalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAtaqueAnimalMouseClicked
        
        JLabel lblAtacante = new javax.swing.JLabel();
        
        lblAtacante.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblRecursoMousePressed(evt);
            }
        });
        pnlMap.add(lblAtacante);
        pnlMap.setComponentZOrder(lblAtacante, 0);
        lblAtacante.setBounds(Integer.parseInt(lblCoordsX.getText()), Integer.parseInt(lblCoordsY.getText()), 40, 40);
        lblAtacante.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Resources/ForTheMap/tigre.png")));
        Atacante atacante = new Atacante(lblAtacante, 20);
        ThreadAtacante gigachad = new ThreadAtacante(this, atacante);
        gigachad.start();
                
    }//GEN-LAST:event_btnAtaqueAnimalMouseClicked

    private void btnBitCientificoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBitCientificoActionPerformed
        abrirLog(2);
    }//GEN-LAST:event_btnBitCientificoActionPerformed

    private void btnBitCazadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBitCazadorActionPerformed
        abrirLog(1);
    }//GEN-LAST:event_btnBitCazadorActionPerformed

    private void btnBitConstructorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBitConstructorActionPerformed
        abrirLog(3);
    }//GEN-LAST:event_btnBitConstructorActionPerformed

    private void btnBitExploradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBitExploradorActionPerformed
        abrirLog(4);
    }//GEN-LAST:event_btnBitExploradorActionPerformed

    private void btnBitCuranderoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBitCuranderoActionPerformed
        abrirLog(5);
    }//GEN-LAST:event_btnBitCuranderoActionPerformed

    private void btnBitRecolectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBitRecolectorActionPerformed
        abrirLog(6);
    }//GEN-LAST:event_btnBitRecolectorActionPerformed

    private void btnSpawnearUnguentoInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSpawnearUnguentoInventarioActionPerformed
        String seleccion = (String) cmbCantidadASpawnear.getSelectedItem();
        int cantidadASpawnear = Integer.parseInt(seleccion);
        Recurso medicina = new Recurso("Remedio", cantidadASpawnear);
        
        if (revisandoStats != null && revisandoStats.agregarRecurso(medicina)) {
            System.out.println("Se genero " + medicina.toString() + " para " + revisandoStats.getNombre());
            actualizarIconosDeInventarioPersonaje(personajes.indexOf(revisandoStats));
        } else System.out.println("El inventario esta lleno");
    }//GEN-LAST:event_btnSpawnearUnguentoInventarioActionPerformed

    private void btnCazarCazadorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCazarCazadorMouseClicked
        cazarAnimal();
    }//GEN-LAST:event_btnCazarCazadorMouseClicked

    private void botonSpawnearCerdoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSpawnearCerdoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonSpawnearCerdoActionPerformed

    private void botonSpawnearLoboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSpawnearLoboActionPerformed
        crearAnimal(2,Integer.parseInt(lblCoordsX.getText()), Integer.parseInt(lblCoordsY.getText()));
    }//GEN-LAST:event_botonSpawnearLoboActionPerformed

    private void botonSpawnearOsoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSpawnearOsoActionPerformed
        crearAnimal(3,Integer.parseInt(lblCoordsX.getText()), Integer.parseInt(lblCoordsY.getText()));
    }//GEN-LAST:event_botonSpawnearOsoActionPerformed

    private void botonSpawnearCerdoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonSpawnearCerdoMouseClicked
        crearAnimal(1,Integer.parseInt(lblCoordsX.getText()), Integer.parseInt(lblCoordsY.getText()));
    }//GEN-LAST:event_botonSpawnearCerdoMouseClicked

    private void lblRecursoMousePressed(java.awt.event.MouseEvent evt) {                                        
        recursoEnMapa = (JLabel) evt.getSource();;
    } 
    
    private void lblAnimalMousePressed(java.awt.event.MouseEvent evt){
        animalEnMapa = (JLabel) evt.getSource();;
    }
    
    private void lblRefugioMousePressed(java.awt.event.MouseEvent evt) {                                        
        refugioEnMapa = (JLabel) evt.getSource();;
        revisandoRefugio = refugios.get(buscarIndexRefugioConUnLabel(refugioEnMapa));
        isCheckingHut = true;
    } 
    
    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barraDeEnergia;
    private javax.swing.JProgressBar barraDeEstabilidad;
    private javax.swing.JProgressBar barraDeVida;
    private javax.swing.JButton botonSpawnearCerdo;
    private javax.swing.JButton botonSpawnearLobo;
    private javax.swing.JButton botonSpawnearOso;
    private javax.swing.JButton btnAtaqueAnimal;
    private javax.swing.JButton btnBitCazador;
    private javax.swing.JButton btnBitCientifico;
    private javax.swing.JButton btnBitConstructor;
    private javax.swing.JButton btnBitCurandero;
    private javax.swing.JButton btnBitExplorador;
    private javax.swing.JButton btnBitRecolector;
    private javax.swing.JButton btnCazarCazador;
    private javax.swing.JButton btnComerCazador;
    private javax.swing.JButton btnComerCientifico;
    private javax.swing.JButton btnComerConstructor;
    private javax.swing.JButton btnComerCurandero;
    private javax.swing.JButton btnComerExplorador;
    private javax.swing.JButton btnComerRecolector;
    private javax.swing.JButton btnCrearMedicamentoCientifico;
    private javax.swing.JButton btnCrearRefugioConstructor;
    private javax.swing.JButton btnCrearRemedioCurandero;
    private javax.swing.JButton btnCurarCurandero;
    private javax.swing.JButton btnCurarDebuffsPersonaje;
    private javax.swing.JButton btnEnfermarPersonaje;
    private javax.swing.JButton btnEntregarRecolector;
    private javax.swing.JButton btnExplorarExplorador;
    private javax.swing.JButton btnHerirPersonaje;
    private javax.swing.JButton btnIntercambiarCazador;
    private javax.swing.JButton btnIntercambiarCientifico;
    private javax.swing.JButton btnIntercambiarConstructor;
    private javax.swing.JButton btnIntercambiarCurandero;
    private javax.swing.JButton btnIntercambiarExplorador;
    private javax.swing.JButton btnInventarioCazador_1;
    private javax.swing.JButton btnInventarioCazador_2;
    private javax.swing.JButton btnInventarioCazador_3;
    private javax.swing.JButton btnInventarioCazador_4;
    private javax.swing.JButton btnInventarioCazador_5;
    private javax.swing.JButton btnInventarioCazador_6;
    private javax.swing.JButton btnInventarioCazador_7;
    private javax.swing.JButton btnInventarioCazador_8;
    private javax.swing.JButton btnInventarioCazador_9;
    private javax.swing.JButton btnInventarioCientifico_1;
    private javax.swing.JButton btnInventarioCientifico_2;
    private javax.swing.JButton btnInventarioCientifico_3;
    private javax.swing.JButton btnInventarioCientifico_4;
    private javax.swing.JButton btnInventarioCientifico_5;
    private javax.swing.JButton btnInventarioCientifico_6;
    private javax.swing.JButton btnInventarioCientifico_7;
    private javax.swing.JButton btnInventarioCientifico_8;
    private javax.swing.JButton btnInventarioCientifico_9;
    private javax.swing.JButton btnInventarioConstructor_1;
    private javax.swing.JButton btnInventarioConstructor_2;
    private javax.swing.JButton btnInventarioConstructor_3;
    private javax.swing.JButton btnInventarioConstructor_4;
    private javax.swing.JButton btnInventarioConstructor_5;
    private javax.swing.JButton btnInventarioConstructor_6;
    private javax.swing.JButton btnInventarioConstructor_7;
    private javax.swing.JButton btnInventarioConstructor_8;
    private javax.swing.JButton btnInventarioConstructor_9;
    private javax.swing.JButton btnInventarioCurandero_1;
    private javax.swing.JButton btnInventarioCurandero_2;
    private javax.swing.JButton btnInventarioCurandero_3;
    private javax.swing.JButton btnInventarioCurandero_4;
    private javax.swing.JButton btnInventarioCurandero_5;
    private javax.swing.JButton btnInventarioCurandero_6;
    private javax.swing.JButton btnInventarioCurandero_7;
    private javax.swing.JButton btnInventarioCurandero_8;
    private javax.swing.JButton btnInventarioCurandero_9;
    private javax.swing.JButton btnInventarioExplorador_1;
    private javax.swing.JButton btnInventarioExplorador_2;
    private javax.swing.JButton btnInventarioExplorador_3;
    private javax.swing.JButton btnInventarioExplorador_4;
    private javax.swing.JButton btnInventarioExplorador_5;
    private javax.swing.JButton btnInventarioExplorador_6;
    private javax.swing.JButton btnInventarioExplorador_7;
    private javax.swing.JButton btnInventarioExplorador_8;
    private javax.swing.JButton btnInventarioExplorador_9;
    private javax.swing.JButton btnInventarioRecolector_1;
    private javax.swing.JButton btnInventarioRecolector_2;
    private javax.swing.JButton btnInventarioRecolector_3;
    private javax.swing.JButton btnInventarioRecolector_4;
    private javax.swing.JButton btnInventarioRecolector_5;
    private javax.swing.JButton btnInventarioRecolector_6;
    private javax.swing.JButton btnInventarioRecolector_7;
    private javax.swing.JButton btnInventarioRecolector_8;
    private javax.swing.JButton btnInventarioRecolector_9;
    private javax.swing.JButton btnIrRefugioCazador;
    private javax.swing.JButton btnIrRefugioCientifico;
    private javax.swing.JButton btnIrRefugioConstructor;
    private javax.swing.JButton btnIrRefugioCurandero;
    private javax.swing.JButton btnIrRefugioExplorador;
    private javax.swing.JButton btnIrRefugioRecolector;
    private javax.swing.JButton btnMoverCazador;
    private javax.swing.JButton btnMoverCientifico;
    private javax.swing.JButton btnMoverConstructor;
    private javax.swing.JButton btnMoverCurandero;
    private javax.swing.JButton btnMoverExplorador;
    private javax.swing.JButton btnMoverRecolector;
    private javax.swing.JButton btnNextDay;
    private javax.swing.JButton btnRecolectarCurandero;
    private javax.swing.JButton btnRecolectarExplorador;
    private javax.swing.JButton btnRecolectarRecolector;
    private javax.swing.JButton btnRepararRefugioConstructor1;
    private javax.swing.JButton btnSpawnearAgua;
    private javax.swing.JButton btnSpawnearCarnes;
    private javax.swing.JButton btnSpawnearCiencia;
    private javax.swing.JButton btnSpawnearFrutas;
    private javax.swing.JButton btnSpawnearHierbas;
    private javax.swing.JButton btnSpawnearMadera;
    private javax.swing.JButton btnSpawnearMedicinaInventario;
    private javax.swing.JButton btnSpawnearTormenta;
    private javax.swing.JButton btnSpawnearUnguentoInventario;
    private javax.swing.JComboBox<String> cmbCantidadASpawnear;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblAvatar;
    private javax.swing.JLabel lblCazadorMapa;
    private javax.swing.JLabel lblCazadorSelect;
    private javax.swing.JLabel lblCientificoMapa;
    private javax.swing.JLabel lblCientificoSelect;
    private javax.swing.JLabel lblConstructorMapa;
    private javax.swing.JLabel lblConstructorSelect;
    private javax.swing.JLabel lblCoordsX;
    private javax.swing.JLabel lblCoordsY;
    private javax.swing.JLabel lblCubre00;
    private javax.swing.JLabel lblCubre01;
    private javax.swing.JLabel lblCubre02;
    private javax.swing.JLabel lblCubre03;
    private javax.swing.JLabel lblCubre04;
    private javax.swing.JLabel lblCubre05;
    private javax.swing.JLabel lblCubre10;
    private javax.swing.JLabel lblCubre11;
    private javax.swing.JLabel lblCubre12;
    private javax.swing.JLabel lblCubre13;
    private javax.swing.JLabel lblCubre14;
    private javax.swing.JLabel lblCubre15;
    private javax.swing.JLabel lblCubre20;
    private javax.swing.JLabel lblCubre21;
    private javax.swing.JLabel lblCubre22;
    private javax.swing.JLabel lblCubre23;
    private javax.swing.JLabel lblCubre24;
    private javax.swing.JLabel lblCubre25;
    private javax.swing.JLabel lblCubre30;
    private javax.swing.JLabel lblCubre31;
    private javax.swing.JLabel lblCubre34;
    private javax.swing.JLabel lblCubre35;
    private javax.swing.JLabel lblCubre40;
    private javax.swing.JLabel lblCubre41;
    private javax.swing.JLabel lblCubre44;
    private javax.swing.JLabel lblCubre45;
    private javax.swing.JLabel lblCubre50;
    private javax.swing.JLabel lblCubre51;
    private javax.swing.JLabel lblCubre52;
    private javax.swing.JLabel lblCubre53;
    private javax.swing.JLabel lblCubre54;
    private javax.swing.JLabel lblCubre55;
    private javax.swing.JLabel lblCuranderoMapa;
    private javax.swing.JLabel lblCuranderoSelect;
    private javax.swing.JLabel lblCurrentSelectedCharacter;
    private javax.swing.JLabel lblDiaActual;
    private javax.swing.JLabel lblDuracionTormenta;
    private javax.swing.JLabel lblEstadoClima;
    private javax.swing.JLabel lblExploradorMapa;
    private javax.swing.JLabel lblExploradorSelect;
    private javax.swing.JLabel lblMap;
    private javax.swing.JLabel lblRecolectorMapa;
    private javax.swing.JLabel lblRecolectorSelect;
    private javax.swing.JLabel lblStatusPersonajeEnRefugio;
    private javax.swing.JLabel lblStatusPersonajeEnRefugio_1;
    private javax.swing.JLabel lblStatusPersonajeEnRefugio_2;
    private javax.swing.JLabel lblStatusPersonajeEnRefugio_3;
    private javax.swing.JLabel lblStatusPersonajeHeridas;
    private javax.swing.JLabel lblStatusPersonajeSalud;
    private javax.swing.JLabel lbl_energiaText;
    private javax.swing.JLabel lbl_statusText;
    private javax.swing.JLabel lbl_statusText1;
    private javax.swing.JLabel lbl_vidaText;
    private javax.swing.JLabel lbl_vidaText1;
    private javax.swing.JPanel pnlAnimales;
    private javax.swing.JPanel pnlBitacoras;
    private javax.swing.JPanel pnlCazador;
    private javax.swing.JPanel pnlCientifico;
    private javax.swing.JPanel pnlComida;
    private javax.swing.JPanel pnlConstructor;
    private javax.swing.JPanel pnlContainer;
    private javax.swing.JPanel pnlCurandero;
    private javax.swing.JPanel pnlDatos;
    private javax.swing.JPanel pnlDatosPersonaje;
    private javax.swing.JPanel pnlDatosRefugio;
    private javax.swing.JPanel pnlDatosRefugio1;
    private javax.swing.JPanel pnlExplorador;
    private javax.swing.JPanel pnlInventarioCazador;
    private javax.swing.JPanel pnlInventarioCientifico;
    private javax.swing.JPanel pnlInventarioConstructor;
    private javax.swing.JPanel pnlInventarioCurandero;
    private javax.swing.JPanel pnlInventarioExplorador;
    private javax.swing.JPanel pnlInventariorecolector;
    private javax.swing.JPanel pnlMap;
    private javax.swing.JPanel pnlMedicinas;
    private javax.swing.JPanel pnlOpcionesDios;
    private javax.swing.JPanel pnlRecolector;
    private javax.swing.JPanel pnlRecursos;
    private javax.swing.JTextArea txfInventarioDisplay;
    // End of variables declaration//GEN-END:variables
}
