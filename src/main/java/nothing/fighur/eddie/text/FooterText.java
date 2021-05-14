package nothing.fighur.eddie.text;

import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public interface FooterText extends VisibleText {
    /**
     * Put a warning message
     * @param warning the message
     */
    void putWarning(Terminal terminal, String warning) throws IOException;

    /**
     *  Put an error message
     * @param error the message
     */
    void putError(Terminal terminal, String error) throws IOException;

    /**
     * Put a message
     * @param message the message
     */
    void putMessage(Terminal terminal, String message) throws IOException;

    /**
     * Promp the user for input
     * @param promptMessage message to show to the user
     * @return the user's input
     */
    String promptForInput(Terminal terminal, String promptMessage) throws IOException;
}
