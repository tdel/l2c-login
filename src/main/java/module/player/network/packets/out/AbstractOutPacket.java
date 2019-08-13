package module.player.network.packets.out;

import module.player.network.PacketWriter;

abstract public class AbstractOutPacket {
    abstract public void write(PacketWriter _writer);
}
