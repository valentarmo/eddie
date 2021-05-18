package nothing.fighur.eddie.text;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class DefaultHeaderText implements HeaderText {

    private TextColor defaultBackgroundColor = TextColor.ANSI.WHITE;
    private TextColor defaultForegroundColor = TextColor.ANSI.BLACK;

    private String title = "BLANK";
    private String subtitle = "text";
    private String logo = "EDDIE";

    private int firstRow;
    private int lastRow;
    private int titleFirstCol;
    private int titleLastCol;
    private int subtitleFirstCol;
    private int subtitleLastCol;
    private int logoFirstCol;
    private int logoLastCol;

    @Override
    public void resize(Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) throws IOException {
        setFirstRow(firstRow);
        setLastRow(lastRow);
        int sectionLength = (lastCol - firstCol) / 3;
        setSubtitleFirstCol(firstCol);
        setSubtitleLastCol(getSubtitleFirstCol() + sectionLength);
        setTitleFirstCol(getSubtitleLastCol());
        setTitleLastCol(getTitleFirstCol() + sectionLength);
        setLogoFirstCol(getTitleLastCol());
        setLogoLastCol(lastCol);
        drawSubtitle(terminal);
        drawTitle(terminal);
        drawLogo(terminal);
    }

    @Override
    public void updateTitle(Terminal terminal, String title) throws IOException {
        setTitle(title);
        drawTitle(terminal);
    }

    private void drawTitle(Terminal terminal) throws IOException {
        drawText(terminal, getTitleFirstCol(), getTitleLastCol(), getTitle());
    }

    @Override
    public void updateSubtitle(Terminal terminal, String subtitle) throws IOException {
        setSubtitle(subtitle);
        drawSubtitle(terminal);
    }

    private void drawSubtitle(Terminal terminal) throws IOException {
        drawText(terminal, getSubtitleFirstCol(), getSubtitleLastCol(), getSubtitle());
    }

    @Override
    public void updateLogo(Terminal terminal, String logo) throws IOException {
        setLogo(logo);
        drawLogo(terminal);
    }

    private void drawLogo(Terminal terminal) throws IOException {
        drawText(terminal, getLogoFirstCol(), getLogoLastCol(), getLogo());
    }

    private void drawText(Terminal terminal, int firstCol, int lastCol, String text) throws IOException {
        terminal.setBackgroundColor(getDefaultBackgroundColor());
        terminal.setForegroundColor(getDefaultForegroundColor());
        int textIndex = 0;
        for (int i = getFirstRow(); i <= getLastRow(); i++) {
            terminal.setCursorPosition(firstCol, i);
            for (int j = firstCol; j < lastCol; j++) {
                char c = textIndex < text.length() ? text.charAt(textIndex++) : ' ';
                if (c == '\n') {
                    for (int k = j; k < lastCol; k++)
                        terminal.putCharacter(' ');
                    break;
                } else {
                    terminal.putCharacter(c);
                }
            }
        }
        terminal.flush();
    }

    public TextColor getDefaultBackgroundColor() {
        return defaultBackgroundColor;
    }

    public void setDefaultBackgroundColor(TextColor defaultBackgroundColor) {
        this.defaultBackgroundColor = defaultBackgroundColor;
    }

    public TextColor getDefaultForegroundColor() {
        return defaultForegroundColor;
    }

    public void setDefaultForegroundColor(TextColor defaultForegroundColor) {
        this.defaultForegroundColor = defaultForegroundColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(int firstRow) {
        this.firstRow = firstRow;
    }

    public int getLastRow() {
        return lastRow;
    }

    public void setLastRow(int lastRow) {
        this.lastRow = lastRow;
    }

    public int getTitleFirstCol() {
        return titleFirstCol;
    }

    public void setTitleFirstCol(int titleFirstCol) {
        this.titleFirstCol = titleFirstCol;
    }

    public int getTitleLastCol() {
        return titleLastCol;
    }

    public void setTitleLastCol(int titleLastCol) {
        this.titleLastCol = titleLastCol;
    }

    public int getSubtitleFirstCol() {
        return subtitleFirstCol;
    }

    public void setSubtitleFirstCol(int subtitleFirstCol) {
        this.subtitleFirstCol = subtitleFirstCol;
    }

    public int getSubtitleLastCol() {
        return subtitleLastCol;
    }

    public void setSubtitleLastCol(int subtitleLastCol) {
        this.subtitleLastCol = subtitleLastCol;
    }

    public int getLogoFirstCol() {
        return logoFirstCol;
    }

    public void setLogoFirstCol(int logoFirstCol) {
        this.logoFirstCol = logoFirstCol;
    }

    public int getLogoLastCol() {
        return logoLastCol;
    }

    public void setLogoLastCol(int logoLastCol) {
        this.logoLastCol = logoLastCol;
    }
}
