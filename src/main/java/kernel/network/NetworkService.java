package kernel.network;

import kernel.configuration.Config;
import kernel.KernelServiceInterface;
import game.network.server.gameserver.GSServer;
import com.google.inject.Inject;
import kernel.Kernel;
import game.network.server.gameclient.GCServer;

public class NetworkService implements KernelServiceInterface {

    private final Config config;
    private final GCServer gcServer;
    private final GSServer gsServer;


    @Inject
    public NetworkService(Config _config, GCServer _gcServer, GSServer _gsServer) {
        this.config = _config;
        this.gcServer = _gcServer;
        this.gsServer = _gsServer;
    }

    @Override
    public void onBoot(Kernel _kernel) {
        int gcPort = this.config.getInt("network.gameclient.server.port");
        this.gcServer.start(gcPort);

        int gsPort = this.config.getInt("network.gameserver.server.port");
        this.gsServer.start(gsPort);
    }

    @Override
    public void onHalt(Kernel _kernel) {
        this.gcServer.stop();
        this.gsServer.stop();
    }

}
