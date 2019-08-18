package app.main.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import app.kernel.subsystem.network.gameclient.security.PasswordSecurity;
import model.service.gameserver.GameServers;
import model.service.playerlogin.PlayerLoginService;

public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(GameServers.class).in(Singleton.class);
        this.bind(PasswordSecurity.class).in(Singleton.class);
        this.bind(PlayerLoginService.class).in(Singleton.class);
    }

}

