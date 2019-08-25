package network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.AbstractApplicationModule;
import network.gameserver.GameServerChannelInitializer;
import network.gameserver.GameServerServer;
import network.gameserver.packets.codec.JSONToPacket;
import network.gameserver.packets.codec.PacketToJSON;
import com.google.inject.Singleton;
import network.gameclient.GameClientChannelInitializer;
import network.gameclient.GameClientServer;
import network.gameclient.security.BlowfishGenerator;

public class NetworkGuiceModule extends AbstractApplicationModule {

    @Override
    protected void configure() {
        this.bind(NetworkKernelModule.class).in(Singleton.class);
        this.bindToKernel(NetworkKernelModule.class);


        this.bind(GameClientServer.class).in(Singleton.class);
        this.bind(GameClientChannelInitializer.class).in(Singleton.class);
        this.bind(BlowfishGenerator.class).in(Singleton.class);

        this.bind(GameServerServer.class).in(Singleton.class);
        this.bind(GameServerChannelInitializer.class).in(Singleton.class);
        this.bind(PacketToJSON.class).in(Singleton.class);
        this.bind(JSONToPacket.class).in(Singleton.class);

        Gson gson = new GsonBuilder().serializeNulls().create();
        this.bind(Gson.class).toInstance(gson);


    }

}
