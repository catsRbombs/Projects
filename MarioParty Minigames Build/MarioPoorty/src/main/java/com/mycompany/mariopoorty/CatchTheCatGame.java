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
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author fluff
 */
public class CatchTheCatGame extends javax.swing.JFrame {
    private final String imagesPath = "E:/_repositorios/MarioPoorty/MarioPoorty/src/Data/";
    private final String emptyTile = "casilla.png";
    private final String blockedTile = "cerrado.png";
    private final String catTile = "gato.png";
    private final int gridSize = 11;
    private final JButton[][] buttons = new JButton[gridSize][gridSize];
    private final boolean[][] blocked = new boolean[gridSize][gridSize];
    private int catRow = gridSize / 2; // Inicialmente el gato está al centro
    private int catCol = gridSize / 2;

    public CatchTheCatGame() {
        setTitle("Atrapa al Gato");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(gridSize, gridSize, 2, 2));
        initializeGrid(gridPanel);

        add(gridPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void initializeGrid(JPanel gridPanel) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                JButton button = new JButton(new ImageIcon(loadImage(emptyTile)));
                button.addActionListener(new TileClickListener(i, j));
                buttons[i][j] = button;
                blocked[i][j] = false; // Inicialmente, todas las casillas están desbloqueadas
                gridPanel.add(button);
            }
        }
        buttons[catRow][catCol].setIcon(new ImageIcon(loadImage(catTile))); // Posición inicial del gato
    }

    private Image loadImage(String imageName) {
        return new ImageIcon(imagesPath + imageName).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < gridSize && col >= 0 && col < gridSize && !blocked[row][col];
    }

    private boolean moveCat() {
    // Movimientos posibles (4 direcciones: arriba, abajo, izquierda, derecha)
    int[] rowOffsets = {-1, 1, 0, 0};
    int[] colOffsets = {0, 0, -1, 1};

    // Validación de si el gato está rodeado
    boolean isTrapped = true;
    for (int d = 0; d < 4; d++) {
        int nextRow = catRow + rowOffsets[d];
        int nextCol = catCol + colOffsets[d];
        
        if (isValid(nextRow, nextCol)) {
            isTrapped = false;
            break; // Hay al menos un movimiento válido, por lo que no está atrapado
        }
    }

    if (isTrapped) {
        return false; // Indica que el gato no se movió y está atrapado
    }

    int nearestExitDistance = Integer.MAX_VALUE;
    int newCatRow = catRow;
    int newCatCol = catCol;

    for (int d = 0; d < 4; d++) {
        int nextRow = catRow + rowOffsets[d];
        int nextCol = catCol + colOffsets[d];

        if (isValid(nextRow, nextCol)) {
            int distanceToEdge = Math.min(Math.min(nextRow, gridSize - 1 - nextRow),
                                          Math.min(nextCol, gridSize - 1 - nextCol));
            if (distanceToEdge < nearestExitDistance) {
                nearestExitDistance = distanceToEdge;
                newCatRow = nextRow;
                newCatCol = nextCol;
            }
        }
    }

    // Si hay un movimiento válido, actualizamos la posición del gato
    if (newCatRow != catRow || newCatCol != catCol) {
        buttons[catRow][catCol].setIcon(new ImageIcon(loadImage(emptyTile))); // Limpia la casilla anterior
        catRow = newCatRow;
        catCol = newCatCol;
        buttons[catRow][catCol].setIcon(new ImageIcon(loadImage(catTile))); // Mueve el gato
    }

    // Verificar si el gato escapó
    return isEdge(catRow, catCol);
}

    private boolean isEdge(int row, int col) {
        return row == 0 || row == gridSize - 1 || col == 0 || col == gridSize - 1;
    }

    private void checkGameState(boolean catEscaped) {
        if (catEscaped) {
            JOptionPane.showMessageDialog(this, "¡El gato escapó! Has perdido.");
            System.exit(0);
        } else if (isCatTrapped()) {
            JOptionPane.showMessageDialog(this, "¡Has atrapado al gato! ¡Has ganado!");
            System.exit(0);
        }
    }

private boolean isCatTrapped() {
    int[] rowOffsets = {-1, 1, 0, 0}; // Solo arriba, abajo
    int[] colOffsets = {0, 0, -1, 1}; // Solo izquierda, derecha

    for (int d = 0; d < 4; d++) {
        int newRow = catRow + rowOffsets[d];
        int newCol = catCol + colOffsets[d];
        if (isValid(newRow, newCol)) {
            return false; // El gato aún tiene al menos un movimiento posible
        }
    }
    return true; // El gato está atrapado en las cuatro direcciones cardinales
}

    private class TileClickListener implements ActionListener {
        private final int row, col;

        public TileClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (blocked[row][col] || (row == catRow && col == catCol)) {
                return; // Ignora clics en casillas ya bloqueadas o donde está el gato
            }

            buttons[row][col].setIcon(new ImageIcon(loadImage(blockedTile)));
            blocked[row][col] = true;

            boolean catEscaped = moveCat();
            checkGameState(catEscaped);
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
        SwingUtilities.invokeLater(CatchTheCatGame::new);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
