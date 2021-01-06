package kernel.configuration;

import com.google.inject.Inject;
import kernel.Kernel;
import kernel.KernelServiceInterface;

public class ConfigService implements KernelServiceInterface {

    private final Config config;
    private final String filename;

    @Inject
    public ConfigService(Config _config, String _configFilename) {
        this.config = _config;
        this.filename = _configFilename;
    }

    @Override
    public void onBoot(Kernel _kernel) throws Exception {
        this.config.load(this.filename);
    }

    @Override
    public void onHalt(Kernel _kernel) {

    }
}
