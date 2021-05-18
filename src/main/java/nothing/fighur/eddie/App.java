package nothing.fighur.eddie;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nothing.fighur.eddie.folder.FolderModule;
import nothing.fighur.eddie.penpouch.PenPouchModule;
import nothing.fighur.eddie.sheet.SheetModule;
import nothing.fighur.eddie.text.TextModule;

public class App
{
    public static void main( String[] args )
    {
        Injector injector = Guice.createInjector(new EditorModule(), new TextModule(), new SheetModule(), new PenPouchModule(), new FolderModule());
        Editor editor = injector.getInstance(Editor.class);
        if (args.length >= 1) {
            editor.edit(args[0]);
        } else {
            editor.edit();
        }
    }
}
