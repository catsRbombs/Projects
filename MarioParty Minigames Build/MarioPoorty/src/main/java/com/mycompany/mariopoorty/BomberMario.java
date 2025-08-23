/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.mariopoorty;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author fluff
 */
public class BomberMario extends javax.swing.JFrame {
    private JButton[][] board;  // Matriz para el tablero
    private final int boardSize;  // Tamaño del tablero aleatorio: 10, 15 o 20
    private final String[][] treasureMap;  // Mapa para almacenar la ubicación del tesoro
    private int bombsLeft = 7;  // Bombas disponibles
    private int treasureFound = 0; // Casillas del tesoro descubiertas

    private final String imageDir = "E:/_repositorios/MarioPoorty/MarioPoorty/src/Data/";
    private JButton selectedBomb; // Bombas que el usuario selecciona

    public BomberMario() {
        this.boardSize = getRandomBoardSize();
        this.board = new JButton[boardSize][boardSize];
        this.treasureMap = new String[boardSize][boardSize];
        
        setTitle("BomberMario");
        setSize(boardSize * 100, boardSize * 100 + 150);
        setLayout(new BorderLayout());
        
        initializeBoard();
        placeTreasure();
        initializeBombPanel();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Método para seleccionar aleatoriamente el tamaño del tablero
    private int getRandomBoardSize() {
        int[] sizes = {10, 15, 20};
        Random rand = new Random();
        return sizes[rand.nextInt(sizes.length)];
    }

    // Inicializar el tablero
private void initializeBoard() {
    JPanel boardPanel = new JPanel();
    boardPanel.setLayout(new GridLayout(boardSize, boardSize));
    for (int i = 0; i < boardSize; i++) {
        for (int j = 0; j < boardSize; j++) {
            JButton cell = new JButton();
            cell.setPreferredSize(new Dimension(100, 100));
            cell.setIcon(new ImageIcon(new ImageIcon(imageDir + "block.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));

            // Hacer el fondo invisible
            cell.setContentAreaFilled(false);
            cell.setBorderPainted(false);
            cell.setFocusPainted(false);

            int row = i, col = j;
            cell.addActionListener(e -> handleCellClick(row, col));
            board[i][j] = cell;
            boardPanel.add(cell);
            treasureMap[i][j] = "empty";
        }
    }
    add(boardPanel, BorderLayout.CENTER);
}

    // Colocar el tesoro aleatoriamente en el tablero
    private void placeTreasure() {
        Random rand = new Random();
        int row, col;
        boolean placed = false;
        
        while (!placed) {
            row = rand.nextInt(boardSize - 1);
            col = rand.nextInt(boardSize - 1);

            if (treasureMap[row][col].equals("empty") && treasureMap[row+1][col].equals("empty") &&
                treasureMap[row][col+1].equals("empty") && treasureMap[row+1][col+1].equals("empty")) {
                
                treasureMap[row][col] = "tesoro1";
                treasureMap[row][col+1] = "tesoro2";
                treasureMap[row+1][col] = "tesoro3";
                treasureMap[row+1][col+1] = "tesoro4";
                placed = true;
            }
        }
    }

    // Panel de selección de bombas
    private void initializeBombPanel() {
        JPanel bombPanel = new JPanel();
        bombPanel.setLayout(new FlowLayout());

        String[] bombTypes = {"simple", "doble", "cruz", "linea"};
        Random rand = new Random();

        for (int i = 0; i < 7; i++) {
            String bombType = bombTypes[rand.nextInt(bombTypes.length)];
            JButton bombButton = new JButton(new ImageIcon(new ImageIcon(imageDir + bombType + ".png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
            bombButton.setActionCommand(bombType);
            bombButton.addActionListener(e -> selectBomb(bombButton));
            bombPanel.add(bombButton);
        }
        add(bombPanel, BorderLayout.SOUTH);
    }

    // Selección de bomba
    private void selectBomb(JButton bombButton) {
        selectedBomb = bombButton;
    }

    // Manejar clic en una celda del tablero
    private void handleCellClick(int row, int col) {
        if (selectedBomb != null && bombsLeft > 0) {
            String bombType = selectedBomb.getActionCommand();
            detonateBomb(row, col, bombType);
            bombsLeft--;
            selectedBomb.setEnabled(false); // Deshabilitar la bomba usada
            selectedBomb.setBackground(Color.GRAY); // Ponerla gris
            selectedBomb = null;
            checkGameOver();
        }
    }

    // Lógica de detonación según el tipo de bomba
    private void detonateBomb(int row, int col, String bombType) {
        switch (bombType) {
            case "simple":
                revealCell(row, col);
                checkGameOver();
                break;
            case "doble":
                reveal2x2(row, col);
                checkGameOver();
                break;
            case "cruz":
                revealCross(row, col);
                checkGameOver();
                break;
            case "linea":
                revealLine(row, col);
                checkGameOver();
                break;
        }
    }

    // Métodos para revelar celdas de cada tipo de bomba
    private void revealCell(int row, int col) {
        reveal(row, col);
    }

    private void reveal2x2(int row, int col) {
        reveal(row, col);
        reveal(row + 1, col);
        reveal(row, col + 1);
        reveal(row + 1, col + 1);
    }

    private void revealCross(int row, int col) {
        reveal(row, col);
        reveal(row - 1, col);
        reveal(row + 1, col);
        reveal(row, col - 1);
        reveal(row, col + 1);
    }

    private void revealLine(int row, int col) {
        for (int i = -2; i <= 2; i++) {
            reveal(row, col + i);
            reveal(row + i, col);
        }
    }

    // Método para revelar una celda específica
    private void reveal(int row, int col) {
        if (row >= 0 && row < boardSize && col >= 0 && col < boardSize) {
            String content = treasureMap[row][col];
            if (content.startsWith("tesoro")) {
                board[row][col].setIcon(new ImageIcon(new ImageIcon(imageDir + content + ".png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
                treasureFound++;
            } else {
                board[row][col].setEnabled(false);
            }
        }
    }

    // Comprobar si el jugador ha ganado o perdido
    private void checkGameOver() {
        if (treasureFound == 4) {
            JOptionPane.showMessageDialog(this, "¡Felicidades! Has encontrado el tesoro.");
            System.exit(0);
        } else if (bombsLeft == 0 && treasureFound < 4) {
            revealTreasure();  // Revela el tesoro cuando se termina el juego sin ganarlo
            JOptionPane.showMessageDialog(this, "Te has quedado sin bombas. ¡Has perdido!");
            System.exit(0);
        }
    }

    // Método para revelar la ubicación del tesoro al perder
    private void revealTreasure() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                String content = treasureMap[i][j];
                if (content.startsWith("tesoro")) {
                    board[i][j].setIcon(new ImageIcon(new ImageIcon(imageDir + content + ".png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
                }
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
        new BomberMario();  // Ejemplo: crear un tablero de 15x15
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
