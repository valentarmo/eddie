package nothing.fighur.eddie.penpouch;

import com.google.inject.Inject;
import com.googlecode.lanterna.TextColor;
import nothing.fighur.eddie.sheet.SheetContent;
import nothing.fighur.eddie.sheet.SheetFooter;
import nothing.fighur.eddie.sheet.SheetHeader;
import nothing.fighur.eddie.text.*;

import java.util.ArrayList;
import java.util.List;

public class SuperPen implements Pencil, Highlighter, Glue, Scissors {

    private SheetContent sheetContent;
    private SheetHeader sheetHeader;
    private SheetFooter sheetFooter;
    private Clipboard clipboard;

    private List<ToolObserver> toolObservers = new ArrayList<>();
    private TextColor highlighterColor = TextColor.ANSI.BLUE;

    @Inject
    public SuperPen(SheetContent sheetContent, SheetHeader sheetHeader, SheetFooter sheetFooter, Clipboard clipboard) {
        setSheetContent(sheetContent);
        setSheetHeader(sheetHeader);
        setSheetFooter(sheetFooter);
        setClipboard(clipboard);
    }

    @Override
    public void insertCharacter(TextCharacter character, Position position) {
        notifyToolObservers(getSheetContent().insertCharacter(character, position));
    }

    @Override
    public void deleteCharacter(Position position) {
        notifyToolObservers(getSheetContent().deleteCharacter(position));
    }

    @Override
    public void insertNewLine(Position position) {
        notifyToolObservers(getSheetContent().insertNewLine(position));
    }

    @Override
    public void writeTitle(String title) {
        getSheetHeader().updateTitle(title);
    }

    @Override
    public void writeSubtitle(String subtitle) {
        getSheetHeader().updateSubtitle(subtitle);
    }

    @Override
    public void writeLogo(String logo) {
        getSheetHeader().updateLogo(logo);
    }

    @Override
    public void writeWarning(String warning) {
        getSheetFooter().putWarning(warning);
    }

    @Override
    public void writeError(String error) {
        getSheetFooter().putError(error);
    }

    @Override
    public void writeMessage(String message) {
        getSheetFooter().putMessage(message);
    }

    @Override
    public String promptForInput(String prompt) {
        return getSheetFooter().promptForInput(prompt);
    }

    public SheetContent getSheetContent() {
        return sheetContent;
    }

    public void setSheetContent(SheetContent sheetContent) {
        this.sheetContent = sheetContent;
    }

    public SheetHeader getSheetHeader() {
        return sheetHeader;
    }

    public void setSheetHeader(SheetHeader sheetHeader) {
        this.sheetHeader = sheetHeader;
    }

    public SheetFooter getSheetFooter() {
        return sheetFooter;
    }

    public void setSheetFooter(SheetFooter sheetFooter) {
        this.sheetFooter = sheetFooter;
    }

    public Clipboard getClipboard() {
        return clipboard;
    }

    public void setClipboard(Clipboard clipboard) {
        this.clipboard = clipboard;
    }

    @Override
    public void highlight(Mark from, Mark to) {
        getSheetContent().setHighlightColor(getColor());
        getSheetContent().setHighlightMarks(from, to);
    }

    @Override
    public void cleanHighlight() {
        getSheetContent().cleanHighlight();
    }

    @Override
    public TextColor getColor() {
        return highlighterColor;
    }

    @Override
    public void setColor(TextColor color) {
        highlighterColor = color;
    }

    @Override
    public void addToolObserver(ToolObserver toolObserver) {
        if (!toolObservers.contains(toolObserver))
            toolObservers.add(toolObserver);
    }

    @Override
    public void removeToolObserver(ToolObserver toolObserver) {
        toolObservers.remove(toolObserver);
    }

    @Override
    public void notifyToolObservers(Position position) {
        for (ToolObserver toolObserver : toolObservers)
            toolObserver.update(position);
    }

    @Override
    public void paste(Position position) {
        getSheetContent().insertText(position, getClipboard().retrieve());
    }

    @Override
    public void cut(Mark from, Mark to) {
        getClipboard().store(getSheetContent().getCharactersBetween(from, to));
        notifyToolObservers(getSheetContent().deleteCharactersBetween(from, to));
    }
}
