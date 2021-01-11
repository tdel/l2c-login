package kernel;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import guice.AppGuice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Kernel {

    private static final Logger logger = LogManager.getLogger();

    private KernelStatus status = KernelStatus.STOPED;

    public Kernel() {

    }

    private void setStatus(KernelStatus _status) {
        logger.info("Switch state from " + this.status + " to " + _status);
        this.status = _status;
    }

    public void boot() throws Exception {
        if (this.status != KernelStatus.STOPED) {
            throw new IllegalStateException("Kernel must be stopped to be started !");
        }

        this.setStatus(KernelStatus.STARTING);

        Injector injector = Guice.createInjector(Stage.DEVELOPMENT,
                new AppGuice("src/main/resources/app.properties")
        );

        this.setStatus(KernelStatus.RUNNING);

        injector.getInstance(KernelInit.class).start(this);

        logger.info("READY");
    }

    public void halt() {
        if (this.status != KernelStatus.RUNNING) {
            throw new IllegalStateException("Kernel must be running to be stopped");
        }

        this.setStatus(KernelStatus.STOPING);

        this.setStatus(KernelStatus.STOPED);
    }

}
