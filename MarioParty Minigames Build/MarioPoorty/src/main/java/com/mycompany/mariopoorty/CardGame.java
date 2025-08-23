/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mariopoorty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fluff
 */
public class CardGame {
    private static final String[] SUITS = {"hearts", "clubs", "spades", "diamonds"};
    private static final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king", "ace"};
    private List<String> deck;
    private Map<Integer, String> playerCards; // Almacena cartas de los jugadores

    public CardGame() {
        initializeDeck();
        playerCards = new HashMap<>();
    }

    // Inicializar baraja
    private void initializeDeck() {
        deck = new ArrayList<>();
        for (String suit : SUITS) {
            for (String rank : RANKS) {
                deck.add(rank + "_of_" + suit);
            }
        }
        Collections.shuffle(deck); // Mezcla las cartas
    }

    // Obtener carta para un jugador
    public String drawCard(int player) {
        if (deck.isEmpty()) return null;
        String card = deck.remove(0);
        playerCards.put(player, card);
        return card;
    }

    // Determinar ganador
    public int determineWinner() {
        int winner = -1;
        String winningCard = null;

        for (Map.Entry<Integer, String> entry : playerCards.entrySet()) {
            String currentCard = entry.getValue();
            if (winningCard == null || isHigher(currentCard, winningCard)) {
                winningCard = currentCard;
                winner = entry.getKey();
            }
        }
        return winner;
    }

    // Comparar cartas
    private boolean isHigher(String card1, String card2) {
        int rank1 = getRankValue(card1.split("_")[0]);
        int rank2 = getRankValue(card2.split("_")[0]);

        if (rank1 != rank2) return rank1 > rank2;

        int suit1 = getSuitPriority(card1.split("_")[2]);
        int suit2 = getSuitPriority(card2.split("_")[2]);
        return suit1 > suit2;
    }

    // Obtener valor de la carta
    private int getRankValue(String rank) {
        return Arrays.asList(RANKS).indexOf(rank);
    }

    // Obtener prioridad del palo
    private int getSuitPriority(String suit) {
        return Arrays.asList("hearts", "clubs", "spades", "diamonds").indexOf(suit);
    }
};