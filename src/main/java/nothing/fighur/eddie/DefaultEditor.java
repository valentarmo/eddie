package nothing.fighur.eddie;

import com.google.inject.Inject;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.Terminal;
import nothing.fighur.eddie.exceptions.ArtifactExistsException;
import nothing.fighur.eddie.folder.Folder;
import nothing.fighur.eddie.penpouch.PenPouch;

import java.io.IOException;

public class DefaultEditor implements Editor {
    private PenPouch penPouch;
    private Folder folder;
    private Terminal terminal;

    @Inject
    public DefaultEditor(Terminal terminal, PenPouch penPouch, Folder folder) {
        setPenPouch(penPouch);
        setFolder(folder);
        setTerminal(terminal);
    }

    @Override
    public void edit() {
        getFolder().takeOutCleanSheet();
        try {
            while (true) {
                KeyStroke keyStroke = getTerminal().readInput();
                getPenPouch().doAction(keyStroke.getKeyType(), keyStroke);
            }
        } catch (IOException e) { }
    }

    @Override
    public void edit(String key) {
        getFolder().takeOutSheet(key);
        try {
            while (true) {
                KeyStroke keyStroke = getTerminal().readInput();
                getPenPouch().doAction(keyStroke.getKeyType(), keyStroke);
            }
        } catch (IOException e) { }
    }

    @Override
    public boolean takeOutSheet(String key) {
        return getFolder().takeOutSheet(key);
    }


    @Override
    public boolean saveSheet() {
        return getFolder().saveSheet();
    }

    @Override
    public boolean saveSheetAs(String key) throws ArtifactExistsException {
        return getFolder().saveSheetAs(key);
    }

    @Override
    public boolean forceSaveSheetAs(String key) {
        return getFolder().forceSaveSheetAs(key);
    }

    @Override
    public void close() {
        System.exit(0);
    }

    public PenPouch getPenPouch() {
        return penPouch;
    }

    public void setPenPouch(PenPouch penPouch) {
        this.penPouch = penPouch;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }
}
