package com.michaelstucki.triremeflashcards;

import com.michaelstucki.triremeflashcards.constants.Constants;
import com.michaelstucki.triremeflashcards.util.SceneManager;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Main class
 * @author Michael Stucki
 * @version 1.0
 * @since 2025-09-21
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Enable use of fonts
        for (String font : Constants.fonts) Font.loadFont(getClass().getResourceAsStream("/fonts/" + font), 10);

        // Get reference to SceneManager singleton to manager changing scenes
        SceneManager screenManager = SceneManager.getScreenManager();
        screenManager.setStage(stage);

        // Create and cache UI scenes and FXMLLoaders
        for (String fxml: Constants.fxmls) screenManager.showView("/fxml/" + fxml + ".fxml");
        screenManager.showView("/fxml/home.fxml");

        // Make app fixed in size
        stage.setResizable(false);
        stage.show();

        // Create on-exit handler
        stage.setOnCloseRequest(event -> {
            screenManager.exit();
        });
    }

    /**
     * main method
     * @param args app arguments (there are none)
     */
    public static void main(String[] args) {
        launch();
    }
}
