package nothing.fighur.eddie.penpouch;

import nothing.fighur.eddie.text.Position;

public interface Hand extends ToolObserver {
    /**
     * Move the hand one unit to the left.
     * If it's at the start of the line, move to the previous line (if there is one).
     */
    void moveLeft();

    /**
     * Move the hand one unit to the right.
     * If it's at the end of the line, move to the next line (if there is one).
     */
    void moveRight();

    /**
     * Move the hand one unit up.
     */
    void moveUp();

    /**
     * Move the hand one unit down.
     */
    void moveDown();

    /**
     * Move the hand to the start of the next word (if there is one).
     */
    void moveToStartOfNextWord();

    /**
     * Move the hand to the end of the next word (if there is one).
     */
    void moveToEndOfNextWord();

    /**
     * Move the hand to the start of the previous word (if there is one).
     */
    void moveToStartOfPreviousWord();

    /**
     * Reset the hand to its initial position
     */
    void reset();

    /**
     * Get the hand's current position.
     */
    Position getPosition();
}
