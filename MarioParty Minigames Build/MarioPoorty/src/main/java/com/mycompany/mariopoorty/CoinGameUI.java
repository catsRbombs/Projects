/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.mariopoorty;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 *
 * @author fluff
 */
public class CoinGameUI extends javax.swing.JFrame {
    private JButton[][] botones = new JButton[25][25];
    private int[][] valoresMonedas = new int[25][25];
    private boolean[][] monedasSeleccionadas = new boolean[25][25];
    private int tiempoRestante;
    private Timer temporizador;
    private JLabel tiempoLabel;
    private int puntajeTotal = 0;

    public CoinGameUI() {
        setTitle("Coin Collector Game");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        inicializarJuego();
        inicializarInterfaz();
    }

    private void inicializarJuego() {
        // Generar valores aleatorios para las monedas
        Random random = new Random();
        int cantidadMonedas = 25 * 25;
        int mitad = cantidadMonedas / 2;

        // Generar valores positivos y negativos
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                if (mitad > 0) {
                    valoresMonedas[i][j] = random.nextInt(10) + 1; // Positivo
                    mitad--;
                } else {
                    valoresMonedas[i][j] = -(random.nextInt(10) + 1); // Negativo
                }
            }
        }

        // Mezclar valores en la matriz
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                int x = random.nextInt(25);
                int y = random.nextInt(25);
                int temp = valoresMonedas[i][j];
                valoresMonedas[i][j] = valoresMonedas[x][y];
                valoresMonedas[x][y] = temp;
            }
        }

        // Tiempo aleatorio (30, 45, 60 segundos)
        tiempoRestante = new int[]{30, 45, 60}[random.nextInt(3)];
    }

    private void inicializarInterfaz() {
        // Panel para la matriz de botones
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(25, 25));

        // Crear botones con iconos
        ImageIcon iconoMoneda = new ImageIcon("E:/_repositorios/MarioPoorty/MarioPoorty/src/Data/Coin.png");
        Image imagenEscalada = iconoMoneda.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        iconoMoneda = new ImageIcon(imagenEscalada);

        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                botones[i][j] = new JButton();
                botones[i][j].setIcon(iconoMoneda);
                botones[i][j].setBorderPainted(false);   // Quitar el borde
                botones[i][j].setContentAreaFilled(false); // Quitar el área de fondo
                botones[i][j].setFocusPainted(false);    // Quitar el borde de foco cuando el botón está seleccionado
                int fila = i, columna = j; // Variables finales para el listener
                botones[i][j].addActionListener(e -> seleccionarMoneda(fila, columna));
                gridPanel.add(botones[i][j]);
                
            }
        }

        // Etiqueta para mostrar el tiempo restante
        tiempoLabel = new JLabel("Tiempo restante: " + tiempoRestante + " segundos", SwingConstants.CENTER);

        // Temporizador para actualizar el tiempo
        temporizador = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tiempoRestante--;
                tiempoLabel.setText("Tiempo restante: " + tiempoRestante + " segundos");

                if (tiempoRestante <= 0) {
                    temporizador.stop();
                    finalizarJuego();
                }
            }
        });

        temporizador.start();

        // Añadir componentes al JFrame
        add(tiempoLabel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
    }

    private void seleccionarMoneda(int fila, int columna) {
        if (!monedasSeleccionadas[fila][columna]) {
            monedasSeleccionadas[fila][columna] = true;
            botones[fila][columna].setEnabled(false); // Desactivar botón
            puntajeTotal += valoresMonedas[fila][columna]; // Sumar puntaje
        }
    }

    private void finalizarJuego() {
        // Reemplazar iconos por valores
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                String valor = String.valueOf(valoresMonedas[i][j]);
                botones[i][j].setText(valor);
                botones[i][j].setIcon(null); // Quitar el icono
            }
        }

        // Mostrar mensaje de resultado
        String mensaje = (puntajeTotal > 0) ? "¡Ganaste con un puntaje de " + puntajeTotal + "!" 
                                            : "Perdiste con un puntaje de " + puntajeTotal + ".";
        JOptionPane.showMessageDialog(this, mensaje);

        // Reiniciar juego
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Quieres jugar otra vez?", "Reiniciar", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            reiniciarJuego();
        } else {
            dispose(); // Cerrar la ventana
        }
    }

    private void reiniciarJuego() {
        puntajeTotal = 0;
        inicializarJuego();
        getContentPane().removeAll();
        inicializarInterfaz();
        revalidate();
        repaint();
    }
    /**
     * Creates new form CoinGameUI
     */

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
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
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
            java.util.logging.Logger.getLogger(CoinGameUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CoinGameUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CoinGameUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CoinGameUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CoinGameUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
