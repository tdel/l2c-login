package model.repository;

import com.google.inject.Inject;
import com.google.inject.Provider;
import kernel.Kernel;
import kernel.KernelModuleInterface;

import java.util.Map;

public class RepositoryKernelModule implements KernelModuleInterface {

    private final Map<Class, Provider<PreloadableRepositoryInterface>> repositories;

    @Inject
    public RepositoryKernelModule(Map<Class, Provider<PreloadableRepositoryInterface>> _repositories) {
        this.repositories = _repositories;
    }

    @Override
    public void onBoot(Kernel _kernel) throws Exception {
        for (Provider<PreloadableRepositoryInterface> repository : this.repositories.values()) {
            repository.get().preload();
        }
    }

    @Override
    public void onHalt(Kernel _kernel) {

    }
}
