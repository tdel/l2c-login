package module.client;


import com.google.inject.Inject;
import kernel.AbstractKernelModule;
import kernel.Kernel;
import module.client.network.ClientServer;

public class ClientModule extends AbstractKernelModule {

    private ClientServer server;

    @Inject
    public ClientModule(Kernel _kernel) {
        super(_kernel);
    }

    @Override
    protected void onModuleStart() throws Exception {
        int port = this.getKernelParameter("module.player.server.port");

        this.server = this.getService(ClientServer.class);
        this.server.start(port);
    }

    @Override
    protected void onModuleStop() {
        this.server.stop();
    }
}
