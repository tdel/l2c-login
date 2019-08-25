package network;

import configuration.Config;
import kernel.KernelModuleInterface;
import network.gameserver.GameServerServer;
import com.google.inject.Inject;
import kernel.Kernel;
import network.gameclient.GameClientServer;

public class NetworkKernelModule implements KernelModuleInterface {

    private final Config config;
    private final GameClientServer gcServer;
    private final GameServerServer gsServer;


    @Inject
    public NetworkKernelModule(Config _config, GameClientServer _gcServer, GameServerServer _gsServer) {
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
