package kernel.network.gameserver.packets;

import kernel.network.gameserver.GameServerChannelHandler;

public interface IncomingGameServerPacketInterface {
    public boolean supports(PacketReader _reader);
    void execute(PacketReader _reader, GameServerChannelHandler _client);
}
