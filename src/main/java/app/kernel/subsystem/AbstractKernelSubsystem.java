package app.kernel.subsystem;

import com.google.inject.Inject;
import app.kernel.Kernel;

abstract public class AbstractKernelSubsystem {

    private Kernel kernel;
    private KernelSubsystemStatus status;

    @Inject
    public AbstractKernelSubsystem(Kernel _kernel) {
        this.kernel = _kernel;
        this.status = KernelSubsystemStatus.STOPED;
    }

    public void start() throws Exception {
        if (this.status != KernelSubsystemStatus.STOPED) {
            throw new IllegalStateException("Kernel module must be stopped to be started !");
        }

        this.status = KernelSubsystemStatus.STARTING;
        try {
            this.onModuleStart();
        } catch (Exception e) {
            this.status = KernelSubsystemStatus.STOPED;

            throw e;
        }

        this.status = KernelSubsystemStatus.RUNNING;
    }

    public boolean stop() {
        if (this.status != KernelSubsystemStatus.RUNNING) {
            throw new IllegalStateException("Kernel module must be running to be stoped !");
        }

        this.status = KernelSubsystemStatus.STOPING;
        this.onModuleStop();

        this.status = KernelSubsystemStatus.STOPED;

        return true;
    }

    abstract protected void onModuleStart() throws Exception;
    abstract protected void onModuleStop();

    protected final <T> T getKernelParameter(String _name) {
        return this.kernel.getConfigParam(_name);
    }

    protected <T> T getService(Class<T> _class) {
        T service = (T) this.kernel.getService(_class);
        if (null == service) {
            throw new IllegalArgumentException("Class " + _class.getName() + " is not a model.service");
        }

        return service;
    }
}
