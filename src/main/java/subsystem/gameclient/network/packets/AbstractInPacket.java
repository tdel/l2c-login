package subsystem.gameclient.network.packets;

import subsystem.gameclient.network.ChannelHandler;

abstract public class AbstractInPacket {

    abstract public void execute(PacketReader _reader, ChannelHandler _client);
}
