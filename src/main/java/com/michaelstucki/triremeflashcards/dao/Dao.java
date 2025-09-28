package com.michaelstucki.triremeflashcards.dao;

import com.michaelstucki.triremeflashcards.dto.Card;
import com.michaelstucki.triremeflashcards.dto.Deck;
import com.michaelstucki.triremeflashcards.dto.User;
import java.util.Map;

/**
 * Dao (Data Access Object) Interface
 * @author Michael Stucki
 * @version 1.0
 * @since 2025-09-21
 */
public interface Dao {
    /**
     * Copy JAR-internal database to location on disk to enable read-write access
     */
    void copyDatabase();

    /**
     * Add user
     * @param userName user name
     * @param password user password
     * @param securityAnswer user answer to security question (used to recover password)
     */
    void addUser(String userName, String password, String securityAnswer);

    /**
     * Get user
     * @param userName user name
     * @return User instance
     */
    User getUser(String userName);

    /**
     * Get current user instance
     * @return User instance
     */
    User getCurrentUser();

    /**
     * Change user password
     * @param userName user name
     * @param password user password
     */
    void changeUserPassword(String userName, String password);

    /**
     * Delete user
     * @param userName user name
     */
    void deleteUser(String userName);

    /**
     * Add deck
     * @param deck Deck instance
     */
    void addDeck(Deck deck);

    /**
     * Get deck
     * @param deckName deck name
     * @return Deck instance
     */
    Deck getDeck(String deckName);

    /**
     * Get all user's decks
     * @return map of decks
     */
    Map<String, Deck> getDecks();

    /**
     * Change deck's name
     * @param oldName current deck name
     * @param newName new deck name
     */
    void changeDeckName(String oldName, String newName);

    /**
     * Delete deck
     * @param deckName deck name
     */
    void deleteDeck(String deckName);

    /**
     * Add card
     * @param front front text
     * @param back back text
     * @param deck associated deck
     * @return Card instance
     */
    Card addCard(String front, String back, Deck deck);

    /**
     * Change card's front and/or back text
     * @param card @{Card} instance
     */
    void updateCard(Card card);

    /**
     * Delete card
     * @param cardId card's ID
     */
    void deleteCard(int cardId);

    /**
     * Clear all decks from decks {@code Map<String, Deck>}
     */
    void clearDecks();
}
