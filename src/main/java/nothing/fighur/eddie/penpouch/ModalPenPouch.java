package nothing.fighur.eddie.penpouch;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

public class ModalPenPouch extends AbstractPenPouch {

    private enum Mode { Insert, Normal, Visual }

    private Mode currentMode = Mode.Insert;

    public ModalPenPouch(Pencil pencil, Highlighter highlighter, Glue glue, Scissors scissors, Hand hand) {
        super(pencil, highlighter, glue, scissors, hand);
    }

    @Override
    public void performAction(KeyType keyType, KeyStroke keyStroke) {
        Mode currentMode = getCurrentMode();
        if (currentMode == Mode.Normal)
            handleInNormalMode(keyType, keyStroke);
        else if (currentMode == Mode.Insert)
            handleInInsertMode(keyType, keyStroke);
        else
            handleInVisualMode(keyType, keyStroke);
    }

    private void handleInNormalMode(KeyType keyType, KeyStroke keyStroke) {
        switch (keyType) {
            case ArrowUp:
                moveUp();
                break;
            case ArrowDown:
                moveDown();
                break;
            case ArrowRight:
                moveRight();
                break;
            case ArrowLeft:
                moveLeft();
                break;
            case Character:
                handleNormalModeCharacter(keyStroke.getCharacter());
                break;
            default:
                break;
        }
    }

    private void handleNormalModeCharacter(char c) {
        switch (c) {
            case 'k':
                moveUp();
                break;
            case 'j':
                moveDown();
                break;
            case 'l':
                moveRight();
                break;
            case 'h':
                moveLeft();
                break;
            case 'w':
                moveToStartOfNextWord();
                break;
            case 'e':
                moveToEndOfNextWord();
                break;
            case 'b':
                moveToStartOfPreviousWord();
                break;
            case 'p':
                paste();
                break;
            case 'x':
                deleteCharacter();
            case 'i':
                setCurrentMode(Mode.Insert);
            case 'v':
                setCurrentMode(Mode.Visual);
                setStartingMark();
            case ':':
                // TODO commands
                break;
            default:
                break;
        }
    }

    private void handleInInsertMode(KeyType keyType, KeyStroke keyStroke) {
        switch (keyType) {
            case ArrowUp:
                moveUp();
                break;
            case ArrowDown:
                moveDown();
                break;
            case ArrowRight:
                moveRight();
                break;
            case ArrowLeft:
                moveLeft();
                break;
            case Enter:
                insertNewLine();
                break;
            case Backspace:
                deleteCharacter();
                break;
            case Tab:
                insertCharacter('\t');
                break;
            case Escape:
                setCurrentMode(Mode.Normal);
                break;
            case Character:
                insertCharacter(keyStroke.getCharacter());
                break;
            default:
                break;
        }
    }

    private void handleInVisualMode(KeyType keyType, KeyStroke keyStroke) {
        switch (keyType) {
            case ArrowUp:
                moveUp();
                setClosingMark();
                getHighlighter().highlight(getStartingMark(), getClosingMark());
                break;
            case ArrowDown:
                moveDown();
                setClosingMark();
                getHighlighter().highlight(getStartingMark(), getClosingMark());
                break;
            case ArrowRight:
                moveRight();
                setClosingMark();
                getHighlighter().highlight(getStartingMark(), getClosingMark());
                break;
            case ArrowLeft:
                moveLeft();
                setClosingMark();
                getHighlighter().highlight(getStartingMark(), getClosingMark());
                break;
            case Escape:
                getHighlighter().cleanHighlight(getStartingMark(), getClosingMark());
                setCurrentMode(Mode.Normal);
                break;
            case Character:
                handleVisualModeCharacter(keyStroke.getCharacter());
                break;
            default:
                break;
        }
    }

    private void handleVisualModeCharacter(char c) {
        switch (c) {
            case 'k':
                moveUp();
                setClosingMark();
                getHighlighter().highlight(getStartingMark(), getClosingMark());
                break;
            case 'j':
                moveDown();
                setClosingMark();
                getHighlighter().highlight(getStartingMark(), getClosingMark());
                break;
            case 'l':
                moveRight();
                setClosingMark();
                getHighlighter().highlight(getStartingMark(), getClosingMark());
                break;
            case 'h':
                moveLeft();
                setClosingMark();
                getHighlighter().highlight(getStartingMark(), getClosingMark());
                break;
            case 'w':
                moveToStartOfNextWord();
                setClosingMark();
                getHighlighter().highlight(getStartingMark(), getClosingMark());
                break;
            case 'e':
                moveToEndOfNextWord();
                setClosingMark();
                getHighlighter().highlight(getStartingMark(), getClosingMark());
                break;
            case 'b':
                moveToStartOfPreviousWord();
                setClosingMark();
                getHighlighter().highlight(getStartingMark(), getClosingMark());
                break;
            case 'd':
                cut();
                break;
            default:
                break;
        }
    }

    public Mode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(Mode currentMode) {
        this.currentMode = currentMode;
    }
}
