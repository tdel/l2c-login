package kernel.network.gameclient.packets;

import kernel.network.gameclient.GameClientChannelHandler;
import kernel.network.gameclient.GameClientConnectionState;

public interface IncomingGameClientPacketInterface {
    public void execute(PacketReader _reader, GameClientChannelHandler _client);
    public boolean supports(PacketReader _reader, GameClientConnectionState _state);
}
