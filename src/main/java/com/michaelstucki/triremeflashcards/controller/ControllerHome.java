package com.michaelstucki.triremeflashcards.controller;

import com.michaelstucki.triremeflashcards.dao.Dao;
import com.michaelstucki.triremeflashcards.dao.DaoSQLite;
import com.michaelstucki.triremeflashcards.dto.User;
import com.michaelstucki.triremeflashcards.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Objects;

/**
 * Home/Login UI Controller
 * @author Michael Stucki
 * @version 1.0
 * @since 2025-09-21
 */
public class ControllerHome {
    @FXML
    private Hyperlink exit;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ImageView eyeIcon;
    @FXML
    private Label userMessage;
    private boolean passwordVisible = false;
    private SceneManager sceneManager;
    private Dao dao;

    /**
     * Set controller shared resources
     * This is the first UI presented upon starting app.
     * Called by SceneManager before presenting UI
     */
    public void init() {
        // Initialize decks map by clearing it
        dao.clearDecks();
        // Copy the JAR-internal database resource to external location on disk,
        // since JAR-resources are read-only and the app requires read-write.
        dao.copyDatabase();
    }

    /**
     * Sign In button onAction
     */
    public void signInClick() {
        if (username.getText().isEmpty()) {
            userMessage.setText("username not entered!");
        } else {
            User user = dao.getUser(username.getText());
            if (user == null) {
                userMessage.setText("unrecognized username!");
            } else if (!password.getText().equals(user.getPassword())) {
                userMessage.setText("invalid password!");
            } else {
                clearInputs();
                sceneManager.showView("/fxml/welcome.fxml");
            }
        }
    }

    /**
     * Toggle old password visibility
     */
    public void togglePasswordVisibility() {
        if (passwordVisible) {
            passwordField.setText(password.getText()); // Sync text
            passwordField.setVisible(true);
            password.setVisible(false);
            eyeIcon.setImage(new Image(Objects.requireNonNull(getClass().
                    getResourceAsStream("/images/eye_closed.jpg"))));
        } else {
            password.setText(passwordField.getText()); // Sync text
            passwordField.setVisible(false);
            password.setVisible(true);
            eyeIcon.setImage(new Image(Objects.requireNonNull(getClass().
                    getResourceAsStream("/images/eye_open.jpg"))));
        }
        passwordVisible = !passwordVisible;
    }

    /**
     * Create Account button onAction (goes to Create Account UI)
     */
    public void createAccountClick() {
        clearInputs();
        sceneManager.showView("/fxml/create_account.fxml");
    }

    /**
     * Forgot Password hyperlink onAction (goes to Forgot Password UI)
     */
    public void forgotPasswordClick() {
        clearInputs();
        sceneManager.showView("/fxml/forgot_password.fxml");
    }

    private void clearInputs() {
        username.setText("");
        password.setText("");
        userMessage.setText("");
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

        password.textProperty().bindBidirectional(passwordField.textProperty());
        username.focusedProperty().addListener((observable, oldValue, newValue) -> {
            userMessage.setText("");
        });

        password.focusedProperty().addListener((observable, oldValue, newValue) -> {
            userMessage.setText("");
        });

        passwordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            userMessage.setText("");
        });
    }
}