package com.michaelstucki.triremeflashcards.controller;

import com.michaelstucki.triremeflashcards.dao.Dao;
import com.michaelstucki.triremeflashcards.dao.DaoSQLite;
import com.michaelstucki.triremeflashcards.dto.Card;
import com.michaelstucki.triremeflashcards.dto.Deck;
import com.michaelstucki.triremeflashcards.util.SceneManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import static com.michaelstucki.triremeflashcards.constants.Constants.cardToken;

/**
 * Card UI Controller
 * @author Michael Stucki
 * @version 1.0
 * @since 2025-09-21
 */
public class ControllerCards {
    @FXML
    private Button cancel;
    @FXML
    private Button save;
    @FXML
    private MenuItem add;
    @FXML
    private MenuItem edit;
    @FXML
    private MenuItem delete;
    @FXML
    private ContextMenu itemContextMenu;
    @FXML
    private Label deckName;
    @FXML
    private TextArea front;
    @FXML
    private TextArea back;
    @FXML
    private ListView<String> cardsView;
    private SceneManager sceneManager;
    private Deck deck;
    private String saveMode;
    private int selectedCardId;
    private Dao dao;

    /**
     * Set controller shared resources
     * Called by SceneManager before presenting UI
     * @param sharedDeck Deck set in SceneManger singleton
     */
    public void init(Deck sharedDeck) {
        // Get access to deck selected on Deck's UI
        deck = sharedDeck;
        deckName.setText(deck.getName());
        // Clear UI
        resetListView();
    }

    private void resetListView() {
        front.clear();
        back.clear();
        cardsView.getItems().clear();
        deck.getCards().forEach((key, value) -> cardsView.getItems().add(value.toString()));
    }

    /**
     * Save button onAction
     */
    public void saveClick() {
        front.setEditable(false);
        back.setEditable(false);
        save.setDisable(true);
        cancel.setDisable(true);
        // Only acts if card's front and back have content
        if (!front.getText().trim().isEmpty() && !back.getText().trim().isEmpty()) {
            Card card;
            switch (saveMode) {
                case "add":
                    card = dao.addCard(front.getText(), back.getText(), deck);
                    cardsView.getItems().add(card.toString());
                    break;
                case "edit":
                    card = deck.getCard(selectedCardId);
                    card.setFront(front.getText());
                    card.setBack(back.getText());
                    dao.updateCard(card);
                    resetListView();
                    break;
            }
        }
    }

    /**
     * Cancel button onAction
     */
    public void cancelClick() {
        front.clear();
        back.clear();
        front.setEditable(false);
        back.setEditable(false);
        save.setDisable(true);
        cancel.setDisable(true);
        cardsView.requestFocus();
        resetListView();
    }

    /**
     * Decks hyperlink onAction (goes to Decks UI)
     */
    public void decksClick() {
        sceneManager.showView("/fxml/decks.fxml");
    }

    /**
     * Welcome hyperlink onAction (goes to Welcome UI)
     */
    public void welcomeClick() { sceneManager.showView("/fxml/welcome.fxml"); }

    /**
     * Exit app
     */
    public void exitClick() {
        sceneManager.exit();
    }

    /**
     * Initialize UI widgets and event handlers
     */
    @FXML
    public void initialize() {
        sceneManager = SceneManager.getScreenManager();
        cardsView.setContextMenu(itemContextMenu);
        front.setEditable(false);
        back.setEditable(false);
        save.setDisable(true);
        cancel.setDisable(true);
        dao = DaoSQLite.getDao();

        // Select card
        cardsView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    String[] tokens = newValue.split(cardToken);
                    String frontStr = tokens[1];
                    String backStr = tokens[2];
                    front.setText(frontStr);
                    back.setText(backStr);
                } else {
                    front.clear();
                    back.clear();
                }
            }
        });

        // Add card
        add.setOnAction(event -> {
            front.clear();
            back.clear();
            front.setEditable(true);
            back.setEditable(true);
            front.requestFocus();
            save.setDisable(false);
            cancel.setDisable(false);
            saveMode = "add";
        });

        // Edit card
        edit.setOnAction(event -> {
            String selectedItem = cardsView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String[] tokens = selectedItem.split(cardToken);
                selectedCardId = Integer.parseInt((tokens[0]));
                front.setEditable(true);
                back.setEditable(true);
                front.requestFocus();
                save.setDisable(false);
                cancel.setDisable(false);
                saveMode = "edit";
            }
        });

        // Delete card
        delete.setOnAction(event -> {
            String selectedItem = cardsView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String[] tokens = selectedItem.split(cardToken);
                int id = Integer.parseInt((tokens[0]));
                dao.deleteCard(id);
                deck.deleteCard(id);
                cardsView.getItems().remove(selectedItem);
            }
        });
    }
}