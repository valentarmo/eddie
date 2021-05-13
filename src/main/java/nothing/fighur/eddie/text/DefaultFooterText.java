package nothing.fighur.eddie.text;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;

public class DefaultFooterText implements FooterText {

    private TextColor footerBackgroundColor = TextColor.ANSI.WHITE;
    private TextColor footerForegroundColor = TextColor.ANSI.BLACK;



    @Override
    public void resize(Terminal terminal, int firstRow, int lastRow, int firstCol, int lastCol) {
        for (int i = firstRow; i < lastCol; i++) {
            for (int j = firstCol; j < lastCol; j++) {

            }
        }
    }
}
