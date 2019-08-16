package kernel;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import kernel.subsystem.AbstractKernelSubsystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kernel {

    private static final Logger logger = LogManager.getLogger();

    private KernelEnvironment env;
    private KernelStatus status;

    private Map<String, Object> configuration;

    private Map<Class, AbstractKernelSubsystem> modules;
    private List<AbstractKernelSubsystem> subsystems;

    private Injector injector;

    @Inject
    public Kernel() {
        this.status = KernelStatus.STOPED;
        this.configuration = new HashMap<>();
        this.modules = new HashMap<>();
        this.subsystems = new ArrayList<>();
    }

    private void setStatus(KernelStatus _status) {
        logger.info("Switch state from " + this.status + " to " + _status);
        this.status = _status;
    }

    public void start(KernelEnvironment _env, Injector _injector, List<AbstractKernelSubsystem> _subsystems) throws Exception {
        if (this.status != KernelStatus.STOPED) {
            throw new IllegalStateException("Kernel must be stopped to be started !");
        }
        this.setStatus(KernelStatus.STARTING);
        this.env = _env;
        this.injector = _injector;
        this.subsystems = _subsystems;

        this.injector.getInstance(PersistService.class).start();

        this.loadConfiguration();

        for (AbstractKernelSubsystem subsystem : this.subsystems) {
            subsystem.start();
        }

        this.setStatus(KernelStatus.RUNNING);
    }

    public void stop() {
        if (this.status != KernelStatus.RUNNING) {
            throw new IllegalStateException("Kernel must be running to be stopped");
        }

        this.setStatus(KernelStatus.STOPING);

        for (AbstractKernelSubsystem subsystem : this.subsystems) {
            subsystem.stop();
        }

        this.setStatus(KernelStatus.STOPED);
    }


    private void loadConfiguration() {
        this.configuration.put("kernel.environment", this.env);
        this.configuration.put("module.player.server.port", 2106);
    }

    public <T> T getConfigParam(String _name) {
        if (!this.configuration.containsKey(_name)) {
            throw new IllegalArgumentException("Parameter "+ _name + " does not exist");
        }

        return (T) this.configuration.get(_name);
    }


    public <T> T getService(Class<T> _class) {
        return this.injector.getInstance(_class);
    }
}
