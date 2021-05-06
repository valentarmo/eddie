package nothing.fighur.eddie.text;

import com.googlecode.lanterna.terminal.Terminal;

public interface VisibleText {
    /**
     * Resize the text accordingly
     * @param terminal terminal where to draw itself
     * @param firstRow starting row of the text's zone within the provided terminal
     * @param lastRow final row of the text's zone within the provided terminal
     * @param firstCol starting column of the text's zone within the provided terminal
     * @param lastCol final column of the text's zone within the provided terminal
     */
    void resize(Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol);
}
