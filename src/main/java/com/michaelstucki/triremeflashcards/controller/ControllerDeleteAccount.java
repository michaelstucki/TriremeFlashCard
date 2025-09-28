package com.michaelstucki.triremeflashcards.controller;

import com.michaelstucki.triremeflashcards.dao.Dao;
import com.michaelstucki.triremeflashcards.dao.DaoSQLite;
import com.michaelstucki.triremeflashcards.dto.User;
import com.michaelstucki.triremeflashcards.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import java.util.Objects;
import java.util.Optional;

/**
 * Delete Account UI Controller
 * @author Michael Stucki
 * @version 1.0
 * @since 2025-09-21
 */
public class ControllerDeleteAccount {
    @FXML
    private TextField username;
    @FXML
    private TextField oldPassword;
    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private ImageView oldEyeIcon;
    @FXML
    private TextField password;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordRetype;
    @FXML
    private ImageView eyeIcon;
    @FXML
    private Label userMessage;
    private boolean passwordVisible = false;
    private SceneManager sceneManager;
    private Dao dao;

    /**
     * Delete Account button onAction
     */
    public void deleteAccountClick() {
        // Validate user inputs (does not allow users to share names)
        User user = dao.getCurrentUser();
        if (password.getText().isEmpty()) {
            userMessage.setText("password not entered!");
        } else if (!password.getText().equals(user.getPassword())) {
            userMessage.setText("invalid password!");
        } else if (passwordRetype.getText().isEmpty()) {
            userMessage.setText("password not entered!");
        } else if (!passwordRetype.getText().equals(password.getText())) {
            userMessage.setText("passwords do not match!");
        } else {
            // Warn user
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/images/icons8-exclamation-mark-64.png")));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(48);
            imageView.setFitWidth(48);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Do you really want to delete your account?");
            alert.setGraphic(imageView);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                dao.deleteUser(user.getUsername());
                clearInputs();
                sceneManager.showView("/fxml/home.fxml");
            } else {
                clearInputs();
            }
        }
    }

    /**
     * Toggle password visibility
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

    private void clearInputs() {
        password.setText("");
        passwordRetype.setText("");
        userMessage.setText("");
        userMessage.setTextFill(Color.RED);
    }

    /**
     * Welcome hyperlink onAction (goes to Welcome UI)
     */
    public void welcomeClick() {
        clearInputs();
        sceneManager.showView("/fxml/welcome.fxml");
    }

    /**
     * Exit app
     */
    public void exitClick() { sceneManager.exit(); }

    /**
     * Initialize UI widgets and event handlers
     */
    @FXML
    public void initialize() {
        sceneManager = SceneManager.getScreenManager();
        // Get reference to DaoSQLite singleton (used to update model & database)
        dao = DaoSQLite.getDao();

        // Bidirectionally bind password inputs (so a change in one is reflected in the other)
        password.textProperty().bindBidirectional(passwordField.textProperty());

        password.focusedProperty().addListener((observable, oldValue, newValue) -> {
            userMessage.setText("");
        });

        passwordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            userMessage.setText("");
        });

        passwordRetype.focusedProperty().addListener((observable, oldValue, newValue) -> {
            userMessage.setText("");
        });
    }
}
