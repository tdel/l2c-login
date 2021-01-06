package game.guice;

import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import game.network.controller.GameClientController;
import game.network.controller.gameclient.AuthGameGuard;
import game.network.controller.gameclient.RequestAuthLogin;
import game.network.controller.gameclient.RequestGameServerLogin;
import game.network.controller.gameserver.RequestAuth;
import game.network.server.gameclient.GCServer;
import game.network.server.gameserver.GSServer;
import kernel.network.gameclient.security.PasswordSecurity;
import main.AbstractApplicationModule;
import kernel.network.gameclient.packets.IncomingGameClientPacketInterface;
import kernel.network.gameserver.packets.IncomingGameServerPacketInterface;
import game.model.repository.AccountRepository;
import game.model.repository.GameServerRepository;
import game.model.repository.LoadableRepositoryInterface;
import game.model.repository.RepositoryKernelService;
import game.service.playerlogin.PlayerLoginService;

public class GameGuice extends AbstractApplicationModule {

    @Override
    protected void configure() {
        this.configureNetwork();
        this.configureRepositories();
        this.configureControllers();
        this.configureServices();
    }

    private void configureNetwork() {
        this.bind(GCServer.class).in(Singleton.class);
        this.bind(GSServer.class).in(Singleton.class);
    }

    private void configureRepositories() {
        this.bind(GameServerRepository.class).in(Singleton.class);
        this.bind(AccountRepository.class).in(Singleton.class);

        this.bind(RepositoryKernelService.class).in(Singleton.class);

        this.bindToKernel(RepositoryKernelService.class);
        this.bindToMap(LoadableRepositoryInterface.class, GameServerRepository.class);
    }

    private void configureControllers() {
        this.bind(AuthGameGuard.class).in(Singleton.class);
        this.bind(RequestAuthLogin.class).in(Singleton.class);
        this.bind(RequestGameServerLogin.class).in(Singleton.class);

        this.bind(GameClientController.class).in(Singleton.class);

        Multibinder<IncomingGameClientPacketInterface> igcpBinder = Multibinder.newSetBinder(binder(), IncomingGameClientPacketInterface.class);
        igcpBinder.addBinding().to(AuthGameGuard.class);
        igcpBinder.addBinding().to(RequestAuthLogin.class);
        igcpBinder.addBinding().to(RequestGameServerLogin.class);

        Multibinder<IncomingGameServerPacketInterface> igspBinder = Multibinder.newSetBinder(binder(), IncomingGameServerPacketInterface.class);
        igspBinder.addBinding().to(RequestAuth.class);
    }

    private void configureServices() {
        this.bind(PasswordSecurity.class).in(Singleton.class);
        this.bind(PlayerLoginService.class).in(Singleton.class);
    }
}
