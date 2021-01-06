package kernel.network.gameclient;

import com.google.inject.Inject;
import kernel.network.core.NetworkServer;

public class GameClientServer extends NetworkServer {

    @Inject
    public GameClientServer(GameClientChannelInitializer _channelInitializer) {
        super(_channelInitializer);
    }

}
