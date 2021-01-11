package configuration;

import com.google.inject.Inject;
import kernel.Kernel;
import kernel.LoadableKernelServiceInterface;

public class ConfigLoadableKernel implements LoadableKernelServiceInterface {

    private final Config config;
    private final String filename;

    @Inject
    public ConfigLoadableKernel(Config _config, String _configFilename) {
        this.config = _config;
        this.filename = _configFilename;
    }

    @Override
    public void register(Kernel _kernel) throws Exception {
        this.config.load(this.filename);
    }

    @Override
    public void unregister(Kernel _kernel) {

    }

}
