package nothing.fighur.eddie;

public interface Editor {
    /**
     * Start editing
     */
    void edit();

    /**
     * Edit the contents of the artifact identified by key
     * @param key the artifact's identifier
     * @return true if the creation was successful, false otherwise
     */
    boolean edit(String key);

    /**
     * Save the editor's content
     * @return true if the save was successful, false otherwise
     */
    boolean saveSheet();

    /**
     * Save the editor's content to the artifact identified by key
     * @param key  the artifact's identifier
     * @return true if the save was successful, false otherwise
     */
    boolean saveSheetAs(String key);

    /**
     * Close the editor
     */
    void close();
}
