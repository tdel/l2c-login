package network.gameclient;

import network.gameclient.packets.PacketReader;
import network.server.gameclient.GCClient;

public interface GameServerControllerInterface {
    void handle(PacketReader _reader, GCClient _client);
}
