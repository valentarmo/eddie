package nothing.fighur.eddie.sheet;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class SheetModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(SuperSheet.class).in(Singleton.class);
        bind(ContentCursor.class).to(SuperSheet.class);
        bind(Sheet.class).to(SuperSheet.class);
        bind(SheetHeader.class).to(SuperSheet.class);
        bind(SheetContent.class).to(SuperSheet.class);
        bind(SheetFooter.class).to(SuperSheet.class);
    }
}
