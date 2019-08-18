package app.kernel.subsystem.network;

import com.google.inject.Inject;
import app.kernel.Kernel;
import app.kernel.subsystem.AbstractKernelSubsystem;
import app.kernel.subsystem.network.core.NetworkServer;
import app.kernel.subsystem.network.gameclient.GameClientServer;

public class NetworkSubsystem extends AbstractKernelSubsystem {

    private NetworkServer gameClientServer;

    @Inject
    public NetworkSubsystem(Kernel _kernel) {
        super(_kernel);
    }

    @Override
    protected void onModuleStart() throws Exception {
        int gameClientPort = this.getKernelParameter("app.kernel.subsystem.network.gameclient.server.port");

        this.gameClientServer = this.getService(GameClientServer.class);
        this.gameClientServer.start(gameClientPort);
    }

    @Override
    protected void onModuleStop() {
        this.gameClientServer.stop();
    }
}
