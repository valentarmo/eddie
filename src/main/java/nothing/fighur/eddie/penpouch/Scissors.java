package nothing.fighur.eddie.penpouch;

public interface Scissors extends Tool {
    /**
     * Copy text between two Marks
     * @param from Mark
     * @param to Mark
     */
    void cut(Mark from, Mark to);
}
