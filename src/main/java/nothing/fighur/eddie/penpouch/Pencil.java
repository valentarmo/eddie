package nothing.fighur.eddie.penpouch;

import nothing.fighur.eddie.text.Position;
import nothing.fighur.eddie.text.TextCharacter;

public interface Pencil extends Tool {
    /**
     * Insert a character at the given position
     * @param character the character
     * @param position where to insert the character
     */
    void insertCharacter(TextCharacter character, Position position);

    /**
     * Delete a character at the given position
     * @param position where to delete a character
     */
    void deleteCharacter(Position position);

    /**
     * Insert a new line at the given position
     * @param position where to insert a new line
     */
    void insertNewLine(Position position);

    /**
     * Write a title
     * @param title the title
     */
    void writeTitle(String title);

    /**
     * Write a subtitle
     * @param subtitle the subtitle
     */
    void writeSubtitle(String subtitle);

    /**
     * Write a logo
     * @param logo the logo
     */
    void writeLogo(String logo);

    /**
     * Write a warning
     * @param warning the warning
     */
    void writeWarning(String warning);

    /**
     * Write an error
     * @param error the error
     */
    void writeError(String error);

    /**
     * Write a note
     * @param note the note
     */
    void writeNote(String note);

    /**
     * Prompt for input
     * @param prompt prompt message
     * @return the input
     */
    String promptForInput(String prompt);
}
