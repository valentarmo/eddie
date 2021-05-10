package nothing.fighur.eddie.penpouch;

import com.google.inject.Inject;
import com.googlecode.lanterna.TextColor;
import nothing.fighur.eddie.EditorVariables;
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
        List<TextCharacter> characters = getSheetContent().getCharactersBetween(from, to);
        for (TextCharacter character : characters) {
            character.setBackgroundColor(getColor());
        }
    }

    @Override
    public void cleanHighlight(Mark from, Mark to) {
        List<TextCharacter> characters = getSheetContent().getCharactersBetween(from, to);
        for (TextCharacter character : characters) {
            character.setBackgroundColor(EditorVariables.getBackgroundColor());
        }
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

    }

    @Override
    public void cut(Mark from, Mark to) {

    }
}
