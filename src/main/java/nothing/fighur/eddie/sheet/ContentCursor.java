package nothing.fighur.eddie.sheet;

import nothing.fighur.eddie.text.Position;

public interface ContentCursor {
    /**
     * Move one unit to the left of the given position.
     * @param position starting position
     * @return position in the text after the operation completes
     */
    Position moveLeft(Position position);

    /**
     * Move one unit to the right of the given position.
     * @param position starting position
     * @return position in the text after the operation completes
     */
    Position moveRight(Position position);

    /**
     * Move one unit up the given position.
     * @param position starting position
     * @return position in the text after the operation completes
     */
    Position moveUp(Position position);

    /**
     * Move one unit down the given position.
     * @param position starting position
     * @return position in the text after the operation completes
     */
    Position moveDown(Position position);

    /**
     * Move the sheet's content cursor to the start of the next word.
     * @param position starting position
     * @return position in the text after the operation completes.
     */
    Position moveToStartOfNextWord(Position position);

    /**
     * Move to the end of the next word.
     * @param position starting position
     * @return position in the text after the operation completes.
     */
    Position moveToEndOfNextWord(Position position);

    /**
     * Move to the start of the previous word.
     * @param position starting position
     * @return position in the text after the operation completes.
     */
    Position moveToStartOfPreviousWord(Position position);
}
