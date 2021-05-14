package nothing.fighur.eddie.penpouch;

import com.google.inject.Inject;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import nothing.fighur.eddie.EditorVariables;

public class ModalPenPouch extends AbstractPenPouch {

    private enum Mode { Insert, Normal, Visual }

    private Mode currentMode = Mode.Normal;

    @Inject
    public ModalPenPouch(Pencil pencil, Highlighter highlighter, Glue glue, Scissors scissors, Hand hand) {
        super(pencil, highlighter, glue, scissors, hand);
    }

    @Override
    public void doAction(KeyType keyType, KeyStroke keyStroke) {
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
                break;
            case 'i':
                setCurrentMode(Mode.Insert);
                break;
            case 'v':
                setCurrentMode(Mode.Visual);
                setStartingMark();
                setClosingMark();
                break;
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
                int col = getHand().getPosition().getCol();
                do {
                    col = getHand().getPosition().getCol();
                    insertCharacter(' ');
                } while (col % EditorVariables.getTabStop() != 0);
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
                highlight();
                break;
            case ArrowDown:
                moveDown();
                setClosingMark();
                highlight();
                break;
            case ArrowRight:
                moveRight();
                setClosingMark();
                highlight();
                break;
            case ArrowLeft:
                moveLeft();
                setClosingMark();
                highlight();
                break;
            case Escape:
                cleanHighlight();
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
                highlight();
                break;
            case 'j':
                moveDown();
                setClosingMark();
                highlight();
                break;
            case 'l':
                moveRight();
                setClosingMark();
                highlight();
                break;
            case 'h':
                moveLeft();
                setClosingMark();
                highlight();
                break;
            case 'w':
                moveToStartOfNextWord();
                setClosingMark();
                highlight();
                break;
            case 'e':
                moveToEndOfNextWord();
                setClosingMark();
                highlight();
                break;
            case 'b':
                moveToStartOfPreviousWord();
                setClosingMark();
                highlight();
                break;
            case 'd':
                cleanHighlight();
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
