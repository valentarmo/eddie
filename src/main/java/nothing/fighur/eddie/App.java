package nothing.fighur.eddie;

import com.googlecode.lanterna.terminal.*;

import java.util.ArrayList;
import java.util.List;

public class App
{
    public static void main( String[] args )
    {
        List<Integer> l = new ArrayList<>();
        l.add(1);
        int s = l.size();
        for (int i = 0; i < s; i++)
            System.out.println(l.get(i));

        l.add(1, 8);
        for (int i = 0; i < l.size(); i++)
            System.out.println(l.get(i));
//        Terminal terminal = null;
//        Editor editor = null;
//        try {
//            terminal = new DefaultTerminalFactory().createTerminal();
//            terminal.enterPrivateMode();
//            if (args.length > 0)
//                editor = Editor.createWithFile(terminal, args[0]);
//            else
//                editor = Editor.createEmpty(terminal);
//            editor.edit();
//        } catch (Exception e) {
//            // ...
//        } finally {
//            try {
//                terminal.exitPrivateMode();
//                terminal.close();
//            } catch(Exception e) {
//
//            }
//        }
    }
}
