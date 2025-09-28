package com.michaelstucki.triremeflashcards.controller;

import com.michaelstucki.triremeflashcards.dao.Dao;
import com.michaelstucki.triremeflashcards.dao.DaoSQLite;
import com.michaelstucki.triremeflashcards.dto.User;
import com.michaelstucki.triremeflashcards.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import java.util.Objects;

/**
 * Create Account UI Controller
 * @author Michael Stucki
 * @version 1.0
 * @since 2025-09-21
 */
public class ControllerCreateAccount {
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
    @FXML
    private PasswordField passwordRetype;
    @FXML
    private TextField securityAnswer;
    private boolean passwordVisible = false;
    private SceneManager sceneManager;
    private Dao dao;

    /**
     * Create Account button onAction
     */
    public void createAccountClick() {
        // Validate user inputs
        if (username.getText().isEmpty()) {
            userMessage.setText("username not entered!");
        } else {
            User user = dao.getUser(username.getText());
            if (user != null) { // username already taken
                userMessage.setText("username is taken!");
            } else if (password.getText().isEmpty()) {
                userMessage.setText("password not entered!");
            } else if (!passwordRetype.getText().equals(password.getText())) {
                userMessage.setText("passwords do not match!");
            } else if (securityAnswer.getText().isEmpty()) {
                userMessage.setText("security question not answered!");
            } else {
                // entries are all valid, so add user to database
                dao.addUser(username.getText(), password.getText(), securityAnswer.getText());
                userMessage.setTextFill(Color.GREEN);
                userMessage.setText("account created!");
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
     * Home hyperlink onAction
     */
    public void homeClick() {
        clearInputs();
        sceneManager.showView("/fxml/home.fxml");
    }

    private void clearInputs() {
        username.setText("");
        password.setText("");
        passwordRetype.setText("");
        securityAnswer.setText("");
        userMessage.setText("");
        userMessage.setTextFill(Color.RED);
    }

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

        passwordRetype.focusedProperty().addListener((observable, oldValue, newValue) -> {
            userMessage.setText("");
        });

        securityAnswer.focusedProperty().addListener((observable, oldValue, newValue) -> {
            userMessage.setText("");
        });
    }
}
