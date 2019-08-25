package database;

import com.google.inject.Inject;
import com.google.inject.persist.PersistService;
import kernel.Kernel;
import kernel.KernelModuleInterface;

public class DatabaseKernelModule implements KernelModuleInterface {

    private PersistService persistService;

    @Inject
    public DatabaseKernelModule() {

    }

    @Override
    public void onBoot(Kernel _kernel) {
        this.persistService = _kernel.getService(PersistService.class);
        this.persistService.start();
    }

    @Override
    public void onHalt(Kernel _kernel) {
        this.persistService.stop();
    }
}
