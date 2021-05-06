package nothing.fighur.eddie.penpouch;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

public interface PenPouch {
    /**
     * Do the appropriate action according to the provided input.
     * @param keyType key type
     * @param keyStroke key stroke
     */
    void performAction(KeyType keyType, KeyStroke keyStroke);
}
