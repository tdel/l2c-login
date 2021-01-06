package kernel.network.gameclient;

import kernel.network.gameclient.packets.PacketReader;

public interface GameServerControllerInterface {
    public void handle(PacketReader _reader, GameClientChannelHandler _client);
}
