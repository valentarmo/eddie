package nothing.fighur.eddie.text;

import com.googlecode.lanterna.terminal.Terminal;
import nothing.fighur.eddie.EditorVariables;
import nothing.fighur.eddie.penpouch.Mark;
import nothing.fighur.eddie.sheet.SheetContent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DefaultContentText implements ContentText {

    private List<List<TextCharacter>> text = new ArrayList<>();
    private SheetContent sheetContent;
    private int rowOff = 0;
    private int colOff = 0;

    public DefaultContentText() {
        text.add(new ArrayList<>());
    }

    @Override
    public Position insertCharacter(TextCharacter character, Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) {
        int row = position.getRow();
        int col = position.getCol();
        text.get(row).add(col, character);
        drawContent(terminal, firstRow, lastRow, firstCol, lastCol);
        return new Position(row, col + 1);
    }

    @Override
    public Position deleteCharacter(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) {
        int row = position.getRow();
        int col = position.getCol() - 1;
        int previousRow = row - 1;
        if (row == 0 && col < 0) {
            drawContent(terminal, firstRow, lastRow, firstCol, lastCol);
            return new Position(0, 0);
        }
        List<TextCharacter> entireRow = text.get(row);
        if (col < 0) {
            text.get(previousRow).addAll(entireRow);
            text.remove(row);
        } else {
            List<TextCharacter> textToTheRight = entireRow.subList(col, entireRow.size());
            text.get(previousRow).addAll(textToTheRight);
        }
        drawContent(terminal, firstRow, lastRow, firstCol, lastCol);
        return new Position(previousRow, text.get(previousRow).size());
    }

    @Override
    public Position insertNewLine(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) {
        int row = position.getRow();
        int col = position.getCol();
        int nextRow = row + 1;
        if (col == text.get(row).size())
            text.add(nextRow, new ArrayList<>());
        else {
            List<TextCharacter> entireRow = text.get(row);
            List<TextCharacter> textToTheRight = entireRow.subList(col, entireRow.size());
            text.add(nextRow, textToTheRight);
            removeFromRow(entireRow, col, entireRow.size());
        }
        drawContent(terminal, firstRow, lastRow, firstCol, lastCol);
        return new Position(nextRow, 0);
    }

    @Override
    public Position moveRight(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) {
        int row = position.getRow();
        int col = position.getCol();
        if (col >= text.get(row).size())
            return position;
        else {
            drawContent(terminal, firstRow, lastRow, firstCol, lastCol);
            return new Position(row, col + 1);
        }
    }

    @Override
    public Position moveLeft(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) {
        int row = position.getRow();
        int col = position.getCol();
        if (col <= 0)
            return position;
        else {
            drawContent(terminal, firstRow, lastRow, firstCol, lastCol);
            return new Position(row, col - 1);
        }
    }

    @Override
    public Position moveUp(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) {
        int row = position.getRow() - 1;
        int col = position.getCol();
        if (row < 0 )
            return position;
        else {
            if (col > text.get(row).size()) {
                drawContent(terminal, firstRow, lastRow, firstCol, lastCol);
                return new Position(row, text.get(row).size());
            } else {
                drawContent(terminal, firstRow, lastRow, firstCol, lastCol);
                return new Position(row, col);
            }
        }
    }

    @Override
    public Position moveDown(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) {
        int row = position.getRow() + 1;
        int col = position.getCol();
        if (row >= text.size())
            return position;
        else {
            if (col > text.get(row).size()) {
                drawContent(terminal, firstRow, lastRow, firstCol, lastCol);
                return new Position(row, text.get(row).size());
            } else {
                drawContent(terminal, firstRow, lastRow, firstCol, lastCol);
                return new Position(row, col);
            }
        }
    }

    @Override
    public Position moveToStartOfNextWord(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) {
        return null;
    }

    @Override
    public Position moveToEndOfNextWord(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) {
        return null;
    }

    @Override
    public Position moveToStartOfPreviousWord(Position position, Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) {
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
    public void resize(Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) {
        drawContent(terminal, firstRow, lastRow, firstCol, lastCol);
    }

    private void removeFromRow(List<TextCharacter> row, int start, int end) {
        for (int i = start; i < end; i++)
            row.remove(start);
    }

    private void drawContent(Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) {
        try {
//            terminal.clearScreen();
            terminal.setCursorPosition(firstCol, firstRow);
            for (int row = firstRow, relativeRow = 0; row <= lastRow; row++, relativeRow++) {
                int textRow = relativeRow + getRowOff();
                List<TextCharacter> rowCharacters = getCharactersAt(textRow);
                if (!rowCharacters.isEmpty()) {
                    List<TextCharacter> visibleRowCharacters = rowCharacters.subList(getColOff(), rowCharacters.size());
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
                terminal.putCharacter('\r');
                terminal.putCharacter('\n');
            }
        } catch (IOException e) { /* TODO possibly throw */ }
    }

    private void scroll(Terminal terminal, Position position, int lastRow, int lastCol)
    {
        int cursorRow = position.getRow();
        int cursorCol = position.getCol();
        int rowOff = getRowOff();
        int colOff = getColOff();
        if (cursorRow < rowOff) /* The cursor is past the top */
            setRowOff(cursorRow);
        if (cursorRow >= rowOff + lastRow) /* The cursor is past the bottom */
            setRowOff(cursorRow - lastRow + 1);
        if (cursorCol < colOff) /* The cursor is past the left */
            setColOff(cursorCol);
        if (cursorCol >= colOff + lastCol) /* cursor is past the right */
            setColOff(cursorCol - lastCol + 1);
    }

    private List<TextCharacter> getCharactersAt(int row) {
        try {
            return text.get(row);
        } catch (IndexOutOfBoundsException e) {
            return new ArrayList<>();
        }
    }

    public SheetContent getSheetContent() {
        return sheetContent;
    }

    public void setSheetContent(SheetContent sheetContent) {
        this.sheetContent = sheetContent;
    }

    public int getRowOff() {
        return rowOff;
    }

    public void setRowOff(int rowOff) {
        this.rowOff = rowOff;
    }

    public int getColOff() {
        return colOff;
    }

    public void setColOff(int colOff) {
        this.colOff = colOff;
    }
}
