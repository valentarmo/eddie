package nothing.fighur.eddie.folder;

public interface Folder {
    boolean takeOutCleanSheet();
    boolean takeOutSheet(String key);
    boolean saveSheet();
    boolean saveSheetAs(String key);
}
