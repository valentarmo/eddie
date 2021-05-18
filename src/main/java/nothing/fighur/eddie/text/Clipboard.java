package nothing.fighur.eddie.text;

import java.util.List;

public interface Clipboard {
    /**
     * Store the given text in the clipboard.
     * Any text previously stored is replaced by the new text.
     * @param text the text to store
     */
    void store(List<TextCharacter> text);

    /**
     * Retrieve the clipboard's contents
     * @return a list of TextCharacter. An empty list if the clipboard is empty.
     */
    List<TextCharacter> retrieve();
}
