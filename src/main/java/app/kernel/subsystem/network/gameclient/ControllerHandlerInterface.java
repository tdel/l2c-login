package app.kernel.subsystem.network.gameclient;

import app.kernel.subsystem.network.gameclient.packets.IncomingGameClientPacketInterface;
import app.kernel.subsystem.network.gameclient.packets.PacketReader;

public interface ControllerHandlerInterface {
    public IncomingGameClientPacketInterface handle(PacketReader _reader, ChannelHandler _client);
}
