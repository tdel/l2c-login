package module.player.network.packets.in;

import module.player.network.packets.PacketReader;

abstract public class AbstractInPacket {

    abstract public void read(PacketReader _reader);

    abstract public void execute();
}
