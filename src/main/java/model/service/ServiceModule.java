package model.service;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import network.gameclient.security.PasswordSecurity;
import model.service.playerlogin.PlayerLoginService;

public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {

        this.bind(PasswordSecurity.class).in(Singleton.class);
        this.bind(PlayerLoginService.class).in(Singleton.class);





    }

}

