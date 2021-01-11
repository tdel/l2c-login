package network.gameclient.packets;

import network.server.gameclient.GCClient;
import network.server.gameclient.GCClientState;

public interface GCPacketInInterface {
    void execute(PacketReader _reader, GCClient _client);
    boolean supports(PacketReader _reader, GCClientState _state);
}
