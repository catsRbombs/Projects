/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.mariopoorty;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author fluff
 */
public class WordSearchGameUI extends javax.swing.JFrame {
    private final Set<JButton> selectedButtons = new HashSet<>();
    private static final int MIN_SIZE = 10;
    private static final int MAX_SIZE = 20;
    private static final int WORD_COUNT = 4;
    private static final int TIME_LIMIT = 120; // 2 minutos en segundos
    private final List<String> words = new ArrayList<>();
    private final JButton[][] gridButtons;
    private final String[] selectedWords = new String[WORD_COUNT];
    private int foundWords = 0;
    private final Map<Character, Integer> selectedCounts = new HashMap<>();
    private final JLabel timerLabel = new JLabel();
    private final JLabel wordListLabel = new JLabel();
    private Timer timer;
    private int remainingTime;

    public WordSearchGameUI() throws IOException {
        setTitle("Word Search Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Leer palabras desde el archivo
        loadWords("E:\\_repositorios\\MarioPoorty\\MarioPoorty\\src\\Data\\sopaPalabras.txt");

        // Seleccionar palabras aleatorias
        for (int i = 0; i < WORD_COUNT; i++) {
            selectedWords[i] = words.get((int) (Math.random() * words.size())).toUpperCase();
        }

        // Tamaño aleatorio de la sopa de letras
        int gridSize = new Random().nextInt((MAX_SIZE - MIN_SIZE) + 1) + MIN_SIZE;
        gridButtons = new JButton[gridSize][gridSize];

        // Crear la sopa de letras
        char[][] grid = createWordSearch(gridSize);

        // Interfaz gráfica
        setLayout(new BorderLayout());
        JPanel gridPanel = new JPanel(new GridLayout(gridSize, gridSize));
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                JButton button = new JButton(String.valueOf(grid[i][j]));
                button.setFont(new Font("Arial", Font.BOLD, 14));
                button.addActionListener(new LetterClickListener(i, j));
                gridButtons[i][j] = button;
                gridPanel.add(button);
            }
        }
        add(gridPanel, BorderLayout.CENTER);

        // Etiqueta para mostrar las palabras seleccionadas
        wordListLabel.setText("Palabras: " + String.join(", ", selectedWords));
        wordListLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(wordListLabel, BorderLayout.SOUTH);

        // Temporizador
        remainingTime = TIME_LIMIT;
        timerLabel.setText("Tiempo restante: " + remainingTime + " segundos");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(timerLabel, BorderLayout.NORTH);

        startTimer();

        setSize(600, 600);
        setVisible(true);
    }

    private void loadWords(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() >= 5) {
                    words.add(line.toUpperCase());
                }
            }
        }
        if (words.size() < 100) {
            throw new IllegalArgumentException("El archivo debe contener al menos 100 palabras válidas.");
        }
    }

    private char[][] createWordSearch(int size) {
    char[][] grid = new char[size][size];
    for (char[] row : grid) Arrays.fill(row, '.');

    Random rand = new Random();
    for (String word : selectedWords) {
        placeWord(grid, word, size, rand);
    }

    // Rellenar espacios vacíos con letras aleatorias
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            if (grid[i][j] == '.') {
                grid[i][j] = (char) ('A' + rand.nextInt(26));
            }
        }
    }
    return grid;
}

private void placeWord(char[][] grid, String word, int gridSize, Random rand) {
    boolean placed = false;
    while (!placed) {
        int row = rand.nextInt(gridSize);
        int col = rand.nextInt(gridSize);
        int rowDir = rand.nextInt(3) - 1; // -1, 0, 1
        int colDir = rand.nextInt(3) - 1; // -1, 0, 1

        // Evitar dirección nula (sin movimiento)
        if (rowDir == 0 && colDir == 0) continue;

        // Verificar si la palabra cabe en la dirección seleccionada
        if (canPlaceWord(grid, word, row, col, rowDir, colDir, gridSize)) {
            for (char c : word.toCharArray()) {
                grid[row][col] = c;
                row += rowDir;
                col += colDir;
            }
            placed = true;
        }
    }
}

private boolean canPlaceWord(char[][] grid, String word, int row, int col, int rowDir, int colDir, int gridSize) {
    for (int i = 0; i < word.length(); i++) {
        // Verificar límites del tablero
        if (row < 0 || row >= gridSize || col < 0 || col >= gridSize) return false;

        // Verificar si la celda está vacía o coincide con la letra actual
        if (grid[row][col] != '.' && grid[row][col] != word.charAt(i)) return false;

        row += rowDir;
        col += colDir;
    }
    return true;
}

    private void startTimer() {
        timer = new Timer(1000, e -> {
            remainingTime--;
            timerLabel.setText("Tiempo restante: " + remainingTime + " segundos");
            if (remainingTime <= 0) {
                timer.stop();
                revealWords();
                JOptionPane.showMessageDialog(this, "¡Se acabó el tiempo! Has perdido.");
                System.exit(0);
            }
        });
        timer.start();
    }
    
    private void revealWords() {
        for (JButton[] row : gridButtons) {
            for (JButton button : row) {
                button.setBackground(Color.LIGHT_GRAY);
                button.setEnabled(false);
            }
        }
        JOptionPane.showMessageDialog(this, "Las palabras eran: " + String.join(", ", selectedWords));
    }

    private Map<Character, Integer> buildLetterCount(String word) {
        Map<Character, Integer> letterCount = new HashMap<>();
        for (char c : word.toCharArray()) {
            letterCount.put(c, letterCount.getOrDefault(c, 0) + 1);
        }
        return letterCount;
    }

    private boolean matchesWord(Map<Character, Integer> selectedCounts, Map<Character, Integer> wordCounts) {
        return selectedCounts.equals(wordCounts);
    }

    private class LetterClickListener implements ActionListener {
        private final int row;
        private final int col;

        public LetterClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = gridButtons[row][col];
            char letter = button.getText().charAt(0);

            if (selectedButtons.contains(button)) {
                button.setBackground(null);
                selectedButtons.remove(button);
                selectedCounts.put(letter, selectedCounts.get(letter) - 1);
                if (selectedCounts.get(letter) <= 0) {
                    selectedCounts.remove(letter);
                }
            } else {
                button.setBackground(Color.YELLOW);
                selectedButtons.add(button);
                selectedCounts.put(letter, selectedCounts.getOrDefault(letter, 0) + 1);
            }

            checkWordFound();
        }
    }

    private void checkWordFound() {
        for (int i = 0; i < selectedWords.length; i++) {
            if (selectedWords[i] == null) continue;

            Map<Character, Integer> wordCounts = buildLetterCount(selectedWords[i]);
            if (matchesWord(selectedCounts, wordCounts)) {
                markWordAsFound();
                selectedCounts.clear();
                selectedWords[i] = null;
                updateWordListLabel();
                return;
            }
        }
    }

    private void markWordAsFound() {
        for (JButton button : selectedButtons) {
            button.setBackground(Color.GREEN);
            button.setEnabled(false);
        }
        foundWords++;
        if(foundWords == 4){
                JOptionPane.showMessageDialog(null, "¡Has ganado!", "Victoria", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);}
        selectedButtons.clear();
    }

    private void updateWordListLabel() {
        StringBuilder remainingWords = new StringBuilder("Palabras restantes: ");
        for (String word : selectedWords) {
            if (word != null) {
                remainingWords.append(word).append(", ");
            }
        }
        if (remainingWords.length() > 18) {
            remainingWords.setLength(remainingWords.length() - 2);
        }
        wordListLabel.setText(remainingWords.toString());
    }


    
    /**
     * Creates new form WordSearchGameUI
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
        try {
            new WordSearchGameUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
