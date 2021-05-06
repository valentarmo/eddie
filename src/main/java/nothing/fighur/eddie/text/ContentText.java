package nothing.fighur.eddie.text;

import com.googlecode.lanterna.terminal.Terminal;
import nothing.fighur.eddie.penpouch.Mark;

import java.util.List;

public interface ContentText extends VisibleText {
    /**
     * Insert the given character at the given position
     * @param character character to insert
     * @param position starting position
     * @param terminal terminal where to draw itself
     * @param firstRow starting row of the content zone within the provided terminal
     * @param lastRow final row of the content zone within the provided terminal
     * @param firstCol starting column of the content zone within the provided terminal
     * @param lastCol final column of the content zone within the provided terminal
     * @return position in the text after the operation completes
     */
    Position insertCharacter(TextCharacter character, Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol);

    /**
     * Delete the character to the left of the given position.
     * If the given position is at the beginning of a row,
     * it moves the contents of that row to the end of the previous row
     * @param position starting position
     * @param terminal terminal where to draw itself
     * @param firstRow starting row of the content zone within the provided terminal
     * @param lastRow final row of the content zone within the provided terminal
     * @param firstCol starting column of the content zone within the provided terminal
     * @param lastCol final column of the content zone within the provided terminal
     * @return position in the text after the operation completes.
     */
    Position deleteCharacter(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol);

    /**
     * Insert a new line at the given position
     * @param position starting position
     * @param terminal terminal where to draw itself
     * @param firstRow starting row of the content zone within the provided terminal
     * @param lastRow final row of the content zone within the provided terminal
     * @param firstCol starting column of the content zone within the provided terminal
     * @param lastCol final column of the content zone within the provided terminal
     * @return position of the start of the new line
     */
    Position insertNewLine(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol);

    /**
     * Move one unit to the left of the given position.
     * If the given position is at the end of a row, move to the next row.
     * @param position starting position
     * @param terminal terminal where to draw itself
     * @param firstRow starting row of the content zone within the provided terminal
     * @param lastRow final row of the content zone within the provided terminal
     * @param firstCol starting column of the content zone within the provided terminal
     * @param lastCol final column of the content zone within the provided terminal
     * @return position in the text after the operation completes
     */
    Position moveLeft(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol);

    /**
     * Move one unit to the right of the given position.
     * If the given position is at the beginning of a row, move to the previous row.
     * @param terminal terminal where to draw itself
     * @param firstRow starting row of the content zone within the provided terminal
     * @param lastRow final row of the content zone within the provided terminal
     * @param firstCol starting column of the content zone within the provided terminal
     * @param lastCol final column of the content zone within the provided terminal
     * @param position starting position
     * @return position in the text after the operation completes
     */
    Position moveRight(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol);

    /**
     * Move one unit up the given position
     * @param terminal terminal where to draw itself
     * @param firstRow starting row of the content zone within the provided terminal
     * @param lastRow final row of the content zone within the provided terminal
     * @param firstCol starting column of the content zone within the provided terminal
     * @param lastCol final column of the content zone within the provided terminal
     * @param position starting position
     * @return position in the text after the operation completes
     */
    Position moveUp(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol);

    /**
     * Move one unit down the given position
     * @param position starting position
     * @param terminal terminal where to draw itself
     * @param firstRow starting row of the content zone within the provided terminal
     * @param lastRow final row of the content zone within the provided terminal
     * @param firstCol starting column of the content zone within the provided terminal
     * @param lastCol final column of the content zone within the provided terminal
     * @return position in the text after the operation completes
     */
    Position moveDown(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol);

    /**
     * Move the sheet's content cursor to the start of the next word.
     * @param position starting position
     * @param terminal terminal where to draw itself
     * @param firstRow starting row of the content zone within the provided terminal
     * @param lastRow final row of the content zone within the provided terminal
     * @param firstCol starting column of the content zone within the provided terminal
     * @param lastCol final column of the content zone within the provided terminal
     * @return position in the text after the operation completes.
     */
    Position moveToStartOfNextWord(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol);

    /**
     * Move to the end of the next word.
     * @param position starting position
     * @param terminal terminal where to draw itself
     * @param firstRow starting row of the content zone within the provided terminal
     * @param lastRow final row of the content zone within the provided terminal
     * @param firstCol starting column of the content zone within the provided terminal
     * @param lastCol final column of the content zone within the provided terminal
     * @return position in the text after the operation completes.
     */
    Position moveToEndOfNextWord(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol);

    /**
     * Move to the start of the previous word.
     * @param position starting position
     * @param terminal terminal where to draw itself
     * @param firstRow starting row of the content zone within the provided terminal
     * @param lastRow final row of the content zone within the provided terminal
     * @param firstCol starting column of the content zone within the provided terminal
     * @param lastCol final column of the content zone within the provided terminal
     * @return position in the text after the operation completes.
     */
    Position moveToStartOfPreviousWord(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol);

    /**
     * Delete characters between the given marks
     * @param from starting mark
     * @param to closing mark
     * @param terminal terminal where to draw itself
     * @param firstRow starting row of the content zone within the provided terminal
     * @param lastRow final row of the content zone within the provided terminal
     * @param firstCol starting column of the content zone within the provided terminal
     * @param lastCol final column of the content zone within the provided terminal
     */
    void deleteCharactersBetween(Mark from, Mark to, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol);

    /**
     * Get the text characters between the given marks
     * @param from starting mark
     * @param to closing mark
     * @return list of characters encountered between the given marks.
     */
    List<TextCharacter> getCharactersBetween(Mark from, Mark to);

    /**
     * Get all text as a CharSequence
     * @return CharSequence
     */
    CharSequence asCharSequence();
}
