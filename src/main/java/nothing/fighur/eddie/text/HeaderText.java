package nothing.fighur.eddie.text;

import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public interface HeaderText extends VisibleText {
    /**
     * Update the header's title. The title is drawn at the header's middle position
     * @param terminal terminal where to draw
     * @param title the title. Will put whatever fits in the assigned section.
     * @throws IOException If there was an underlying I/O error
     */
    void updateTitle(Terminal terminal, String title) throws IOException;

    /**
     * Update the header's subtitle. The subtitle is drawn at the header's leftmost position
     * @param terminal terminal where to draw
     * @param subtitle the subtitle. Will put whatever fits in the assigned section.
     * @throws IOException If there was an underlying I/O error
     */
    void updateSubtitle(Terminal terminal, String subtitle) throws IOException;

    /**
     * Update the logo. The logo is drawn at the header's rightmost position.
     * @param terminal terminal where to draw
     * @param logo character based logo. Will put whatever fits in the assigned section.
     * @throws IOException If there was an underlying I/O error
     */
    void updateLogo(Terminal terminal, String logo) throws IOException;
}
