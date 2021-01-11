package network;

import com.google.inject.Inject;
import kernel.Kernel;
import kernel.LoadableKernelServiceInterface;
import network.gameclient.security.NetworkSecurity;
import network.server.gameclient.GCServer;
import network.server.gameserver.GSServer;

public class KernelNetworkService implements LoadableKernelServiceInterface {

    private final NetworkSecurity networkSecurity;
    private final GCServer gcServer;
    private final GSServer gsServer;


    @Inject
    public KernelNetworkService(NetworkSecurity _networkSecurity, GCServer _gcServer, GSServer _gsServer) {
        this.networkSecurity = _networkSecurity;
        this.gcServer = _gcServer;
        this.gsServer = _gsServer;
    }

    @Override
    public void register(Kernel _kernel) throws Exception {
        this.networkSecurity.load();
        this.startServerServer();
        this.startClientServer();
    }

    @Override
    public void unregister(Kernel _kernel) {
        this.stopClientServer();
        this.stopServerServer();
    }

    public void startClientServer() {
        this.gcServer.start();
    }

    public void stopClientServer() {
        this.gcServer.stop();
    }

    public void startServerServer() {
        this.gsServer.start();
    }

    public void stopServerServer() {
        this.gsServer.stop();
    }

}
