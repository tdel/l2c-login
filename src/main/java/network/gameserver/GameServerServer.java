package network.gameserver;

import network.core.NetworkServer;
import com.google.inject.Inject;

public class GameServerServer extends NetworkServer {

    @Inject
    public GameServerServer(GameServerChannelInitializer _channelInitializer) {
        super(_channelInitializer);
    }

}
