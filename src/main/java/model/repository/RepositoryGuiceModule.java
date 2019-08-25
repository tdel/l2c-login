package model.repository;

import com.google.inject.Singleton;
import main.AbstractApplicationModule;

public class RepositoryGuiceModule extends AbstractApplicationModule {

    @Override
    protected void configure() {
        this.bind(GameServerRepository.class).in(Singleton.class);
        this.bind(AccountRepository.class).in(Singleton.class);

        this.bind(RepositoryKernelModule.class).in(Singleton.class);

        this.bindToKernel(RepositoryKernelModule.class);
        this.bindToMap(PreloadableRepositoryInterface.class, GameServerRepository.class);
    }
}
