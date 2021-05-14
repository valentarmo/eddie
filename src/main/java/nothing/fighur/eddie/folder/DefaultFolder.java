package nothing.fighur.eddie.folder;

import com.google.inject.Inject;
import nothing.fighur.eddie.sheet.Sheet;
import nothing.fighur.eddie.text.TextCharacter;

import java.io.IOException;
import java.util.LinkedList;

public class DefaultFolder implements Folder {

    private Sheet sheet;
    private PersistenceProvider persistenceProvider;
    private String currentSheetKey = "";

    @Inject
    public DefaultFolder(Sheet sheet, PersistenceProvider persistenceProvider) {
        setSheet(sheet);
        setPersistenceProvider(persistenceProvider);
    }

    @Override
    public void takeOutCleanSheet() {
        getSheet().writeText(new LinkedList<>());
    }

    @Override
    public boolean takeOutSheet(String key) {
        try {
            CharSequence text = getPersistenceProvider().load(key);
            LinkedList<TextCharacter> characterList = new LinkedList<>();
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                characterList.add(TextCharacter.withDefaultCharacteristics(c));
            }
            setCurrentSheetKey(key);
            getSheet().writeText(characterList);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean saveSheet() {
        try {
            if (getCurrentSheetKey().isEmpty())
                return false;
            getPersistenceProvider().persist(getSheet().asCharSequence(), getCurrentSheetKey());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean saveSheetAs(String key) {
        try {
            if (key.isEmpty())
                return false;
            setCurrentSheetKey(key);
            getPersistenceProvider().persist(getSheet().asCharSequence(), getCurrentSheetKey());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String getCurrentSheetKey() {
        return this.currentSheetKey;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public PersistenceProvider getPersistenceProvider() {
        return persistenceProvider;
    }

    public void setPersistenceProvider(PersistenceProvider persistenceProvider) {
        this.persistenceProvider = persistenceProvider;
    }

    public void setCurrentSheetKey(String currentSheetKey) {
        this.currentSheetKey = currentSheetKey;
    }
}
