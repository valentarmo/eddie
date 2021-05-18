package nothing.fighur.eddie;

import nothing.fighur.eddie.exceptions.ArtifactExistsException;

public interface Editor {
    /**
     * Start editing
     */
    void edit();

    /**
     * Start editing. With the contents of the artifact identified by key
     * @param key the artifact's identifier
     */
    void edit(String key);

    /**
     * Edit the contents of the artifact identified by key
     * @param key the artifact's identifier
     * @return true if the creation was successful, false otherwise
     */
    boolean takeOutSheet(String key);

    /**
     * Save the editor's content
     * @return true if the save was successful, false otherwise
     */
    boolean saveSheet();

    /**
     * Save the editor's content to the artifact identified by key
     * If the key points to an existent artifact, an ArtifactExistsException will be thrown.
     * @param key  the artifact's identifier
     * @throws ArtifactExistsException if the key points to an existent artifact
     * @return true if the save was successful, false otherwise
     */
    boolean saveSheetAs(String key) throws ArtifactExistsException;

    /**
     * Save the editor's content to the artifact identified by key
     * @param key  the artifact's identifier
     * @return true if the save was successful, false otherwise
     */
    boolean forceSaveSheetAs(String key);

    /**
     * Close the editor
     */
    void close();
}
