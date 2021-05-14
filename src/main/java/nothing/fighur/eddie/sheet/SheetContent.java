package nothing.fighur.eddie.sheet;

import com.googlecode.lanterna.TextColor;
import nothing.fighur.eddie.penpouch.Mark;
import nothing.fighur.eddie.text.Position;
import nothing.fighur.eddie.text.TextCharacter;

import java.util.List;

public interface SheetContent extends Sheet {
    /**
     * Insert the given character at the given position
     * @param character character to insert
     * @param position starting position
     * @return position in the text after the operation completes
     */
    Position insertCharacter(TextCharacter character, Position position);

    /**
     * Delete the character to the left of the given position.
     * If the given position is at the beginning of a row,
     * it moves the contents of that row to the end of the previous row
     * @param position starting position
     * @return position in the text after the operation completes.
     */
    Position deleteCharacter(Position position);

    /**
     * Insert a new line at the given position
     * @param position starting position
     * @return position of the start of the new line
     */
    Position insertNewLine(Position position);

    /**
     * Get the text characters between the given marks
     * @param from starting mark
     * @param to closing mark
     * @return list of characters encountered between the given marks.
     */
    List<TextCharacter> getCharactersBetween(Mark from, Mark to);

    /**
     * Delete characters between the given marks
     * @param from starting mark
     * @param to closing mark
     */
    void deleteCharactersBetween(Mark from, Mark to);

    /**
     * Get all text as a CharSequence
     * @return CharSequence
     */
    CharSequence asCharSequence();

    /**
     * Set the text's background color for highlighting
     * @param color the color
     */
    void setHighlightColor(TextColor color);

    /**
     * Mark where the text is highlighted. To remove them pass the unset Mark.
     * @param from initial Mark
     * @param to finishing Mark
     */
    void setHighlightMarks(Mark from, Mark to);

    /**
     * Clean highlight
     */
    void cleanHighlight();
}
