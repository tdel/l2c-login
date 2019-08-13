package module.player;


import kernel.AbstractKernelModule;
import kernel.Kernel;
import module.player.network.PlayerServer;

public class PlayerModule extends AbstractKernelModule {

    private PlayerServer server;

    public PlayerModule(Kernel _kernel) {
        super(_kernel);
    }

    @Override
    protected void onModuleStart() {
        int port = this.getKernelParameter("module.player.server.port");

        this.server = new PlayerServer(port);
        this.server.start();
    }

    @Override
    protected void onModuleStop() {
        this.server.stop();
    }
}
