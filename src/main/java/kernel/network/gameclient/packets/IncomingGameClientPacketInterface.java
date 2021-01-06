package kernel.network.gameclient.packets;

import game.network.server.gameclient.GCClient;
import game.network.server.gameclient.GCClientState;

public interface IncomingGameClientPacketInterface {
    public void execute(PacketReader _reader, GCClient _client);
    public boolean supports(PacketReader _reader, GCClientState _state);
}
