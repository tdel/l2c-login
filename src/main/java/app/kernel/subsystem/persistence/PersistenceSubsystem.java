package app.kernel.subsystem.persistence;

import com.google.inject.Inject;
import com.google.inject.persist.PersistService;
import app.kernel.Kernel;
import app.kernel.subsystem.AbstractKernelSubsystem;

public class PersistenceSubsystem extends AbstractKernelSubsystem {

    @Inject
    public PersistenceSubsystem(Kernel _kernel) {
        super(_kernel);
    }

    @Override
    protected void onModuleStart() throws Exception {
        this.getService(PersistService.class).start();
    }

    @Override
    protected void onModuleStop() {

    }
}
