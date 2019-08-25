package configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import kernel.KernelModuleInterface;

public class ConfigGuiceModule extends AbstractModule {

    private String configFilename;

    public ConfigGuiceModule(String _configFilename) {
        this.configFilename = _configFilename;
    }

    @Override
    protected void configure() {
        this.bind(Config.class).in(Singleton.class);

        ConfigKernelModule kernelModule = new ConfigKernelModule(this.configFilename);
        this.bind(ConfigKernelModule.class).toInstance(kernelModule);

        Multibinder<KernelModuleInterface> binder = Multibinder.newSetBinder(binder(), KernelModuleInterface.class);
        binder.addBinding().to(ConfigKernelModule.class);
    }
}
