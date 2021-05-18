package nothing.fighur.eddie.penpouch;

import nothing.fighur.eddie.text.Position;

public interface Glue extends Tool {
    /**
     * Paste contents from the clipboard at the specified position
     * @param position the position
     */
    void paste(Position position);
}
