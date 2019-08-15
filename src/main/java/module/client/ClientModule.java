package module.client;


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

    public ClientModule(Kernel _kernel) {
        super(_kernel);
    }

    @Override
    protected void onModuleStart() throws Exception {
        int port = this.getKernelParameter("module.player.server.port");

        this.loadServices();

        ClientChannelInitializer channelInitializer = new ClientChannelInitializer(this.getKernel(), this.getService(BlowfishGenerator.class));
        this.server = new ClientServer(port, channelInitializer);
        this.server.start();
    }

    private void loadServices() throws Exception {
        this.registerService(new PasswordSecurity());
        this.registerService(new GameServers(this.getService(EntityManager.class)));
        this.registerService(new PlayerLoginService(
                this.getService(PasswordSecurity.class),
                this.getService(EntityManager.class)
        ));

        this.registerService(new AuthGameGuard());
        this.registerService(new RequestAuthLogin(this.getService(PlayerLoginService.class), this.getService(GameServers.class)));
        this.registerService(new RequestGameServerLogin());

    }


    @Override
    protected void onModuleStop() {
        this.server.stop();
    }
}
