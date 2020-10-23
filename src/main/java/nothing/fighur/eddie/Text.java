package nothing.fighur.eddie;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This class is responsible for storing the buffers
 */
public class Text
{
    private ArrayList<StringBuilder> buffers;
    private LinkedList<String> copyBuffer;
    private int tabstop;

    public Text(int tabstop)
    {
        buffers = new ArrayList<>();
        this.tabstop = tabstop;
    }

    // region Getters

    /**
     * Get the tabstop being used to render tabs
     * @return int - the tabstop
     */
    public int getTabstop()
    { return tabstop; }

    /**
     * Get the current number of lines in the text
     */
    public int size()
    { return buffers.size(); }

    /**
     * Get the text at a given line
     * @param line int - The line
     * @return String - The text
     */
    public String getTextAt(int line)
    {
        if (line >= buffers.size())
            return null;
        return buffers.get(line).toString();
    }

    /**
     * Get all the text as a single string
     */
    public String getWritableText()
    {
        String text = "";
        for (StringBuilder buffer : buffers) {
            text += buffer.toString() + "\n";
        }
        return text;
    }

    // endregion

    // region Text manipulation

    /**
     * Insert a new line at the end
     * @param text String - new line
     */
    public void appendLine(String text)
    {
        StringBuilder str = new StringBuilder();
        tabsToSpaces(str, text);
        buffers.add(str);
    }

    /**
     * Insert a new line at the given index
     * @param index int - the index
     * @param text String - the line
     */
    public void insertLine(int index, String text)
    {
        if (index < 0 || index > buffers.size())
            return;
        StringBuilder buffer = new StringBuilder();
        tabsToSpaces(buffer, text);
        buffers.add(index, buffer);
    }

    /**
     * Insert a character at a given line and column
     * @param line int - the line
     * @param col int - the column
     * @param c char to insert
     */
    public void insertChar(int line, int col, char c)
    {
        StringBuilder buffer = buffers.get(line);
        if (col < 0 || col > buffer.length())
            buffer.append(c);
        else
            buffer.insert(col, c);
        /* Get rid of nasty tabs*/
        if (c == '\t') {
            String tmp = buffer.toString();
            buffer.delete(0, buffer.length());
            tabsToSpaces(buffer, tmp);
        }
    }

    /**
     * Append a string at the given line
     * @param line int - the line
     * @param str String - the string
     */
    public void appendString(int line, String str)
    {
        if (line < 0 || line >= buffers.size())
            return;
        StringBuilder buffer = buffers.get(line);
        buffer.append(str);
    }

    /**
     * Remove the character at a given line and column
     * @param line int - the line
     * @param col int - the column
     */
    public void deleteChar(int line, int col)
    {
        StringBuilder buffer = buffers.get(line);
        if (col < 0 || col > buffer.length())
            return;
        buffer.deleteCharAt(col);
    }

    /**
     * Remove from the given column to the end of the line
     * @param line int - the line
     * @param col int - the column
     */
    public void deleteSubstring(int line, int col)
    {
        StringBuilder buffer = buffers.get(line);
        if (col < 0 || col > buffer.length())
            return;
        buffer.delete(col, buffer.length());
    }

    /**
     * Remove the given line
     * @param line int - the line
     */
    public void deleteLine(int line)
    {
        if (line < 0 || line >= buffers.size())
            return;
        buffers.remove(line);
    }

    // endregion

    // region Region manipulation

    /**
     * Delete the region enclosed withing two marks
     * to the copy buffer
     * @param from Mark - Starting point. Should be <= 'to'
     * @param to Mark - Finish point. Should be >= 'from'
     */
    public void deleteRegion(Mark from, Mark to)
    {
        if (from.compareTo(to) > 0)
            return;

        int sline = from.getRow();
        int scol = from.getColumn();
        int eline = to.getRow();
        int ecol = to.getColumn() + 1;

        if (sline == eline)
        {
            StringBuilder line = buffers.get(sline);
            if (scol == line.length() && scol > 0)
                return;
            line.delete(scol, ecol);
        }
        else
        {
            String tmp = null;
            for (int i = sline; i <= eline; i++)
            {
                StringBuilder line = buffers.get(i);
                if (scol == line.length() && scol > 0)
                    continue;
                if (i == sline) {
                    line.delete(scol, line.length());
                } else if (i == eline) {
                    tmp = (ecol >= line.length()) ? null : line.substring(ecol);
                    buffers.remove(line);
                    i--;
                    eline--;
                } else {
                    buffers.remove(line);
                    i--;
                    eline--;
                }
            }
            if (tmp != null)
                buffers.get(sline).append(tmp);
        }
        return;
    }

    /**
     * Copy the region enclosed withing two marks
     * to the copy buffer
     * @param from Mark - Starting point. Should be <= 'to'
     * @param to Mark - Finish point. Should be >= 'from'
     */
    public void copy(Mark from, Mark to)
    {
        if (from.compareTo(to) > 0)
            return;

        copyBuffer = new LinkedList<>();
        int sline = from.getRow();
        int scol = from.getColumn();
        int eline = to.getRow();
        int ecol = to.getColumn();

        if (sline == eline)
        {
            StringBuilder line = buffers.get(sline);
            if (scol == line.length() && scol > 0) {
                copyBuffer = null;
                return;
            }
            if (ecol >= line.length() && ecol > 0)
                ecol = line.length()-1;
            copyBuffer.addFirst(line.substring(scol, ecol+1));
        }
        else
        {
            for (int i = sline; i <= eline; i++) {
                StringBuilder line = buffers.get(i);
                if (scol == line.length() && scol > 0)
                    continue;
                if (i == sline)
                    copyBuffer.addFirst(line.substring(scol));
                else if (i == eline) {
                    if (ecol >= line.length() && ecol > 0)
                        ecol = line.length()-1;
                    copyBuffer.addLast(line.substring(0, ecol+1));
                } else
                    copyBuffer.addLast(line.toString());
            }
        }
        return;
    }

    /**
     * Paste the contents of the copy buffer, if any,
     * at the given line and column
     * @param line int - the line
     * @param col int - the column
     */
    public void paste(int line, int col)
    {
        if (copyBuffer == null)
            return;

        int copySize = copyBuffer.size();
        StringBuilder firstInsertion = buffers.get(line);

        String tmp = null;
        if (copySize > 1) {
            tmp = firstInsertion.substring(col);
            firstInsertion.delete(col, firstInsertion.length());
        }

        firstInsertion.insert(col, copyBuffer.getFirst());
        if (copySize == 1)
            return;

        for (int i = 1; i < copySize-1; i++) {
            insertLine(line+i, copyBuffer.get(i));
        }

        String last = (tmp == null) ? copyBuffer.getLast() : copyBuffer.getLast() + tmp;
        if (line + copySize-1 < buffers.size()) {
            buffers.add(line+copySize-1, new StringBuilder(last));
        } else {
            appendLine(last);
        }
    }

    // endregion

    // region Helpers

    /**
     * Convert tabs to spaces
     * @param to StringBuilder - where to store the result
     * @param from String - text from which to convert tabs to spaces
     */
    private void tabsToSpaces(StringBuilder to, String from)
    {
        int j = 0;
        for (int i = 0; i < from.length(); i++) {
            char c = from.charAt(i);
            if (c == '\t') {
                to.append(' ');
                j++;
                while (j % tabstop != 0) {
                    to.append(' ');
                    j++;
                }
            } else {
                to.append(c);
                j++;
            }
        }
    }

    // endregion
}