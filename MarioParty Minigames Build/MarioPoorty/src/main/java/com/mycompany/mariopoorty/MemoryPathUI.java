/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.mariopoorty;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author fluff
 */
public class MemoryPathUI extends javax.swing.JFrame {
    private JButton[][] botones = new JButton[6][3]; // Botones para la cuadrícula
    private int[] caminoCorrecto = new int[6]; // Camino correcto en cada fila
    private int intentos = 3; // Intentos iniciales
    private int nivelActual = 5; // Nivel que el jugador puede intentar actualmente (última fila)
    private JLabel intentosLabel; // Etiqueta para mostrar intentos restantes
    private boolean juegoActivo = true; // Controla si el juego está activo
    private boolean[] nivelesCompletados = new boolean[6]; // Registro de niveles completados
    private int memoryIntentos = 3;

    public MemoryPathUI() {
        setTitle("Memory Path Game");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        inicializarInterfaz();
        inicializarJuego();
    }

    // Método para inicializar la interfaz gráfica
    private void inicializarInterfaz() {
        // Panel de la cuadrícula
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(6, 3));

        // Crear botones y añadirlos al panel
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                botones[i][j] = new JButton();
                int fila = i, columna = j; // Variables finales para el listener
                botones[i][j].addActionListener(e -> casillaClic(fila, columna)); // Evento de clic
                gridPanel.add(botones[i][j]);
            }
        }

        // Etiqueta de intentos
        intentosLabel = new JLabel("Intentos restantes: " + intentos, SwingConstants.CENTER);

        // Botón para reiniciar el juego
        JButton reiniciarButton = new JButton("Reiniciar Juego");
        reiniciarButton.addActionListener(e -> reiniciarJuego());

        // Panel inferior con intentos y botón de reinicio
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(intentosLabel, BorderLayout.NORTH);
        bottomPanel.add(reiniciarButton, BorderLayout.SOUTH);

        // Añadir paneles al JFrame
        this.add(gridPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    // Método para inicializar el juego
    private void inicializarJuego() {
        nivelActual = 5; // Comienza desde la última fila
        juegoActivo = true;
        for (int i = 0; i < 6; i++) {
            caminoCorrecto[i] = (int) (Math.random() * 3); // Casilla aleatoria en cada fila
            nivelesCompletados[i] = false; // Reiniciar niveles completados
        }
        actualizarIntentos();
        resetearBotones();
    }

    // Acción al hacer clic en una casilla
    private void casillaClic(int fila, int columna) {
        if (!juegoActivo) {
            JOptionPane.showMessageDialog(this, "El juego ha terminado. Reinicia para jugar de nuevo.");
            return;
        }

        if (nivelesCompletados[fila]) {
            JOptionPane.showMessageDialog(this, "¡Este nivel ya está completado!");
            return;
        }

        if (fila < nivelActual) {
            JOptionPane.showMessageDialog(this, "¡Debes completar los niveles inferiores primero!");
            return;
        }

        if (caminoCorrecto[fila] == columna) {
            botones[fila][columna].setBackground(Color.GREEN); // Correcto
            botones[fila][columna].setEnabled(false); // Deshabilitar botón
            nivelesCompletados[fila] = true; // Marcar nivel como completado
            if (fila == nivelActual) {
                nivelActual--; // Retrocede al siguiente nivel
            }
            verificarVictoria();
        } else {
            botones[fila][columna].setBackground(Color.RED); // Incorrecto
            intentos--;
            actualizarIntentos();
            if (intentos == 0) {
                juegoActivo = false;
                JOptionPane.showMessageDialog(this, "¡Game Over! Puedes reiniciar el juego.");
            }
        }
    }

    // Método para reiniciar el juego
    private void reiniciarJuego() {
        memoryIntentos++; // Incrementar intentos al reiniciar
        intentos = memoryIntentos;
        inicializarJuego();
    }

    // Método para actualizar la etiqueta de intentos restantes
    private void actualizarIntentos() {
        intentosLabel.setText("Intentos restantes: " + intentos);
    }

    // Método para verificar si el jugador ha ganado
    private void verificarVictoria() {
        if (nivelActual < 0) {
            juegoActivo = false;
            JOptionPane.showMessageDialog(this, "¡Felicidades! Has ganado el juego.");
        }
    }

    // Método para resetear los botones
    private void resetearBotones() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                botones[i][j].setBackground(null); // Restablecer color
                botones[i][j].setEnabled(true); // Habilitar botón
            }
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1517, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 351, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MemoryPathUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MemoryPathUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MemoryPathUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MemoryPathUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MemoryPathUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
