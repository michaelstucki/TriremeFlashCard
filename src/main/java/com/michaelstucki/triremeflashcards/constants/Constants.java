package com.michaelstucki.triremeflashcards.constants;

/**
 * Defines constants used throughout application
 * @author Michael Stucki
 * @version 1.0
 * @since 2025-09-21
 */

public final class Constants {
    private static final double ASPECT_RATIO = 1.1;
    public static final double height = 750;
    public static final double width = height * ASPECT_RATIO;
    public static final String[] fonts = {"HerculanumLTProRoman.TTF", "EBGaramond-Regular.ttf",
            "EBGaramond-Italic.ttf", "EBGaramond-Bold.ttf"};
    public static final String[] fxmls = {"delete_account", "welcome", "drills", "cards", "decks", "change_password",
            "forgot_password", "create_account", "home"};
    public static final String cardToken = "::";
    public static final String databasePathExternal = "/Users/userName/.flashcards.db";
    public static final String databasePathInternal = "src/main/resources/database/flashcards.db";
    public static final String databasePathJAR = "/database/flashcards.db";
    public static final String usersTable = "users";
    public static final String decksTable = "decks";
    public static final String cardsTable = "cards";
    private Constants() {}
}
