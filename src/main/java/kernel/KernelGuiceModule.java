package kernel;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import module.client.ClientModule;
import module.gameserver.GameServerModule;

public class KernelGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(Kernel.class).in(Singleton.class);
        this.bind(ClientModule.class).in(Singleton.class);
        this.bind(GameServerModule.class).in(Singleton.class);
    }

}

