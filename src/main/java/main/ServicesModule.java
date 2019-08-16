package main;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import kernel.Kernel;
import subsystem.gameclient.GameClientSubsystem;
import subsystem.gameclient.network.ServerChannelInitializer;
import subsystem.gameclient.network.Server;
import subsystem.gameclient.network.packets.in.AuthGameGuard;
import subsystem.gameclient.network.packets.in.RequestAuthLogin;
import subsystem.gameclient.network.packets.in.RequestGameServerLogin;
import subsystem.gameclient.security.BlowfishGenerator;
import subsystem.gameclient.security.PasswordSecurity;
import subsystem.gameclient.service.GameServers;
import subsystem.gameclient.service.playerlogin.PlayerLoginService;

public class ServicesModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(Kernel.class).in(Singleton.class);

        this.bind(GameServers.class).in(Singleton.class);


        this.bind(BlowfishGenerator.class).in(Singleton.class);
        this.bind(PasswordSecurity.class).in(Singleton.class);


        this.bind(GameClientSubsystem.class).in(Singleton.class);
        this.bind(ServerChannelInitializer.class).in(Singleton.class);
        this.bind(PlayerLoginService.class).in(Singleton.class);
        this.bind(Server.class).in(Singleton.class);


        this.bind(AuthGameGuard.class).in(Singleton.class);
        this.bind(RequestAuthLogin.class).in(Singleton.class);
        this.bind(RequestGameServerLogin.class).in(Singleton.class);


    }

}

