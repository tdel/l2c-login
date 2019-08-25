package kernel;

public interface KernelModuleInterface {
    void onBoot(Kernel _kernel) throws Exception;
    void onHalt(Kernel _kernel);
}
