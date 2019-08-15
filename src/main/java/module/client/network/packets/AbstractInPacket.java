package module.client.network.packets;

import module.client.network.ClientHandler;

abstract public class AbstractInPacket {
    abstract public void execute(PacketReader _reader, ClientHandler _client);
}
