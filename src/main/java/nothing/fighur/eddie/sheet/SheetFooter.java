package nothing.fighur.eddie.sheet;

public interface SheetFooter {
    /**
     * Put a warning message
     * @param warning the message
     */
    void putWarning(String warning);

    /**
     *  Put an error message
     * @param error the message
     */
    void putError(String error);

    /**
     * Put a message
     * @param message the message
     */
    void putMessage(String message);

    /**
     * Promp the user for input
     * @param promptMessage message to show to the user
     * @return the user's input
     */
    String promptForInput(String promptMessage);
}
