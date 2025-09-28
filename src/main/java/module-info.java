module com.michaelstucki.triremeflashcards {
        requires javafx.controls;
        requires javafx.fxml;
        requires java.desktop;
        requires java.sql;

        opens com.michaelstucki.triremeflashcards to javafx.fxml;
        exports com.michaelstucki.triremeflashcards;
        exports com.michaelstucki.triremeflashcards.controller;
        opens com.michaelstucki.triremeflashcards.controller to javafx.fxml;
        exports com.michaelstucki.triremeflashcards.util;
        opens com.michaelstucki.triremeflashcards.util to javafx.fxml;
}