package nothing.fighur.eddie.sheet;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class SheetModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ContentCursor.class).to(SuperSheet.class).in(Singleton.class);
        bind(Sheet.class).to(SuperSheet.class).in(Singleton.class);
        bind(SheetHeader.class).to(SuperSheet.class).in(Singleton.class);
        bind(SheetContent.class).to(SuperSheet.class).in(Singleton.class);
        bind(SheetFooter.class).to(SuperSheet.class).in(Singleton.class);
    }
}
