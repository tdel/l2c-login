package kernel;

public interface LoadableKernelServiceInterface {
    void register(Kernel _kernel) throws Exception;
    void unregister(Kernel _kernel);
}
