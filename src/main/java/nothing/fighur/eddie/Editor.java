package nothing.fighur.eddie;

import com.googlecode.lanterna.terminal.*;
import com.googlecode.lanterna.input.*;

import nothing.fighur.eddie.Sheet.CommandBarMsgType;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Editor
{
    private Text text;
    private Sheet sheet;
    private boolean starting = false;
    private final Terminal terminal;
    private String currentFileName = "";
    private String currentFilePath = "";
    private Mode currentMode = Mode.Normal;
    private int hasModified = 0; // TODO move to Text
    private Mark fixedMark;
    private Mark cursorMark;
    private final int tabstop = 8;

    public enum Mode {
        Insert, Normal, Visual
    }

    private Editor(Terminal terminal) throws IOException
    {
        this.terminal = terminal;
        this.text = new Text(tabstop);
        this.sheet = new Sheet(terminal, text, this);
    }

    private Editor(Terminal terminal, String filePath) throws IOException
    {
        starting = true;
        this.terminal = terminal;
        this.text = new Text(tabstop);
        this.sheet = new Sheet(terminal, text, this);
        openFile(filePath);
        starting = false;
    }

    public static Editor createEmpty(Terminal terminal) throws IOException
    { return new Editor(terminal); }

    public static Editor createWithFile(Terminal terminal, String filePath) throws IOException
    { return new Editor(terminal, filePath); }

    /**
     * Start editing the document i.e display the text
     * and handle user input
     */
    public void edit() throws IOException
    {
        while (true) {
            sheet.refreshScreen();
            KeyStroke keyStroke = terminal.readInput();
            handleInput(keyStroke.getKeyType(), keyStroke);
        }
    }

    // region Getters

    /**
     * Get the Mode the editor is currently in
     * @return Mode - Insert, Normal or Visual
     */
    public Mode getMode()
    { return currentMode; }

    /**
     * Get the name of the file currently being edited
     * @return String - the name
     */
    public String getCurrentFileName()
    { return currentFileName; }

    // endregion

    // region Input management

    private void handleInput(KeyType keyType, KeyStroke key) throws IOException
    {
        int cursorRow = sheet.getCursorRow();
        int cursorColumn = sheet.getCursorColumn();
        int nrows = text.size();
        String row = (cursorRow < nrows) ? text.getTextAt(cursorRow): null;

        switch (keyType) {
            case ArrowDown:
                if (cursorRow < nrows-1)
                    sheet.changeCursorRow(++cursorRow);
                if (currentMode == Mode.Visual)
                    cursorMark.setRow(cursorRow);
                break;
            case ArrowUp:
                if (cursorRow != 0)
                    sheet.changeCursorRow(--cursorRow);
                if (currentMode == Mode.Visual)
                    cursorMark.setRow(cursorRow);
                break;
            case ArrowLeft:
                if (cursorColumn != 0) {
                    sheet.changeCursorColumn(--cursorColumn);
                    if (currentMode == Mode.Visual)
                        cursorMark.setColumn(cursorColumn);
                }
                else if (cursorRow > 0) {
                    sheet.changeCursorRow(--cursorRow);
                    row = text.getTextAt(cursorRow);
                    sheet.changeCursorColumn(row.length());
                    if (currentMode == Mode.Visual) {
                        cursorMark.setRow(cursorRow);
                        cursorMark.setColumn(row.length());
                    }
                }
                break;
            case ArrowRight:
                if (row != null && cursorColumn < row.length()) {
                    sheet.changeCursorColumn(++cursorColumn);
                    if (currentMode == Mode.Visual)
                        cursorMark.setColumn(cursorColumn);
                }
                else if (row != null && cursorColumn == row.length() && cursorRow < nrows-1) {
                    sheet.changeCursorRow(++cursorRow);
                    sheet.changeCursorColumn(0);
                    cursorColumn = 0;
                    if (currentMode == Mode.Visual) {
                        cursorMark.setRow(cursorRow);
                        cursorMark.setColumn(0);
                    }
                }
                break;
            case PageUp:
            case PageDown:
                if (currentMode != Mode.Visual)
                {
                    int rowoff = sheet.getRowOffset();
                    if (keyType == KeyType.PageUp)
                        sheet.changeCursorRow(rowoff);
                    else
                        sheet.changeCursorRow(rowoff+terminal.getTerminalSize().getRows()-1);
                    cursorRow = sheet.getCursorRow();
                    if (cursorRow > nrows)
                        sheet.changeCursorRow(nrows);
                    cursorRow = sheet.getCursorRow();
                    int times = nrows;
                    while (times-- > 0)
                        handleInput((keyType == KeyType.PageUp ? KeyType.ArrowUp : KeyType.ArrowDown), null);
                }
                break;
            case Home:
                sheet.changeCursorColumn(0);
                if (currentMode == Mode.Visual) {
                    cursorMark.setColumn(0);
                }
                break;
            case End:
                if (cursorRow < nrows) {
                    sheet.changeCursorColumn(row.length());
                    if (currentMode == Mode.Visual) {
                        cursorMark.setColumn(row.length());
                    }
                }
                break;
            case Escape:
                if (currentMode == Mode.Visual) {
                    sheet.clearHighlight();
                    if (fixedMark.compareTo(cursorMark) > 0) {
                        sheet.changeCursorRow(cursorMark.getRow());
                        sheet.changeCursorColumn(cursorMark.getColumn());
                    } else {
                        sheet.changeCursorRow(fixedMark.getRow());
                        sheet.changeCursorColumn(fixedMark.getColumn());
                    }
                } else if (currentMode != Mode.Normal)
                    sheet.changeCursorColumn(sheet.getCursorColumn()-1);
                currentMode = Mode.Normal;
                break;
            case Enter:
                if (currentMode == Mode.Insert)
                    insertNewLine(sheet.getCursorRow(), sheet.getCursorColumn());
                break;
            case Backspace:
                if (currentMode == Mode.Insert)
                    deleteChar(sheet.getCursorRow(), sheet.getCursorColumn());
                else
                    handleInput(KeyType.ArrowLeft, null);
                break;
            case Delete: // TODO: Fix when at the end of line
                handleInput(KeyType.ArrowRight, null);
                deleteChar(sheet.getCursorRow(), sheet.getCursorColumn());
                break;
            case Tab:
                if (currentMode == Mode.Insert) {
                    insertChar(cursorRow, cursorColumn, '\t');
                }
                break;
            case Character:
                if (currentMode == Mode.Normal)
                    handleInNormalMode(key.getCharacter());
                else if (currentMode == Mode.Visual) {
                    handleInVisualMode(key.getCharacter());
                } else {
                    insertChar(cursorRow, cursorColumn, key.getCharacter());
                }
                break;
            default:
                break;
        }

        /* Update cursor position in case the row changed */
        row = (cursorRow >= nrows) ? null : text.getTextAt(cursorRow);
        int rowlen = (row != null) ? row.length() : 0;
        if (cursorColumn > rowlen) {
            sheet.changeCursorColumn(rowlen);
            if (currentMode == Mode.Visual)
                cursorMark.setColumn(rowlen);
        }

        if (currentMode == Mode.Visual) { /* Highlight */
            if (fixedMark.compareTo(cursorMark) > 0)
                sheet.highlight(cursorMark.getColumn(), cursorMark.getRow(), fixedMark.getColumn(), fixedMark.getRow());
            else
                sheet.highlight(fixedMark.getColumn(), fixedMark.getRow(), cursorMark.getColumn(), cursorMark.getRow());
        }
    }

    /**
     * Handle characters when in normal mode
     * @param c char - the character to handle
     */
    private void handleInNormalMode(char c) throws IOException
    {
        int row, col;

        switch (c) {
            case ':':
                String command = promptForInput(":", CommandBarMsgType.Normal);
                handleCommand(command);
                break;
            case 'a':
                if (sheet.getCursorColumn() < text.getTextAt(sheet.getCursorRow()).length())
                    handleInput(KeyType.ArrowRight, null);
                currentMode = Mode.Insert;
                break;
            case 'A':
                handleInput(KeyType.End, null);
                currentMode = Mode.Insert;
                break;
            case 'I':
                handleInput(KeyType.Home, null);
                currentMode = Mode.Insert;
                break;
            case 'i':
                currentMode = Mode.Insert;
                break;
            case 'l':
                handleInput(KeyType.ArrowRight, null);
                break;
            case 'k':
                handleInput(KeyType.ArrowUp, null);
                break;
            case 'j':
                handleInput(KeyType.ArrowDown, null);
                break;
            case 'h':
                handleInput(KeyType.ArrowLeft, null);
                break;
            case 'o':
                row = sheet.getCursorRow();
                col = row < text.size() ? text.getTextAt(row).length() : 0;
                insertNewLine(row, col);
                currentMode = Mode.Insert;
                break;
            case 'O':
                row = sheet.getCursorRow();
                if (row > 0) {
                    sheet.changeCursorRow(--row);
                    col = row < text.size() ? text.getTextAt(row).length() : 0;
                    insertNewLine(row, col);
                    hasModified++;
                } else {
                    col = 0;
                    insertNewLine(row, col);
                    sheet.changeCursorRow(row);
                    hasModified++;
                }
                currentMode = Mode.Insert;
                break;
            case 'w':
                row = sheet.getCursorRow();
                col = sheet.getCursorColumn();
                moveToStartOfNextWord(row, col);
                break;
            case 'b':
                row = sheet.getCursorRow();
                col = sheet.getCursorColumn();
                moveToStartOfPreviousWord(row, col);
                break;
            case 'e':
                row = sheet.getCursorRow();
                col = sheet.getCursorColumn();
                moveToEndOfNextWord(row, col);
                break;
            case '0':
                handleInput(KeyType.Home, null);
                break;
            case '$':
                handleInput(KeyType.End, null);
                break;
            case 'p':
                if (cursorMark != null && fixedMark != null) {
                    text.paste(sheet.getCursorRow(), sheet.getCursorColumn());
                    hasModified++;
                }
                break;
            case 'x':
                handleInput(KeyType.Delete, null);
                break;
            case 'X':
                deleteChar(sheet.getCursorRow(), sheet.getCursorColumn());
                break;
            case 'v':
                currentMode = Mode.Visual;
                row = sheet.getCursorRow();
                col = sheet.getCursorColumn();
                fixedMark = new Mark(row, col);
                cursorMark = new Mark(row, col);
                break;
            default:
                break;
        }
    }

    /**
     * Handle characters when in visual mode
     * @param c char - the character to handle
     */
    private void handleInVisualMode(char c) throws IOException
    {
        switch (c) {
            case 'l':
                handleInput(KeyType.ArrowRight, null);
                break;
            case 'k':
                handleInput(KeyType.ArrowUp, null);
                break;
            case 'j':
                handleInput(KeyType.ArrowDown, null);
                break;
            case 'h':
                handleInput(KeyType.ArrowLeft, null);
                break;
            case '0':
                handleInput(KeyType.Home, null);
                break;
            case '$':
                handleInput(KeyType.End, null);
                break;
            case 'd':
                if (fixedMark.compareTo(cursorMark) > 0) {
                    text.copy(cursorMark, fixedMark);
                    text.deleteRegion(cursorMark, fixedMark);
                } else {
                    text.copy(fixedMark, cursorMark);
                    text.deleteRegion(fixedMark, cursorMark);
                }
                hasModified++;
                handleInput(KeyType.Escape, null);
                break;
            case 'y':
                if (fixedMark.compareTo(cursorMark) > 0)
                    text.copy(cursorMark, fixedMark);
                else
                    text.copy(fixedMark, cursorMark);
                handleInput(KeyType.Escape, null);
                break;
            default:
                break;
        }
    }

    /**
     * Handle a command typed by the user, this would be
     * commands like quit, open, save...
     */
    private void handleCommand(String command) throws IOException
    {
        if (command == null)
            return;

        switch (command) {
            case "":
                break; /* Empty command. Do nothing */
            case "quit":
                if (hasModified == 0)
                    System.exit(0);
                else {
                    String msg = "There have been changes. Exit? (y/n) ";
                    String response = promptForInput(msg, CommandBarMsgType.Warning);
                    while (response != null && !response.equals("y") && !response.equals("n"))
                        response = promptForInput("Please enter y or n: ", CommandBarMsgType.Warning);
                    if (response != null && response.equals("y"))
                        System.exit(0);
                    else {
                        promptWarning("Operation canceled");
                        return;
                    }
                }
                break;
            case "open":
                if (hasModified > 0) {
                    String msg = "There have been changes. Continue? (y/n) ";
                    String response = promptForInput(msg, CommandBarMsgType.Warning);
                    while (response != null && !response.equals("y") && !response.equals("n"))
                        response = promptForInput("Please enter y or n: ", CommandBarMsgType.Warning);
                    if (response != null && response.equals("y"))
                        ;
                    else {
                        promptWarning("Operation canceled");
                        return;
                    }
                }

                String prompt = "Type the path: ";
                String path = promptForInput(prompt, CommandBarMsgType.Normal);
                if (path != null) {
                    openFile(path);
                } else {
                    promptWarning("Operation canceled");
                    return;
                }
                break;
            case "save":
                saveFile();
                break;
            default:
                String emsg = "Not and editor command: " + command;
                promptError(emsg);
                break;
        }
    }

    /**
     * Insert a character at the given row and column
     * @param row int - the row
     * @param col int - the column
     * @param c char - the character
     */
    public void insertChar(int row, int col, char c)
    {
        if (row == text.size())
            text.appendLine("");
        text.insertChar(row, col, c);
        /* Deal with those evil tabs */
        if (c == '\t') {
            while (++col % tabstop != 0)
                ;
            sheet.changeCursorColumn(col);
        } else {
            sheet.changeCursorColumn(++col);
        }
        hasModified++;
    }

    /**
     * Insert a new line at the given row and column
     * @param row int - the row
     * @param col int - the column
     */
    public void insertNewLine(int row, int col)
    {
        if (col == 0 && row == 0) /* Inserting at the beginning of the file */
            text.insertLine(0, "");
        else if (row == text.size()) /* Inserting at the end of a line */
            text.appendLine("");
        else { /* Inserting in the middle of a line */
            String line = text.getTextAt(row);
            text.insertLine(row+1, line.substring(col));
            text.deleteSubstring(row, col);
        }
        sheet.changeCursorColumn(0);
        sheet.changeCursorRow(row+1);
        hasModified++;
    }

    /**
     * Delete the character in the given row and before the given column
     * @param row int - the row
     * @param col int - the column
     */
    public void deleteChar(int row, int col)
    {
        if (row == text.size())
            return;
        if (row == 0 && col == 0)
            return;
        if (col > 0) {
            char c = text.getTextAt(row).charAt(col-1);
            text.deleteChar(row, col - 1);
            sheet.changeCursorColumn(col - 1);
        } else { /* Deleting at the beginning of a line */
            int pRowLength = text.getTextAt(row-1).length();
            sheet.changeCursorColumn(pRowLength);
            text.appendString(row-1, text.getTextAt(row));
            text.deleteLine(row);
            sheet.changeCursorRow(row-1);
        }
        hasModified++;
    }

    /**
     * Move the cursor to the end of the current word
     * of it is already there, to the end of the next
     * word, by word I mean a string delimited by empty
     * spaces
     * @param row int - cursor row
     * @param col int - cursor col
     */
    private void moveToEndOfNextWord(int row, int col)
    {
        String line = row < text.size() ? text.getTextAt(row) : null;
        if (line != null)
            if (col + 1 < line.length())
                if (Character.isWhitespace(line.charAt(col + 1)))
                    moveToEndOfNextWordAux(row, col + 1);
                else
                    moveToEndOfNextWordAux(row, col);
            else if (col == line.length()-1)
                moveToEndOfNextWordAux(row+1, 0);
            else
                moveToEndOfNextWordAux(row, col);
    }

    /**
     * Helper method to move the cursor to the end of
     * the current or next word
     * @param row int - cursor row
     * @param col int - cursor col
     */
    private void moveToEndOfNextWordAux(int row, int col)
    {
        if (row >= text.size())
            return;

        String line = text.getTextAt(row);
        if (col == line.length() && row + 1 < text.size()) {
            sheet.changeCursorRow(++row);
            sheet.changeCursorColumn(0);
            col = 0;
            line = text.getTextAt(row);
        }

        int length = line.length();

        if (col < length && !Character.isWhitespace(line.charAt(col))) {
            while (col < length && !Character.isWhitespace(line.charAt(col)))
                col++;
            sheet.changeCursorRow(row);
            sheet.changeCursorColumn(--col);
        } else if (col < length && Character.isWhitespace(line.charAt(col))) {
            moveToEndOfNextWordAux(row, ++col);
        }
    }

    /**
     * Move the cursor to the start of the next
     * word, by word I mean a string delimited by
     * empty spaces
     * @param row int - cursor row
     * @param col int - cursor col
     */
    private void moveToStartOfNextWord(int row, int col)
    {
        String line = row < text.size() ? text.getTextAt(row) : null;
        if (line != null)
            if (col + 1 < line.length())
                if (Character.isWhitespace(line.charAt(col + 1)))
                    moveToStartOfNextWordAux(row, col + 1);
                else {
                    moveToStartOfNextWord(row, ++col);
                }
            else if (col == line.length()-1)
                moveToStartOfNextWordAux(row+1, 0);
            else
                moveToStartOfNextWordAux(row, col);
    }

    /**
     * Helper method to move the cursor to the end of
     * the current or next word
     * @param row int - cursor row
     * @param col int - cursor col
     */
    private void moveToStartOfNextWordAux(int row, int col)
    {
        if (row >= text.size())
            return;

        String line = text.getTextAt(row);
        if (col == line.length() && row + 1 < text.size()) {
            sheet.changeCursorRow(++row);
            sheet.changeCursorColumn(0);
            col = 0;
            line = text.getTextAt(row);
        } else if (col < line.length() && !Character.isWhitespace(line.charAt(col))) {
            sheet.changeCursorRow(row);
            sheet.changeCursorColumn(col);
            return;
        }

        int length = line.length();

        if (col < length && Character.isWhitespace(line.charAt(col))) {
            moveToStartOfNextWordAux(row, ++col);
        }
    }

    /**
     * Move the cursor to the start of the current word
     * of it is already there, to the start of the previous
     * word, by word I mean a string delimited by empty
     * spaces
     * @param row int - cursor row
     * @param col int - cursor col
     */
    private void moveToStartOfPreviousWord(int row, int col)
    {
        String line = row < text.size() ? text.getTextAt(row) : null;
        if (line != null)
            if (col > 0) {
                if (col == line.length())
                    col--;
                if (Character.isWhitespace(line.charAt(col - 1)))
                    moveToStartOfPreviousWordAux(row, col - 1);
                else
                    moveToStartOfPreviousWordAux(row, col);
            } else if (col == 0) {
                if (row - 1 >= 0) {
                    int tmp = text.getTextAt(row-1).length() - 1;
                    moveToStartOfPreviousWordAux(row-1, tmp);
                }
            } else
                moveToStartOfPreviousWordAux(row, col);
    }

    /**
     * Helper method to move the cursor to the start of
     * the current or previous word
     * @param row int - cursor row
     * @param col int - cursor col
     */
    private void moveToStartOfPreviousWordAux(int row, int col)
    {
        if (row < 0)
            return;

        String line = text.getTextAt(row);
        if (col < 0 && row - 1 >= 0) {
            sheet.changeCursorRow(--row);
            line = text.getTextAt(row);
            col = line.length() - 1;
            sheet.changeCursorColumn(col);
        }

        if (col >= 0 && !Character.isWhitespace(line.charAt(col))) {
            while (col > 0 && !Character.isWhitespace(line.charAt(col)))
                col--;
            sheet.changeCursorRow(row);
            if (col == 0)
                sheet.changeCursorColumn(col);
            else
                sheet.changeCursorColumn(++col);
        } else if (col >= 0 && Character.isWhitespace(line.charAt(col))) {
            moveToStartOfPreviousWordAux(row, --col);
        } else if (row == 0) {
            sheet.changeCursorColumn(line.length());
            sheet.changeCursorRow(row);
        }
    }

    // endregion

    // region prompts

    /**
     * Prompt the user to enter something on the command bar
     * @param msg String - the message
     * @param type CommandBarMsgType - Prompt as a Warning, Error, or Normal
     * @return String - the user's input or null if the user cancels
     */
    private String promptForInput(String msg, CommandBarMsgType type) throws IOException
    {
        /* To prevent the user from deleting the original message
        and to deliver the input */
        int originalLength = msg.length();

        sheet.setCommandBarMsg(msg, type);
        KeyStroke keyStroke = terminal.readInput();
        KeyType keyType = keyStroke.getKeyType();

        if (keyType == KeyType.Character)
            msg += keyStroke.getCharacter();
        sheet.setCommandBarMsg(msg, type);

        while (keyType != KeyType.Escape && keyType != KeyType.Enter) {
            keyStroke = terminal.readInput();
            keyType = keyStroke.getKeyType();
            if (keyType == KeyType.Character)
                msg += keyStroke.getCharacter();
            else if (keyType == KeyType.Backspace) {
                if (msg.length() > originalLength)
                    msg = msg.substring(0, msg.length()-1);
            }
            sheet.setCommandBarMsg(msg, type);
        }

        if (keyType == KeyType.Escape) /* cancelled */
            msg = null;
        sheet.setCommandBarMsg("", CommandBarMsgType.Normal); /* clean the command bar */
        return (msg == null) ? null : msg.substring(originalLength); /* Remove the prompt message */
    }

    /**
     * Display a warning message on the command bar
     * @param msg String - the message
     */
    private void promptWarning(String  msg) throws IOException
    {
        sheet.setCommandBarMsg("WARNING: " + msg, CommandBarMsgType.Warning);
    }

    /**
     * Display an error message on the command bar
     * @param msg String - the message
     */
    private void promptError(String msg) throws IOException
    {
        sheet.setCommandBarMsg("ERROR: " + msg, CommandBarMsgType.Error);
    }

    private void promptSuccess(String msg) throws IOException
    {
        sheet.setCommandBarMsg("SUCCESS: " + msg, CommandBarMsgType.Success);
    }

    // endregion

    // region File management

    /**
     * Open or create a new file
     * @param filePath String - path leading to the file
     */
    private void openFile(String filePath) throws IOException
    {
        File file = new File(filePath);
        Text tmp = new Text(tabstop);

        if (file.createNewFile()) {
            text = tmp;
            currentFileName = file.getName();
            currentFilePath = filePath;
            hasModified = 0;
            promptSuccess("Created file - " + currentFileName);
            sheet.changeText(text);
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
            {
                String content = reader.readLine();
                while (content != null) {
                    tmp.appendLine(content);
                    content = reader.readLine();
                }
                text = tmp;
                sheet.changeText(tmp);
                currentFileName = file.getName();
                currentFilePath = filePath;
                hasModified = 0;
                if (!starting) promptSuccess("File opened");
            } catch (IOException e) {
                promptError("Couldn't open the file");
            }
        }
    }

    /**
     * Save changes to the current file or to a new one
     * if there is no current file
     */
    private void saveFile() throws IOException
    {
        String path = currentFilePath;
        File file = null;

        if (path.length() == 0) { /* new file */
            path = promptForInput("Insert a path to save the file: ", CommandBarMsgType.Normal);
            if (path == null) {
                promptWarning("Save canceled");
                return;
            }
            file = new File(path);
            if (!file.createNewFile()) { /* File already exists */
                String msg = "There is already a file in this path. Override? (y/n) ";
                String response = promptForInput(msg, CommandBarMsgType.Warning);
                if (response != null) {
                    while (response != null && !response.equals("y") && !response.equals("n"))
                        response = promptForInput("Please enter y or n: ", CommandBarMsgType.Warning);
                    if (response.equals("n")) {
                        promptWarning("Save canceled");
                        return;
                    }
                } else {
                    promptWarning("Save canceled");
                    return;
                }
            }
        }

        file = new File(path);
        try (BufferedWriter writer = new BufferedWriter (new FileWriter(path))) {
            writer.write(text.getWritableText());
            currentFilePath = path;
            currentFileName = file.getName();
            hasModified = 0;
            promptSuccess("Changes have been saved");
        } catch (IOException e) {
            promptError("Couldn't save the File");
        }
    }

    // endregion

    // region tutorial

    private void displayHelp()
    {
        // TODO
        try {
            promptError("Tutorial not yet implemented");
        } catch(IOException e) {
            try {
                promptError(e.getMessage());
            } catch (IOException e2) { }
        }
    }

    // endregion
}