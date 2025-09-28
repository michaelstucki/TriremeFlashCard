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
 * Change Password UI Controller
 * @author Michael Stucki
 * @version 1.0
 * @since 2025-09-21
 */
public class ControllerChangePassword {
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
    private boolean oldPasswordVisible = false;
    private SceneManager sceneManager;
    private Dao dao;

    /**
     * Change Password Button's onAction
     */
    public void changePasswordClick() {
        // Validate user inputs
        if (username.getText().isEmpty()) {
            userMessage.setText("username not entered!");
        } else {
            User user = dao.getUser(username.getText());
            if (user == null) {
                userMessage.setText("unrecognized username!");
            } else if (oldPassword.getText().isEmpty()) {
                userMessage.setText("old password not entered!");
            } else if (!oldPassword.getText().equals(user.getPassword())) {
                userMessage.setText("invalid old password!");
            } else if (password.getText().isEmpty()) {
                userMessage.setText("new password not entered!");
            } else if (!passwordRetype.getText().equals(password.getText())) {
                userMessage.setText("new passwords do not match!");
            } else {
                dao.changeUserPassword(user.getUsername(), password.getText());
                userMessage.setTextFill(Color.GREEN);
                userMessage.setText("password changed!");
                clearInputs();
                sceneManager.showView("/fxml/welcome.fxml");
            }
        }
    }

    /**
     * Toggle old password visibility
     */
    public void OldTogglePasswordVisibility() {
        if (oldPasswordVisible) {
            oldPasswordField.setText(oldPassword.getText()); // Sync text
            oldPasswordField.setVisible(true);
            oldPassword.setVisible(false);
            eyeIcon.setImage(new Image(Objects.requireNonNull(getClass().
                    getResourceAsStream("/images/eye_closed.jpg"))));
        } else {
            oldPassword.setText(oldPasswordField.getText()); // Sync text
            oldPasswordField.setVisible(false);
            oldPassword.setVisible(true);
            oldEyeIcon.setImage(new Image(Objects.requireNonNull(getClass().
                    getResourceAsStream("/images/eye_open.jpg"))));
        }
        oldPasswordVisible = !oldPasswordVisible;
    }

    /**
     * Toggle new password visibility
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
     * Welcome hyperlink onAction (goes to Welcome UI)
     */
    public void welcomeClick() {
        clearInputs();
        sceneManager.showView("/fxml/welcome.fxml");
    }

    private void clearInputs() {
        username.setText("");
        oldPassword.setText("");
        password.setText("");
        passwordRetype.setText("");
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

        username.focusedProperty().addListener((observable, oldValue, newValue) -> {
            userMessage.setText("");
        });

        // Bidirectionally bind old password inputs (so a change in one is reflected in the other)
        oldPassword.textProperty().bindBidirectional(oldPasswordField.textProperty());

        oldPassword.focusedProperty().addListener((observable, oldValue, newValue) -> {
            userMessage.setText("");
        });

        oldPasswordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            userMessage.setText("");
        });

        // Bidirectionally bind new password inputs (so a change in one is reflected in the other)
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
