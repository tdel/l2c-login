package module.client.network.packets;

import module.client.network.PacketWriter;

abstract public class AbstractOutPacket {
    abstract public void write(PacketWriter _writer);
}
