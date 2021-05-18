package nothing.fighur.eddie.text;

import java.util.LinkedList;
import java.util.List;

public class DefaultClipboard implements Clipboard {
    private List<TextCharacter> text = new LinkedList<>();

    @Override
    public void store(List<TextCharacter> text) {
        this.text = text;
    }

    @Override
    public List<TextCharacter> retrieve() {
        return this.text;
    }
}
