package kernel.network.gameserver.packets;

import game.network.server.gameserver.GSClient;

public interface IncomingGameServerPacketInterface {
    public boolean supports(PacketReader _reader);
    void execute(PacketReader _reader, GSClient _client);
}
