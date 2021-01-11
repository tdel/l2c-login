package game.model.repository;

import com.google.inject.Inject;
import com.google.inject.Provider;
import kernel.Kernel;
import kernel.LoadableKernelServiceInterface;

import java.util.Map;

public class RepositoryLoadableKernelService implements LoadableKernelServiceInterface {

    private final Map<Class, Provider<LoadableRepositoryInterface>> repositories;

    @Inject
    public RepositoryLoadableKernelService(Map<Class, Provider<LoadableRepositoryInterface>> _repositories) {
        this.repositories = _repositories;
    }

    @Override
    public void register(Kernel _kernel) throws Exception {
        for (Provider<LoadableRepositoryInterface> repository : this.repositories.values()) {
            repository.get().preload();
        }
    }

    @Override
    public void unregister(Kernel _kernel) {

    }
}
