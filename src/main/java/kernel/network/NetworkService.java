package kernel.network;

import kernel.KernelServiceInterface;
import game.network.server.gameserver.GSServer;
import com.google.inject.Inject;
import kernel.Kernel;
import game.network.server.gameclient.GCServer;

public class NetworkService implements KernelServiceInterface {

    private final GCServer gcServer;
    private final GSServer gsServer;


    @Inject
    public NetworkService(GCServer _gcServer, GSServer _gsServer) {
        this.gcServer = _gcServer;
        this.gsServer = _gsServer;
    }

    @Override
    public void onBoot(Kernel _kernel) {
        this.gcServer.start();
        this.gsServer.start();
    }

    @Override
    public void onHalt(Kernel _kernel) {
        this.gcServer.stop();
        this.gsServer.stop();
    }

}
