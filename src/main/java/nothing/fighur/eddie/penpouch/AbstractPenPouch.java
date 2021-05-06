package nothing.fighur.eddie.penpouch;

import nothing.fighur.eddie.text.TextCharacter;

public abstract class AbstractPenPouch implements PenPouch {

    private Pencil pencil;
    private Highlighter highlighter;
    private Glue glue;
    private Scissors scissors;
    private Hand hand;

    private Mark startingMark = Mark.unsetMark();
    private Mark closingMark = Mark.unsetMark();

    public AbstractPenPouch(Pencil pencil, Highlighter highlighter, Glue glue, Scissors scissors, Hand hand) {
        setPencil(pencil);
        setHighlighter(highlighter);
        setGlue(glue);
        setScissors(scissors);
        setHand(hand);

        pencil.addToolObserver(hand);
        highlighter.addToolObserver(hand);
        glue.addToolObserver(hand);
        scissors.addToolObserver(hand);
    }

    protected void insertCharacter(char c) {
        TextCharacter character = TextCharacter.withDefaultCharacteristics(c);
        getPencil().insertCharacter(character, getHand().getPosition());
    }

    protected void deleteCharacter() {
        getPencil().deleteCharacter(getHand().getPosition());
    }

    protected void insertNewLine() {
        getPencil().insertNewLine(getHand().getPosition());
    }

    protected void moveLeft() {
        getHand().moveLeft();
    }

    protected void moveRight() {
        getHand().moveRight();
    }

    protected void moveUp() {
        getHand().moveUp();
    }

    protected void moveDown() {
        getHand().moveDown();
    }

    protected void moveToStartOfNextWord() {
        getHand().moveToStartOfNextWord();
    }

    protected void moveToEndOfNextWord() {
        getHand().moveToEndOfNextWord();
    }

    protected void moveToStartOfPreviousWord() {
        getHand().moveToStartOfPreviousWord();
    }

    protected void setStartingMark() {
        int row = getHand().getPosition().getRow();
        int col = getHand().getPosition().getCol();
        this.startingMark = new Mark(row, col);
    }

    protected void setClosingMark() {
        int row = getHand().getPosition().getRow();
        int col = getHand().getPosition().getCol();
        this.closingMark = new Mark(row, col);
    }

    protected void paste() {
        getGlue().paste(getHand().getPosition());
    }

    protected void cut() {
        getScissors().cut(getStartingMark(), getClosingMark());
    }

    public Pencil getPencil() {
        return pencil;
    }

    public void setPencil(Pencil pencil) {
        this.pencil = pencil;
    }

    public Highlighter getHighlighter() {
        return highlighter;
    }

    public void setHighlighter(Highlighter highlighter) {
        this.highlighter = highlighter;
    }

    public Glue getGlue() {
        return glue;
    }

    public void setGlue(Glue glue) {
        this.glue = glue;
    }

    public Scissors getScissors() {
        return scissors;
    }

    public void setScissors(Scissors scissors) {
        this.scissors = scissors;
    }

    public Mark getStartingMark() {
        return startingMark;
    }

    public Mark getClosingMark() {
        return closingMark;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }
}
