package kernel.guice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Singleton;
import kernel.Kernel;
import kernel.configuration.Config;
import kernel.configuration.ConfigService;
import kernel.database.DatabaseService;
import kernel.network.NetworkService;
import kernel.network.gameclient.GameClientChannelInitializer;
import kernel.network.gameclient.GameClientServer;
import kernel.network.gameclient.security.BlowfishGenerator;
import kernel.network.gameserver.GameServerChannelInitializer;
import kernel.network.gameserver.GameServerServer;
import kernel.network.gameserver.packets.codec.JSONToPacket;
import kernel.network.gameserver.packets.codec.PacketToJSON;
import main.AbstractApplicationModule;

public class KernelGuice extends AbstractApplicationModule {

    private String configFilename;

    public KernelGuice(String _configFilename) {
        this.configFilename = _configFilename;
    }

    @Override
    protected void configure() {
        this.bind(Kernel.class).in(Singleton.class);


        this.configureConfig();
        //this.configureDatabase();
        this.configureNetwork();

    }

    private void configureDatabase() {
        this.bind(DatabaseService.class).in(Singleton.class);
        this.bindToKernel(DatabaseService.class);
    }

    private void configureNetwork() {
        this.bind(NetworkService.class).in(Singleton.class);
        this.bindToKernel(NetworkService.class);


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

    private void configureConfig() {
        Config config = new Config();
        this.bind(Config.class).toInstance(config);

        ConfigService kernelModule = new ConfigService(config, this.configFilename);
        this.bind(ConfigService.class).toInstance(kernelModule);

        this.bindToKernel(ConfigService.class);
    }


}
