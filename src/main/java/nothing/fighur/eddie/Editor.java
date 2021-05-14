package nothing.fighur.eddie;

public interface Editor {
    /**
     * Start editing
     */
    void edit();

    /**
     * Edit the contents of the artifact identified by key
     * @param key the artifact's identifier
     */
    void edit(String key);

    /**
     * Save the editor's content
     */
    void saveSheet();

    /**
     * Save the editor's content to the specified at key
     * @param key
     */
    void saveSheetAs(String key);
}
