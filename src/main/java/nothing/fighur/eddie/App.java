package nothing.fighur.eddie;

import com.googlecode.lanterna.terminal.*;

public class App
{
    public static void main( String[] args )
    {
        Terminal terminal = null;
        Editor editor = null;
        try {
            terminal = new DefaultTerminalFactory().createTerminal();
            terminal.enterPrivateMode();
            if (args.length > 0)
                editor = Editor.createWithFile(terminal, args[0]);
            else
                editor = Editor.createEmpty(terminal);
            editor.edit();
        } catch (Exception e) {
            // ...
        } finally {
            try {
                terminal.exitPrivateMode();
                terminal.close();
            } catch(Exception e) {

            }
        }
    }
}
