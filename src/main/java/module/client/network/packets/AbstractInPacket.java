package module.client.network.packets;

import module.client.network.ClientHandler;
import module.client.network.packets.PacketReader;

abstract public class AbstractInPacket {

    abstract public void read(PacketReader _reader);

    abstract public void execute(ClientHandler _client);
}
