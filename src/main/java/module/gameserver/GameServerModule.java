package module.gameserver;

import kernel.AbstractKernelModule;
import kernel.Kernel;
import module.client.service.GameServers;

import javax.persistence.EntityManager;

public class GameServerModule extends AbstractKernelModule {

    public GameServerModule(Kernel _kernel) {
        super(_kernel);
    }

    @Override
    protected void onModuleStart() throws Exception {
        this.loadServices();
    }

    @Override
    protected void onModuleStop() {

    }


    private void loadServices() {

    }
}
