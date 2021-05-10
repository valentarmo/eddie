package nothing.fighur.eddie;

import nothing.fighur.eddie.OldEditor.Mode;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.terminal.*;
import com.googlecode.lanterna.graphics.*;

import java.io.IOException;

/**
 * This class is the responsible for displaying everything on the terminal
 */
public class Sheet
{
    private Text text;
    private Terminal terminal;
    private int cx = 0;
    private int cy = 0;
    private int terminalrows;
    private int terminalcols;
    private int rowoff = 0; /* For vertical scrolling */
    private int coloff = 0; /* For horizontal scrolling */
    private TextGraphics tgraphics;
    private String commandBarMsg;
    private CommandBarMsgType currentCommandBarMsgType = CommandBarMsgType.Normal;
    private long lastCommandBarMsgUpdate;
    private final int commandBarMsgLifeTime = 2000;
    private OldEditor oldEditor;
    private final TextColor defaultBackgroundColor = TextColor.ANSI.BLACK;
    private final TextColor defaultForegroundColor = TextColor.ANSI.WHITE;
    private int hlrow1 = -1;
    private int hlcol1 = -1;
    private int hlrow2 = -1;
    private int hlcol2 = -1;

    public enum CommandBarMsgType {
        Error, Normal, Warning, Success;
    }

    public Sheet(Terminal terminal, Text text, OldEditor oldEditor) throws IOException
    {
        this.terminal = terminal;
        this.text = text;
        this.oldEditor = oldEditor;
        tgraphics = terminal.newTextGraphics();
        TerminalSize tmp = terminal.getTerminalSize();
        terminalrows = tmp.getRows();
        terminalcols = tmp.getColumns();
    }

    // region public

    /**
     * Give a String to display in the command bar
     * @param msg String to display
     * @param type CommandBarMsgType - Error, Normal, Warning, Success
     */
    public void setCommandBarMsg(String msg, CommandBarMsgType type) throws IOException
    {
        lastCommandBarMsgUpdate = System.currentTimeMillis();
        currentCommandBarMsgType = type;
        for (int i = msg.length(); i < terminalcols; i++)
            msg += " ";
        commandBarMsg = msg;
        refreshScreen();
    }

    /**
     * Get the current command message
     * @return String the message or null if there is none
     */
    public String getCommandMsg()
    { return commandBarMsg; }

    /**
     * Get the column the cursor is currently at
     * @return int - the column
     */
    public int getCursorColumn()
    { return cx; }

    /**
     * Get the row the cursor is currently at
     * @return int - the row
     */
    public int getCursorRow()
    { return cy; }

    /**
     * Forcibly change the column the cursor is currently at
     * use with care, only changes to positive numbers
     * @param col int - the new column
     */
    public void changeCursorColumn(int col)
    { if (col >=0 ) cx = col; }

    /**
     * Forcibly change the row the cursor is currently at
     * use with care, only changes to positive numbers
     * @param row int - the new row
     */
    public void changeCursorRow(int row)
    { if (row >= 0) cy = row; }

    /**
     * Get the column offset used in horizontal scrolling
     */
    public int getColumnOffset()
    { return coloff; }

    /**
     * Get the row offset used in vertical scrolling
     */
    public int getRowOffset()
    { return rowoff; }

    /**
     * Change the text being currently displayed
     * @param text new text
     */
    public void changeText(Text text)
    {
        this.text = text;
        this.cx = 0;
        this.cy = 0;
        this.rowoff = 0;
        this.coloff = 0;
    }

    /**
     * Highlight a region
     * @param x1 int - column where to start highlighting
     * @param y1 int - row where to start highlighting
     * @param x2 int - column where to end highlighting
     * @param y2 int - row where to end highlighting
     */
    public void highlight(int x1, int y1, int x2, int y2)
    {
        hlcol1 = x1;
        hlrow1 = y1;
        hlcol2 = x2;
        hlrow2 = y2;
    }

    /**
     * Clear highlight region
     */
    public void clearHighlight()
    {
        hlrow1 = -1;
        hlcol1 = -1;
        hlrow2 = -1;
        hlcol2 = -1;
    }

    /**
     * Refresh the screen
     */
    public void refreshScreen() throws IOException
    {
        TerminalSize tsize = terminal.getTerminalSize();
        terminalrows = tsize.getRows();
        terminalcols = tsize.getColumns();

        terminal.setBackgroundColor(defaultBackgroundColor);
        terminal.setForegroundColor(defaultForegroundColor);

        scroll();

        printText();
        printStatusBar();
        printCommandBarMsg();
        /* ------------- */

        terminal.setCursorPosition(cx-coloff, cy-rowoff);
        terminal.flush();
    }

    // endregion

    // region Screen printing

    /**
     * Print text on the screen
     * @throws IOException
     */
	private void printText() throws IOException {
        int printingRows = terminalrows-2; /* Number of rows dedicated to text */
        terminal.setCursorPosition(0, 0);

        if (text.size() == 0) { /* There is no text */
            for (int row = 0; row < printingRows; row++) {
                terminal.putCharacter('~');
                for (int col = 1; col < terminalcols; col++)
                    terminal.putCharacter(' ');
                terminal.putCharacter('\r');
                terminal.putCharacter('\n');
            }
        }

        for (int row = 0; row < printingRows; row++)
        {
            int textrow = row + rowoff;
            String render = text.getTextAt(textrow);
            if (render != null)
            {
                /* Handle horizontal scrolling */
                int len = render.length() - coloff;
                if (len < 0)  len = 0;
                if (len > terminalcols) len = terminalcols;
                /* --------------------------- */
                String tmp = "";
                if (coloff < render.length())
                    tmp = render.substring(coloff);
                for (int col = 0; col < tmp.length() && col < terminalcols; col++) {
                    if (inHighligthZone(row, col))
                        terminal.setBackgroundColor(TextColor.ANSI.BLUE); /* Highlight */
                    else
                        terminal.setBackgroundColor(defaultBackgroundColor);
                    terminal.putCharacter(tmp.charAt(col));
                }
                terminal.setBackgroundColor(defaultBackgroundColor);
                for (int i = tmp.length(); i < terminalcols; i++)
                    terminal.putCharacter(' '); /* Clean to the right */
            }
            else
            {
                terminal.putCharacter('~');
                for (int i = 1; i < terminalcols; i++)
                    terminal.putCharacter(' '); /* Clean to the right */
            }
            terminal.putCharacter('\r');
            terminal.putCharacter('\n');
        }
    }

    /**
     * Print the status bar
     */
    private void printStatusBar()
    {
        StringBuilder msg = new StringBuilder();

        if (oldEditor.getMode() == Mode.Insert)
            msg.append("INSERT");
        else if (oldEditor.getMode() == Mode.Visual)
            msg.append("VISUAL");
        else
            msg.append("NORMAL");

        msg.append(" | File: " + oldEditor.getCurrentFileName());
        msg.append(" | Ln " + (cy + 1));
        msg.append(", Col " + (cx + 1));

        for (int i = msg.length(); i < terminalcols; i++)
            msg.append(" ");

        tgraphics.setBackgroundColor(TextColor.ANSI.WHITE);
        tgraphics.setForegroundColor(TextColor.ANSI.BLACK);
        tgraphics.putString(0, terminalrows-2, msg.toString());
        tgraphics.setBackgroundColor(defaultBackgroundColor);
        tgraphics.setForegroundColor(defaultBackgroundColor);
    }

    /**
     * Print the command bar
     */
    private void printCommandBarMsg() throws IOException
    {
        tgraphics.setBackgroundColor(defaultBackgroundColor);
        tgraphics.setForegroundColor(defaultForegroundColor);

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCommandBarMsgUpdate > commandBarMsgLifeTime)
            setCommandBarMsg("", CommandBarMsgType.Normal); /* The message has expired */

        if (currentCommandBarMsgType == CommandBarMsgType.Error) {
            tgraphics.setBackgroundColor(TextColor.ANSI.RED);
            tgraphics.setForegroundColor(TextColor.ANSI.WHITE);
        } else if (currentCommandBarMsgType == CommandBarMsgType.Warning) {
            tgraphics.setBackgroundColor(TextColor.ANSI.YELLOW);
            tgraphics.setForegroundColor(TextColor.ANSI.WHITE);
        } else if (currentCommandBarMsgType == CommandBarMsgType.Success) {
            tgraphics.setBackgroundColor(TextColor.ANSI.GREEN);
            tgraphics.setForegroundColor(TextColor.ANSI.BLACK);
        }
        if (commandBarMsg != null)
            tgraphics.putString(0, terminalrows-1, commandBarMsg);

        tgraphics.setBackgroundColor(defaultBackgroundColor);
        tgraphics.setForegroundColor(defaultForegroundColor);
    }

    // endregion

    // region helpers

    /**
     * Check if the given coordinates are inside the
     * highligth zone
     * @param row int - Row
     * @param col int - Column
     * @return true if the coordinates are inside, false otherwise
     */
    private boolean inHighligthZone(int row, int col)
    {
        boolean is = false;
        int hlcol1 = this.hlcol1;
        int hlcol2 = this.hlcol2;

        row += rowoff; /* Adapt to screen coordinates */
        col += coloff;

        if (row >= hlrow1 && row <= hlrow2)
        {
            if (hlrow1 == hlrow2) {
                if (col >= hlcol1 && col <= hlcol2)
                    is = true;
            } else {
                if (row == hlrow1 && col >= hlcol1)
                    is = true;
                else if (row == hlrow2 && col <= hlcol2)
                    is = true;
                else if (row > hlrow1 && row < hlrow2)
                    is = true;
                else
                    is = false;
            }
        }
        return is;
    }

    /**
     * Handle values for horizontal and vertical scrolling
     */
    private void scroll()
    {
        if (cy < rowoff) /* The cursor is past the top */
            rowoff = cy;
        if (cy >= rowoff + terminalrows-2) /* The cursor is past the bottom */
            rowoff = cy - (terminalrows-2) + 1;
        if (cx < coloff) /* The cursor is past the left */
            coloff = cx;
        if (cx >= coloff + terminalcols) /* cursor is past the right */
            coloff = cx - terminalcols + 1;
    }

    // endregion
}
