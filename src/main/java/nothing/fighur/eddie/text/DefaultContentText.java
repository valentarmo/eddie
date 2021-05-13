package nothing.fighur.eddie.text;

import com.googlecode.lanterna.terminal.Terminal;
import nothing.fighur.eddie.EditorVariables;
import nothing.fighur.eddie.penpouch.Mark;
import nothing.fighur.eddie.sheet.SheetContent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DefaultContentText implements ContentText {

    private List<List<TextCharacter>> text = new ArrayList<>();
    private SheetContent sheetContent;
    private int rowOffset = 0;
    private int colOffset = 0;

    public DefaultContentText() {
        text.add(new ArrayList<>());
    }

    @Override
    public Position insertCharacter(TextCharacter character, Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) throws IOException {
        int row = position.getRow();
        int col = position.getCol();
        text.get(row).add(col, character);
        Position newPosition = new Position(row, col + 1);
        drawContent(newPosition, terminal, firstRow, lastRow, firstCol, lastCol);
        return newPosition;
    }

    @Override
    public Position deleteCharacter(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) throws IOException {
        int row = position.getRow();
        int col = position.getCol() - 1;
        int previousRow = row - 1;
        if (row == 0 && col < 0) {
            return new Position(0, 0);
        }
        List<TextCharacter> entireRow = text.get(row);
        if (col < 0) {
            int previousLastCol = text.get(previousRow).size();
            text.get(previousRow).addAll(entireRow);
            text.remove(row);
            Position newPosition = new Position(previousRow, previousLastCol);
            drawContent(newPosition, terminal, firstRow, lastRow, firstCol, lastCol);
            return newPosition;
        } else {
            entireRow.remove(col);
            Position newPosition = new Position(row, col);
            drawContent(newPosition, terminal, firstRow, lastRow, firstCol, lastCol);
            return newPosition;
        }
    }

    @Override
    public Position insertNewLine(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) throws IOException {
        int row = position.getRow();
        int col = position.getCol();
        int nextRow = row + 1;
        if (col == text.get(row).size())
            text.add(nextRow, new ArrayList<>());
        else {
            List<TextCharacter> entireRow = text.get(row);
            List<TextCharacter> textToTheRight = new ArrayList<>(entireRow.subList(col, entireRow.size()));
            text.add(nextRow, textToTheRight);
            removeFromRow(entireRow, col, entireRow.size());
        }
        Position newPosition = new Position(nextRow, 0);
        drawContent(newPosition, terminal, firstRow, lastRow, firstCol, lastCol);
        return newPosition;
    }

    @Override
    public Position moveRight(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) throws IOException {
        int row = position.getRow();
        int col = position.getCol();
        if (col >= text.get(row).size())
            return position;
        else {
            Position newPosition = new Position(row, col + 1);
            drawContent(newPosition, terminal, firstRow, lastRow, firstCol, lastCol);
            return newPosition;
        }
    }

    @Override
    public Position moveLeft(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) throws IOException {
        int row = position.getRow();
        int col = position.getCol();
        if (col <= 0)
            return position;
        else {
            Position newPosition = new Position(row, col - 1);
            drawContent(newPosition, terminal, firstRow, lastRow, firstCol, lastCol);
            return newPosition;
        }
    }

    @Override
    public Position moveUp(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) throws IOException {
        int row = position.getRow() - 1;
        int col = position.getCol();
        if (row < 0 )
            return position;
        else {
            Position newPosition;
            if (col > text.get(row).size()) {
                newPosition = new Position(row, text.get(row).size());
            } else {
                newPosition = new Position(row, col);
            }
            drawContent(newPosition, terminal, firstRow, lastRow, firstCol, lastCol);
            return newPosition;
        }
    }

    @Override
    public Position moveDown(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) throws IOException {
        int row = position.getRow() + 1;
        int col = position.getCol();
        if (row >= text.size())
            return position;
        else {
            Position newPosition;
            if (col > text.get(row).size()) {
                newPosition = new Position(row, text.get(row).size());
            } else {
                newPosition = new Position(row, col);
            }
            drawContent(newPosition, terminal, firstRow, lastRow, firstCol, lastCol);
            return newPosition;
        }
    }

    @Override
    public Position moveToStartOfNextWord(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) throws IOException {
        return null;
    }

    @Override
    public Position moveToEndOfNextWord(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) throws IOException {
        return null;
    }

    @Override
    public Position moveToStartOfPreviousWord(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) throws IOException {
        return null;
    }

    @Override
    public void deleteCharactersBetween(Mark from, Mark to, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) {

    }

    @Override
    public List<TextCharacter> getCharactersBetween(Mark from, Mark to) {
        return null;
    }

    @Override
    public CharSequence asCharSequence() {
        StringBuilder str = new StringBuilder();
        for (List<TextCharacter> row : text) {
            for (TextCharacter c : row) {
                str.append(c);
            }
            str.append('\n');
        }
        return str;
    }

    @Override
    public void resize(Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) throws IOException {
        drawContent(null, terminal, firstRow, lastRow, firstCol, lastCol);
    }

    @Override
    public int getRowOffset() {
        return rowOffset;
    }

    @Override
    public int getColOffset() {
        return colOffset;
    }

    private void removeFromRow(List<TextCharacter> row, int start, int end) {
        for (int i = start; i < end; i++)
            row.remove(start);
    }

    private void drawContent(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) throws IOException {
        if (Objects.nonNull(position))
            scroll(position, lastRow, lastCol);
        for (int row = firstRow, relativeRow = 0; row <= lastRow; row++, relativeRow++) {
            terminal.setCursorPosition(firstCol, row);
            int textRow = relativeRow + getRowOffset();
            List<TextCharacter> rowCharacters = getCharactersAt(textRow);
            if (relativeRow < text.size()) {
                List<TextCharacter> visibleRowCharacters = getColOffset() <= rowCharacters.size() ? rowCharacters.subList(getColOffset(), rowCharacters.size()) : new ArrayList<>();
                for (int col = firstCol, textCol = 0; textCol < visibleRowCharacters.size() && col < lastCol; col++, textCol++) {
                    TextCharacter character = visibleRowCharacters.get(textCol);
                    terminal.setBackgroundColor(character.getBackgroundColor());
                    terminal.setForegroundColor(character.getForegroundColor());
                    terminal.putCharacter(character.getCharacter());
                }
                for (int col = visibleRowCharacters.size(); col < lastCol; col++) {
                    terminal.setBackgroundColor(EditorVariables.getBackgroundColor());
                    terminal.setForegroundColor(EditorVariables.getForegroundColor());
                    terminal.putCharacter(' ');
                }
            } else {
                terminal.putCharacter('~');
                for (int col = 1; col < lastCol; col++)
                    terminal.putCharacter(' ');
            }
        }
    }

    private void scroll(Position position, int lastRow, int lastCol)
    {
        int cursorRow = position.getRow();
        int cursorCol = position.getCol();
        int rowOffset = getRowOffset();
        int colOffset = getColOffset();
        if (cursorRow < rowOffset) /* The cursor is past the top */
            setRowOffset(cursorRow);
        if (cursorRow >= rowOffset + lastRow) /* The cursor is past the bottom */
            setRowOffset(cursorRow - lastRow + 1);
        if (cursorCol < colOffset) /* The cursor is past the left */
            setColOffset(cursorCol);
        if (cursorCol >= colOffset + lastCol) /* cursor is past the right */
            setColOffset(cursorCol - lastCol + 1);
    }

    private List<TextCharacter> getCharactersAt(int row) {
        return row < text.size() ? text.get(row) : new ArrayList<>();
    }

    public SheetContent getSheetContent() {
        return sheetContent;
    }

    public void setSheetContent(SheetContent sheetContent) {
        this.sheetContent = sheetContent;
    }

    public void setRowOffset(int rowOffset) {
        this.rowOffset = rowOffset;
    }

    public void setColOffset(int colOffset) {
        this.colOffset = colOffset;
    }
}
