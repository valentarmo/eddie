package nothing.fighur.eddie;

import com.google.inject.Inject;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.Terminal;
import nothing.fighur.eddie.folder.Folder;
import nothing.fighur.eddie.penpouch.PenPouch;

import java.io.IOException;

public class Eddie implements Editor {
    private PenPouch penPouch;
    private Folder folder;
    private Terminal terminal;

    @Inject
    public Eddie(Terminal terminal, PenPouch penPouch, Folder folder) {
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
    }

    @Override
    public void saveSheet() {
        getFolder().saveSheet();
    }

    @Override
    public void saveSheetAs(String key) {
        getFolder().saveSheetAs(key);
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
