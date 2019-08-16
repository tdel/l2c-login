package kernel;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import module.client.ClientModule;
import module.client.network.ClientChannelInitializer;
import module.client.network.ClientServer;
import module.client.network.packets.in.AuthGameGuard;
import module.client.network.packets.in.RequestAuthLogin;
import module.client.network.packets.in.RequestGameServerLogin;
import module.client.security.BlowfishGenerator;
import module.client.security.PasswordSecurity;
import module.client.service.GameServers;
import module.client.service.playerlogin.PlayerLoginService;
import module.gameserver.GameServerModule;

public class KernelGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(Kernel.class).in(Singleton.class);


        this.bind(GameServerModule.class).in(Singleton.class);
        this.bind(GameServers.class).in(Singleton.class);


        this.bind(BlowfishGenerator.class).in(Singleton.class);
        this.bind(PasswordSecurity.class).in(Singleton.class);


        this.bind(ClientModule.class).in(Singleton.class);
        this.bind(ClientChannelInitializer.class).in(Singleton.class);
        this.bind(PlayerLoginService.class).in(Singleton.class);
        this.bind(ClientServer.class).in(Singleton.class);


        this.bind(AuthGameGuard.class).in(Singleton.class);
        this.bind(RequestAuthLogin.class).in(Singleton.class);
        this.bind(RequestGameServerLogin.class).in(Singleton.class);


    }

}

