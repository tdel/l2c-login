package app.kernel.subsystem.network.gameclient.packets;

import app.kernel.subsystem.network.gameclient.ChannelHandler;

public interface IncomingGameClientPacketInterface {
    public void execute(PacketReader _reader, ChannelHandler _client);
}
