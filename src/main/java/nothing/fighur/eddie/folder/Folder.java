package nothing.fighur.eddie.folder;

import nothing.fighur.eddie.exceptions.ArtifactExistsException;

public interface Folder {
    /**
     * Create a clean editor
     */
    void takeOutCleanSheet();

    /**
     * Create an editor with the content from the artifact identified by key
     * @param key content source
     * @return true if the creation was successful, false otherwise
     */
    boolean takeOutSheet(String key);

    /**
     * Save the editor's content to the current key.
     * @return true if the save was successful, false otherwise
     */
    boolean saveSheet();

    /**
     * Save the editor's content to the artifact identified by key
     * If the key points to an existent artifact, an ArtifactExistsException will be thrown.
     * @param key the artifact's identifier
     * @throws ArtifactExistsException if the key points to an existent artifact
     * @return true if the save was successful, false otherwise.
     */
    boolean saveSheetAs(String key) throws ArtifactExistsException;

    /**
     * Save the editor's content to the artifact identified by key.
     * @param key the artifact's identifier
     * @return true if the save was successful, false otherwise.
     */
    boolean forceSaveSheetAs(String key);

    /**
     * Get the key of the current opened sheet.
     * @return A String representing the key. An empty string if the sheet hasn't been persisted.
     */
    String getCurrentSheetKey();
}
