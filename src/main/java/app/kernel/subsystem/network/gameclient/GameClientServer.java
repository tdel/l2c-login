package app.kernel.subsystem.network.gameclient;

import com.google.inject.Inject;
import app.kernel.subsystem.network.core.NetworkServer;

public class GameClientServer extends NetworkServer {

    @Inject
    public GameClientServer(GameClientChannelInitializer _channelInitializer) {
        super(_channelInitializer);
    }

}
