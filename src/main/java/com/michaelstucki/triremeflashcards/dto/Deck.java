package com.michaelstucki.triremeflashcards.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Deck POJO: represents a deck (of cards)
 * @author Michael Stucki
 * @version 1.0
 * @since 2025-09-21
 */
public class Deck {
    private final String name;
    private Map<Integer, Card> cards;

    /**
     * Deck constructor
     * @param title deck title (name)
     */
    public Deck(String title) {
        this.name = title;
        cards = new HashMap<>();
    }

    /**
     * Add card to deck
     * @param cardId card ID
     * @param card instance
     */
    public void addCard(int cardId, Card card) {
        cards.put(cardId, card);
    }

    /**
     * Get card from deck
     * @param id card ID
     * @return Card instance
     */
    public Card getCard(int id) { return cards.get(id); }

    /**
     * Delete deck
     * @param id deck ID
     */
    public void deleteCard(Integer id) {
        cards.remove(id);
    }

    /**
     * Get deck name
     * @return deck name
     */
    public String getName() { return name; }

    /**
     * Get deck cards
     * @return deck cards
     */
    public Map<Integer, Card> getCards() { return cards; }

    /**
     * Set the deck cards
     * @param cards deck cards
     */
    public void setCards(Map<Integer, Card> cards) { this.cards = cards; }

    /**
     * Deck string representation
     * @return deck name
     */
    @Override
    public String toString() { return name; }
}