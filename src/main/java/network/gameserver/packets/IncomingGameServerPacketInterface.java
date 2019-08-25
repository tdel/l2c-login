package network.gameserver.packets;

import network.gameserver.GameServerChannelHandler;
import network.gameserver.packets.PacketReader;

public interface IncomingGameServerPacketInterface {
    void execute(PacketReader _reader, GameServerChannelHandler _client);
}
