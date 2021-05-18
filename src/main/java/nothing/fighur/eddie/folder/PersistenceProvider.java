package nothing.fighur.eddie.folder;

import nothing.fighur.eddie.exceptions.ArtifactExistsException;

import java.io.IOException;

public interface PersistenceProvider {
    /**
     * Save text to an artifact identified by the key.
     * If the artifact already exists, an exception will be thrown.
     * @param text the text
     * @param key the artifact identifier
     * @throws IOException If there was an underlying I/O error
     * @throws ArtifactExistsException if the artifact already exists
     */
    void persist(CharSequence text, String key) throws IOException, ArtifactExistsException;

    /**
     * Save text to the artifact identified by the key.
     * If the artifact already exists, it's contents are overridden.
     * @param text the text
     * @param key the artifact identifier
     * @throws IOException If there was an underlying I/O error
     */
    void persistOverride(CharSequence text, String key) throws IOException;

    /**
     * Read text from the artifact identified by the key.
     * @param key the artifact identifier
     * @return a CharSequence with the artifact's contents
     */
    CharSequence load(String key) throws IOException;
}
