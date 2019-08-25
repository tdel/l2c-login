package configuration;

import main.AbstractApplicationModule;

public class ConfigGuiceModule extends AbstractApplicationModule {

    private String configFilename;

    public ConfigGuiceModule(String _configFilename) {
        this.configFilename = _configFilename;
    }

    @Override
    protected void configure() {
        Config config = new Config();
        this.bind(Config.class).toInstance(config);

        ConfigKernelModule kernelModule = new ConfigKernelModule(config, this.configFilename);
        this.bind(ConfigKernelModule.class).toInstance(kernelModule);

        this.bindToKernel(ConfigKernelModule.class);
    }
}
