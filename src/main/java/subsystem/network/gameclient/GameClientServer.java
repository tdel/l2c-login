package subsystem.network.gameclient;

import com.google.inject.Inject;
import subsystem.network.core.NetworkServer;

public class GameClientServer extends NetworkServer {

    @Inject
    public GameClientServer(GameClientChannelInitializer _channelInitializer) {
        super(_channelInitializer);
    }

}
