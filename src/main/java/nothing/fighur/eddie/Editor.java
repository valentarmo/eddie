package nothing.fighur.eddie;

public interface Editor {
    /**
     * Start editing
     */
    void edit();

    /**
     * Edit file
     * @param file the file's path
     */
    void edit(String file);
}
