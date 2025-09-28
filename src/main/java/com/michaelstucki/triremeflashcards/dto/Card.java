package com.michaelstucki.triremeflashcards.dto;

import static com.michaelstucki.triremeflashcards.constants.Constants.cardToken;

/**
 * Card POJO: represents a flashcard
 * @author Michael Stucki
 * @version 1.0
 * @since 2025-09-21
 */
public class Card {
    private final int cardId;
    private String front;
    private String back;
    private final String creationDate;
    private String reviewedDate;
    private String dueDate;
    private int leitnerBox;
    private int leitnerTarget;
    private int numberOfReviews;
    private int numberOfPasses;

    /**
     * Card Constructor
     * @param cardId ID (auto-assigned)
     * @param front front text
     * @param back back texts
     * @param creationDate creation date
     * @param reviewedDate last reviewed date
     * @param dueDate next review due dater
     * @param leitnerBox Leitner box
     * @param leitnerTarget Target Leitner box
     * @param numberOfReviews number of times it's been reviewed
     * @param numberOfPasses number of times it's been passed
     */
    public Card(int cardId, String front, String back, String creationDate, String reviewedDate,
                String dueDate, int leitnerBox, int leitnerTarget, int numberOfReviews, int numberOfPasses) {
        this.cardId = cardId;
        this.front = front;
        this.back = back;
        this.creationDate = creationDate;
        this.reviewedDate = reviewedDate;
        this.dueDate = dueDate;
        this.leitnerBox = leitnerBox;
        this.leitnerTarget = leitnerTarget;
        this.numberOfReviews = numberOfReviews;
        this.numberOfPasses = numberOfPasses;
    }

    /**
     * Get Card ID
     * @return card ID
     */
    public int getId() {
        return cardId;
    }

    /**
     * Get card front text
     * @return card front text
     */
    public String getFront() {
        return front;
    }

    /**
     * Set card front text
     * @param front new front text
     */
    public void setFront(String front) {
        this.front = front;
    }

    /**
     * Get card back text
     * @return card back text
     */
    public String getBack() {
        return back;
    }

    /**
     * Set card back text
     * @param back new back text
     */
    public void setBack(String back) {
        this.back = back;
    }

    /**
     * Get card reviewed date
     * @return card reviewed date
     */
    public String getReviewedDate() { return reviewedDate; }

    /**
     * Set card reviewed date
     * @param reviewedDate card reviewed date
     */
    public void setReviewedDate(String reviewedDate) {
        this.reviewedDate = reviewedDate;
    }

    /**
     * Get card due date
     * @return card due date
     */
    public String getDueDate() {
        return dueDate;
    }

    /**
     * Set card due date
     * @param dueDate card due date
     */
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Get Leitner box
     * @return Leitner box
     */
    public int getLeitnerBox() {
        return leitnerBox;
    }

    /**
     * Set Leitner box
     * @param leitnerBox Leitner box
     */
    public void setLeitnerBox(int leitnerBox) {
        this.leitnerBox = leitnerBox;
    }

    /**
     * Get Leitner Target box
     * @return Leitner Target box
     */
    public int getLeitnerTarget() {
        return leitnerTarget;
    }

    /**
     * Set Leitner Target box
     * @param leitnerTarget Leitner Target box
     */
    public void setLeitnerTarget(int leitnerTarget) {
        this.leitnerTarget = leitnerTarget;
    }

    /**
     * Get number of reviews
     * @return number of reviews
     */
    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    /**
     * Set number of reviews
     * @param numberOfReviews number of reviews
     */
    public void setNumberOfReviews(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }

    /**
     * Get number of passes
     * @return number of passes
     */
    public int getNumberOfPasses() {
        return numberOfPasses;
    }

    /**
     * Set number of passes
     * @param numberOfPasses number of passes
     */
    public void setNumberOfPasses(int numberOfPasses) {
        this.numberOfPasses = numberOfPasses;
    }

    /**
     * Get card string representation
     * @return card ID, front, back
     */
    @Override
    public String toString() { return cardId + cardToken + front + cardToken + back; }
}