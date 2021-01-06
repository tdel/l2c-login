package kernel.network.gameclient;

import game.network.server.gameclient.GCClient;
import kernel.network.gameclient.packets.PacketReader;

public interface GameServerControllerInterface {
    public void handle(PacketReader _reader, GCClient _client);
}
