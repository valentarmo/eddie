package nothing.fighur.eddie.text;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;
import nothing.fighur.eddie.EditorVariables;
import nothing.fighur.eddie.penpouch.Mark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class DefaultContentText implements ContentText {

    private List<List<TextCharacter>> text = new ArrayList<>();
    private TextColor highlightColor = TextColor.ANSI.YELLOW;
    private Mark fromHighlightMark = Mark.unsetMark();
    private Mark toHighlightMark = Mark.unsetMark();
    private int rowOffset = 0;
    private int colOffset = 0;

    public DefaultContentText() {
        text.add(new ArrayList<>());
    }

    @Override
    public void writeText(List<TextCharacter> text, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) throws IOException {
        this.text = new ArrayList<>();
        this.text.add(new ArrayList<>());
        insertText(new Position(0, 0), text, terminal, firstRow, lastRow, firstCol, lastCol);
    }

    @Override
    public void insertText(Position position, List<TextCharacter> text, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) throws IOException {
        int row = position.getRow();
        int col = position.getCol();
        for (TextCharacter character : text) {
            if (character.getCharacter() == '\n') {
                if (col == this.text.get(row).size()) {
                    this.text.add(++row, new ArrayList<>());
                } else {
                    List<TextCharacter> entireRow = this.text.get(row);
                    List<TextCharacter> textToTheRight = new ArrayList<>(entireRow.subList(col, entireRow.size()));
                    this.text.add(++row, textToTheRight);
                    removeFromRow(entireRow, col, entireRow.size());
                }
                col = 0;
            } else {
                this.text.get(row).add(col++, character);
            }
        }
        drawContent(position, terminal, firstRow, lastRow, firstCol, lastCol);
    }

    @Override
    public Position insertCharacter(TextCharacter character, Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) throws IOException {
        int row = position.getRow();
        int col = position.getCol();
        if (row == text.size())
            text.add(new ArrayList<>());
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
        int row = position.getRow();
        int col = position.getCol();
        List<TextCharacter> rowCharacters = text.get(row);
        Position newPosition;
        if (col + 1 < rowCharacters.size())
            newPosition = moveToStartOfNextWord(row, col + 1);
        else if (col + 1 == rowCharacters.size())
            newPosition = moveToStartOfNextWord(row + 1, 0);
        else
            newPosition = moveToStartOfNextWord(row, col);
        drawContent(newPosition, terminal, firstRow, lastRow, firstCol, lastCol);
        return newPosition;
    }

    private Position moveToStartOfNextWord(int row, int col) {
        if (row >= text.size())
            return new Position(text.size() - 1, text.get(text.size() - 1).size());
        List<TextCharacter> rowCharacters = text.get(row);
        if (col < rowCharacters.size()) {
            if (col == 0) {
                if (Character.isWhitespace(rowCharacters.get(col).getCharacter())) {
                    return moveToStartOfNextWord(row, col + 1);
                } else {
                    return new Position(row, col);
                }
            } else if (col > 0) {
                if (Character.isWhitespace(rowCharacters.get(col - 1).getCharacter())) {
                    if (!Character.isWhitespace(rowCharacters.get(col).getCharacter()))
                        return new Position(row, col);
                    else
                        return moveToStartOfNextWord(row, col + 1);
                } else {
                    return moveToStartOfNextWord(row, col + 1);
                }
            } else {
                return moveToStartOfNextWord(row, 0);
            }
        } else {
            return moveToStartOfNextWord(row + 1, 0);
        }
    }

    @Override
    public Position moveToEndOfNextWord(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) throws IOException {
        int row = position.getRow();
        int col = position.getCol();
        List<TextCharacter> rowCharacters = text.get(row);
        Position newPosition;
        if (col + 1 < rowCharacters.size())
            if (Character.isWhitespace(rowCharacters.get(col + 1).getCharacter()))
                newPosition = moveToEndOfNextWord(row, col + 1);
            else
                newPosition = moveToEndOfNextWord(row, col);
        else if (col + 1 == rowCharacters.size())
            newPosition = moveToEndOfNextWord(row + 1, 0);
        else
            newPosition = moveToEndOfNextWord(row, col);
        drawContent(newPosition, terminal, firstRow, lastRow, firstCol, lastCol);
        return newPosition;
    }

    private Position moveToEndOfNextWord(int row, int col) {
        if (row >= text.size())
            return new Position(text.size() - 1, text.get(text.size() - 1).size());
        List<TextCharacter> rowCharacters = text.get(row);
        if (col < rowCharacters.size()) {
            if (!Character.isWhitespace(rowCharacters.get(col).getCharacter())) {
                while (col < rowCharacters.size() && !Character.isWhitespace(rowCharacters.get(col).getCharacter()))
                    col++;
                return new Position(row, --col);
            } else {
                return moveToEndOfNextWord(row, col + 1);
            }
        } else {
            return moveToEndOfNextWord(row + 1, 0);
        }
    }

    @Override
    public Position moveToStartOfPreviousWord(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) throws IOException {
        int row = position.getRow();
        int col = position.getCol();
        Position newPosition;
        if (col > 0) {
            newPosition = moveToStartOfPreviousWord(row, col - 1);
        } else if (col == 0) {
            if (row - 1 >= 0) {
                newPosition = moveToStartOfPreviousWord(row - 1, text.get(row - 1).size() - 1);
            } else {
                newPosition = position;
            }
        } else {
            newPosition = moveToStartOfPreviousWord(new Position(row, 0), terminal, firstRow, lastRow, firstCol, lastCol);
        }
        drawContent(newPosition, terminal, firstRow, lastRow, firstCol, lastCol);
        return newPosition;
    }

    private Position moveToStartOfPreviousWord(int row, int col) {
        if (row < 0)
            return  new Position(0, 0);
        List<TextCharacter> rowCharacters = text.get(row);
        if (col < 0 || rowCharacters.isEmpty()) {
            return moveToStartOfPreviousWord(row - 1, text.get(row - 1).size() - 1);
        } else {
            if (!Character.isWhitespace(rowCharacters.get(col).getCharacter())) {
                while (col > 0 && !Character.isWhitespace(rowCharacters.get(col).getCharacter()))
                    col--;
                if (col > 0 && !Character.isWhitespace(rowCharacters.get(col + 1).getCharacter())) {
                    return new Position(row, col + 1);
                } else if (col == 0 && !Character.isWhitespace(rowCharacters.get(col).getCharacter())) {
                    return new Position(row, col);
                } else {
                    return moveToStartOfPreviousWord(row, col - 1);
                }
            } else {
                return moveToStartOfPreviousWord(row, col - 1);
            }
        }
    }

    @Override
    public Position deleteCharactersBetween(Mark from, Mark to, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) throws IOException {
        Mark head;
        Mark tail;
        if (from.compareTo(to) > 0) {
            head = from;
            tail = to;
        } else {
            head = to;
            tail = from;
        }
        int tailingRow = tail.getRow();
        int tailingCol = tail.getColumn();
        int headingRow = head.getRow();
        int headingCol = head.getColumn();
        if (tailingRow != headingRow) {
            while (tailingRow < headingRow) {
                List<TextCharacter> rowCharacters = text.get(tailingRow);
                removeFromRow(rowCharacters, tailingCol, rowCharacters.size());
                tailingCol = 0;
                if (rowCharacters.isEmpty()) {
                    text.remove(tailingRow);
                    headingRow--;
                } else {
                    tailingRow++;
                }
            }
        }
        removeFromRow(text.get(tailingRow), tailingCol, headingCol);
        drawContent(null, terminal, firstRow, lastRow, firstCol, lastCol);
        return new Position(tailingRow, tailingCol);
    }

    @Override
    public List<TextCharacter> getCharactersBetween(Mark from, Mark to) {
        List<TextCharacter> characters = new LinkedList<>();
        Mark head;
        Mark tail;
        if (from.compareTo(to) > 0) {
            head = from;
            tail = to;
        } else {
            head = to;
            tail = from;
        }
        int tailingRow = tail.getRow();
        int tailingCol = tail.getColumn();
        int headingRow = head.getRow();
        int headingCol = head.getColumn();
        if (tailingRow != headingRow) {
            while (tailingRow < headingRow) {
                List<TextCharacter> rowCharacters = text.get(tailingRow);
                characters.addAll(rowCharacters.subList(tailingCol, rowCharacters.size()));
                characters.add(TextCharacter.withDefaultCharacteristics('\n'));
                tailingCol = 0;
                tailingRow++;
            }
        }
        characters.addAll(text.get(tailingRow).subList(tailingCol, headingCol));
        return characters;
    }

    @Override
    public CharSequence asCharSequence() {
        StringBuilder str = new StringBuilder();
        for (List<TextCharacter> row : text) {
            for (TextCharacter c : row) {
                str.append(c.getCharacter());
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
    public void setHighlightColor(TextColor color) {
        this.highlightColor = color;
    }

    @Override
    public void setHighlightMarks(Mark from, Mark to, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) throws IOException {
        if (Mark.isUnset(from) || Mark.isUnset(to)) {
            setFromHighlightMark(Mark.unsetMark());
            setToHighlightMark(Mark.unsetMark());
        } else {
            setFromHighlightMark(from);
            setToHighlightMark(to);
        }
        drawContent(null, terminal, firstRow, lastRow, firstCol, lastCol);
    }

    @Override
    public void cleanHighlight(Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol)  throws IOException{
        setHighlightMarks(Mark.unsetMark(), Mark.unsetMark(), terminal, firstRow, lastRow, firstCol, lastCol);
    }

    @Override
    public int getColOffset() {
        return colOffset;
    }

    private void removeFromRow(List<TextCharacter> row, int start, int end) {
        if (end > start) {
            row.subList(start, end).clear();
        }
    }

    private void drawContent(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) throws IOException {
        if (Objects.nonNull(position))
            scroll(position, lastRow, lastCol);
        for (int row = firstRow, relativeRow = 0; row <= lastRow; row++, relativeRow++) {
            terminal.setCursorPosition(firstCol, row);
            int textRow = relativeRow + getRowOffset();
            List<TextCharacter> rowCharacters = getCharactersAt(textRow);
            if (textRow < text.size()) {
                List<TextCharacter> visibleRowCharacters = getColOffset() <= rowCharacters.size() ? rowCharacters.subList(getColOffset(), rowCharacters.size()) : new ArrayList<>();
                for (int col = firstCol, textCol = 0; textCol < visibleRowCharacters.size() && col < lastCol; col++, textCol++) {
                    TextCharacter character = visibleRowCharacters.get(textCol);
                    terminal.setForegroundColor(character.getForegroundColor());
                    if (inHighlightZone(textRow, textCol))
                        terminal.setBackgroundColor(getHighlightColor());
                    else
                        terminal.setBackgroundColor(character.getBackgroundColor());
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

    private boolean inHighlightZone(int row, int col) {
        Mark from = getFromHighlightMark();
        Mark to = getToHighlightMark();
        if (Mark.isUnset(from) || Mark.isUnset(to))
            return false;
        Mark head;
        Mark tail;
        if (from.compareTo(to) > 0) {
            head = from;
            tail = to;
        } else {
            head = to;
            tail = from;
        }
        int tailingRow = tail.getRow();
        int tailingCol = tail.getColumn();
        int headingRow = head.getRow();
        int headingCol = head.getColumn();
        if (!(row >= tailingRow && row <= headingRow))
            return false;
        else return (row != tailingRow || col >= tailingCol) && (row != headingRow || col < headingCol);
    }

    public void setRowOffset(int rowOffset) {
        this.rowOffset = rowOffset;
    }

    public void setColOffset(int colOffset) {
        this.colOffset = colOffset;
    }

    public TextColor getHighlightColor() {
        return highlightColor;
    }

    public Mark getFromHighlightMark() {
        return fromHighlightMark;
    }

    public void setFromHighlightMark(Mark fromHighlightMark) {
        this.fromHighlightMark = fromHighlightMark;
    }

    public Mark getToHighlightMark() {
        return toHighlightMark;
    }

    public void setToHighlightMark(Mark toHighlightMark) {
        this.toHighlightMark = toHighlightMark;
    }
}
