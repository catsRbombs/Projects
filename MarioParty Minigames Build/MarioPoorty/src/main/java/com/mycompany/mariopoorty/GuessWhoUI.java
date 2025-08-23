/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.mariopoorty;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author fluff
 */
public class GuessWhoUI extends javax.swing.JFrame {
    private final int GRID_SIZE = 10; // Tamaño de la matriz
    private final JButton[][] buttons = new JButton[GRID_SIZE][GRID_SIZE];
    private final String[] personajes = {
        "Mario", "Luigi", "Peach", "Yoshi", "Bowser", "Daisy", "Toad", 
        "Wario", "Waluigi", "Donkey Kong", "Koopa Troopa", "Boo", 
        "Rosalina", "Shy Guy", "Bowser Jr."
    };
    private final JLabel intentosLabel = new JLabel();
    private int intentosRestantes;
    private String personajeSeleccionado;
    private ImageIcon personajeImagen;

    public GuessWhoUI() {
        setTitle("Guess Who");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());

        // Configurar intentos
        intentosRestantes = (int) (Math.random() * 5) + 4; // Entre 4 y 8 intentos
        intentosLabel.setText("Intentos restantes: " + intentosRestantes);
        add(intentosLabel, BorderLayout.NORTH);

        // Selección aleatoria del personaje
        int personajeIndex = (int) (Math.random() * personajes.length);
        personajeSeleccionado = personajes[personajeIndex];
        personajeImagen = new ImageIcon("E:/_repositorios/MarioPoorty/MarioPoorty/src/Data/" + personajeSeleccionado + ".png");

        // Panel con la imagen de fondo
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(personajeImagen.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        imagePanel.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        add(imagePanel, BorderLayout.CENTER);

        // Añadir botones para cubrir la imagen
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                JButton button = new JButton();
                button.setBackground(Color.DARK_GRAY); // Color del botón
                button.addActionListener(new CellClickListener(i, j));
                buttons[i][j] = button;
                imagePanel.add(button);
            }
        }

        // Panel de selección de personajes
        JPanel guessPanel = new JPanel();
        JComboBox<String> comboBox = new JComboBox<>(personajes);
        JButton guessButton = new JButton("Adivinar");
        guessButton.addActionListener(e -> verificarAdivinanza(comboBox.getSelectedItem().toString()));

        guessPanel.add(new JLabel("¿Quién es?"));
        guessPanel.add(comboBox);
        guessPanel.add(guessButton);
        add(guessPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Clase interna para manejar los clics en las celdas
    private class CellClickListener implements ActionListener {
        private final int row;
        private final int col;

        public CellClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (intentosRestantes > 0) {
                JButton button = buttons[row][col];
                button.setVisible(false); // Ocultar el botón
                intentosRestantes--;
                intentosLabel.setText("Intentos restantes: " + intentosRestantes);

                // Deshabilitar todos los botones si no hay intentos restantes
                if (intentosRestantes == 0) {
                    for (int i = 0; i < GRID_SIZE; i++) {
                        for (int j = 0; j < GRID_SIZE; j++) {
                            buttons[i][j].setEnabled(false);
                        }
                    }
                }
            }
        }
    }

    // Método para verificar la adivinanza del jugador
    private void verificarAdivinanza(String seleccion) {
        if (seleccion.equals(personajeSeleccionado)) {
            JOptionPane.showMessageDialog(this, "¡Correcto! Era " + personajeSeleccionado + ".");
        } else {
            JOptionPane.showMessageDialog(this, "Incorrecto. Era " + personajeSeleccionado + ".");
        }
        // Reiniciar el juego o salir
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Quieres jugar de nuevo?", "Reiniciar", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            dispose(); // Cierra la ventana actual
            new GuessWhoUI(); // Inicia un nuevo juego
        } else {
            System.exit(0); // Cierra la aplicación
        }
    }


    /**
     * Creates new form GuessWho
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
    public static void main(String[] args) {
        new GuessWhoUI();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
