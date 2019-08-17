package main.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import subsystem.network.gameclient.security.PasswordSecurity;
import service.gameserver.GameServers;
import service.playerlogin.PlayerLoginService;

public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(GameServers.class).in(Singleton.class);
        this.bind(PasswordSecurity.class).in(Singleton.class);
        this.bind(PlayerLoginService.class).in(Singleton.class);
    }

}

