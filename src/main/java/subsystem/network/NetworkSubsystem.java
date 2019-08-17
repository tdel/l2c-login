package subsystem.network;

import com.google.inject.Inject;
import kernel.Kernel;
import kernel.subsystem.AbstractKernelSubsystem;
import subsystem.network.gameclient.GameClientServer;

public class NetworkSubsystem extends AbstractKernelSubsystem {

    private GameClientServer server;

    @Inject
    public NetworkSubsystem(Kernel _kernel) {
        super(_kernel);
    }

    @Override
    protected void onModuleStart() throws Exception {
        int port = this.getKernelParameter("module.player.server.port");

        this.server = this.getService(GameClientServer.class);
        this.server.start(port);
    }

    @Override
    protected void onModuleStop() {
        this.server.stop();
    }
}
