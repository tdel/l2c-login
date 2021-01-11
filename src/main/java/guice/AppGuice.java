package guice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import configuration.Config;
import configuration.ConfigLoadableKernel;
import database.DatabaseLoadableKernel;
import game.model.repository.AccountRepository;
import game.model.repository.GameServerRepository;
import game.model.repository.LoadableRepositoryInterface;
import game.model.repository.RepositoryLoadableKernelService;
import kernel.KernelInit;
import kernel.LoadableKernelServiceInterface;
import network.KernelNetworkService;
import network.controller.GameClientController;
import network.controller.gameclient.AuthGameGuardIn;
import network.controller.gameclient.RequestAuthLogin;
import network.controller.gameclient.RequestGameServerLoginIn;
import network.controller.gameserver.RequestAuth;
import network.gameclient.packets.GCPacketInInterface;
import network.gameclient.security.NetworkSecurity;
import network.gameclient.security.PasswordSecurity;
import network.gameserver.packets.IncomingGameServerPacketInterface;
import network.gameserver.packets.codec.JSONToPacket;
import network.gameserver.packets.codec.PacketToJSON;
import network.server.gameclient.GCServer;
import network.server.gameserver.GSServer;
import service.playerlogin.PlayerLoginService;

public class AppGuice extends AbstractModule {

    private final String configFilename;

    public AppGuice(String _configFilename) {
        this.configFilename = _configFilename;
    }

    @Override
    protected void configure() {
        this.bind(KernelInit.class).in(Singleton.class);

        this.configureConfig();
        //this.configureDatabase();
        this.configureRepositories();
        this.configureServices();
        this.configureControllers();

        this.configureNetwork();
    }

    private void configureDatabase() {
        this.bind(DatabaseLoadableKernel.class).in(Singleton.class);
        this.bindToKernel(DatabaseLoadableKernel.class);
    }

    private void configureNetwork() {
        this.bind(GCServer.class).in(Singleton.class);
        this.bind(GSServer.class).in(Singleton.class);

        this.bind(KernelNetworkService.class).in(Singleton.class);
        this.bindToKernel(KernelNetworkService.class);

        this.bind(NetworkSecurity.class).in(Singleton.class);

        this.bind(PacketToJSON.class).in(Singleton.class);
        this.bind(JSONToPacket.class).in(Singleton.class);

        Gson gson = new GsonBuilder().serializeNulls().create();
        this.bind(Gson.class).toInstance(gson);
    }

    private void configureConfig() {
        Config config = new Config();
        this.bind(Config.class).toInstance(config);

        ConfigLoadableKernel kernelModule = new ConfigLoadableKernel(config, this.configFilename);
        this.bind(ConfigLoadableKernel.class).toInstance(kernelModule);

        this.bindToKernel(ConfigLoadableKernel.class);
    }

    private void configureRepositories() {
        this.bind(GameServerRepository.class).in(Singleton.class);
        this.bind(AccountRepository.class).in(Singleton.class);

        this.bind(RepositoryLoadableKernelService.class).in(Singleton.class);

        this.bindToKernel(RepositoryLoadableKernelService.class);
        this.bindToMap(LoadableRepositoryInterface.class, GameServerRepository.class);
    }

    private void configureControllers() {
        this.bind(AuthGameGuardIn.class).in(Singleton.class);
        this.bind(RequestAuthLogin.class).in(Singleton.class);
        this.bind(RequestGameServerLoginIn.class).in(Singleton.class);

        this.bind(GameClientController.class).in(Singleton.class);

        Multibinder<GCPacketInInterface> igcpBinder = Multibinder.newSetBinder(binder(), GCPacketInInterface.class);
        igcpBinder.addBinding().to(AuthGameGuardIn.class);
        igcpBinder.addBinding().to(RequestAuthLogin.class);
        igcpBinder.addBinding().to(RequestGameServerLoginIn.class);

        Multibinder<IncomingGameServerPacketInterface> igspBinder = Multibinder.newSetBinder(binder(), IncomingGameServerPacketInterface.class);
        igspBinder.addBinding().to(RequestAuth.class);
    }

    private void configureServices() {
        this.bind(PasswordSecurity.class).in(Singleton.class);
        this.bind(PlayerLoginService.class).in(Singleton.class);
    }



    protected void bindToKernel(Class<? extends LoadableKernelServiceInterface> _class) {
        Multibinder<LoadableKernelServiceInterface> binder = Multibinder.newSetBinder(binder(), LoadableKernelServiceInterface.class);
        binder.addBinding().to(_class);

        //return this;
    }

    protected <V> void bindToMap(Class<V> _valueMap, Class<? extends V> _value) {
        MapBinder<Class, V> mapbinder = MapBinder.newMapBinder(binder(), Class.class, _valueMap);
        mapbinder.addBinding(_value).to(_value);

        // return this;
    }

}
