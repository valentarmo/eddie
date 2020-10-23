package nothing.fighur.eddie;

public class Mark implements Comparable<Mark>
{
    private int row;
    private int col;

    public Mark(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    public int getRow()
    { return row; }

    public int getColumn()
    { return col; }

    public void setRow(int row)
    { if (row >= 0) this.row = row; }

    public void setColumn(int col)
    { if (col >= 0) this.col = col; }

    public int compareTo(Mark other) {
        int orow = other.getRow();
        int ocol = other.getColumn();

        if (row == orow) {
            return col - ocol;
        } else
            return row - orow;
    }
}