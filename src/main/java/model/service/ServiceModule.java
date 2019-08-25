package model.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import network.gameclient.security.PasswordSecurity;
import model.repository.AccountRepository;
import model.service.gameserver.GameServers;
import model.service.playerlogin.PlayerLoginService;

public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(GameServers.class).in(Singleton.class);
        this.bind(PasswordSecurity.class).in(Singleton.class);
        this.bind(PlayerLoginService.class).in(Singleton.class);


        this.bind(AccountRepository.class).in(Singleton.class);

        Gson gson = new GsonBuilder().serializeNulls().create();
        this.bind(Gson.class).toInstance(gson);
    }

}

