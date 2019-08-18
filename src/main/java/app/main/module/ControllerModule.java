package app.main.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import controller.gameclient.AuthGameGuard;
import controller.gameclient.RequestAuthLogin;
import controller.gameclient.RequestGameServerLogin;

public class ControllerModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(AuthGameGuard.class).in(Singleton.class);
        this.bind(RequestAuthLogin.class).in(Singleton.class);
        this.bind(RequestGameServerLogin.class).in(Singleton.class);

    }
}
