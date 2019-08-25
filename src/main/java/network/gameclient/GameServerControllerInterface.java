package network.gameclient;

import network.gameclient.packets.IncomingGameClientPacketInterface;
import network.gameclient.packets.PacketReader;

public interface GameServerControllerInterface {
    public IncomingGameClientPacketInterface handle(PacketReader _reader, GameClientChannelHandler _client);
}
