package subsystem.network.gameclient.packets;

import subsystem.network.gameclient.ChannelHandler;

abstract public class AbstractInPacket {
    abstract public void execute(PacketReader _reader, ChannelHandler _client);
}
