package nothing.fighur.eddie.penpouch;

import nothing.fighur.eddie.sheet.ContentCursor;
import nothing.fighur.eddie.text.Position;

public class DefaultHand implements Hand, ToolObserver {

    private ContentCursor contentCursor;
    private Position position = new Position(0, 0);

    @Override
    public void moveLeft() {
        setPosition(getContentCursor().moveLeft(getPosition()));
    }

    @Override
    public void moveRight() {
        setPosition(getContentCursor().moveRight(getPosition()));
    }

    @Override
    public void moveUp() {
        setPosition(getContentCursor().moveUp(getPosition()));
    }

    @Override
    public void moveDown() {
        setPosition(getContentCursor().moveDown(getPosition()));
    }

    @Override
    public void moveToStartOfNextWord() {
        setPosition(getContentCursor().moveToStartOfNextWord(getPosition()));
    }

    @Override
    public void moveToEndOfNextWord() {
        setPosition(getContentCursor().moveToEndOfNextWord(getPosition()));
    }

    @Override
    public void moveToStartOfPreviousWord() {
        setPosition(getContentCursor().moveToStartOfPreviousWord(getPosition()));
    }

    @Override
    public Position getPosition() {
        return this.getPosition();
    }

    @Override
    public void update(Position position) {
        setPosition(position);
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public ContentCursor getContentCursor() {
        return contentCursor;
    }

    public void setContentCursor(ContentCursor contentCursor) {
        this.contentCursor = contentCursor;
    }
}
