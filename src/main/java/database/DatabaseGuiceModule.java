package database;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.persist.jpa.JpaPersistModule;
import kernel.KernelModuleInterface;

public class DatabaseGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        this.install(new JpaPersistModule("default"));

        this.bind(DatabaseKernelModule.class).in(Singleton.class);

        Multibinder<KernelModuleInterface> binder = Multibinder.newSetBinder(binder(), KernelModuleInterface.class);
        binder.addBinding().to(DatabaseKernelModule.class);
    }
}
