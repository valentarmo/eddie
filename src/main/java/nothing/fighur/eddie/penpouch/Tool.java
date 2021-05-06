package nothing.fighur.eddie.penpouch;

import nothing.fighur.eddie.text.Position;

public interface Tool {
    /**
     * Register an observer
     * @param toolObserver the observer
     */
    void addToolObserver(ToolObserver toolObserver);

    /**
     * Deregister an observer
     * @param toolObserver the observer
     */
    void removeToolObserver(ToolObserver toolObserver);

    /**
     * Notify observers
     * @param position the tool's position
     */
    void notifyToolObservers(Position position);
}
