package kernel;

abstract public class AbstractKernelModule {

    private Kernel kernel;
    private KernelModuleStatus status;

    public AbstractKernelModule(Kernel _kernel) {
        this.kernel = _kernel;
        this.status = KernelModuleStatus.STOPED;
    }

    public boolean start() {
        if (this.status != KernelModuleStatus.STOPED) {
            throw new IllegalStateException("Kernel module must be stopped to be started !");
        }

        this.status = KernelModuleStatus.STARTING;
        try {
            this.onModuleStart();
        } catch (Exception e) {
            this.status = KernelModuleStatus.STOPED;

            return false;
        }

        this.status = KernelModuleStatus.RUNNING;

        return true;
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

    abstract protected void onModuleStart();
    abstract protected void onModuleStop();

    protected final <T> T getKernelParameter(String _name) {
        return this.kernel.getConfigParam(_name);
    }

}
