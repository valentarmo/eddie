package nothing.fighur.eddie.sheet;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalResizeListener;
import nothing.fighur.eddie.penpouch.Mark;
import nothing.fighur.eddie.text.*;

import java.io.IOException;
import java.util.List;

public class SuperSheet implements SheetContent, TerminalResizeListener, ContentCursor {

    private ContentText contentText;
    private HeaderText headerText;
    private FooterText footerText;
    private Terminal terminal;
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

    public SuperSheet(ContentText contentText, HeaderText headerText, FooterText footerText, Terminal terminal) throws IOException {
        setContentText(contentText);
        setHeaderText(headerText);
        setFooterText(footerText);
        setTerminal(terminal);
        setSheetRows(terminal.getTerminalSize().getRows());
        setSheetCols(terminal.getTerminalSize().getColumns());
    }

    @Override
    public Position insertCharacter(TextCharacter character, Position position) {
        Position newPosition = getContentText().insertCharacter(character, position, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
        setCursorPosition(newPosition);
        return newPosition;
    }

    @Override
    public Position deleteCharacter(Position position) {
        Position newPosition = getContentText().deleteCharacter(position, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
        setCursorPosition(newPosition);
        return newPosition;
    }

    @Override
    public Position insertNewLine(Position position) {
        Position newPosition = getContentText().insertNewLine(position, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
        setCursorPosition(newPosition);
        return newPosition;
    }

    @Override
    public Position moveLeft(Position position) {
        Position newPosition = getContentText().moveLeft(position, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
        setCursorPosition(newPosition);
        return newPosition;
    }

    @Override
    public Position moveRight(Position position) {
        Position newPosition = getContentText().moveRight(position, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
        setCursorPosition(newPosition);
        return newPosition;
    }

    @Override
    public Position moveUp(Position position) {
        Position newPosition = getContentText().moveUp(position, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
        setCursorPosition(newPosition);
        return newPosition;
    }

    @Override
    public Position moveDown(Position position) {
        Position newPosition = getContentText().moveDown(position, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
        setCursorPosition(newPosition);
        return newPosition;
    }

    @Override
    public Position moveToStartOfNextWord(Position position) {
        Position newPosition = getContentText().moveToStartOfNextWord(position, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
        setCursorPosition(newPosition);
        return newPosition;
    }

    @Override
    public Position moveToEndOfNextWord(Position position) {
        Position newPosition = getContentText().moveToEndOfNextWord(position, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
        setCursorPosition(newPosition);
        return newPosition;
    }

    @Override
    public Position moveToStartOfPreviousWord(Position position) {
        Position newPosition = getContentText().moveToStartOfPreviousWord(position, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
        setCursorPosition(newPosition);
        return newPosition;
    }

    @Override
    public List<TextCharacter> getCharactersBetween(Mark from, Mark to) {
        return getContentText().getCharactersBetween(from, to);
    }

    @Override
    public void deleteCharactersBetween(Mark from, Mark to) {
        getContentText().deleteCharactersBetween(from, to, getTerminal(), getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
    }

    @Override
    public CharSequence asCharSequence() {
        return getContentText().asCharSequence();
    }

    @Override
    public void onResized(Terminal terminal, TerminalSize terminalSize) {
        setSheetRows(terminalSize.getRows());
        setSheetCols(terminalSize.getColumns());
        getHeaderText().resize(terminal, getHeaderFirstRow(), getHeaderLastRow(), getHeaderFirstCol(), getHeaderLastCol());
        getContentText().resize(terminal, getContentFirstRow(), getContentLastRow(), getContentFirstCol(), getContentLastCol());
        getFooterText().resize(terminal, getFooterFirstRow(), getFooterLastRow(), getFooterFirstCol(), getFooterLastCol());
    }

    private void drawHeader() {
        Terminal terminal = getTerminal();
        // TODO
    }

    private void drawFooter() {
        Terminal terminal = getTerminal();
        // TODO
    }

    private void setCursorPosition(Position position)
    {
        try {
            getTerminal().setCursorPosition(position.getRow(), position.getCol());
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
        setContentLastRow(sheetRows - 1);
        setFooterFirstRow(sheetRows);
        setFooterLastRow(sheetRows);
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
}
