package main.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import subsystem.network.gameclient.GameClientChannelInitializer;
import subsystem.network.gameclient.GameClientServer;
import subsystem.network.gameclient.security.BlowfishGenerator;

public class NetworkSubsystemModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(GameClientServer.class).in(Singleton.class);
        this.bind(GameClientChannelInitializer.class).in(Singleton.class);
        this.bind(BlowfishGenerator.class).in(Singleton.class);
    }
}
