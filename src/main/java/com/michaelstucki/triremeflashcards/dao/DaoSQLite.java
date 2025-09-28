package com.michaelstucki.triremeflashcards.dao;

import com.michaelstucki.triremeflashcards.dto.Card;
import com.michaelstucki.triremeflashcards.dto.Deck;
import com.michaelstucki.triremeflashcards.dto.User;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.michaelstucki.triremeflashcards.constants.Constants.*;

/**
 * Dao (Data Access Object) SQLite implementation (a singleton shared by controllers)
 * @author Michael Stucki
 * @version 1.0
 * @since 2025-09-21
 */
public class DaoSQLite implements Dao {
    private static DaoSQLite DAO;
    private final Map<String, Deck> decks;
    private User user;
    private String url;

    private DaoSQLite() {
        decks = new HashMap<>();

        // Set URL based on whether running inside a JAR or not
        if (!isRunningInJar()) {
            url = "jdbc:sqlite:" + databasePathInternal + "?foreign_keys=true";
            createTables();
        } else {
            String userName = getUsersName();
            url = "jdbc:sqlite:" + databasePathExternal.replace("userName", userName) + "?foreign_keys=true";
        }
    }

    // Determine if running inside a JAR
    private boolean isRunningInJar() {
        boolean result = false;
        try {
            CodeSource codeSource = getClass().getProtectionDomain().getCodeSource();
            if (codeSource != null) {
                URL location = codeSource.getLocation();
                result = location.toExternalForm().contains(".jar");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    /**
     * Get single instance of DaoSQLite class
     * @return DaoSQLite instance
     */
    public static synchronized DaoSQLite getDao() {
        if (DAO == null) DAO = new DaoSQLite();
        return DAO;
    }

    /**
     * Clear all decks from decks {@code Map<String, Deck>}
     */
    @Override
    public void clearDecks() {
        decks.clear();
    }

    /**
     * Copy JAR-internal database to location on disk to enable read-write access
     */
    public void copyDatabase() {
        if (isRunningInJar()) {
            String userName = getUsersName();
            Path destination = Paths.get(databasePathExternal.replace("userName", userName));
            if (!Files.exists(destination)) {
                try (InputStream inputStream = getClass().getResourceAsStream(databasePathJAR);
                     FileOutputStream outputStream = new FileOutputStream(destination.toString())) {
                    byte[] buffer = new byte[4096]; // Or a suitable buffer size
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                } catch (IOException | NullPointerException e) {
                    throw new RuntimeException(e);
                }
                url = "jdbc:sqlite:" + destination + "?foreign_keys=true";
            }
        }
    }

    // MacOS-specific: find the user's macOS account name (to find where to copy the JAR-internal database there)
    private String getUsersName() {
        String userName = null;
        try {
            // Get specific user's name (macOS only)
            ProcessBuilder processBuilder = new ProcessBuilder("whoami");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            userName = reader.readLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return userName;
    }

    // Create the tables: users, decks, cards
    private void createTables() {
        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement()) {
            // Create users table
            stmt.execute("CREATE TABLE IF NOT EXISTS " + usersTable +
                    " (user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "userName VARCHAR(20) UNIQUE, password VARCHAR(20), " +
                    " securityAnswer VARCHAR(20));");

            // Create decks table
            stmt.execute("CREATE TABLE IF NOT EXISTS " + decksTable +
                    " (deck_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name VARCHAR(40), user_id INTEGER, " +
                    "FOREIGN KEY (user_id) REFERENCES " + usersTable + " (user_id) " +
                    "ON DELETE CASCADE);");

            // Create cards table
            stmt.execute("CREATE TABLE IF NOT EXISTS " + cardsTable +
                    " (card_id INTEGER PRIMARY KEY, " +
                    "front TEXT, back TEXT, leitner_box INTEGER, leitner_target INTEGER, " +
                    "creation_date TEXT, reviewed_date TEXT, " +
                    "due_date TEXT, deck_id INTEGER, " +
                    "number_reviews INTEGER, number_passes INTEGER, " +
                    "FOREIGN KEY (deck_id) REFERENCES " + decksTable + " (deck_id) " +
                    "ON DELETE CASCADE);");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**
     * Add user
     * @param userName user name
     * @param password user password
     * @param securityAnswer user answer to security question (used to recover password)
     */
    @Override
    public void addUser(String userName, String password, String securityAnswer) {
        user = new User(userName, password, securityAnswer);
        String command = "INSERT INTO " + usersTable + " (userName, password, securityAnswer) VALUES ('" +
                userName + "', '" + password + "', '" + securityAnswer + "');";

        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(command);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**
     * Get user
     * @param userName user name
     * @return User instance
     */
    @Override
    public User getUser(String userName) {
        user = null;
        String command = "SELECT * FROM " + usersTable + " WHERE userName = '" + userName + "';";

        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(command);
            while (rs.next()) {
                userName = rs.getString("username");
                String password = rs.getString("password");
                String securityAnswer = rs.getString("securityAnswer");
                user = new User(userName, password, securityAnswer);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return user;
    }

    /**
     * Get current user instance
     * @return User instance
     */
    @Override
    public User getCurrentUser() { return user; }

    /**
     * Change user password
     * @param userName user name
     * @param password user password
     */
    @Override
    public void changeUserPassword(String userName, String password) {
        String command = "UPDATE " + usersTable + " SET password = " + "'" + password + "'" +
                " WHERE username = '" + userName + "';";

        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(command);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**
     * Delete user
     * @param userName user name
     */
    @Override
    public void deleteUser(String userName) {
        String command = "DELETE FROM " + usersTable + " WHERE username = " + "'" + userName + "';";

        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(command);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**
     * Add deck
     * @param deck Deck instance
     */
    @Override
    public void addDeck(Deck deck) {
        // Update model
        String deckName = deck.getName();
        decks.put(deckName, deck);
        // Update database
        String userName = user.getUsername();
        String command =  "INSERT INTO decks (name, user_id) VALUES (" + "'" + deckName + "'," +
                "(SELECT user_id FROM users WHERE username = " +  "'" + userName + "'));";

        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(command);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**
     * Get all user's decks
     * @return map of decks
     */
    @Override
    public Map<String, Deck> getDecks() {
        // Get all user's decks from database
        String userName = user.getUsername();
        String command = "SELECT * FROM decks d " +
                "JOIN users u on d.user_id = u.user_id " +
                "WHERE username = '" + userName + "';";

        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(command);
            // Instantiate a deck for each database deck
            while (rs.next()) {
                String deckName = rs.getString("name");
                Deck deck = new Deck(deckName);
                decks.put(deckName, deck);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        // Get all cards for each user's deck
        for (Deck deck : decks.values()) {
            command = "SELECT * FROM cards c " +
                    "JOIN decks d ON d.deck_id = c.deck_id " +
                    "JOIN users u ON u.user_id = d.user_id " +
                    "WHERE u.username = '" + userName + "' AND d.name = '" + deck.getName() + "';";

            try (Connection connection = DriverManager.getConnection(url);
                 Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(command);
                while(rs.next()) {
                    int cardId = rs.getInt("card_id");
                    String front = rs.getString("front");
                    String back = rs.getString("back");
                    int leitnerBox = rs.getInt("leitner_box");
                    int leitnerTarget = rs.getInt("leitner_target");
                    String creationDate = rs.getString("creation_date");
                    String reviewedDate = rs.getString("reviewed_date");
                    String dueDate = rs.getString("due_date");
                    int numberOfReviews = rs.getInt("number_reviews");
                    int numberOfPasses = rs.getInt("number_passes");
                    Card card = new Card(cardId, front, back, creationDate, reviewedDate, dueDate, leitnerBox,
                            leitnerTarget, numberOfReviews, numberOfPasses);
                    deck.addCard(cardId, card);
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
        return decks;
    }

    /**
     * Get deck
     * @param deckName deck name
     * @return Deck instance
     */
    @Override
    public Deck getDeck(String deckName) { return decks.get(deckName); }

    /**
     * Change deck's name
     * @param oldName current deck name
     * @param newName new deck name
     */
    @Override
    public void changeDeckName(String oldName, String newName) {
        // Update model
        // decks is a map, its key is the deck's title
        // so, to change the deck's title, the deck must be replaced
        Deck oldDeck = decks.remove(oldName);
        Deck newDeck = new Deck(newName);
        newDeck.setCards(oldDeck.getCards());
        decks.put(newName, newDeck);
        // Update database
        String userName = user.getUsername();
        String command = "UPDATE decks SET name = '" + newName + "' " +
                "WHERE user_id = (SELECT user_id FROM users WHERE username = '" + userName +
                "') AND name = '" + oldName + "';";

        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(command);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**
     * Delete deck
     * @param deckName deck name
     */
    @Override
    public void deleteDeck(String deckName) {
        // Update model
        decks.remove(deckName);
        // Update database
        String userName = user.getUsername();
        String command = "DELETE FROM decks WHERE user_id = (SELECT user_id from users " +
                "WHERE username = '" + userName + "') AND name = '" + deckName + "';";

        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(command);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**
     * Add card
     * @param front front text
     * @param back back text
     * @param deck associated deck
     * @return Card instance
     */
    @Override
    public Card addCard(String front, String back, Deck deck) {
        String today = LocalDate.now().toString();
        int card_id = -1;
        String userName = user.getUsername();
        String deckName = deck.getName();
        String command = "INSERT INTO cards (front, back, leitner_box, leitner_target, creation_date, reviewed_date, due_date, " +
                "deck_id, number_reviews, number_passes) " +
                "VALUES ('" + front + "', '" + back + "', 0, 0, '" + today + "', '" + today + "', '" + today + "'," +
                "(SELECT deck_id FROM decks d JOIN users u ON d.user_id = u.user_id " +
                "WHERE u.username = '" + userName + "' AND d.name = '" + deckName + "'), " +
                "0, 0);";

        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(command);
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            card_id = generatedKeys.getInt(1);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        // Now get the card just added so it can be instanced and added to the data model
        command = "SELECT * FROM cards WHERE card_id = '" + card_id + "';";

        Card card = null;
        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(command);
            while(rs.next()) {
                int cardId = rs.getInt("card_id");
                front = rs.getString("front");
                back = rs.getString("back");
                int leitnerBox = rs.getInt("leitner_box");
                int leitnerTarget = rs.getInt("leitner_target");
                String creationDate = rs.getString("creation_date");
                String reviewedDate = rs.getString("reviewed_date");
                String dueDate = rs.getString("due_date");
                int numberOfReviews = rs.getInt("number_reviews");
                int numberOfPasses = rs.getInt("number_passes");
                card = new Card(cardId, front, back, creationDate, reviewedDate, dueDate, leitnerBox,
                        leitnerTarget, numberOfReviews, numberOfPasses);
                deck.addCard(cardId, card);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return card;
    }

    /**
     * Change card's front and/or back text
     * @param card @{Card} instance
     */
    @Override
    public void updateCard(Card card) {
        int card_id = card.getId();
        String front = card.getFront();
        String back = card.getBack();
        int leitner_box = card.getLeitnerBox();
        int leitner_target = card.getLeitnerTarget();
        String reviewed_date = card.getReviewedDate();
        String due_date = card.getDueDate();
        int number_reviews = card.getNumberOfReviews();
        int number_passes = card.getNumberOfPasses();

        String command = "UPDATE cards SET " +
                "front = " + "'" + front + "', " +
                "back = " + "'" + back + "', " +
                "leitner_box = " + "'" + leitner_box + "', " +
                "leitner_target = " + "'" + leitner_target + "', " +
                "reviewed_date = " + "'" + reviewed_date + "', " +
                "due_date = " + "'" + due_date + "', " +
                "number_reviews = " + "'" + number_reviews + "', " +
                "number_passes = " + "'" + number_passes + "' " +
                "WHERE card_id = '" + card_id + "';";

        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(command);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**
     * Delete card
     * @param cardId card's ID
     */
    @Override
    public void deleteCard(int cardId) {
        String command = "DELETE FROM cards WHERE card_id = '" + cardId + "';";

        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(command);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}