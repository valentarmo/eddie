package nothing.fighur.eddie;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.Objects;

public class EditorModule extends AbstractModule {

    private static final Terminal terminal = getTerminal();

    static {
        Runtime.getRuntime().addShutdownHook(new EddieShutdown());
    }

    static class EddieShutdown extends Thread {
        public void run() {
            try {
                if (Objects.nonNull(terminal)) {
                    terminal.exitPrivateMode();
                }
            } catch (IOException e) { }
        }
    }

    private static Terminal getTerminal() {
        try {
            Terminal terminal = new  DefaultTerminalFactory().createTerminal();
            terminal.enterPrivateMode();
            terminal.setCursorVisible(true);
            return terminal;
        } catch (IOException e) {
            return null;
        }
    }

    @Provides
    static Terminal provideTerminal() {
        return terminal;
    }

    @Override
    protected void configure() {
        bind(Editor.class).to(Eddie.class).in(Singleton.class);
    }
}
