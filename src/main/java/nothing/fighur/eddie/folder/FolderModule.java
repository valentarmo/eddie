package nothing.fighur.eddie.folder;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class FolderModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Folder.class).to(DefaultFolder.class).in(Singleton.class);
        bind(PersistenceProvider.class).to(FilePersistenceProvider.class).in(Singleton.class);
    }
}
