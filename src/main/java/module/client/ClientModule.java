package module.client;


import com.google.inject.Inject;
import kernel.AbstractKernelModule;
import kernel.Kernel;
import module.client.network.ClientChannelInitializer;
import module.client.network.ClientServer;
import module.client.network.packets.AbstractInPacket;
import module.client.network.packets.in.AuthGameGuard;
import module.client.network.packets.in.RequestAuthLogin;
import module.client.network.packets.in.RequestGameServerLogin;
import module.client.security.BlowfishGenerator;
import module.client.security.PasswordSecurity;
import module.client.service.playerlogin.PlayerLoginService;
import module.client.service.GameServers;

import javax.persistence.EntityManager;

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
