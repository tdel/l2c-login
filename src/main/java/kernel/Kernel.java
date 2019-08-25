package kernel;

import com.google.inject.Inject;
import com.google.inject.Injector;
import network.gameclient.packets.IncomingGameClientPacketInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Kernel {

    private static final Logger logger = LogManager.getLogger();

    private KernelStatus status;
    private Set<KernelModuleInterface> modules;

    private Injector injector;

    @Inject
    public Kernel(Set<KernelModuleInterface> _modules) {
        this.status = KernelStatus.STOPED;
        this.modules = _modules;
    }

    private void setStatus(KernelStatus _status) {
        logger.info("Switch state from " + this.status + " to " + _status);
        this.status = _status;
    }

    public void boot(Injector _injector) throws Exception {
        if (this.status != KernelStatus.STOPED) {
            throw new IllegalStateException("Kernel must be stopped to be started !");
        }
        this.setStatus(KernelStatus.STARTING);
        this.injector = _injector;

        for (KernelModuleInterface module : this.modules) {
            logger.info("Loading module : " + module.getClass().getName());
            module.onBoot(this);
        }

        this.setStatus(KernelStatus.RUNNING);
    }

    public void halt() {
        if (this.status != KernelStatus.RUNNING) {
            throw new IllegalStateException("Kernel must be running to be stopped");
        }

        this.setStatus(KernelStatus.STOPING);

        for (KernelModuleInterface module : this.modules) {
            module.onHalt(this);
        }

        this.setStatus(KernelStatus.STOPED);
    }

    public <T> T getService(Class<T> _class) {
        return this.injector.getInstance(_class);
    }
}
