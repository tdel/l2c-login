package configuration;

import com.google.inject.Singleton;
import main.AbstractApplicationModule;

public class ConfigGuiceModule extends AbstractApplicationModule {

    private String configFilename;

    public ConfigGuiceModule(String _configFilename) {
        this.configFilename = _configFilename;
    }

    @Override
    protected void configure() {
        this.bind(Config.class).in(Singleton.class);

        ConfigKernelModule kernelModule = new ConfigKernelModule(this.configFilename);
        this.bind(ConfigKernelModule.class).toInstance(kernelModule);

        this.bindToKernel(ConfigKernelModule.class);
    }
}
