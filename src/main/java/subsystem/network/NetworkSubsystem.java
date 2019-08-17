package subsystem.network;

import com.google.inject.Inject;
import kernel.Kernel;
import kernel.subsystem.AbstractKernelSubsystem;
import subsystem.network.gameclient.GameClientServer;

public class NetworkSubsystem extends AbstractKernelSubsystem {

    private GameClientServer gameClientServer;

    @Inject
    public NetworkSubsystem(Kernel _kernel) {
        super(_kernel);
    }

    @Override
    protected void onModuleStart() throws Exception {
        int port = this.getKernelParameter("subsystem.network.gameclient.server.port");

        this.gameClientServer = this.getService(GameClientServer.class);
        this.gameClientServer.start(port);
    }

    @Override
    protected void onModuleStop() {
        this.gameClientServer.stop();
    }
}
