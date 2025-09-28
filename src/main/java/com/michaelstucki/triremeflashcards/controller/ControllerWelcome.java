package com.michaelstucki.triremeflashcards.controller;

import com.michaelstucki.triremeflashcards.util.SceneManager;

/**
 * Welcome UI Controller
 * @author Michael Stucki
 * @version 1.0
 * @since 2025-09-21
 */
public class ControllerWelcome {
    private SceneManager sceneManager;

    /**
     * Decks hyperlink onAction (goes to Decks UI)
     */
    public void decksClick() { sceneManager.showView("/fxml/decks.fxml"); }

    /**
     * Change Password hyperlink onAction (goes to Change Password UI)
     */
    public void changePasswordClick() { sceneManager.showView("/fxml/change_password.fxml"); }

    /**
     * Delete Account hyperlink onAction (goes to Delete Account UI)
     */
    public void deleteAccountClick() { sceneManager.showView("/fxml/delete_account.fxml"); }

    /**
     * Logout hyperlink onAction (goes to Home/Login UI)
     */
    public void logoutClick() { sceneManager.showView("/fxml/home.fxml"); }

    /**
     * Exit app
     */
    public void exitClick() {
        sceneManager.exit();
    }

    /**
     * Initialize UI by getting SceneManager singleton used to change between UIs
     */
    public void initialize() { sceneManager = SceneManager.getScreenManager();  }
}