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
}
