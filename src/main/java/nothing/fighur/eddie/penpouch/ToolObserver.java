package nothing.fighur.eddie.penpouch;

import nothing.fighur.eddie.text.Position;

public interface ToolObserver {
    /**
     * Update observer
     * @param position a tool's position
     */
    void update(Position position);
}
