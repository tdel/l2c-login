package kernel;

import module.player.PlayerModule;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Kernel {

    static private Logger logger = Logger.getLogger(Kernel.class.getName());

    private KernelEnvironment env;
    private KernelStatus status;

    private Map<String, Object> configuration;

    private Map<Class, AbstractKernelModule> modules;

    public Kernel(KernelEnvironment _env) {
        this.env = _env;
        this.status = KernelStatus.STOPED;
        this.configuration = new HashMap<>();
        this.modules = new HashMap<>();
    }

    private void setStatus(KernelStatus _status) {
        logger.info("Kernel switch state from " + this.status + " to " + _status);
        this.status = _status;
    }

    public void start() {
        if (this.status != KernelStatus.STOPED) {
            throw new IllegalStateException("Kernel must be stopped to be started !");
        }

        this.setStatus(KernelStatus.STARTING);


        this.loadModules();
        this.loadConfiguration();


        this.modules.forEach((k,v) -> v.start());

        this.setStatus(KernelStatus.RUNNING);
    }

    public void stop() {
        if (this.status != KernelStatus.RUNNING) {
            throw new IllegalStateException("Kernel must be running to be stopped");
        }

        this.setStatus(KernelStatus.STOPING);

        this.modules.forEach((k,v) -> v.stop());

        this.setStatus(KernelStatus.STOPED);
    }

    private void loadConfiguration() {
        this.configuration.put("kernel.environment", this.env);
        this.configuration.put("module.player.server.port", 1234);
    }

    public <T> T getConfigParam(String _name) {
        if (false == this.configuration.containsKey(_name)) {
            throw new IllegalArgumentException("Parameter "+ _name + " does not exist");
        }

        return (T) this.configuration.get(_name);
    }


    private void loadModules() {
        this.modules.put(PlayerModule.class, new PlayerModule(this));
    }

    public <T> T getModule(Class<T> _class) {
        return (T) this.modules.get(_class);
    }

}
