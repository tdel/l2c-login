package subsystem.gameclient;


import com.google.inject.Inject;
import kernel.subsystem.AbstractKernelSubsystem;
import kernel.Kernel;
import subsystem.gameclient.network.Server;

public class GameClientSubsystem extends AbstractKernelSubsystem {

    private Server server;

    @Inject
    public GameClientSubsystem(Kernel _kernel) {
        super(_kernel);
    }

    @Override
    protected void onModuleStart() throws Exception {
        int port = this.getKernelParameter("module.player.server.port");

        this.server = this.getService(Server.class);
        this.server.start(port);
    }

    @Override
    protected void onModuleStop() {
        this.server.stop();
    }
}
