package nothing.fighur.eddie.sheet;

import com.google.inject.Inject;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalResizeListener;
import nothing.fighur.eddie.penpouch.Mark;
import nothing.fighur.eddie.text.*;

import java.io.IOException;
import java.util.List;

public class SuperSheet implements SheetHeader, SheetContent, SheetFooter, TerminalResizeListener, ContentCursor {

    private ContentText contentText;
    private HeaderText headerText;
    private FooterText footerText;
    private Terminal terminal;
    private Position lastPosition = new Position(0, 0);
    private int sheetRows;
    private int sheetCols;
    private int contentFirstRow;
    private int contentLastRow;
    private int contentFirstCol;
    private int contentLastCol;
    private int headerFirstRow;
    private int headerLastRow;
    private int headerFirstCol;
    private int headerLastCol;
    private int footerFirstRow;
    private int footerLastRow;
    private int footerFirstCol;
    private int footerLastCol;

    private SuperSheet() { }

    @Inject
    public SuperSheet(ContentText contentText, HeaderText headerText, FooterText footerText, Terminal terminal) throws IOException {
        setContentText(contentText);
        setHeaderText(headerText);
        setFooterText(footerText);
        setTerminal(terminal);
        setSheetRows(terminal.getTerminalSize().getRows());
        setSheetCols(terminal.getTerminalSize().getColumns());
    }

    @Override
    public void insertText(Position position, List<TextCharacter> text) {
        try {
            getContentText().insertText(position, text, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
            setLastPosition(position);
            setTerminalCursorPosition(position);
        } catch (IOException e) {
            // TODO
        }
    }

    @Override
    public Position insertCharacter(TextCharacter character, Position position) {
        try {
            Position newPosition = getContentText().insertCharacter(character, position, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
            setLastPosition(newPosition);
            setTerminalCursorPosition(newPosition);
            return newPosition;
        } catch (IOException e) {
            // TODO
            return position;
        }
    }

    @Override
    public Position deleteCharacter(Position position) {
        try {
            Position newPosition = getContentText().deleteCharacter(position, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
            setLastPosition(newPosition);
            setTerminalCursorPosition(newPosition);
            return newPosition;
        } catch (IOException e) {
            // TODO
            return position;
        }
    }

    @Override
    public Position insertNewLine(Position position) {
        try {
            Position newPosition = getContentText().insertNewLine(position, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
            setLastPosition(newPosition);
            setTerminalCursorPosition(newPosition);
            return newPosition;
        } catch (IOException e) {
            // TODO
            return position;
        }
    }

    @Override
    public Position moveLeft(Position position) {
        try {
            Position newPosition = getContentText().moveLeft(position, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
            setLastPosition(newPosition);
            setTerminalCursorPosition(newPosition);
            return newPosition;
        } catch (IOException e) {
            // TODO
            return position;
        }
    }

    @Override
    public Position moveRight(Position position) {
        try {
            Position newPosition = getContentText().moveRight(position, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
            setLastPosition(newPosition);
            setTerminalCursorPosition(newPosition);
            return newPosition;
        } catch (IOException e) {
            // TODO
            return position;
        }
    }

    @Override
    public Position moveUp(Position position) {
        try {
            Position newPosition = getContentText().moveUp(position, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
            setLastPosition(newPosition);
            setTerminalCursorPosition(newPosition);
            return newPosition;
        } catch (IOException e) {
            // TODO
            return position;
        }
    }

    @Override
    public Position moveDown(Position position) {
        try {
            Position newPosition = getContentText().moveDown(position, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
            setLastPosition(newPosition);
            setTerminalCursorPosition(newPosition);
            return newPosition;
        } catch (IOException e) {
            // TODO
            return position;
        }
    }

    @Override
    public Position moveToStartOfNextWord(Position position) {
        try {
            Position newPosition = getContentText().moveToStartOfNextWord(position, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
            setLastPosition(newPosition);
            setTerminalCursorPosition(newPosition);
            return newPosition;
        } catch (IOException e) {
            // TODO
            return position;
        }
    }

    @Override
    public Position moveToEndOfNextWord(Position position) {
        try {
            Position newPosition = getContentText().moveToEndOfNextWord(position, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
            setLastPosition(newPosition);
            setTerminalCursorPosition(newPosition);
            return newPosition;
        } catch (IOException e) {
            // TODO
            return position;
        }
    }

    @Override
    public Position moveToStartOfPreviousWord(Position position) {
        try {
            Position newPosition = getContentText().moveToStartOfPreviousWord(position, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
            setLastPosition(newPosition);
            setTerminalCursorPosition(newPosition);
            return newPosition;
        } catch (IOException e) {
            // TODO
            return position;
        }
    }

    @Override
    public List<TextCharacter> getCharactersBetween(Mark from, Mark to) {
        return getContentText().getCharactersBetween(from, to);
    }

    @Override
    public void deleteCharactersBetween(Mark from, Mark to) {
        try {
            getContentText().deleteCharactersBetween(from, to, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
            setTerminalCursorPosition(getLastPosition());
        } catch (IOException e) {
            // TODO
        }
    }

    @Override
    public CharSequence asCharSequence() {
        return getContentText().asCharSequence();
    }

    @Override
    public void setHighlightColor(TextColor color) {
        getContentText().setHighlightColor(color);
    }

    @Override
    public void setHighlightMarks(Mark from, Mark to) {
        try {
            getContentText().setHighlightMarks(from, to, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
            setTerminalCursorPosition(getLastPosition());
        } catch (IOException e) {
            // TODO
        }
    }

    @Override
    public void cleanHighlight() {
        try {
            getContentText().cleanHighlight(getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
            setTerminalCursorPosition(getLastPosition());
        } catch (IOException e) {
            // TODO
        }
    }

    @Override
    public void onResized(Terminal terminal, TerminalSize terminalSize) {
        if (terminal == getTerminal()) {
            setSheetRows(terminalSize.getRows());
            setSheetCols(terminalSize.getColumns());
            refresh();
        }
    }

    @Override
    public void refresh() {
        try {
            getHeaderText().resize(terminal, getHeaderFirstRow(), getHeaderLastRow(), getHeaderFirstCol(), getHeaderLastCol());
            getContentText().resize(terminal, getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
            getFooterText().resize(terminal, getFooterFirstRow(), getFooterLastRow(), getFooterFirstCol(), getFooterLastCol());
            setTerminalCursorPosition(getLastPosition());
        } catch (IOException e) {
            // TODO
        }
    }

    @Override
    public void updateTitle(String title) {
        try {
            getHeaderText().updateTitle(getTerminal(), title);
            setTerminalCursorPosition(getLastPosition());
        } catch (IOException e) {
            // TODO
        }
    }

    @Override
    public void updateSubtitle(String subtitle) {
        try {
            getHeaderText().updateSubtitle(getTerminal(), subtitle);
            setTerminalCursorPosition(getLastPosition());
        } catch (IOException e) {
            // TODO
        }
    }

    @Override
    public void updateLogo(String logo) {
        try {
            getHeaderText().updateLogo(getTerminal(), logo);
            setTerminalCursorPosition(getLastPosition());
        } catch (IOException e) {
            // TODO
        }
    }

    private void setTerminalCursorPosition(Position position)
    {
        try {
            int row = position.getRow() + getContentFirstRow() - getContentText().getRowOffset();
            int col = position.getCol() + getContentFirstCol() - getContentText().getColOffset();
            getTerminal().setCursorPosition(col, row);
            getTerminal().flush();
        } catch (IOException e) {
            // TODO
        }
    }

    public ContentText getContentText() {
        return contentText;
    }

    public void setContentText(ContentText contentText) {
        this.contentText = contentText;
    }

    public HeaderText getHeaderText() {
        return headerText;
    }

    public void setHeaderText(HeaderText headerText) {
        this.headerText = headerText;
    }

    public FooterText getFooterText() {
        return footerText;
    }

    public void setFooterText(FooterText footerText) {
        this.footerText = footerText;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public int getSheetRows() {
        return sheetRows;
    }

    public void setSheetRows(int sheetRows) {
        this.sheetRows = sheetRows;
        setHeaderFirstRow(0);
        setHeaderLastRow(0);
        setContentFirstRow(1);
        setContentLastRow(sheetRows - 2);
        setFooterFirstRow(sheetRows);
        setFooterLastRow(sheetRows - 1);
    }

    public int getSheetCols() {
        return sheetCols;
    }

    public void setSheetCols(int sheetCols) {
        this.sheetCols = sheetCols;
        setHeaderFirstCol(0);
        setHeaderLastCol(sheetCols);
        setContentFirstCol(0);
        setContentLastCol(sheetCols);
        setFooterFirstCol(0);
        setFooterLastCol(sheetCols);
    }

    public int getContentFirstRow() {
        return contentFirstRow;
    }

    public void setContentFirstRow(int contentFirstRow) {
        this.contentFirstRow = contentFirstRow;
    }

    public int getContentLastRow() {
        return contentLastRow;
    }

    public void setContentLastRow(int contentLastRow) {
        this.contentLastRow = contentLastRow;
    }

    public int getContentFirstCol() {
        return contentFirstCol;
    }

    public void setContentFirstCol(int contentFirstCol) {
        this.contentFirstCol = contentFirstCol;
    }

    public int getContentLastCol() {
        return contentLastCol;
    }

    public void setContentLastCol(int contentLastCol) {
        this.contentLastCol = contentLastCol;
    }

    public int getHeaderFirstRow() {
        return headerFirstRow;
    }

    public void setHeaderFirstRow(int headerFirstRow) {
        this.headerFirstRow = headerFirstRow;
    }

    public int getHeaderLastRow() {
        return headerLastRow;
    }

    public void setHeaderLastRow(int headerLastRow) {
        this.headerLastRow = headerLastRow;
    }

    public int getHeaderFirstCol() {
        return headerFirstCol;
    }

    public void setHeaderFirstCol(int headerFirstCol) {
        this.headerFirstCol = headerFirstCol;
    }

    public int getHeaderLastCol() {
        return headerLastCol;
    }

    public void setHeaderLastCol(int headerLastCol) {
        this.headerLastCol = headerLastCol;
    }

    public int getFooterFirstRow() {
        return footerFirstRow;
    }

    public void setFooterFirstRow(int footerFirstRow) {
        this.footerFirstRow = footerFirstRow;
    }

    public int getFooterLastRow() {
        return footerLastRow;
    }

    public void setFooterLastRow(int footerLastRow) {
        this.footerLastRow = footerLastRow;
    }

    public int getFooterFirstCol() {
        return footerFirstCol;
    }

    public void setFooterFirstCol(int footerFirstCol) {
        this.footerFirstCol = footerFirstCol;
    }

    public int getFooterLastCol() {
        return footerLastCol;
    }

    public void setFooterLastCol(int footerLastCol) {
        this.footerLastCol = footerLastCol;
    }

    public Position getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(Position lastPosition) {
        this.lastPosition = lastPosition;
    }
}
