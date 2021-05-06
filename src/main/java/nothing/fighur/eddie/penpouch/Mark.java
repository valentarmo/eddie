package nothing.fighur.eddie.penpouch;

import nothing.fighur.eddie.text.Position;

public class Mark {
    private int row;
    private int col;

    public Mark() { }

    public Mark(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static Mark fromPosition(Position position) {
        return new Mark(position.getRow(), position.getCol());
    }

    public static Mark unsetMark() {
        return new Mark(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public static boolean isUnset(Mark mark) {
        return mark.getColumn() == Integer.MIN_VALUE || mark.getRow() == Integer.MIN_VALUE;
    }

    public int compareTo(Mark other) {
        int row = other.getRow();
        int col = other.getColumn();

        if (this.row == row) {
            return this.col - col;
        } else
            return this.row - row;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        if (row >= 0) this.row = row;
    }

    public int getColumn() {
        return col;
    }

    public void setColumn(int col) {
        if (col >= 0) this.col = col;
    }
}
