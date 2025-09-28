package com.michaelstucki.triremeflashcards.dto;

/**
 * User POJO: represents a user
 * @author Michael Stucki
 * @version 1.0
 * @since 2025-09-21
 */
public class User {
    private final String username;
    private final String password;
    private final String securityAnswer;

    /**
     * User constructor
     * @param username user name
     * @param password user password
     * @param securityAnswer user answer to security question
     */
    public User(String username, String password, String securityAnswer) {
        this.username = username;
        this.password = password;
        this.securityAnswer = securityAnswer;
    }

    /**
     * Get username
     * @return user name
     */
    public String getUsername() { return username; }

    /**
     * Get user password
     * @return password
     */
    public String getPassword() { return password; }

    /**
     * Get user security question's answer
     * @return security answer
     */
    public String getSecurityAnswer() { return securityAnswer; }
}
