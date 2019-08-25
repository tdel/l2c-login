package network.gameclient.packets;

import network.gameclient.GameClientChannelHandler;

public interface IncomingGameClientPacketInterface {
    public void execute(PacketReader _reader, GameClientChannelHandler _client);
}
