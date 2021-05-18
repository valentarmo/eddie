package nothing.fighur.eddie.penpouch;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class PenPouchModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(SuperPen.class).in(Singleton.class);
        bind(Glue.class).to(SuperPen.class);
        bind(Highlighter.class).to(SuperPen.class);
        bind(Scissors.class).to(SuperPen.class);
        bind(Pencil.class).to(SuperPen.class);
        bind(Hand.class).to(DefaultHand.class).in(Singleton.class);
        bind(PenPouch.class).to(ModalPenPouch.class).in(Singleton.class);
    }
}
