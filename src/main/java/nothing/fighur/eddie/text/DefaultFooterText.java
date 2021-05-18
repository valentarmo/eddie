package nothing.fighur.eddie.text;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class DefaultFooterText implements FooterText {

    private TextColor defaultBackgroundColor = TextColor.ANSI.WHITE;
    private TextColor defaultForegroundColor = TextColor.ANSI.BLACK;
    private TextColor warningBackgroundColor = TextColor.ANSI.YELLOW;
    private TextColor warningForegroundColor = TextColor.ANSI.WHITE;
    private TextColor errorBackgroundColor = TextColor.ANSI.RED;
    private TextColor errorForegroundColor = TextColor.ANSI.WHITE;
    private TextColor promptBackgroundColor = TextColor.ANSI.BLACK;
    private TextColor promptForegroundColor = TextColor.ANSI.WHITE;

    private String currentText = "";

    private int firstRow;
    private int lastRow;
    private int firstCol;
    private int lastCol;

    @Override
    public void resize(Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) throws IOException {
        setFirstRow(firstRow);
        setLastRow(lastRow);
        setFirstCol(firstCol);
        setLastCol(lastCol);
        draw(terminal, getCurrentText(), getDefaultBackgroundColor(), getDefaultForegroundColor());
    }

    @Override
    public void putWarning(Terminal terminal, String warning) throws IOException {
        setCurrentText(warning);
        draw(terminal, warning, getWarningBackgroundColor(), getWarningForegroundColor());
    }

    @Override
    public void putError(Terminal terminal, String error) throws IOException {
        setCurrentText(error);
        draw(terminal, error, getErrorBackgroundColor(), getErrorForegroundColor());
    }

    @Override
    public void putMessage(Terminal terminal, String message) throws IOException {
        setCurrentText(message);
        draw(terminal, message, getDefaultBackgroundColor(), getDefaultForegroundColor());
    }

    @Override
    public String promptForInput(Terminal terminal, String promptMessage) throws IOException {
        String input = "";
        draw(terminal, promptMessage + input, getPromptBackgroundColor(), getPromptForegroundColor());

        KeyStroke keyStroke;
        KeyType keyType;
        do {
            keyStroke = terminal.readInput();
            keyType = keyStroke.getKeyType();
            if (keyType == KeyType.Character)
                input += keyStroke.getCharacter();
            else if (keyType == KeyType.Backspace) {
                if (input.length() > 0)
                    input = input.substring(0, input.length() - 1);
            }
            draw(terminal, promptMessage + input, getPromptBackgroundColor(), getPromptForegroundColor());
        } while (keyType != KeyType.Escape && keyType != KeyType.Enter);

        if (keyType == KeyType.Escape) input = "";

        draw(terminal, "", getDefaultBackgroundColor(), getDefaultForegroundColor());
        return input;
    }

    private void draw(Terminal terminal, String message, TextColor backgroundColor, TextColor foregroundColor) throws IOException {
        terminal.setForegroundColor(foregroundColor);
        terminal.setBackgroundColor(backgroundColor);
        int textIndex = 0;
        int messageLastRow = getFirstRow();
        int messageLastCol = getFirstCol();
        for (int i = getFirstRow(); i <= getLastRow(); i++) {
            terminal.setCursorPosition(getFirstCol(), i);
            for (int j = getFirstCol(); j < getLastCol(); j++) {
                char c = textIndex < message.length() ? message.charAt(textIndex) : ' ';
                if (textIndex++ == message.length()) {
                    messageLastRow = i;
                    messageLastCol = j;
                }
                if (c == '\n') {
                    continue;
                } else {
                    terminal.putCharacter(c);
                }
            }
        }
        terminal.setCursorPosition(messageLastCol, messageLastRow);
        terminal.flush();
    }

    public TextColor getDefaultBackgroundColor() {
        return defaultBackgroundColor;
    }

    public void setDefaultBackgroundColor(TextColor defaultBackgroundColor) {
        this.defaultBackgroundColor = defaultBackgroundColor;
    }

    public TextColor getDefaultForegroundColor() {
        return defaultForegroundColor;
    }

    public void setDefaultForegroundColor(TextColor defaultForegroundColor) {
        this.defaultForegroundColor = defaultForegroundColor;
    }

    public int getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(int firstRow) {
        this.firstRow = firstRow;
    }

    public int getLastRow() {
        return lastRow;
    }

    public void setLastRow(int lastRow) {
        this.lastRow = lastRow;
    }

    public int getFirstCol() {
        return firstCol;
    }

    public void setFirstCol(int firstCol) {
        this.firstCol = firstCol;
    }

    public int getLastCol() {
        return lastCol;
    }

    public void setLastCol(int lastCol) {
        this.lastCol = lastCol;
    }

    public TextColor getWarningBackgroundColor() {
        return warningBackgroundColor;
    }

    public void setWarningBackgroundColor(TextColor warningBackgroundColor) {
        this.warningBackgroundColor = warningBackgroundColor;
    }

    public TextColor getWarningForegroundColor() {
        return warningForegroundColor;
    }

    public void setWarningForegroundColor(TextColor warningForegroundColor) {
        this.warningForegroundColor = warningForegroundColor;
    }

    public TextColor getErrorBackgroundColor() {
        return errorBackgroundColor;
    }

    public void setErrorBackgroundColor(TextColor errorBackgroundColor) {
        this.errorBackgroundColor = errorBackgroundColor;
    }

    public TextColor getErrorForegroundColor() {
        return errorForegroundColor;
    }

    public void setErrorForegroundColor(TextColor errorForegroundColor) {
        this.errorForegroundColor = errorForegroundColor;
    }

    public TextColor getPromptBackgroundColor() {
        return promptBackgroundColor;
    }

    public void setPromptBackgroundColor(TextColor promptBackgroundColor) {
        this.promptBackgroundColor = promptBackgroundColor;
    }

    public TextColor getPromptForegroundColor() {
        return promptForegroundColor;
    }

    public void setPromptForegroundColor(TextColor promptForegroundColor) {
        this.promptForegroundColor = promptForegroundColor;
    }

    public String getCurrentText() {
        return currentText;
    }

    public void setCurrentText(String currentText) {
        this.currentText = currentText;
    }
}
