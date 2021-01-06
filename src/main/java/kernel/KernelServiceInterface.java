package kernel;

public interface KernelServiceInterface {
    void onBoot(Kernel _kernel) throws Exception;
    void onHalt(Kernel _kernel);
}
