package main;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import kernel.Kernel;
import subsystem.network.NetworkSubsystem;
import subsystem.network.gameclient.GameClientChannelInitializer;
import controller.AuthGameGuard;
import controller.RequestAuthLogin;
import controller.RequestGameServerLogin;
import subsystem.network.gameclient.GameClientServer;
import subsystem.network.gameclient.security.BlowfishGenerator;
import subsystem.network.gameclient.security.PasswordSecurity;
import service.gameserver.GameServers;
import service.playerlogin.PlayerLoginService;

public class ServicesModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(Kernel.class).in(Singleton.class);

        this.bind(GameServers.class).in(Singleton.class);


        this.bind(BlowfishGenerator.class).in(Singleton.class);
        this.bind(PasswordSecurity.class).in(Singleton.class);


        this.bind(NetworkSubsystem.class).in(Singleton.class);
        this.bind(GameClientChannelInitializer.class).in(Singleton.class);
        this.bind(PlayerLoginService.class).in(Singleton.class);
        this.bind(GameClientServer.class).in(Singleton.class);


        this.bind(AuthGameGuard.class).in(Singleton.class);
        this.bind(RequestAuthLogin.class).in(Singleton.class);
        this.bind(RequestGameServerLogin.class).in(Singleton.class);
    }

}

