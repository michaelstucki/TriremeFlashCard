package com.michaelstucki.triremeflashcards.util;

import com.michaelstucki.triremeflashcards.Main;
import static com.michaelstucki.triremeflashcards.constants.Constants.*;
import com.michaelstucki.triremeflashcards.controller.ControllerCards;
import com.michaelstucki.triremeflashcards.controller.ControllerDecks;
import com.michaelstucki.triremeflashcards.controller.ControllerDrills;
import com.michaelstucki.triremeflashcards.controller.ControllerHome;
import com.michaelstucki.triremeflashcards.dto.Deck;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * SceneManager Singleton to create scenes and present them
 * It runs FXMLLoader once for each UI and caches their associated scenes and
 * FXMLLoader references for select UIs to give access to their controller references.
 * @author Michael Stucki
 * @version 1.0
 * @since 2025-09-21
 */
// Marked final to prevent extension
public final class SceneManager {
    private Stage stage;

    // Cache of UI scenes
    private final Map<String, Scene> sceneCache = new HashMap<>();
    // Cache of UI FXMLLoaders
    private final Map<String, FXMLLoader> loaderCache = new HashMap<>();
    private final static SceneManager SCENE_MANAGER = new SceneManager();
    // The deck of interest selected in Decks UI and shared with Cards UI (and elsewhere as needed)
    private Deck sharedDeck = new Deck("");

    // Set to private to prevent instancing from outside this singleton class
    private SceneManager() {}

    /**
     * Get single reference to this class
     * @return @{SceneManager} singleton instance
     */
    public static SceneManager getScreenManager() { return SCENE_MANAGER; }

    /**
     * Set the JavaFX stage
     * @param stage Stage instance
     */
    public void setStage(Stage stage) { this.stage = stage; }

    /**
     * Set shared deck reference
     * @param sharedDeck Deck instance shared by select UI controllers
     */
    public void setSharedDeck(Deck sharedDeck) { this.sharedDeck = sharedDeck; }

    /**
     * Create, cache, and present UI scenes and FXMLLoaders (to make app responsive to scene changes)
     * @param fxmlPath full path to .fxml files defined in @{Constants} class and passed in here by main
     */
    public void showView(String fxmlPath) {
        try {
            if (sceneCache.containsKey(fxmlPath)) {
                FXMLLoader loader = loaderCache.get(fxmlPath);
                // invoke controller init() only if FXMLLoader has been run
                if (fxmlPath.contains("cards")) {
                    ControllerCards controller = loader.getController();
                    controller.init(sharedDeck);
                } else if (fxmlPath.contains("drills")) {
                    ControllerDrills controller = loader.getController();
                    controller.init(sharedDeck);
                } else if (fxmlPath.contains("decks")) {
                    ControllerDecks controller = loader.getController();
                    controller.init();
                } else if (fxmlPath.contains("home")) {
                    ControllerHome controller = loader.getController();
                    controller.init();
                }
                Scene scene = sceneCache.get(fxmlPath);
                stage.setScene(scene);
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                Scene scene = new Scene(loader.load(), width, height);
                sceneCache.put(fxmlPath, scene);
                loaderCache.put(fxmlPath, loader);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Exit app
     */
    public void exit() { stage.close(); }
}

