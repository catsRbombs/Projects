/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.mariopoorty;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;



/**
 *
 * @author fluff
 */
public class MemoryGameUI extends javax.swing.JFrame {
    private final String imagesPath = "E:/_repositorios/MarioPoorty/MarioPoorty/src/Data/";
    private final String[] images = {"hoja.png", "hongo.png", "flor.png", "toad.png", "teemo.png", "vida.png", "dado.png", "bowser.png", "tec.png"};
    private final String hiddenCard = "cartaoculta.png";
    private final JButton[][] buttons = new JButton[6][3];
    private final String[][] board = new String[6][3];
    private final boolean[][] revealed = new boolean[6][3];
    private int currentPlayer = 1;
    private int player1Score = 0;
    private int player2Score = 0;
    private int revealedCount = 0;
    private JButton firstSelected = null;
    private JButton secondSelected = null;

    // Etiquetas dinámicas
    private JLabel playerLabel;
    private JLabel scoreLabel;

    public MemoryGameUI() {
        setTitle("Juego de Memoria");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel para la matriz de cartas
        JPanel gridPanel = new JPanel(new GridLayout(6, 3, 5, 5));
        initializeBoard();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                JButton button = new JButton(new ImageIcon(loadImage(hiddenCard)));
                button.addActionListener(new CardClickListener(i, j));
                buttons[i][j] = button;
                gridPanel.add(button);
            }
        }

        add(gridPanel, BorderLayout.CENTER);

        // Panel para mostrar información del juego
        JPanel infoPanel = new JPanel(new FlowLayout());
        playerLabel = new JLabel("Turno del Jugador 1");
        scoreLabel = new JLabel("Puntos: Jugador 1 = 0, Jugador 2 = 0");
        infoPanel.add(playerLabel);
        infoPanel.add(scoreLabel);
        add(infoPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void initializeBoard() {
        List<String> cards = new ArrayList<>();
        for (String image : images) {
            cards.add(image);
            cards.add(image); // Agrega dos copias de cada carta
        }
        Collections.shuffle(cards); // Mezcla las cartas

        int index = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = cards.get(index++);
                revealed[i][j] = false;
            }
        }
    }

    private void revealCard(JButton button, int row, int col) {
        String card = board[row][col];
        button.setIcon(new ImageIcon(loadImage(card)));
        revealed[row][col] = true;
    }

    private void hideCard(JButton button, int row, int col) {
        button.setIcon(new ImageIcon(loadImage(hiddenCard)));
        revealed[row][col] = false;
    }

    private void checkMatch() {
        Timer timer = new Timer(1000, e -> {
            if (firstSelected != null && secondSelected != null) {
                int firstRow = -1, firstCol = -1, secondRow = -1, secondCol = -1;

                // Encuentra las posiciones de las cartas seleccionadas
                outer:
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (buttons[i][j] == firstSelected) {
                            firstRow = i;
                            firstCol = j;
                        }
                        if (buttons[i][j] == secondSelected) {
                            secondRow = i;
                            secondCol = j;
                        }
                        if (firstRow != -1 && secondRow != -1) break outer;
                    }
                }

                // Comprueba si las cartas coinciden
                if (board[firstRow][firstCol].equals(board[secondRow][secondCol])) {
                    if (currentPlayer == 1) {
                        player1Score++;
                    } else {
                        player2Score++;
                    }
                    revealedCount += 2;
                    updateInfoPanel();
                } else {
                    hideCard(firstSelected, firstRow, firstCol);
                    hideCard(secondSelected, secondRow, secondCol);
                    currentPlayer = (currentPlayer == 1) ? 2 : 1;
                    updateInfoPanel();
                }

                if (revealedCount == 18) {
                    JOptionPane.showMessageDialog(this, "Juego terminado! Ganador: " +
                            (player1Score > player2Score ? "Jugador 1" : "Jugador 2"));
                }

                firstSelected = null;
                secondSelected = null;
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void updateInfoPanel() {
        playerLabel.setText("Turno del Jugador " + currentPlayer);
        scoreLabel.setText("Puntos: Jugador 1 = " + player1Score + ", Jugador 2 = " + player2Score);
    }

    private Image loadImage(String imageName) {
        return new ImageIcon(imagesPath + imageName).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
    }

    // Listener para botones
    private class CardClickListener implements ActionListener {
        private final int row, col;

        public CardClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (revealed[row][col] || (firstSelected != null && secondSelected != null)) {
                return; // Ignora clics en cartas ya reveladas o si ya hay dos seleccionadas
            }

            JButton clickedButton = buttons[row][col];
            revealCard(clickedButton, row, col);

            if (firstSelected == null) {
                firstSelected = clickedButton;
            } else if (secondSelected == null) {
                secondSelected = clickedButton;
                checkMatch();
            }
        }
    }

    /**
     * Creates new form MemoryGameUI
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
        SwingUtilities.invokeLater(MemoryGameUI::new);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
