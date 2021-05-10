package nothing.fighur.eddie.folder;

import com.google.inject.Inject;
import nothing.fighur.eddie.sheet.Sheet;

public class DefaultFolder implements Folder {

    Sheet sheet;

    @Inject
    public DefaultFolder(Sheet sheet) {
        setSheet(sheet);
    }

    @Override
    public void takeOutCleanSheet() {
        getSheet().refresh();
    }

    @Override
    public boolean takeOutSheet(String key) {
        return false;
    }

    @Override
    public boolean saveSheet() {
        return false;
    }

    @Override
    public boolean saveSheetAs(String key) {
        return false;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }
}
