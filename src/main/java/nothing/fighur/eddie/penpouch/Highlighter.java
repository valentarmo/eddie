package nothing.fighur.eddie.penpouch;

import com.googlecode.lanterna.TextColor;

public interface Highlighter extends Tool {
    /**
     * Highlight the text between the marks
     * @param from starting mark
     * @param to closing mark
     */
    void highlight(Mark from, Mark to);

    /**
     * Remove highlighting
     */
    void cleanHighlight();

    /**
     * Get the highlighter's color
     * @return the color
     */
    TextColor getColor();

    /**
     * Set the highlighter's color
     * @param color the color
     */
    void setColor(TextColor color);
}
