package nothing.fighur.eddie.sheet;

import nothing.fighur.eddie.text.TextCharacter;

import java.util.List;

public interface Sheet {
    /**
     * Write the given text on the sheet.
     * Replaces any text currently on the sheet.
     * @param text list with text characters
     */
    void writeText(List<TextCharacter> text);

    /**
     * Get all text as a CharSequence
     * @return CharSequence
     */
    CharSequence asCharSequence();
}
