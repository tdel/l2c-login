package network.gameclient;

import com.google.inject.Inject;
import network.core.NetworkServer;

public class GameClientServer extends NetworkServer {

    @Inject
    public GameClientServer(GameClientChannelInitializer _channelInitializer) {
        super(_channelInitializer);
    }

}
