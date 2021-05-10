package nothing.fighur.eddie.folder;

public interface Folder {
    /**
     * Create a clean editor
     */
    void takeOutCleanSheet();

    /**
     * Create an editor with the content from key
     * @param key content source
     * @return true if the creation was successful, false otherwise
     */
    boolean takeOutSheet(String key);

    /**
     * Save the editor's content
     * @return true if the save was successful, false otherwise
     */
    boolean saveSheet();

    /**
     * Save the editor's content to the specified at key
     * @param key
     * @return true if the save was successful, false otherwise
     */
    boolean saveSheetAs(String key);
}
