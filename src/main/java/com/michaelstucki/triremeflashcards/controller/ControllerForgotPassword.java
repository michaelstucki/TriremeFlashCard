
package com.michaelstucki.triremeflashcards.controller;

import com.michaelstucki.triremeflashcards.dao.Dao;
import com.michaelstucki.triremeflashcards.dao.DaoSQLite;
import com.michaelstucki.triremeflashcards.dto.User;
import com.michaelstucki.triremeflashcards.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 * Forgot Password UI Controller
 * @author Michael Stucki
 * @version 1.0
 * @since 2025-09-21
 */
public class ControllerForgotPassword {
    @FXML
    private TextField username;
    @FXML
    private Label userMessage;
    @FXML
    private TextField securityAnswer;
    private SceneManager sceneManager;
    private Dao dao;

    /**
     * Recover Password button onAction
     */
    public void recoverPasswordClick() {
        // Validate user inputs
        if (username.getText().isEmpty()) {
            userMessage.setText("username not entered!");
        } else {
            User user = dao.getUser(username.getText());
            if (user == null) {
                userMessage.setText("unrecognized username!");
            } else if (securityAnswer.getText().isEmpty()) {
                userMessage.setText("security answer not entered!");
            } else if (!securityAnswer.getText().equals(user.getSecurityAnswer())) {
                userMessage.setText("security answer is incorrect!");
            } else {
                userMessage.setTextFill(Color.GREEN);
                userMessage.setText("password: " + user.getPassword());
            }
        }
    }

    /**
     * Login button onAction
     */
    public void loginClick() {
        clearInputs();
        sceneManager.showView("/fxml/home.fxml");
    }

    private void clearInputs() {
        username.setText("");
        securityAnswer.setText("");
        userMessage.setText("");
        userMessage.setTextFill(Color.RED);
    }

    /**
     * Exit app
     */
    public void exitClick() { sceneManager.exit(); }

    /**
     * Initialize UI widgets and event handlers
     */
    public void initialize() {
        sceneManager = SceneManager.getScreenManager();
        // Get reference to DaoSQLite singleton (used to update model & database)
        dao = DaoSQLite.getDao();

        username.focusedProperty().addListener((observable, oldValue, newValue) -> {
            userMessage.setText("");
        });

        securityAnswer.focusedProperty().addListener((observable, oldValue, newValue) -> {
            userMessage.setText("");
        });
    }
}
