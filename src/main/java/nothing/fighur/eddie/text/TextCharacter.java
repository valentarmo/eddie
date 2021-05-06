package nothing.fighur.eddie.text;

import com.googlecode.lanterna.TextColor;
import nothing.fighur.eddie.EditorVariables;

public class TextCharacter {
    private char character;
    private TextColor backgroundColor;
    private TextColor foregroundColor;

    private static TextColor DEFAULT_BACKGROUND_COLOR = EditorVariables.getBackgroundColor();
    private static TextColor DEFAULT_FOREGROUND_COLOR = EditorVariables.getDefaultForegroundColor();

    private TextCharacter() { }

    private TextCharacter(char c, TextColor backgroundColor, TextColor foregroundColor) {
        this.character = c;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
    }

    public static TextCharacter withDefaultCharacteristics(char c) {
        return new TextCharacter(c, DEFAULT_BACKGROUND_COLOR, DEFAULT_FOREGROUND_COLOR);
    }

    public static TextCharacter withBackgroundColor(char c, TextColor backgroundColor) {
        return new TextCharacter(c, backgroundColor, DEFAULT_FOREGROUND_COLOR);
    }

    public static TextCharacter withForegroundColor(char c, TextColor foregroundColor) {
        return new TextCharacter(c, DEFAULT_BACKGROUND_COLOR, foregroundColor);
    }

    public static TextCharacter withBackgroundAndForegroundColors(char c, TextColor backgroundColor, TextColor foregroundColor) {
        return new TextCharacter(c, backgroundColor, foregroundColor);
    }

    public static TextCharacter nullCharacter() {
        return TextCharacter.withDefaultCharacteristics('\0');
    }

    public boolean isNullCharacter() {
        return character == '\0';
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public TextColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(TextColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public TextColor getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(TextColor foregroundColor) {
        this.foregroundColor = foregroundColor;
    }
}
