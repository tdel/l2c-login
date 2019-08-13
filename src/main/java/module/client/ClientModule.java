package module.client;


import kernel.AbstractKernelModule;
import kernel.Kernel;
import module.client.network.ClientServer;
import module.client.network.packets.AbstractInPacket;
import module.client.network.packets.in.AuthGameGuard;
import module.client.network.packets.in.RequestAuthLogin;
import module.client.network.packets.in.RequestGameServerLogin;
import module.client.repository.PlayerRepository;
import module.client.security.PasswordSecurity;
import module.client.service.playerlogin.PlayerLoginService;
import module.client.service.GameServers;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

public class ClientModule extends AbstractKernelModule {

    private ClientServer server;
    private Map<Class, AbstractInPacket> packetsIn;


    public ClientModule(Kernel _kernel) {
        super(_kernel);
        this.packetsIn = new HashMap<>();
    }

    @Override
    protected void onModuleStart() throws Exception {
        int port = this.getKernelParameter("module.player.server.port");

        this.loadServices();
        this.loadPacketsIn();

        this.server = new ClientServer(this, port);
        this.server.start();
    }

    private void loadServices() throws Exception {
        this.registerService(new PasswordSecurity());
        this.registerService(new PlayerRepository());
        this.registerService(new GameServers(this.getService(EntityManager.class)));
        this.registerService(new PlayerLoginService(
                this.getService(PasswordSecurity.class),
                this.getService(EntityManager.class)
        ));
    }


    private void loadPacketsIn() throws Exception {
        this.addPacket(new AuthGameGuard());
        this.addPacket(new RequestAuthLogin(this.getService(PlayerLoginService.class), this.getService(GameServers.class)));
        this.addPacket(new RequestGameServerLogin());
    }

    private void addPacket(AbstractInPacket _packet) {
        this.packetsIn.put(_packet.getClass(), _packet);
    }

    public <T> T getInPacket(Class _name) {
        return (T) this.packetsIn.get(_name);
    }

    @Override
    protected void onModuleStop() {
        this.server.stop();
    }
}
