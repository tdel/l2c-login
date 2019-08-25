package network;

import com.google.inject.multibindings.Multibinder;
import database.DatabaseKernelModule;
import kernel.KernelModuleInterface;
import network.gameserver.GameServerChannelInitializer;
import network.gameserver.GameServerServer;
import network.gameserver.packets.codec.JSONToPacket;
import network.gameserver.packets.codec.PacketToJSON;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import network.gameclient.GameClientChannelInitializer;
import network.gameclient.GameClientServer;
import network.gameclient.security.BlowfishGenerator;

public class NetworkGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(NetworkKernelModule.class).in(Singleton.class);


        this.bind(GameClientServer.class).in(Singleton.class);
        this.bind(GameClientChannelInitializer.class).in(Singleton.class);
        this.bind(BlowfishGenerator.class).in(Singleton.class);


        this.bind(GameServerServer.class).in(Singleton.class);
        this.bind(GameServerChannelInitializer.class).in(Singleton.class);
        this.bind(PacketToJSON.class).in(Singleton.class);
        this.bind(JSONToPacket.class).in(Singleton.class);


        Multibinder<KernelModuleInterface> binder = Multibinder.newSetBinder(binder(), KernelModuleInterface.class);
        binder.addBinding().to(NetworkKernelModule.class);

    }

}
