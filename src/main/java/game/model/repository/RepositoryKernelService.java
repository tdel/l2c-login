package game.model.repository;

import com.google.inject.Inject;
import com.google.inject.Provider;
import kernel.Kernel;
import kernel.KernelServiceInterface;

import java.util.Map;

public class RepositoryKernelService implements KernelServiceInterface {

    private final Map<Class, Provider<LoadableRepositoryInterface>> repositories;

    @Inject
    public RepositoryKernelService(Map<Class, Provider<LoadableRepositoryInterface>> _repositories) {
        this.repositories = _repositories;
    }

    @Override
    public void onBoot(Kernel _kernel) throws Exception {
        for (Provider<LoadableRepositoryInterface> repository : this.repositories.values()) {
            repository.get().preload();
        }
    }

    @Override
    public void onHalt(Kernel _kernel) {

    }
}
