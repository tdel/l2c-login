package kernel;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class KernelGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(Kernel.class).in(Singleton.class);
    }


}
