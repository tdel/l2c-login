package module.gameserver;

import com.google.inject.Inject;
import kernel.AbstractKernelModule;
import kernel.Kernel;

public class GameServerModule extends AbstractKernelModule {

    @Inject
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
