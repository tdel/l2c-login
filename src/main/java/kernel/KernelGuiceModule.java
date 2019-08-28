package kernel;

import com.google.inject.Singleton;
import main.AbstractApplicationModule;

public class KernelGuiceModule extends AbstractApplicationModule {

    @Override
    protected void configure() {
        this.bind(Kernel.class).in(Singleton.class);
    }


}
