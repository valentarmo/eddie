package nothing.fighur.eddie;

import com.googlecode.lanterna.TextColor;

/**
 * Global configuration variables
 */
public class EditorVariables {

    private static final TextColor DEFAULT_BACKGROUND_COLOR = TextColor.ANSI.BLACK;
    private static final TextColor DEFAULT_FOREGROUND_COLOR = TextColor.ANSI.WHITE;
    private static TextColor backgroundColor = DEFAULT_BACKGROUND_COLOR;
    private static TextColor foregroundColor = DEFAULT_FOREGROUND_COLOR;
    private static int tabStop = 4;

    public static TextColor getDefaultBackgroundColor() {
        return DEFAULT_BACKGROUND_COLOR;
    }

    public static TextColor getDefaultForegroundColor() {
        return DEFAULT_FOREGROUND_COLOR;
    }

    public static TextColor getBackgroundColor() {
        return backgroundColor;
    }

    public static void setBackgroundColor(TextColor backgroundColor) {
        EditorVariables.backgroundColor = backgroundColor;
    }

    public static TextColor getForegroundColor() {
        return foregroundColor;
    }

    public static void setForegroundColor(TextColor foregroundColor) {
        EditorVariables.foregroundColor = foregroundColor;
    }

    public static int getTabStop() {
        return tabStop;
    }

    public static void setTabStop(int tabStop) {
        EditorVariables.tabStop = tabStop;
    }
}
