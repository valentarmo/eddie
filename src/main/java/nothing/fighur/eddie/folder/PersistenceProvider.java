package nothing.fighur.eddie.folder;

import java.io.IOException;

public interface PersistenceProvider {
    /**
     * Save text to the artifact identified by the key.
     * @param text the text
     * @param key the artifact identifier
     */
    void persist(CharSequence text, String key) throws IOException;

    /**
     * Read text from the artifact identified by the key.
     * @param key the artifact identifier
     * @return a CharSequence with the artifact's contents
     */
    CharSequence load(String key) throws IOException;
}
