package controller;

import com.google.inject.Singleton;
import controller.gameclient.AuthGameGuard;
import controller.gameclient.RequestAuthLogin;
import controller.gameclient.RequestGameServerLogin;
import controller.gameserver.RequestAuth;
import main.AbstractApplicationModule;
import network.gameclient.packets.IncomingGameClientPacketInterface;
import network.gameserver.packets.IncomingGameServerPacketInterface;

public class ControllerGuiceModule extends AbstractApplicationModule {

    @Override
    protected void configure() {
        this.bind(AuthGameGuard.class).in(Singleton.class);
        this.bind(RequestAuthLogin.class).in(Singleton.class);
        this.bind(RequestGameServerLogin.class).in(Singleton.class);

        this.bind(GameClientController.class).in(Singleton.class);

        this.bindToMap(IncomingGameClientPacketInterface.class, AuthGameGuard.class);
        this.bindToMap(IncomingGameClientPacketInterface.class, RequestAuthLogin.class);
        this.bindToMap(IncomingGameClientPacketInterface.class, RequestGameServerLogin.class);

        this.bindToMap(IncomingGameServerPacketInterface.class, RequestAuth.class);
    }
}
