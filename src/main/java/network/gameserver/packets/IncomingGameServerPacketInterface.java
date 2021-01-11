package network.gameserver.packets;

import network.server.gameserver.GSClient;

public interface IncomingGameServerPacketInterface {
    boolean supports(PacketReader _reader);
    void execute(PacketReader _reader, GSClient _client);
}
