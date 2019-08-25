package controller;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.MapBinder;
import controller.gameclient.AuthGameGuard;
import controller.gameclient.RequestAuthLogin;
import controller.gameclient.RequestGameServerLogin;
import network.gameclient.packets.IncomingGameClientPacketInterface;

public class ControllerGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(AuthGameGuard.class).in(Singleton.class);
        this.bind(RequestAuthLogin.class).in(Singleton.class);
        this.bind(RequestGameServerLogin.class).in(Singleton.class);

        this.bind(GameClientController.class).in(Singleton.class);



        MapBinder<Class, IncomingGameClientPacketInterface> binder = MapBinder.newMapBinder(binder(), Class.class, IncomingGameClientPacketInterface.class);
        binder.addBinding(AuthGameGuard.class).to(AuthGameGuard.class);
        binder.addBinding(RequestAuthLogin.class).to(RequestAuthLogin.class);
        binder.addBinding(RequestGameServerLogin.class).to(RequestGameServerLogin.class);

    }
}
