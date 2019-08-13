package kernel;

abstract public class AbstractKernelModule {

    private Kernel kernel;
    private KernelModuleStatus status;

    public AbstractKernelModule(Kernel _kernel) {
        this.kernel = _kernel;
        this.status = KernelModuleStatus.STOPED;
    }

    public void start() throws Exception {
        if (this.status != KernelModuleStatus.STOPED) {
            throw new IllegalStateException("Kernel module must be stopped to be started !");
        }

        this.status = KernelModuleStatus.STARTING;
        try {
            this.onModuleStart();
        } catch (Exception e) {
            this.status = KernelModuleStatus.STOPED;

            throw e;
        }

        this.status = KernelModuleStatus.RUNNING;
    }

    public boolean stop() {
        if (this.status != KernelModuleStatus.RUNNING) {
            throw new IllegalStateException("Kernel module must be running to be stoped !");
        }

        this.status = KernelModuleStatus.STOPING;
        try {
            this.onModuleStop();
        } catch (Exception e) {
            this.status = KernelModuleStatus.STOPED;

            return false;
        }

        this.status = KernelModuleStatus.STOPED;

        return true;
    }

    abstract protected void onModuleStart() throws Exception;
    abstract protected void onModuleStop();

    protected final <T> T getKernelParameter(String _name) {
        return this.kernel.getConfigParam(_name);
    }

    protected final Kernel getKernel() {
        return this.kernel;
    }

    protected final void registerService(Object _service) {
        this.kernel.registerService(_service);
    }
    protected <T> T getService(Class<T> _class) {
        T service = (T) this.kernel.getService(_class);
        if (null == service) {
            throw new IllegalArgumentException("La classe " + _class.getName() + " n'est pas référencé en tant que service");
        }

        return service;
    }
}
