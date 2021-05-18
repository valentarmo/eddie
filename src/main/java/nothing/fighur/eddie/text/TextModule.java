package nothing.fighur.eddie.text;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class TextModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(HeaderText.class).to(DefaultHeaderText.class).in(Singleton.class);
        bind(ContentText.class).to(DefaultContentText.class).in(Singleton.class);
        bind(FooterText.class).to(DefaultFooterText.class).in(Singleton.class);
        bind(Clipboard.class).to(DefaultClipboard.class).in(Singleton.class);
    }
}
