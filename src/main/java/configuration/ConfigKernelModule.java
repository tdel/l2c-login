package configuration;

import com.google.inject.Inject;
import kernel.Kernel;
import kernel.KernelModuleInterface;

public class ConfigKernelModule implements KernelModuleInterface {

    private Config config;
    private String filename;

    @Inject
    public ConfigKernelModule(String _configFilename) {
        this.filename = _configFilename;
    }

    @Override
    public void onBoot(Kernel _kernel) throws Exception {
        this.config = _kernel.getService(Config.class);
        config.load(this.filename);
    }

    @Override
    public void onHalt(Kernel _kernel) {

    }
}
